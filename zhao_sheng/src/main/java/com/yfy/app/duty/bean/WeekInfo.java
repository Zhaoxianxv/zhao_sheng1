package com.yfy.app.duty.bean;

import java.util.List;

public class WeekInfo {


    /**
     * termid : 1005
     * termname : 2018-2019下期
     * startdate : 2019/02/18
     * enddate : 2019/06/24
     * isCurrentTerm : true
     */

    private String termid;
    private String termname;
    private String startdate;
    private String enddate;
    private String isCurrentTerm;

    private List<Week> weeks;

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    public String getTermid() {
        return termid;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public String getTermname() {
        return termname;
    }

    public void setTermname(String termname) {
        this.termname = termname;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getIsCurrentTerm() {
        return isCurrentTerm;
    }

    public void setIsCurrentTerm(String isCurrentTerm) {
        this.isCurrentTerm = isCurrentTerm;
    }
}
