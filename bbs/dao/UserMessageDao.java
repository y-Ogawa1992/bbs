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

	public List<UserMessage> getUserMessages(Connection connection, String category, String minInsertDate, String maxInsertDate) {//String minInsertDate, String maxInsertDate

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_message WHERE insert_date BETWEEN ? AND ? ");

			if(category != null) {
				sql.append("AND category = ? ");
			}
			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, minInsertDate);
			ps.setString(2, maxInsertDate);

			ResultSet rs = ps.executeQuery();
			List<UserMessage> ret = toUserMessageList(rs);
			if(category != null) {
				ps.setString(3, category);
			}
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


}
