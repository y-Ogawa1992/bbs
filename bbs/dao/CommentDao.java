package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import bbs.beans.Comment;
import bbs.exception.SQLRuntimeException;

public class CommentDao {

	public void insert(Connection connection, Comment comment) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO comments ( ");
			sql.append("message_id");
			sql.append(", user_id");
			sql.append(", text");
			sql.append(", insert_date");
			sql.append(", update_date");
			sql.append(") VALUES (");
			sql.append("?"); //message_id
			sql.append(", ?"); //user_id
			sql.append(", ?"); //text
			sql.append(", CURRENT_TIMESTAMP"); //insert_date
			sql.append(", CURRENT_TIMESTAMP"); //update_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, comment.getMessageId());
			ps.setInt(2, comment.getUserId());
			ps.setString(3, comment.getText());

			ps.executeUpdate();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	public void delete(Connection connection, Comment comment) {

			PreparedStatement ps = null;
			try {
				StringBuilder sql = new StringBuilder();
				sql.append("DELETE FROM comments ");
				sql.append("WHERE ");
				sql.append("message_id = ?");

				ps = connection.prepareStatement(sql.toString());

				ps.setInt(1, comment.getMessageId());

				ps.executeUpdate();
			} catch(SQLException e) {
				throw new SQLRuntimeException(e);
			} finally {
				close(ps);
			}
	}
}