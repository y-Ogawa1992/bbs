package bbs.service;

import static bbs.utils.CloseableUtil.*;
import static bbs.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import bbs.beans.User;
import bbs.dao.UserDao;
import bbs.utils.CipherUtil;

public class UserService {

	//ログイン時のパスワード暗号化
	public void register(User user) {

		Connection connection = null;
		try {
			connection = getConnection();

			String encPassword = CipherUtil.encrypt(user.getPassword());
			user.setPassword(encPassword);

			UserDao userDao = new UserDao();
			userDao.insert(connection, user);

			commit(connection);
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

	//ログイン時ID,PW,STOP確認
	public User getUser(String loginId, String password, Boolean stop) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			User user = userDao.getUser(connection, loginId, password, stop);

			commit(connection);

			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	public List<User> getUsers() {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			List<User> ret = userDao.getUsers(connection);

			commit(connection);

			return ret;
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

	//Servletから受取ったstopが某ならば
	public void stop(User user, int id, Boolean stop) {

		Connection connection = null;
		try {
			connection = getConnection();

			if(user.getStop() == false) {
				user.setStop(true);
			}else{
				user.setStop(false);
			}

			UserDao userDao = new UserDao();
			userDao.update(connection, user, id, stop);

			commit(connection);
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

	//Servletから受取ったIDを探す
	public User getUser(int id) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			User user = userDao.getUser(connection, id);

			commit(connection);

			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	//ユーザー編集のアップデート文
	public void update(User user) {

		Connection connection = null;
		try{
			connection = getConnection();

			if(StringUtils.isEmpty(user.getPassword()) != true) {

			String encPassword = CipherUtil.encrypt(user.getPassword());
			user.setPassword(encPassword);

			}

			UserDao userDao = new UserDao();
			userDao.update(connection, user);

			commit(connection);
		} catch(RuntimeException e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

	//ログインID重複確認
	public User getUserId(String loginId) {

		Connection connection = null;
		try {
			connection = getConnection();

			UserDao userDao = new UserDao();
			User user = userDao.getUserId(connection, loginId);

			commit(connection);

			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			throw e;
		} catch (Error e) {
			rollback(connection);
			throw e;
		} finally {
			close(connection);
		}
	}

}