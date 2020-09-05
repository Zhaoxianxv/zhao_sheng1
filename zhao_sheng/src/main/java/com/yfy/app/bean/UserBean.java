package com.yfy.app.bean;

public class UserBean {

    /**
     * classid : 0
     * fxid : 11
     * headPic : https://www.jinwai.net/uploadfile/11/user//201811292257030.JPG
     * id : 1
     * name : 管理员
     * username : yfy
     */

    private int classid;
    private int fxid;
    private String headPic;
    private String id;
    private String name;
    private String username;

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getFxid() {
        return fxid;
    }

    public void setFxid(int fxid) {
        this.fxid = fxid;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
