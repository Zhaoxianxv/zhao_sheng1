package com.yfy.app.cyclopedia.beans;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/20.
 */

public class Hot {

    /**
     * result : true
     * error_code :
     * hot_list : ["牵牛花","花","哈哈哈哈哈","哈","uu","泰国","树"]
     */

    private String result;
    private String error_code;
    private List<String> hot_list;

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

    public List<String> getHot_list() {
        return hot_list;
    }

    public void setHot_list(List<String> hot_list) {
        this.hot_list = hot_list;
    }
}
