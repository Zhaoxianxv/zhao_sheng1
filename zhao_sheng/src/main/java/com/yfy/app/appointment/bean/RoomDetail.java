package com.yfy.app.appointment.bean;

import java.util.List;

/**
 * Created by yfyandr on 2018/5/28.
 */

public class RoomDetail {

    /**
     * id : 250946
     * headpic : http://www.cdeps.sc.cn/uploadfile/photo/20171222112306.jpg
     * room : 画室
     * date : 2018/5/19
     * time : 上午第一节,上午第二节,上午第三节
     * applydate : 2018/5/18 15:46:24
     * reply :
     * description : 测试
     * person : 管理员
     * status : 申请中
     * device : 否
     * devicewords :
     * islogistics : 否
     * logisticswords :
     * typeid : 4
     * typename : 市级及以上
     * logisticsscore : 未评价
     * logisticscontent :
     * same_funcRoom : []
     */

    private String id;
    private String headpic;
    private String room;
    private String date;
    private String time;
    private String applydate;
    private String reply;
    private String description;
    private String person;
    private String status;
    private String device;
    private String devicewords;
    private String islogistics;
    private String logisticswords;
    private String typeid;
    private String typename;
    private String logisticsscore;
    private String logisticscontent;
    private String canedit;
    private String agreestate;
    private List<DoItem> operate;

    public List<DoItem> getOperate() {
        return operate;
    }

    public void setOperate(List<DoItem> operate) {
        this.operate = operate;
    }

    public String getCanedit() {
        return canedit;
    }

    public void setCanedit(String canedit) {
        this.canedit = canedit;
    }

    public String getAgreestate() {
        return agreestate;
    }

    public void setAgreestate(String agreestate) {
        this.agreestate = agreestate;
    }

    private List<SameRoom> same_funcRoom;

    public List<SameRoom> getSame_funcRoom() {
        return same_funcRoom;
    }

    public void setSame_funcRoom(List<SameRoom> same_funcRoom) {
        this.same_funcRoom = same_funcRoom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApplydate() {
        return applydate;
    }

    public void setApplydate(String applydate) {
        this.applydate = applydate;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDevicewords() {
        return devicewords;
    }

    public void setDevicewords(String devicewords) {
        this.devicewords = devicewords;
    }

    public String getIslogistics() {
        return islogistics;
    }

    public void setIslogistics(String islogistics) {
        this.islogistics = islogistics;
    }

    public String getLogisticswords() {
        return logisticswords;
    }

    public void setLogisticswords(String logisticswords) {
        this.logisticswords = logisticswords;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getLogisticsscore() {
        return logisticsscore;
    }

    public void setLogisticsscore(String logisticsscore) {
        this.logisticsscore = logisticsscore;
    }

    public String getLogisticscontent() {
        return logisticscontent;
    }

    public void setLogisticscontent(String logisticscontent) {
        this.logisticscontent = logisticscontent;
    }
}
