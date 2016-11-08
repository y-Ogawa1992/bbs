package bbs.dao;

import static bbs.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bbs.beans.Department;
import bbs.exception.SQLRuntimeException;

public class DepartmentDao {
	public List<Department> getDepartment(Connection connection) {

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM departments");

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<Department> ret = toDepartmentList(rs);
			return ret;
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Department> toDepartmentList(ResultSet rs) throws SQLException {

		List<Department> ret = new ArrayList<Department>();
		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");

				Department department = new Department();
				department.setId(id);
				department.setName(name);

				ret.add(department);
			}
			return ret;
		} finally {
			close(rs);
		}
	}
}
