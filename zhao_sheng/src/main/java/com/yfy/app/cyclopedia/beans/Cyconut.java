package com.yfy.app.cyclopedia.beans;

/**
 * Created by yfy1 on 2017/1/18.
 */

public class Cyconut {

    /**
     * result : true
     * error_code :
     * entry_count  : 4
     * browse_count : 0
     */

    private String result;
    private String error_code;
    private String entry_count;
    private String browse_count;

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

    public String getEntry_count() {
        return entry_count;
    }

    public void setEntry_count(String entry_count) {
        this.entry_count = entry_count;
    }

    public String getBrowse_count() {
        return browse_count;
    }

    public void setBrowse_count(String browse_count) {
        this.browse_count = browse_count;
    }
}
