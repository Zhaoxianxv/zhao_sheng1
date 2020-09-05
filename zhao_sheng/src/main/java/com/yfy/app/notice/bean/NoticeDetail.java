package com.yfy.app.notice.bean;

import java.util.List;

public class NoticeDetail {
	/**
	 * attachment : null
	 * attachment_exetend : null
	 * content :
	 * date : 2018/1/3 16:25:18
	 * images : null
	 * sender : 管理员
	 * title : 测试
	 * voice : null
	 */

	private String attachment;
	private String attachment_exetend;
	private String content;
	private String date;
	private List<String> images;
	private String sender;
	private String title;
	private String voice;

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getAttachment_exetend() {
		return attachment_exetend;
	}

	public void setAttachment_exetend(String attachment_exetend) {
		this.attachment_exetend = attachment_exetend;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
}
