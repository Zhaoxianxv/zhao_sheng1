package com.yfy.app.goods.bean;

import java.util.List;

public class BeanItem {

    /**
     * id : 17
     * username : 管理员
     * user_avatar : http://www.cdeps.sc.cn/uploadfile/photo/20171222112306.jpg
     * item_name : 钢笔
     * type : 申领物品
     * item_id : 8
     * submit_time : 2018/12/25
     * status : 已准备
     * remark :
     * count : 3
     * images : []
     * unit : 支
     */

    private String id;
    private String username;
    private String user_avatar;
    private String item_name;
    private String type;
    private String item_id;
    private String submit_time;
    private String status;
    private String remark;
    private String count;
    private String unit;
    private List<String> images;
    private List<Flowbean> info;

    public List<Flowbean> getInfo() {
        return info;
    }

    public void setInfo(List<Flowbean> info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
