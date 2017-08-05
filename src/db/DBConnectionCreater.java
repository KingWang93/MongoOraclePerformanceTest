package db;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import util.PropertiesUtil;

public class DBConnectionCreater {

	public static Connection ConnectionCreaterForTG(String username,
			String password, String url) throws MalformedURLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return DriverManager.getConnection(PropertiesUtil.getProperties("Database",url),
				PropertiesUtil.getProperties("Database",username),
				PropertiesUtil.getProperties("Database",password));
	}
	
	public static Connection ConnectionCreaterForVIP(String username,
			String password, String url) throws MalformedURLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(PropertiesUtil.getProperties("Database",url),
				PropertiesUtil.getProperties("Database",username),
				PropertiesUtil.getProperties("Database",password));
	}

	
	public static void close(Connection conn, PreparedStatement pstmt,
			ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (conn != null && (!conn.isClosed())) {
				conn.close();
				conn = null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
