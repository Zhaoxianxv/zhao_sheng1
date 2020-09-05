/**
 * 
 */
package com.yfy.app.notice.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author yfy
 * @date 2015-9-27
 * @version 1.0
 * @description SendNoticeBean
 */
public class NoticeBean implements Parcelable {



	private int view_type;
	/**
	 * content : 测试一下
	 * date : 2019/5/6 9:57:06
	 * id : 20047
	 * sender : 网站管理员
	 * state : 1
	 * title : 测试
	 * url : https://www.cdhxps.com/notice.aspx?id=20047&uid=199
	 */

	private String content;
	private String date;
	private String id;
	private String sender;
	private String state;
	private String title;
	private String url;

	public int getView_type() {
		return view_type;
	}

	public void setView_type(int view_type) {
		this.view_type = view_type;
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

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.content);
		dest.writeString(this.date);
		dest.writeString(this.id);
		dest.writeString(this.sender);
		dest.writeString(this.state);
		dest.writeString(this.title);
		dest.writeString(this.url);
	}

	public NoticeBean() {
	}

	protected NoticeBean(Parcel in) {
		this.content = in.readString();
		this.date = in.readString();
		this.id = in.readString();
		this.sender = in.readString();
		this.state = in.readString();
		this.title = in.readString();
		this.url = in.readString();
	}

	public static final Creator<NoticeBean> CREATOR = new Creator<NoticeBean>() {
		@Override
		public NoticeBean createFromParcel(Parcel source) {
			return new NoticeBean(source);
		}

		@Override
		public NoticeBean[] newArray(int size) {
			return new NoticeBean[size];
		}
	};
}
