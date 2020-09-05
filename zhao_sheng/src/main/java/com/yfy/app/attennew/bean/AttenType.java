package com.yfy.app.attennew.bean;

import java.io.Serializable;

public class AttenType implements Serializable{

	private String typeid;
	private String typename;
	private String tea="";

	public String getTea() {
		return tea;
	}

	public void setTea(String tea) {
		this.tea = tea;
	}

	public AttenType(String typeid, String typename) {
		super();
		this.typeid = typeid;
		this.typename = typename;
	}

	public String getTypeid() {
		return typeid;
	}

	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	@Override
	public String toString() {
		return "LeaveType [typeid=" + typeid + ", typename=" + typename + "]";
	}

}
