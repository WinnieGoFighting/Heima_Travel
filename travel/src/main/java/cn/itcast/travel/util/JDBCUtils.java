package cn.itcast.travel.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
	private static DataSource ds;
	static {
		Properties pro = new Properties();
		try {
			pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
			System.out.println(pro);
			ds = DruidDataSourceFactory.createDataSource(pro);
			System.out.println(ds);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// get connection
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	//release resource
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Statement stmt, Connection conn) {
		close(null,stmt,conn);
	}

	public static DataSource getDataSource() {
		return ds;
	}

}
