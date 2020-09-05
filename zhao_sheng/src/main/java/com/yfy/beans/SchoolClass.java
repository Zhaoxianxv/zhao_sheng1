/**
 * 
 */
package com.yfy.beans;

import java.util.List;

import com.yfy.base.ChildCount;

/**
 * @version 1.0
 * @Desprition
 */
public class SchoolClass implements ChildCount {

	private String classid;
	private String classname;
	private String gradeid;
	private String gradename;
	private List<NewsMenu> newsMenuList;

	private int rank;
	private int total;

	public SchoolClass(String classid, String classname, String gradeid,
			String gradename, List<NewsMenu> newsMenuList) {
		super();
		this.classid = classid;
		this.classname = classname;
		this.gradeid = gradeid;
		this.gradename = gradename;
		this.newsMenuList = newsMenuList;
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

	public String getGradeid() {
		return gradeid;
	}

	public void setGradeid(String gradeid) {
		this.gradeid = gradeid;
	}

	public String getGradename() {
		return gradename;
	}

	public void setGradename(String gradename) {
		this.gradename = gradename;
	}

	public List<NewsMenu> getNewsMenuList() {
		return newsMenuList;
	}

	public void setNewsMenuList(List<NewsMenu> newsMenuList) {
		this.newsMenuList = newsMenuList;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "SchoolClass [classid=" + classid + ", classname=" + classname
				+ ", gradeid=" + gradeid + ", gradename=" + gradename
				+ ", newsMenuList=" + newsMenuList + "]";
	}

	@Override
	public int getChildCount() {
		if (newsMenuList == null) {
			return 0;
		}
		return newsMenuList.size();
	}

	@Override
	public Object getChild(int childPosition) {
		if (newsMenuList == null) {
			return null;
		}
		return newsMenuList.get(childPosition);
	}
}
