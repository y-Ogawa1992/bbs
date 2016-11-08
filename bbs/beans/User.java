package bbs.beans;

import java.util.Date;

public class User {
	private int id;
	private String loginId;
	private String password;
	private String name;
	private int branchId;
	private int departmentId;
	private boolean stop;
	private Date insertDate;
	private Date updateDate;

	public int getId() {
		return id;
	}
	public void setId(int id)  {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId)  {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password)  {
		this.password = password;
	}

	public String getName() {
		return name;
	}
	public void setName(String name)  {
		this.name = name;
	}

	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId)  {
		this.branchId = branchId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId)  {
		this.departmentId = departmentId;
	}

	public boolean getStop() {
		return stop;
	}
	public void setStop(boolean stop)  {
		this.stop = stop;
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
