package cn.zc.manager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.zc.dbConnectionUtil.JdbcUtil;
import cn.zc.entity.Reader;
import cn.zc.entity.Student;
import cn.zc.entity.Teacher;

/**
 * 读者管理类
 * @author Administrator
 *
 */
public class ReaderManager {
	
	/**
	 * 新增一个读者		
	 * @param reader	需要新增的读者对象
	 * @return	新增成功为true，否则为false
	 */
	public boolean createNewReader(Reader reader) {
		String sql = "insert into tb_reader(readerId,rName,gender,tel,regDate,type) values(?,?,?,?,?,?)";
		//mysql的now()函数可能不支持其他数据库，所以建议不用
//		String sql = "insert into tb_reader values(?,?,?,?,now(),default)";
		//true在mysql数据库为1，false为0   mysql为date类型，java对应为sql.Date类型
		
		try (Connection conn = JdbcUtil.getConnection()){
			return JdbcUtil.executeUpdate(conn, sql, reader.getReaderId(),reader.getrName(),reader.isGender(),reader.getTel()
					,new java.sql.Date(System.currentTimeMillis()),reader.isType()) == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 注销一个读者(注销读者时必须要保证读者已经归还所有的图书)	
	 * @param id	需要注销的读者id
	 * @return	注销成功为true，否则为false
	 */
	public boolean removeReaderById(int id) {
		try (Connection conn = JdbcUtil.getConnection()){
			String sql = "select lendBookCount from tb_reader where readerId = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next() && rs.getInt(1) == 0) {
				String sql1 = "update tb_reader set available=0 where readerId = ?";
				return JdbcUtil.executeUpdate(conn, sql1, id) == 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 更新一个读者信息	(更新读者信息的时候先要判断这个读者是否已经注销无效了)
	 * @param reader	需要更新的读者信息
	 * @return	更新成功为true，否则为false
	 */
	public boolean updateReaderInfo(Reader reader) {
		try (Connection conn = JdbcUtil.getConnection()){
			String sql = "select available from tb_reader where readerId = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, reader.getReaderId());
			ResultSet rs = ps.executeQuery();
			//available为true，即数据库显示为1的时候才可以进行更新操作，因为这时读者是没有注销的，有效的
			if(rs.next() && rs.getBoolean(1)) {
				String sql1 = "update tb_reader set rName = ?,tel = ? where readerId = ?";
				return JdbcUtil.executeUpdate(conn, sql1, reader.getrName(),reader.getTel(),reader.getReaderId()) == 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据id查询一个读者(根据前台的参数判断查询的类型是老师还是学生)
	 * @param id	读者id
	 * @return	返回该id对应的读者，否则返回null
	 */
	public static Reader findReaderById(int id,boolean isTeacher) {
		
		String sql = "select readerId,rName,gender,tel,regDate,available,lendBookCount,type from tb_reader where readerId = ?";
		try (Connection conn = JdbcUtil.getConnection()){
			//根据isTeacer的真假传入老师或者学生对象参数，在前台进行查询读者类别的判断，工具类只负责读者类型查询
			return JdbcUtil.executeQuery(conn, isTeacher == true?new Teacher():new Student(), sql, id);
			
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();		//结果集
//			if(rs.next()) {
//				Reader reader = new Reader();
//				reader.setId(rs.getInt("readerid"));
//				reader.setName(rs.getString("rname"));
//				reader.setGender(rs.getBoolean("gender"));
//				reader.setTel(rs.getString("tel"));
//				reader.setRegDate(rs.getDate("regdate"));
//				reader.setAvailable(rs.getBoolean("available"));
//				return reader;
//			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		}
}
