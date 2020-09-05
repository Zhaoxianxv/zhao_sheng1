/**
 * 
 */
package com.yfy.app.vote.bean;

/**
 * @version 1.0
 * @Desprition
 */
public class Vote {

	private String id;
	private String title;
	private String data;
	private String isend;

	public Vote(String id, String title, String data, String isend) {
		super();
		this.id = id;
		this.title = title;
		this.data = data;
		this.isend = isend;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getIsend() {
		return isend;
	}

	public void setIsend(String isend) {
		this.isend = isend;
	}

	@Override
	public String toString() {
		return "Vote [id=" + id + ", title=" + title + ", data=" + data
				+ ", isend=" + isend + "]";
	}

}
