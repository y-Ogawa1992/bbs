package bbs.beans;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int messageId;
	private int userId;
	private String text;
	private Date insertDate;
	private Date updateDate;

	public int getId() {
		return id;
	}
	public void setId(int id)  {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId)  {
		this.messageId = messageId;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId)  {
		this.userId = userId;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}