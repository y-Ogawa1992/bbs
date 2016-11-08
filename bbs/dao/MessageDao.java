package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bbs.beans.Message;
import bbs.exception.SQLRuntimeException;

public class MessageDao {

	public void insert(Connection connection, Message message) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("user_id");
			sql.append(", title");
			sql.append(", category");
			sql.append(", text");
			sql.append(", insert_date");
			sql.append(", update_date");
			sql.append(") VALUES (");
			sql.append("?"); //user_id
			sql.append(", ?"); //title
			sql.append(", ?"); //category
			sql.append(", ?"); //text
			sql.append(", CURRENT_TIMESTAMP"); //insert_date
			sql.append(", CURRENT_TIMESTAMP"); //update_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getTitle());
			ps.setString(3, message.getCategory());
			ps.setString(4, message.getText());

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, Message message) {
		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM messages ");
			sql.append("WHERE ");
			sql.append("id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getId());

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}
