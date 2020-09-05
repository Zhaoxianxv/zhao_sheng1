package com.yfy.app.process.bean;

public class ProGroupResultRes {

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
    //---------main-
    private ProGroupResult data;


    public ProGroupResult getData() {
        return data;
    }

    public void setData(ProGroupResult data) {
        this.data = data;
    }
}
