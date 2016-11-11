package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bbs.beans.UserMessage;
import bbs.exception.SQLRuntimeException;

public class UserMessageDao {

	public List<UserMessage> getUserMessages(Connection connection) {

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_message ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserMessage> ret = toUserMessageList(rs);
			return ret;
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessageList(ResultSet rs) throws SQLException {

		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				int userId = rs.getInt("user_id");
				int messageId = rs.getInt("message_id");
				int branchId = rs.getInt("branch_id");
				int departmentId = rs.getInt("department_id");
				String title = rs.getString("title");
				String category = rs.getString("category");
				String name = rs.getString("name");
				String text = rs.getString("text");
				Timestamp insertDate = rs.getTimestamp("insert_date");

				UserMessage message = new UserMessage();
				message.setId(id);
				message.setUserId(userId);
				message.setMessageId(messageId);
				message.setBranchId(branchId);
				message.setDepartmentId(departmentId);
				message.setTitle(title);
				message.setCategory(category);
				message.setName(name);
				message.setText(text);
				message.setInsertDate(insertDate);

				ret.add(message);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

	//11/11
	//categoryを探すselect
	public UserMessage select(Connection connection, String category) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM user_message WHERE category = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, category);

			ResultSet rs = ps.executeQuery();
			List<UserMessage> userList = toUserMessageList(rs);
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

	//11/11
	//categoryでの絞り込み
	public UserMessage getCategory(Connection connection, String category) {

		PreparedStatement ps = null;
		try {
			String sql = "SELECT * FROM user_message WHERE category = ?";

			ps = connection.prepareStatement(sql);
			ps.setString(1, category);

			ResultSet rs = ps.executeQuery();
			List<UserMessage> userList = toUserMessageList(rs);
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
}
