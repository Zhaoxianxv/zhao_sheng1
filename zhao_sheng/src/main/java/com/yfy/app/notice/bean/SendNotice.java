/**
 * 
 */
package com.yfy.app.notice.bean;

/**
 * @author yfy
 * @date 2015-9-27
 * @version 1.0
 * @description ReceiveNoticeBean
 */
public class SendNotice {


	/**
	 * MailSendDate : 2018/11/29 16:18:49
	 * id : 20036
	 * title : 杨好
	 * url : http://www.cdhxps.com/notice.aspx?id=20036&uid=199
	 */

	private String MailSendDate;
	private String id;
	private String title;
	private String url;

	public String getMailSendDate() {
		return MailSendDate;
	}

	public void setMailSendDate(String MailSendDate) {
		this.MailSendDate = MailSendDate;
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
}
