package com.yfy.app.notice.bean;

public class ReadState {

	private String id;
	private String name;
	private String status;

	public ReadState(String id, String name, String status) {
		super();
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ReadState [id=" + id + ", name=" + name + ", status=" + status
				+ "]";
	}

}
