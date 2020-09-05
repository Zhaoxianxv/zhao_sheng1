package com.yfy.app.check.bean;

import java.util.List;

public class CheckRes {

    /**
     * result : true
     * error_code :
     * classlist : [{"classname":"班级名称","classid":"班级id","晨检":"是否检查","午检":"是否检查","晚检":"是否检查","因病缺勤":"返回具体人数如1","illcount":"当前生病人数"}]
     */

    private String result;
    private String error_code;
    private String returndate;

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
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


    //--------------------class-----------------
    private List<ClasslistBean> classlist;
    public List<ClasslistBean> getClasslist() {
        return classlist;
    }

    public void setClasslist(List<ClasslistBean> classlist) {
        this.classlist = classlist;
    }


    //----------------type---------------



    //------------------stu--------------
    private List<CheckStu> users;
    private List<CheckStu> illuserlist;


    public List<CheckStu> getIlluserlist() {
        return illuserlist;
    }

    public void setIlluserlist(List<CheckStu> illuserlist) {
        this.illuserlist = illuserlist;
    }

    public List<CheckStu> getUsers() {
        return users;
    }

    public void setUsers(List<CheckStu> users) {
        this.users = users;
    }

    //-------------------------------check-item--parent----------
    private String illcount;
    private List<ChecKParent> userstate;

    public List<ChecKParent> getUserstate() {
        return userstate;
    }

    public String getIllcount() {
        return illcount;
    }

    public void setIllcount(String illcount) {
        this.illcount = illcount;
    }

    public void setUserstate(List<ChecKParent> userstate) {
        this.userstate = userstate;
    }


    private List<IllTypeGroup> inspecttype;

    public List<IllTypeGroup> getInspecttype() {
        return inspecttype;
    }

    public void setInspecttype(List<IllTypeGroup> inspecttype) {
        this.inspecttype = inspecttype;

    }


    //------------------all----------------

    private List<IllAllBean> illhistory;

    public List<IllAllBean> getIllhistory() {
        return illhistory;
    }

    public void setIllhistory(List<IllAllBean> illhistory) {
        this.illhistory = illhistory;
    }
    //---------------------tj-----------------

    private List<IllGroup> grouplist;

    public List<IllGroup> getGrouplist() {
        return grouplist;
    }

    public void setGrouplist(List<IllGroup> grouplist) {
        this.grouplist = grouplist;
    }


    private List<IllGroup> statistics;


    public List<IllGroup> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<IllGroup> statistics) {
        this.statistics = statistics;
    }
}
