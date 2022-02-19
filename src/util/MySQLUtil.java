package util;

import java.sql.*;
import java.util.*;

public class MySQLUtil {
	public static final String driver = "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://192.168.7.187:3306/userforchatting";
	public static final String name = "root";
	public static final String pwd = "19971213zwx";

	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

	Scanner sc = new Scanner(System.in);
	String sql = null;

	public Connection getCon() throws Exception {
		Class.forName(driver);
		return DriverManager.getConnection(url, name, pwd);
	}

	public boolean update(String sql, Object... objects) {
		int num = 0;
		try {
			con = getCon();
			ps = con.prepareStatement(sql);
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					ps.setObject(i + 1, objects[i]);
				}
			}
			num = ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			closeAll();
		}
		return num > 0;
	}

	public ResultSet query(String sql,Object...objects) {
		try {
			con = getCon();
			ps = con.prepareStatement(sql);			
			if (objects != null) {
				for (int i = 0; i < objects.length; i++) {
					ps.setObject(i + 1, objects[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return rs;
	}

	public void closeAll() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
