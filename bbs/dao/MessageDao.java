package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	//categoryでselectしてtoCategoryに入れる
	public List<Message> getMessageCategories(Connection connection) {
		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT category FROM messages group by category");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<Message> ret = toCategoryList(rs);
			return ret;
			} catch(SQLException e) {
				throw new SQLRuntimeException(e);
			} finally {
				close(ps);
			}
	}

	//toCategoryの処理
	private List<Message> toCategoryList(ResultSet rs) throws SQLException {
		List<Message> ret = new ArrayList<Message>();
		try {
			while (rs.next()) {
				String category = rs.getString("category");

				Message categories = new Message();
				categories.setCategory(category);
				ret.add(categories);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
