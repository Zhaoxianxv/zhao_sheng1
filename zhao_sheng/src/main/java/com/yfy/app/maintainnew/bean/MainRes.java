package com.yfy.app.maintainnew.bean;

import java.util.List;

/**
 * Created by yfyandr on 2017/12/28.
 */

public class MainRes {

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

    private List<MainBean> Maintains;

    public List<MainBean> getMaintains() {
        return Maintains;
    }

    public void setMaintains(List<MainBean> maintains) {
        Maintains = maintains;
    }


    //需要处理次数
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
