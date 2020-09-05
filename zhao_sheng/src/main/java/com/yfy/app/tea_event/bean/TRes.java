package com.yfy.app.tea_event.bean;

import java.util.List;

public class TRes {
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

    private List<TeaClass> evaluatelist;

    public List<TeaClass> getEvaluatelist() {
        return evaluatelist;
    }

    public void setEvaluatelist(List<TeaClass> evaluatelist) {
        this.evaluatelist = evaluatelist;
    }
}
