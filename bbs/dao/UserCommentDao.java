package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bbs.beans.UserComment;
import bbs.exception.SQLRuntimeException;

public class UserCommentDao {

	public List<UserComment> getUserComments(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM user_comment ");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserComment> ret = toUserCommentList(rs);
			return ret;
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserComment> toUserCommentList(ResultSet rs) throws SQLException {

		List<UserComment> ret = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				int userId = rs.getInt("user_id");
				int commentId = rs.getInt("comment_id");
				int branchId = rs.getInt("branch_id");
				int departmentId = rs.getInt("department_id");
				String name = rs.getString("name");
				int messageId = rs.getInt("message_id");
				String text = rs.getString("text");
				Timestamp insertDate = rs.getTimestamp("insert_date");

				UserComment comment = new UserComment();
				comment.setId(id);
				comment.setUserId(userId);
				comment.setCommentId(commentId);
				comment.setBranchId(branchId);
				comment.setDepartmentId(departmentId);
				comment.setName(name);
				comment.setMessageId(messageId);
				comment.setText(text);
				comment.setInsertDate(insertDate);

				ret.add(comment);
			}
			return ret;
		} finally {
			close(rs);
		}
	}

}
