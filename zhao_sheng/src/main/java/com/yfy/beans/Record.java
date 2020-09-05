/**
 * 
 */
package com.yfy.beans;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @Desprition
 */
public class Record implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8615432982880871955L;
	private String name;
	private String path;
	private String date;
	private long size;
	private int duration;

	public Record(String name, String path, String date, long size, int duration) {
		super();
		this.name = name;
		this.path = path;
		this.date = date;
		this.size = size;
		this.duration = duration;
	}

	public Record() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Record [name=" + name + ", path=" + path + ", date=" + date
				+ ", size=" + size + ", duration=" + duration + "]";
	}

	public void repair() {
		setDate(getLastDate(getPath()));
	}

	private static String getLastDate(String path) {

		File file = new File(path);
		if (file.exists()) {
			long time = file.lastModified();
			String ctime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date(time));
			return ctime;
		}

		return "";
	}
}
