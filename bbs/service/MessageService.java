package bbs.service;

import static bbs.utils.CloseableUtil.*;
import static bbs.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;

import bbs.beans.Message;
import bbs.beans.UserMessage;
import bbs.dao.MessageDao;
import bbs.dao.UserMessageDao;

public class MessageService {

	public void register(Message message) {

		Connection connection = null;
		try {
			connection = getConnection();

			MessageDao messageDao = new MessageDao();
			messageDao.insert(connection, message);

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

	public List<UserMessage> getMessage(String category, String minInsertDate, String maxInsertDate) {//String minInsertDate, String maxInsertDate

		Connection connection = null;
		try {
			connection = getConnection();

			UserMessageDao messageDao = new UserMessageDao();
			List<UserMessage> ret = messageDao.getUserMessages(connection, category, minInsertDate, maxInsertDate);//minInsertDate, maxInsertDate

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

	public void delete(Message message){
		Connection connection = null;
		try {
			connection = getConnection();

			MessageDao messageDao = new MessageDao();
			messageDao.delete(connection, message);

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

	//Daoでgroup byしたcategory
	public List<Message> getCategory() {

		Connection connection = null;
		try {
			connection = getConnection();

			MessageDao messageDao = new MessageDao();
			List<Message> ret = messageDao.getMessageCategories(connection);

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

	//日付の初期値
	public List<UserMessage> getInsertOldNew() {

		Connection connection = null;
		try {
			connection = getConnection();

			UserMessageDao messageDao = new UserMessageDao();
			List<UserMessage> ret = messageDao.getInsertOldNew(connection);

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
}