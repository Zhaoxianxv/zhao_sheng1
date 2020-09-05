/**
 * 
 */
package com.yfy.beans;

/**
 * @author yfy
 * @date 2015-9-27
 * @version 1.0
 * @description ReceiveNoticeBean
 */
public class SendNotice {

	private String id;
	private String title;
	private String url;
	private String sendDate;

	public SendNotice(String id, String title, String url, String sendDate) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.sendDate = sendDate;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	@Override
	public String toString() {
		return "SendNoticeBean [id=" + id + ", title=" + title + ", url=" + url
				+ ", sendDate=" + sendDate + "]";
	}

}
