/**
 * 
 */
package com.yfy.beans;

/**
 * @author yfy1
 * @Date 2015��10��29��
 * @version 1.0
 * @Desprition
 */
public class NewsComment {

	private String content;
	private String date;
	private String id;
	private String ip;
	private String name;

	public NewsComment(String content, String date, String id, String ip,
					   String name) {
		super();
		this.content = content;
		this.date = date;
		this.id = id;
		this.ip = ip;
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NewsComment [content=" + content + ", date=" + date + ", id="
				+ id + ", ip=" + ip + ", name=" + name + "]";
	}

}
