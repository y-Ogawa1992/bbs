package bbs.beans;

import java.io.Serializable;
import java.util.Date;

public class UserMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private int messageId;
	private String name;
	private String title;
	private String category;
	private String text;
	private Date insertDate;

	public int getId() {
		return id;
	}
	public void setId(int id)  {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId)  {
		this.userId = userId;
	}

	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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

}
