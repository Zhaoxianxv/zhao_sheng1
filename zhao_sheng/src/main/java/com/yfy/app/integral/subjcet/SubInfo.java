package com.yfy.app.integral.subjcet;

import java.util.List;

/**
 * Created by yfyandr on 2018/1/24.
 */

public class SubInfo {

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

    private List<StuAwrad> award;

    public List<StuAwrad> getAward() {
        return award;
    }

    public void setAward(List<StuAwrad> award) {
        this.award = award;
    }
}
