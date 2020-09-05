package com.yfy.app.info_submit.infos;

public class ItemType {

	private int fragmentType;
	private String postfix;
	private boolean isCanWu;
	private int sysmbol;

	public ItemType() {

	}

	public ItemType(int fragmentType, String postfix, boolean isCanWu,
			int sysmbol) {
		this.fragmentType = fragmentType;
		this.postfix = postfix;
		this.isCanWu = isCanWu;
		this.sysmbol = sysmbol;
	}

	public int getFragmentType() {
		return fragmentType;
	}

	public void setFragmentType(int fragmentType) {
		this.fragmentType = fragmentType;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public boolean isCanWu() {
		return isCanWu;
	}

	public void setCanWu(boolean isCanWu) {
		this.isCanWu = isCanWu;
	}

	public int getSysmbol() {
		return sysmbol;
	}

	public void setSysmbol(int sysmbol) {
		this.sysmbol = sysmbol;
	}
}
