package com.yfy.app.login.bean;

import com.yfy.app.login.bean.Stunlist;

import java.util.List;

/**
 * Created by yfy1 on 2016/11/23.
 */
public class UserRes {

    private String result;
    private String error_code;
    private String userid;
    private String session_key;
    private String classid;
    private String fxid;
    private String headPic;
    private String headpic;
    private String id;
    private String name;
    private String classname;
    private String username;
    private String token;


    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }


    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getFxid() {
        return fxid;
    }

    public void setFxid(String fxid) {
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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 重名
     */
    private List<Stunlist> stunlist;

    public List<Stunlist> getStunlist() {
        return stunlist;
    }

    public void setStunlist(List<Stunlist> stunlist) {
        this.stunlist = stunlist;
    }


    //------------user_right---------
    private String rightlist;

    public String getRightlist() {
        return rightlist;
    }

    public void setRightlist(String rightlist) {
        this.rightlist = rightlist;
    }
}
