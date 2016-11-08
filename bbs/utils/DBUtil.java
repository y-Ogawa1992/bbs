package bbs.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import bbs.exception.SQLRuntimeException;

/**
 * DB(コネクション関係)のユーティリティ
 */

public class DBUtil {

	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/bbs";
	private static final String user = "root";
	private static final String password = "yuutarou0406";

	static {

		try{
			Class.forName(driver);
		} catch(ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * コネクションを取得します。
	 *
	 * @return
	 */
	public static Connection getConnection() {

		try {
			Connection connection = DriverManager.getConnection(url, user, password);

			connection.setAutoCommit(false);

			return connection;
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * コミットします。
	 *
	 * @param connection
	 */
	public static void commit(Connection connection) {

		try {
			connection.commit();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		}
	}

	/**
	 * ロールバックします。
	 *
	 * @param connection
	 */
	public static void rollback(Connection connection) {

		try {
			connection.rollback();
		} catch(SQLException e) {
			throw new SQLRuntimeException(e);
		}
	}
}
