package com.yfy.app.event.bean;

import com.yfy.app.event.bean.EventBean;

import java.util.List;

public class EventInfo {
    private String date;
    private String weekname;
    private String termname;

    private List<EventBean> event_list;


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<EventBean> getEvent_list() {
        return event_list;
    }

    public void setEvent_list(List<EventBean> event_list) {
        this.event_list = event_list;
    }
}
