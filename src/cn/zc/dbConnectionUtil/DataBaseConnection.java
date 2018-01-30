package cn.zc.dbConnectionUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * 数据库连接类
 * JDBC:Java Database Connectivity - API
 * 特定的关系型数据库需要预先加载特定的数据库驱动
 * 数据库驱动就是对JDBC API的实现
 * 1.加载驱动类
 * 2.建立数据库连接
 * @author 张城
 *
 */
public class DataBaseConnection {
	
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://localhost:3306/booksys?useSSL=false&useUnicode=false&characterEncoding=utf8";
	private static final String USER = "root";
	private static final String PASSWORLD = "123456";
	private Connection connection = null;

	public DataBaseConnection() {
		try {
			Class.forName(DBDRIVER);
			this.connection = DriverManager.getConnection(DBURL, USER, PASSWORLD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return 数据库连接对象
	 */
	public Connection getConnection() {
		return this.connection;
	}
	
	/**
	 * 
	 */
	public void close() {
		if(this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
