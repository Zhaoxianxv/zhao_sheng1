package com.yfy.app.exchang.bean;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/23.
 */

public class ExchangeInfor {
    /**
     * result : false
     * error_code :
     */
    private String result;
    private String error_code;
    public void setResult(String result) {
        this.result = result;
    }
    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
    public String getResult() {
        return result;
    }
    public String getError_code() {
        return error_code;
    }

    /****我的课程****/
    private String maxno;
    private List<ScheduleInfor> schedule;

    public List<ScheduleInfor> getSchedule() {
        return schedule;
    }
    public void setSchedule(List<ScheduleInfor> schedule) {
        this.schedule = schedule;
    }
    public String getMaxno() {
        return maxno;
    }
    public void setMaxno(String maxno) {
        this.maxno = maxno;
    }

    /**
     * 与我调课
     */
    private List<MyCouyseBean> schedulelist;
    public List<MyCouyseBean> getSchedulelist() {
        return schedulelist;
    }
    public void setSchedulelist(List<MyCouyseBean> schedulelist) {
        this.schedulelist = schedulelist;
    }
}
