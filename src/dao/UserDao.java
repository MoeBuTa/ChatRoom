package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import util.MySQLUtil;

public class UserDao {
	MySQLUtil util = new MySQLUtil();

	public void insert(User u) {
		util.update("insert into t_user (uname,upwd) values (?,?)", u.getName(), u.getPwd());
	}

	public void update(User u) {
		util.update("update t_user set uname=?, upwd=? where uid=?", u.getName(), u.getPwd(), u.getId());
	}

	public void delete(int id) {
		util.update("delete from t_user where uid=?", id);
	}

	public List<User> query(String name, String pwd) {

		ResultSet rs = null;
		List<User> userList = new ArrayList<User>();
		String sql = "select * from t_user where 1=1";
		if (!"".equals(name) && name != null) {
			sql += " and uname='" + name + "'";
		}
		if (!"".equals(pwd) && pwd != null) {
			sql += " and upwd='" + pwd + "'";
		}

		try {
			rs = util.query(sql);
			while (rs.next()) {
				User u = new User(rs.getInt("uid"), rs.getString("uname"), rs.getString("upwd"));
				userList.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userList;
	}
}
