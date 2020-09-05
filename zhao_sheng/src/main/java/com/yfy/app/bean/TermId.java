package com.yfy.app.bean;

import com.yfy.app.login.bean.UserBaseData;

import java.util.List;

public class TermId {


    /**
     * result : true
     * error_code :
     * term : {"datemax":"2016/9/1","datemin":"2017/1/1","name":"2016-2017上期","id":"2"}
     */
    private String result;
    private String error_code;
    private TermEntity term;

    public void setResult(String result) {
        this.result = result;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setTerm(TermEntity term) {
        this.term = term;
    }

    public String getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public TermEntity getTerm() {
        return term;
    }


    /**
     * sign签到
     */

    private String islogin;

    public String getIslogin() {
        return islogin;
    }

    public void setIslogin(String islogin) {
        this.islogin = islogin;
    }


    /**
     *  user base data
     */
    private List<UserBaseData> stuinfo;

    public List<UserBaseData> getStuinfo() {
        return stuinfo;
    }

    public void setStuinfo(List<UserBaseData> stuinfo) {
        this.stuinfo = stuinfo;
    }
}
