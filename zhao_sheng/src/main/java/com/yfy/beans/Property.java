/**
 * 
 */
package com.yfy.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class Property implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8198283901016718686L;
	private String addtime;
	private String bz;
	private String classid;
	private String classname;
	private String id;
	private List<String> imageList;
	private String lastmodtime;
	private String number;
	private String supplies;
	private String suppliesid;

	public Property(String addtime, String bz, String classid,
			String classname, String id, List<String> imageList,
			String lastmodtime, String number, String supplies,
			String suppliesid) {
		super();
		this.addtime = addtime;
		this.bz = bz;
		this.classid = classid;
		this.classname = classname;
		this.id = id;
		this.imageList = imageList;
		this.lastmodtime = lastmodtime;
		this.number = number;
		this.supplies = supplies;
		this.suppliesid = suppliesid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getLastmodtime() {
		return lastmodtime;
	}

	public void setLastmodtime(String lastmodtime) {
		this.lastmodtime = lastmodtime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSupplies() {
		return supplies;
	}

	public void setSupplies(String supplies) {
		this.supplies = supplies;
	}

	public String getSuppliesid() {
		return suppliesid;
	}

	public void setSuppliesid(String suppliesid) {
		this.suppliesid = suppliesid;
	}

	@Override
	public String toString() {
		return "Property [addtime=" + addtime + ", bz=" + bz + ", classid="
				+ classid + ", classname=" + classname + ", id=" + id
				+ ", imageList=" + imageList + ", lastmodtime=" + lastmodtime
				+ ", number=" + number + ", supplies=" + supplies
				+ ", suppliesid=" + suppliesid + "]";
	}
}
