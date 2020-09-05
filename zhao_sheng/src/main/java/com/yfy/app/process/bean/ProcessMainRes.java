package com.yfy.app.process.bean;

import java.util.List;

public class ProcessMainRes {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;
    private String count;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    //---------main-
    private List<ProcessMainBean> data;

    public List<ProcessMainBean> getData() {
        return data;
    }

    public void setData(List<ProcessMainBean> data) {
        this.data = data;
    }
}
