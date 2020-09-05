package com.yfy.app.goods.bean;

import java.util.List;

public class Flowbean {

    /**
     * status : 已提交
     * name : 管理员
     * user_avatar : http://www.cdeps.sc.cn/uploadfile/photo/20171222112306.jpg
     * time : 2018/12/25
     * remark :
     * images : []
     */

    private String status;
    private String name;
    private String user_avatar;
    private String time;
    private String remark;
    private List<String> images;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
