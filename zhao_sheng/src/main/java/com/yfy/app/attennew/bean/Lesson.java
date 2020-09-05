package com.yfy.app.attennew.bean;

import java.io.Serializable;
import java.util.List;

public class Lesson implements Serializable{
	
	private String time;
	private List<AttenType> items;
	
	
	public Lesson(String time,List<AttenType> settl){
		this.time=time;
		this.items=settl;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<AttenType> getItems() {
		return items;
	}
	public void setItems(List<AttenType> items) {
		this.items = items;
	}
	
	
}
