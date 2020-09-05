package com.yfy.app.event.bean;

import com.yfy.app.duty.bean.Week;
import com.yfy.app.event.bean.Dep;
import com.yfy.app.event.bean.EventDep;
import com.yfy.app.event.bean.EventInfo;

import java.util.List;

public class EventRes {


    /**
     * result : true
     * error_code :
     */

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

    /**
     * -----------dep--------------------
     */
    private List<Dep> evenet_depart;

    public List<Dep> getEvenet_depart() {
        return evenet_depart;
    }

    public void setEvenet_depart(List<Dep> evenet_depart) {
        this.evenet_depart = evenet_depart;
    }


    /**
     * -----------------------week----------
     */
    private List<Week> evenet_week;

    public List<Week> getEvenet_week() {
        return evenet_week;
    }

    public void setEvenet_week(List<Week> evenet_week) {
        this.evenet_week = evenet_week;
    }


    /**
     * --------------------date----------------
     */
    private List<EventInfo> date_list;

    public List<EventInfo> getDate_list() {
        return date_list;
    }

    public void setDate_list(List<EventInfo> date_list) {
        this.date_list = date_list;
    }
    /**
     * ----------------event week---------------
     */
    private String preweek;//0的时候不请求
    private String nextweek;
    private List<EventDep> event_deplist;

    public String getPreweek() {
        return preweek;
    }

    public void setPreweek(String preweek) {
        this.preweek = preweek;
    }

    public String getNextweek() {
        return nextweek;
    }

    public void setNextweek(String nextweek) {
        this.nextweek = nextweek;
    }


    public List<EventDep> getEvent_deplist() {
        return event_deplist;
    }

    public void setEvent_deplist(List<EventDep> event_deplist) {
        this.event_deplist = event_deplist;
    }
}
