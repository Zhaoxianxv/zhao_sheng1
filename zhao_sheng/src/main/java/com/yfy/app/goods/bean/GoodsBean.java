package com.yfy.app.goods.bean;

public class GoodsBean {

    /**
     * id : 19
     * username : 管理员
     * item_name : 没找到这个东西
     * item_id : 0
     * submit_time : 2018/12/25
     * status : 已处理
     */

    private String id;
    private String username;
    private String item_name;
    private String item_id;
    private String submit_time;
    private String status;

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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
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
}
