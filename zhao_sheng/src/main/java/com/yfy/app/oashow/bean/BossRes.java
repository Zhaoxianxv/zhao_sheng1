package com.yfy.app.oashow.bean;

import com.yfy.app.attennew.bean.AttenBean;

import java.util.List;

public class BossRes {
    private String result;
    private String error_code;

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

    private List<BMaintian> Maintains;

    public List<BMaintian> getMaintains() {
        return Maintains;
    }

    public void setMaintains(List<BMaintian> maintain) {
        Maintains = Maintains;
    }


    private List<Blate> punchCard_list;

    public List<Blate> getPunchCard_list() {
        return punchCard_list;
    }

    public void setPunchCard_list(List<Blate> punchCard_list) {
        this.punchCard_list = punchCard_list;
    }


    //--------------------------

    private List<AttenBean> attendance_lists;

    public List<AttenBean> getAttendance_lists() {
        return attendance_lists;
    }

    public void setAttendance_lists(List<AttenBean> attendance_lists) {
        this.attendance_lists = attendance_lists;
    }

    /**
     * =================================
     */
    private List<Admin> my_funcRoom;

    public List<Admin> getAdmin() {
        return my_funcRoom;

    }
}