package com.yfy.app.check.bean;

import java.util.List;

public class ChecKParent  {


    /**
     * illid : 3
     * username : 学生1
     * userid : 7380
     * state : 缺勤中
     * userheadpic : https://www.cdeps.sc.cn/ClientBin/head.png
     * usermobile : 13687456321
     * illdate : 2020/03/31
     * illcheckdate : 2020/03/31
     * illchecktype : 晨检
     * illtype : 传染病
     * adddate : 2020/03/31 16:30
     * adduser : 管理员
     * adduserheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     */

    private String illid;
    private String username;
    private String userid;
    private String state;
    private String userheadpic;
    private String usermobile;
    private String illdate;
    private String illcheckdate;
    private String illchecktype;
    private String illtype;
    private String adddate;
    private String adduser;
    private String adduserheadpic;
    private List<CheckChild> illstate;

    public List<CheckChild> getIllstate() {
        return illstate;
    }

    public void setIllstate(List<CheckChild> illstate) {
        this.illstate = illstate;
    }

    public String getIllid() {
        return illid;
    }

    public void setIllid(String illid) {
        this.illid = illid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserheadpic() {
        return userheadpic;
    }

    public void setUserheadpic(String userheadpic) {
        this.userheadpic = userheadpic;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getIlldate() {
        return illdate;
    }

    public void setIlldate(String illdate) {
        this.illdate = illdate;
    }

    public String getIllcheckdate() {
        return illcheckdate;
    }

    public void setIllcheckdate(String illcheckdate) {
        this.illcheckdate = illcheckdate;
    }

    public String getIllchecktype() {
        return illchecktype;
    }

    public void setIllchecktype(String illchecktype) {
        this.illchecktype = illchecktype;
    }

    public String getIlltype() {
        return illtype;
    }

    public void setIlltype(String illtype) {
        this.illtype = illtype;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getAdduser() {
        return adduser;
    }

    public void setAdduser(String adduser) {
        this.adduser = adduser;
    }

    public String getAdduserheadpic() {
        return adduserheadpic;
    }

    public void setAdduserheadpic(String adduserheadpic) {
        this.adduserheadpic = adduserheadpic;
    }
}
