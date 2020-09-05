package com.yfy.app.school.bean;

import java.util.List;

public class BannerRes {

    public List<Newsbanner> getData() {
        return data;
    }

    public void setData(List<Newsbanner> data) {
        this.data = data;
    }

    /**
     * ---------------image-------------------
     */
    private List<Newsbanner> data;

}
