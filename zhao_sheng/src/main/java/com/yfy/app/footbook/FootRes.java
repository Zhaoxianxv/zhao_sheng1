package com.yfy.app.footbook;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/5.
 */

public class FootRes {

    /**
     * result : true
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

    /**
     * week_foot
     */
    private List<WeekFoot> cookbook;

    public List<WeekFoot> getCookbook() {
        return cookbook;
    }

    public void setCookbook(List<WeekFoot> cookbook) {
        this.cookbook = cookbook;
    }
}
