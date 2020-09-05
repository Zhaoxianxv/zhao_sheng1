/**
 * 
 */
package com.yfy.beans;

/**
 * @version 1.0
 * @Desprition
 */
public class Supply {
	private String bz;
	private String supplies;
	private String suppliesid;

	public Supply(String bz, String supplies, String suppliesid) {
		super();
		this.bz = bz;
		this.supplies = supplies;
		this.suppliesid = suppliesid;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
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
		return "Supply [bz=" + bz + ", supplies=" + supplies + ", suppliesid="
				+ suppliesid + "]";
	}

}
