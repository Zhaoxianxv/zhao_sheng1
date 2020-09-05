package com.yfy.app.process.bean;

import java.util.List;

public class ProDetailRes {

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
    private ProDetailInfo data;


    public ProDetailInfo getData() {
        return data;
    }

    public void setData(ProDetailInfo data) {
        this.data = data;
    }
}
