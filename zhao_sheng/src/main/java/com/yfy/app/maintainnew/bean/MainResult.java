package com.yfy.app.maintainnew.bean;

import java.util.List;

/**
 * Created by yfyandr on 2017/5/11.
 */

public class MainResult {


    /**
     * result : true
     * error_code :
     * Maintainclass : [{"name":"电脑","id":"1"},{"name":"电教","id":"2"},{"name":"后勤","id":"3"}]
     */
    private String result;
    private String error_code;
    private List<DepTag> Maintainclass;

    public void setResult(String result) {
        this.result = result;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setMaintainclass(List<DepTag> Maintainclass) {
        this.Maintainclass = Maintainclass;
    }

    public String getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public List<DepTag> getMaintainclass() {
        return Maintainclass;
    }


}
