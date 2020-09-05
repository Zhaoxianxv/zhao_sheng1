package com.yfy.beans;

import java.io.Serializable;

public class ContactMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6232934994613064805L;
	private String memberId;
	private String memberName;
	private String phone1;
	private String phone2;
	private String headPic;
	private boolean isSelected;

	public ContactMember(String memberId, String memberName, String phone1,
			String phone2, String headPic) {
		super();
		this.memberId = memberId;
		this.memberName = memberName;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.headPic = headPic;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ContactMember [memberId=" + memberId + ", memberName="
				+ memberName + ", phone1=" + phone1 + ", phone2=" + phone2
				+ ", headPic=" + headPic + "]";
	}

}
