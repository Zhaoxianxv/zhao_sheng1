package com.yfy.app.duty.bean;

import java.util.List;

public class Info {

    /**
     * date : 2019/03/08
     */

    private String date;
    private String termname;
    private String weekname;
    private List<MainInfo> dutyreport_type;


    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public String getWeekname() {
        return weekname;
    }

    public void setWeekname(String weekname) {
        this.weekname = weekname;
    }

    public List<MainInfo> getDutyreport_type() {
        return dutyreport_type;
    }

    public void setDutyreport_type(List<MainInfo> dutyreport_type) {
        this.dutyreport_type = dutyreport_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
