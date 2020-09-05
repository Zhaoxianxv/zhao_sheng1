package com.yfy.app.attennew.bean;

import java.io.Serializable;
import java.util.List;

public class Attendance implements Serializable {
	private String id;

	private String Approvername;

	private String adddate;

	private String duration;

	private List<String> images ;

	private String reason;

	private String reply;

	private String start_time;

	private String status;

	private String table_plan;

	private String type;

	private String username;
	
	
	public Attendance (String id,String Approvername,String adddate,
			String duration,String reason,String reply,String start_time,
			String status,String table_plan,String type,
			String username,List<String> images)
	{
		this.id=id;
		this.Approvername=Approvername;
		this.adddate=adddate;
		this.duration=duration;
		this.reason=reason;
		this.reply=reply;
		this.start_time=start_time;
		this.status=status;
		this.table_plan=table_plan;
		this.type=type;
		this.username=username;
		this.images=images;
		
	}
	

	public void setId(String id){
	this.id = id;
	}
	public String getId(){
	return this.id;
	}
	public void setApprovername(String Approvername){
	this.Approvername = Approvername;
	}
	public String getApprovername(){
	return this.Approvername;
	}
	public void setAdddate(String adddate){
	this.adddate = adddate;
	}
	public String getAdddate(){
	return this.adddate;
	}
	public void setDuration(String duration){
	this.duration = duration;
	}
	public String getDuration(){
	return this.duration;
	}
	public void setImages(List<String> images){
	this.images = images;
	}
	public List<String> getImages(){
	return this.images;
	}
	public void setReason(String reason){
	this.reason = reason;
	}
	public String getReason(){
	return this.reason;
	}
	public void setReply(String reply){
	this.reply = reply;
	}
	public String getReply(){
	return this.reply;
	}
	public void setStart_time(String start_time){
	this.start_time = start_time;
	}
	public String getStart_time(){
	return this.start_time;
	}
	public void setStatus(String status){
	this.status = status;
	}
	public String getStatus(){
	return this.status;
	}
	public void setTable_plan(String table_plan){
	this.table_plan = table_plan;
	}
	public String getTable_plan(){
	return this.table_plan;
	}
	public void setType(String type){
	this.type = type;
	}
	public String getType(){
	return this.type;
	}
	public void setUsername(String username){
	this.username = username;
	}
	public String getUsername(){
	return this.username;
	}

}
