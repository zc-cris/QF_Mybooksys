package cn.zc.manager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import cn.zc.dbConnectionUtil.JdbcUtil;
import cn.zc.entity.Book;
import cn.zc.entity.Student;
import cn.zc.entity.Teacher;

/**
 * 书籍管理类
 * @author Administrator
 *
 */
public class BookManager {
	
	private static final int MILLIS_PER_DAY = 86400000;		//一天的总毫秒数
	private static final double PUNISHMENT_PER_DAY = 0.2;	//超出免费借阅时间范围后每天罚款

	/**
	 * 新增一本图书
	 * @param book	需要新增的图书对象
	 * @return	新增成功为true，否则为false
	 */
	public boolean addNewBook(Book book) {
		String sql = "insert into tb_book(bookid,isbn,bname,price,author,publisher,pubdate) values"
				+ "(?,?,?,?,?,?,?) ";
		try (Connection conn = JdbcUtil.getConnection();){
			return JdbcUtil.executeUpdate(conn, sql, book.getId(),book.getIsbn(),book.getName(),book.getPrice(),book.getAuthor(),
					book.getPublisher(),new Date(book.getPubDate().getTime())) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 下架一本图书（先判断该书没有被借出，然后设置为不可借阅  即available设置为false，即数据库显示为0）
	 * @param id 需要被下架的图书id
	 * @return	下架成功为true，否则为false
	 */
	public boolean removeBookById(int id) {
		String sql = "update tb_book set available=0 where bookid = ? and lended = 0";
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeUpdate(conn, sql, id) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 借出一本图书（先判断读者id是否有效没有注销，并且读者可借图书还没有到达最大数量，然后通过update语句判断图书是否有效且没有借出
	 * ，最后借书成功那么该读者的借书数量+1(通过触发器完成)，该书的借阅量+1，该书的状态改为已经借出）
	 * @param bookId	借出书籍id
	 * @param readerId	借书人id
	 * @return	借书成功为true，否则为false
	 * mysql数据库为datetime类型，那么java中对应java.sql.Timestamp类型,也可以java.util.Date
	 * mysql数据库为date类型，那么java中对应java.sql.date类型 new java.sql.date(System.currentTime())
	 * mysql数据库为decimal类型，那么java中对应的是BigDecimal类型
	 * mysql数据库为int类型，那么java中对应的是Integer类型
	 */
	public boolean lendOut(int bookId, int readerId, boolean isTeacher) {
		
		/**
		 * 数据库只负责查询，不负责业务逻辑判断，所以业务逻辑用java代码写，不要放在sql语句中，通过报异常的方式来判断
		 */
//		String sql = "insert into tb_record (bid,rid,lenddate) values ((select bookid from tb_book where bookid = ? and "
//				+ "available = 1),(select readerid from tb_reader where readerid = ? and available = 1),?) ";
		
		Connection conn = JdbcUtil.getConnection();
		try {
			String sql = "select available,lendBookCount from tb_reader where readerid =?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, readerId);
			ResultSet rs = ps.executeQuery();
//			System.out.println(rs.getBoolean(1));	注意：这里必须先判断next()才可以进行取值，否则会报空指针异常
			//该readerid对应的用户存在并且有效,可借图书量还有剩余，那么开启事务操作
			if(rs.next() && rs.getBoolean(1) && rs.getInt(2) < (isTeacher == true?new Teacher().getCanLendCount():new Student().getCanLendCount())) {
				
//			System.out.println(rs.getBoolean(1));		true
				
				//1.开启事务
				JdbcUtil.beginTx(conn);
				String sql1 = "update tb_book set counter = counter+1,lended = 1 where lended = 0 and bookid = ? and available = 1";
				if(JdbcUtil.executeUpdate(conn, sql1, bookId) == 1) {
					String sql2 = "insert into tb_record(bid,rid,lenddate) values (?,?,?)";
					int i = JdbcUtil.executeUpdate(conn, sql2, bookId,readerId,new Timestamp(System.currentTimeMillis()));
					//2.提交事务
					JdbcUtil.commitTx(conn);
					return i == 1;
				}
			}
		} catch (SQLException e) {
					
					//3.事务回滚
					JdbcUtil.rollbackTx(conn);
					e.printStackTrace();
		}finally {
					//关闭数据库连接
					JdbcUtil.closeConnection(conn);
	}
		return false;
}
	
	/**
	 * 归还一本图书,并且统计罚款金额（根据还书时间-借书时间 是否大于规定的时间来判断，其中老师最大借书时间60天，学生最大借书时间为30天）
	 * 并且该读者的借书数量-1(通过触发器完成)
	 * @param bookId	归还图书id
	 * @param readerId  归还图书的读者id
	 * @return	还书成功则返回罚款金额>=0，还书失败返回-1;
	 */
	public double returnBack(int bookId, int readerId, boolean isTeacher) {
		Connection conn = JdbcUtil.getConnection();
		double money = 0;
		
		try {
			//1.开启事务
			JdbcUtil.beginTx(conn);
			String sql = "update tb_book set lended = 0 where bookid = ? and lended = 1";
			//该bookid对应的书本已经设置lended=0
			if(JdbcUtil.executeUpdate(conn, sql, bookId) == 1) {
				
				//根据读者id，书籍id，最后一次借书时间来唯一确定借书记录，其中最后一次借书时间需要通过子查询来进行查询
				String sql1 = " select recordid,lenddate from tb_record where lenddate = (select max(lenddate)"
						+ " from tb_record where bid = ? and rid = ?)";
				
				PreparedStatement ps = conn.prepareStatement(sql1);
				ps.setInt(1, bookId);
				ps.setInt(2, readerId);
				ResultSet rs = ps.executeQuery();
				//先判断，后取值
				if(rs.next()) {
					int recordId = rs.getInt("recordid");
					Date lenddate = rs.getDate("lenddate");
					Date backdate = new Date();
				
					//计算借书的时间
					double days = Math.ceil((backdate.getTime() - lenddate.getTime())/(double)MILLIS_PER_DAY);
					
					//根据不同的读者类型获取不同的最大结束时间作为惩罚的标准
					int punishmentDays = isTeacher == true?new Teacher().getLendDays():new Student().getLendDays();
					
					//满足条件就计算惩罚金额
					money = days >= punishmentDays ? Math.round(((days - punishmentDays) * PUNISHMENT_PER_DAY * 100)) / 100.0: 0 ;
					
					String sql2 = "update tb_record set backdate=?, publishment =? where recordid =?";
					int rows = JdbcUtil.executeUpdate(conn, sql2,backdate, money,recordId);
					//2.提交事务
					if(rows == 1) {
						JdbcUtil.commitTx(conn);
						return money;
					}
				}
			}
		} catch (Exception e) {
			//3.事务回滚
			JdbcUtil.rollbackTx(conn);
			e.printStackTrace();
		}finally {
			//4.关闭数据库连接
			JdbcUtil.closeConnection(conn);
		}
		return -1;
	}
	
	/**
	 * 根据id查找一本图书
	 * @param id	图书id
	 * @return	该id对应的图书，没有为null
	 */
	public Book findBookById(int id) {
		String sql = "select bookid,isbn,bname,price,author,publisher,pubdate,lended,counter,available from tb_book where"
				+ " bookid = ? ";
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeBookQuery(conn, Book.class, sql, id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据isbn号查找所有图书(分页)
	 * @param isbn	图书isbn号
	 * @return	该isbn对应的所有图书，没有为null
	 */
	public List<Book> findBooksByIsbn(String isbn, int page, int size){
		String sql = "select bookid,isbn,bname,price,author,publisher,pubdate,"
				+ " lended,counter,available from tb_book where isbn like ? order by counter limit ?,?";
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeQueryList(conn, Book.class, sql, isbn,(page-1)*size,size);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据作者查找所有图书(分页并根据借阅次数排序)
	 * @param author	作者名(模糊查询)
	 * @return	该作者对应的所有图书，没有为null
	 */
	public List<Book> findBooksByAuthor(String author, int page, int size){
		String sql = "select bookid,isbn,bname,price,author,publisher,pubdate,"
				+ " lended,counter,available from tb_book where author like ? order by counter limit ?,?";
		
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeQueryList(conn, Book.class, sql, author, (page-1)*size, size);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据书名查找所有图书(分页并根据借阅次数排序)
	 * @param name	书名(模糊查询)
	 * @return	该书名对应的所有图书，没有为bull
	 */
	public List<Book> findBooksByName(String name,int page,int size){
		String sql = "select bookid,isbn,bname,price,author,publisher,pubdate,"
				+ "lended,counter,available from tb_book where bname like ? order by counter limit ?,?";
		
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeQueryList(conn, Book.class, sql, name, (page-1)*size, size);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询借阅量前10的（where-->group by-->order by-->limit）
	 * @return
	 */
	public List<Book> searchTop10Books(){
		String sql = "select bookid,isbn,bname,price,author,publisher,pubdate,"
				+ " lended,sum(counter) as counter,available from tb_book group by isbn order by counter desc"
				+ " limit 10";
		try(Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeQueryList(conn, Book.class, sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
