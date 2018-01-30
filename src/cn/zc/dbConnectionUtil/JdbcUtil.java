package cn.zc.dbConnectionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import cn.zc.entity.Book;

public class JdbcUtil {

	/**
	 * 获取配置文件properties类
	 */
	private static Properties props = new Properties();
	
	/**
	 * 构造方法私有化
	 */
	private JdbcUtil() {
		throw new AssertionError();
	}
	
	/**
	 * 加载数据库驱动
	 */
	static {
		try (InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
			props.load(in);
			Class.forName(props.getProperty("driver"));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取数据库连接
	 * @return	Connection连接对象
	 * @throws SQLException
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(props.getProperty("url"), props.getProperty("username"),
					props.getProperty("password"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn 数据库连接对象
	 */
	public static void closeConnection(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 开启事务
	 * @param conn 数据库连接对象
	 */
	public static void beginTx(Connection conn) {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 提交事务
	 * @param conn 数据库连接对象
	 */
	public static void commitTx(Connection conn) {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 回滚事务
	 * @param conn 数据库连接对象
	 */
	public static void rollbackTx(Connection conn) {
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 封装后的数据更新方法
	 * @param conn	数据库连接对象
	 * @param sql	sql语句
	 * @param params	具体的sql语句参数
	 * @return 数据库受影响的行数
	 */
	public static int executeUpdate(Connection conn, String sql, Object...params) {
		try (PreparedStatement ps = conn.prepareStatement(sql)){
			//先检查参数个数能否对上再进行查询
			checkParams(ps, params);
			return ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);				//将受检异常包装成运行时异常,后面不需要在return了
		}
	}

	/**
	 * 检查请求参数和实际参数的个数是否一致（即sql语句的问号占位符和实际参数的个数一致才可以进行sql语句的执行）
	 * @param ps	PreparedStatement
	 * @param params	实际的参数
	 * @throws SQLException
	 */
	public static void checkParams(PreparedStatement ps, Object... params) throws SQLException {
		//请求参数的元数据
		ParameterMetaData parameterMetaData = ps.getParameterMetaData();
		int count = parameterMetaData.getParameterCount();
		//请求参数的个数和实际的参数个数必须一致才可以执行sql语句
		if(count == params.length) {
			for(int i=0;i<params.length;++i) {
				ps.setObject(i+1, params[i]);
			}
		}else {
			throw new RuntimeException("参数个数不一致！");
		}
	}
	
	/**
	 * 封装后的查询一个读者(老师或者学生)的方法
	 * @param <T>	泛型
	 * @param conn	数据库连接
	 * @param sql	查询语句
	 * @param t     该泛型对应的一个无参实例
	 * @param params	具体的参数值
	 * @return	经过处理后的无参实例
	 */
	public static <T> T executeQuery(Connection conn, T t, String sql, Object...params) {
		try (PreparedStatement ps = conn.prepareStatement(sql)){
			//先检查参数个数能否对上再进行查询
			checkParams(ps, params);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				//因为学生和老师是继承的Reader类，所以必须使用父类的class对象获取Field对象数组
				Field[] fields = t.getClass().getSuperclass().getDeclaredFields();
				for(Field field : fields) {
					if("lendDays".equals(field.getName()) || "canLendCount".equals(field.getName())) {
						
					}else {
						field.setAccessible(true);
						field.set(t, rs.getObject(field.getName()));
					}
				}
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 封装后的查询一本书籍的方法
	 * @param <T>	泛型
	 * @param conn	数据库连接
	 * @param sql	查询语句
	 * @param t     该泛型对应的一个无参实例
	 * @param params	具体的参数值
	 * @return	经过处理后的无参实例
	 */
	public static <T> T executeBookQuery(Connection conn, Class clazz, String sql, Object...params) {
		try (PreparedStatement ps = conn.prepareStatement(sql)){
			//先检查参数个数能否对上再进行查询
			checkParams(ps, params);
			ResultSet rs = ps.executeQuery();
			T t = (T) clazz.newInstance();
			if(rs.next()) {
				Field[] fields = clazz.getDeclaredFields();
				for(Field field : fields) {
						field.setAccessible(true);
						field.set(t, rs.getObject(field.getName()));
				}
			}
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 封装后的查询数据集合的方法（模糊查询以及分页）
	 * @param <T>	泛型
	 * @param conn	数据库连接
	 * @param t     该泛型对应的一个无参实例
	 * @param sql	sql查询语句
	 * @param params	具体的参数值
	 * @return	List<T>	
	 */
	public static <T> List<T> executeQueryList(Connection conn, Class clazz, String sql, Object...params) {
		try (PreparedStatement ps = conn.prepareStatement(sql)){
			//根据书名或者作者名模糊查询
			for(int i=0;i<params.length;++i) {
				if(i == 0) {
					ps.setString(i+1, "%"+params[i]+"%");	
				}else if(i == 1 || i == 2) {			//分页功能
					ps.setInt(i+1, (int) params[i]);
				}else {
					ps.setObject(i+1, params[i]);
				}
			}
			
			ResultSet rs = ps.executeQuery();
			List<T> objs = new ArrayList<>();
			
			while(rs.next()) {
				Field[] fields = clazz.getDeclaredFields();
				T newInstance = (T) clazz.newInstance();
				for(int i=0;i<fields.length;++i) {
					fields[i].setAccessible(true);
					fields[i].set(newInstance, rs.getObject(i+1));
				}
				objs.add(newInstance);
			}
			//初始化ArrayList集合的时候就会创建初始长度为10的数组，如果集合对象没有元素，就通过Collections工具类
			//将这个空集合里的数组清除以节省内存空间
			return objs.size() > 0?objs:Collections.emptyList();
		} catch (SQLException | IllegalArgumentException | IllegalAccessException | InstantiationException e) {
			throw new RuntimeException(e);
		}
	}
}
