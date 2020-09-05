package com.yfy.app.attennew.bean;

import com.yfy.app.maintainnew.bean.MainBean;

import java.util.List;

/**
 * Created by yfyandr on 2017/12/28.
 */

public class AttenRes {

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

    //--------------------------

    private List<AttenBean> attendance_lists;

    public List<AttenBean> getMaintains() {
        return attendance_lists;
    }

    public void setMaintains(List<AttenBean> maintains) {
        attendance_lists = maintains;
    }


    //需要处理次数
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    //---------------q请假类型---------
    private List<AttenType> kqtype;

    public List<AttenType> getKqtype() {
        return kqtype;
    }

    public void setKqtype(List<AttenType> kqtype) {
        this.kqtype = kqtype;
    }
}
