package com.yfy.app.allclass.beans;

import java.util.List;

/**
 * Created by yfyandr on 2018/3/12.
 */

public class Top {

    /**
     * result : true
     * error_code :
     */
    private String result;
    private String error_code;
    private List<TopBean> wbs;

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

    public List<TopBean> getWbs() {
        return wbs;
    }

    public void setWbs(List<TopBean> wbs) {
        this.wbs = wbs;
    }
}
