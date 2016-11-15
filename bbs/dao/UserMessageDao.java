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

import bbs.beans.UserMessage;
import bbs.exception.SQLRuntimeException;

public class UserMessageDao {

	//投稿全部を表示させる
	//条件分岐で絞り込みもする
	public List<UserMessage> getUserMessages(Connection connection, String category, String minInsertDate, String maxInsertDate) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_message WHERE insert_date BETWEEN ? AND ? ");

			if(StringUtils.isEmpty(category) != true) {
				sql.append("AND category = ? ");
			}
			sql.append("ORDER BY insert_date DESC ");

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, minInsertDate);
			ps.setString(2, maxInsertDate);
			if(StringUtils.isEmpty(category) != true) {
				ps.setString(3, category);
			}
			System.out.println(ps);
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

	//ここで最小値＝最古検索
	public List<UserMessage> getInsertOld(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_message ");
			sql.append("ORDER BY insert_date ");
			sql.append("LIMIT 1 ");

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

	//ここで最大値＝最新検索
	public List<UserMessage> getInsertNew(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_message ");
			sql.append("ORDER BY insert_date DESC ");
			sql.append("LIMIT 1 ");

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

}

