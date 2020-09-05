package com.yfy.app.net.applied_order;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/10.
 */
@Root(name = TagFinal.ORDER_ADD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {
        TagFinal.session_key,
        "username",
        Base.date,
        "room_id",
        "time_name",
        "device",
        "devicewords",
        "islogistics",
        "logisticswords",
        "typeid",
        "description",
        "teacherid"})
public class OrderApplyNewReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "username", required = false)
    private String username;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "room_id", required = false)
    private int room_id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "time_name", required = false)
    private String time_name;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "device", required = false)
    private int device;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "devicewords", required = false)
    private String devicewords;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "islogistics", required = false)
    private int islogistics;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "logisticswords", required = false)
    private String logisticswords;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "typeid", required = false)
    private int typeid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "description", required = false)
    private String description;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "teacherid", required = false)
    private int teacherid;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setDevicewords(String devicewords) {
        this.devicewords = devicewords;
    }

    public void setIslogistics(int islogistics) {
        this.islogistics = islogistics;
    }

    public void setLogisticswords(String logisticswords) {
        this.logisticswords = logisticswords;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public String getSession_key() {
        return session_key;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getTime_name() {
        return time_name;
    }

    public int getDevice() {
        return device;
    }

    public String getDevicewords() {
        return devicewords;
    }

    public int getIslogistics() {
        return islogistics;
    }

    public String getLogisticswords() {
        return logisticswords;
    }

    public int getTypeid() {
        return typeid;
    }

    public String getDescription() {
        return description;
    }

    public int getTeacherid() {
        return teacherid;
    }
}
