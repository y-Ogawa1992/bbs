package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import bbs.beans.User;
import bbs.exception.SQLRuntimeException;

public class UserDao {

	//ユーザー登録するためのSQL操作
	public void insert(Connection connection, User user) {

		PreparedStatement ps = null;
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO users ( ");
			sql.append("login_id");
			sql.append(", password");
			sql.append(", name");
			sql.append(", branch_id");
			sql.append(", department_id");
			sql.append(", stop");
			sql.append(", insert_date");
			sql.append(", update_date");
			sql.append(") VALUES (");
			sql.append("?"); //login_id
			sql.append(", ?"); //password
			sql.append(", ?"); //name
			sql.append(", ?"); //branch_id
			sql.append(", ?"); //department_id
			sql.append(", false");//stop
			sql.append(", CURRENT_TIMESTAMP"); //insert_date
			sql.append(", CURRENT_TIMESTAMP"); //update_date
			sql.append(")");

			ps=connection.prepareStatement(sql.toString());

			ps.setString(1, user.getLoginId());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getName());
			ps.setInt(4, user.getBranchId());
			ps.setInt(5, user.getDepartmentId());

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//ログインするにあたり、ユーザー情報をDB参照するSQL操作
	public User getUser(Connection connection, String loginId, String password, Boolean stop) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE login_id = ? AND password = ? AND stop = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, loginId);
			ps.setString(2, password);
			ps.setBoolean(3, stop);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			}
			return userList.get(0);

		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//UserList作成
	private List<User> toUserList(ResultSet rs) throws SQLException {

		List<User> ret = new ArrayList<User>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String loginId = rs.getString("login_id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				int branchId = rs.getInt("branch_id");
				int departmentId = rs.getInt("department_id");
				Boolean stop = rs.getBoolean("stop");
				Timestamp insertDate = rs.getTimestamp("insert_date");
				Timestamp updateDate = rs.getTimestamp("update_date");

				User user = new User();
				user.setId(id);
				user.setLoginId(loginId);
				user.setPassword(password);
				user.setName(name);
				user.setBranchId(branchId);
				user.setDepartmentId(departmentId);
				user.setStop(stop);
				user.setInsertDate(insertDate);
				user.setUpdateDate(updateDate);

				ret.add(user);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	//usersテーブルを見て管理画面、編集画面に表示させる
	public List<User> getUsers(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM users ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<User> ret = toUserList(rs);
			return ret;
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//userIdを探すselect
	public User getUser(Connection connection, int id) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE id = ?";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			} else {
				return userList.get(0);
			}
		} catch (SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//true⇔falseへアップデートする
	public void update(Connection connection, User user, int id, Boolean stop) {

		PreparedStatement ps = null;

		try{
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append(" stop = ?");
			sql.append(" WHERE");
			sql.append(" id =?");

			ps = connection.prepareStatement(sql.toString());

			ps.setBoolean(1, user.getStop());
			ps.setInt(2, user.getId());

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//ユーザー情報編集のアップデート
	//出来なきゃpasswordだけでなく全部if囲み
	public void update(Connection connection, User user) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE users SET");
			sql.append(" login_id = ?");
			sql.append(", name = ?");
			if(StringUtils.isEmpty(user.getPassword()) != true) {
				sql.append(", password = ?");
			}
			sql.append(", branch_id = ?");
			sql.append(", department_id = ?");
			sql.append(", update_date = CURRENT_TIMESTAMP");
			sql.append(" WHERE");
			sql.append(" id = ?");


			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, user.getLoginId());
			ps.setString(2, user.getName());

			if(StringUtils.isEmpty(user.getPassword()) != true) {
				ps.setString(3, user.getPassword());
				ps.setInt(4, user.getBranchId());
				ps.setInt(5, user.getDepartmentId());
				ps.setInt(6, user.getId());
			}else {
				ps.setInt(3, user.getBranchId());
				ps.setInt(4, user.getDepartmentId());
				ps.setInt(5, user.getId());
			}

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//ID重複確認
	public User getUserId(Connection connection, String loginId) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM users WHERE login_id = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, loginId);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			}
			return userList.get(0);

		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//userId存在確認
	public Boolean getUserExists(Connection connection, int userId) {
		PreparedStatement ps = null;
		try {
			String sql = "SELECT EXISTS(SELECT * FROM user_message WHERE id = ?)";

			ps = connection.prepareStatement(sql);
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();
			List<User> userList = toUserList(rs);
			if (userList.isEmpty() == true) {
				return null;
			}
			return null;

		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

}