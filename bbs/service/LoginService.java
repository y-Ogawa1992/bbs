package bbs.service;

import static bbs.utils.CloseableUtil.*;
import static bbs.utils.DBUtil.*;

import java.sql.Connection;

import bbs.beans.User;
import bbs.dao.UserDao;
import bbs.utils.CipherUtil;

public class LoginService {

	public User login(String loginId, String password, Boolean stop) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			String encPassword = CipherUtil.encrypt(password);
			User user = userDao.getUser(connection, loginId, encPassword, stop);

			commit(connection);

			return user;
		} catch(RuntimeException e) {
			rollback(connection);
			throw e;
		} catch(Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}
}
