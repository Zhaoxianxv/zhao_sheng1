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
public class SchoolGrade implements ChildCount {

	private String gradeid;
	private String gradename;
	private List<SchoolClass> schoolClassList;

	public SchoolGrade(String gradeid, String gradename,
			List<SchoolClass> schoolClassList) {
		super();
		this.gradeid = gradeid;
		this.gradename = gradename;
		this.schoolClassList = schoolClassList;
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

	public List<SchoolClass> getSchoolClassList() {
		return schoolClassList;
	}

	public void setSchoolClassList(List<SchoolClass> schoolClassList) {
		this.schoolClassList = schoolClassList;
	}

	@Override
	public String toString() {
		return "SchoolGrade [gradeid=" + gradeid + ", gradename=" + gradename
				+ ", schoolClassList=" + schoolClassList + "]";
	}

	@Override
	public int getChildCount() {
		if (schoolClassList == null) {
			return 0;
		}
		return schoolClassList.size();
	}

	@Override
	public Object getChild(int childPosition) {
		if (schoolClassList == null) {
			return null;
		}
		return schoolClassList.get(childPosition);
	}

}
