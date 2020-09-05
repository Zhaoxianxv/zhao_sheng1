package com.yfy.app.login.bean;

/**
 * Created by yfyandr on 2017/9/21.
 */

public class UserBaseData {

    /**
     * result : 2010/05/16
     * tagname : 出生日期
     * isedit : true
     * inputtype : 日期
     * option :
     */
    private String result;
    private String tagname;
    private String isedit;
    private String inputtype;
    private String option;

    public void setResult(String result) {
        this.result = result;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

    public void setInputtype(String inputtype) {
        this.inputtype = inputtype;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getResult() {
        return result;
    }

    public String getTagname() {
        return tagname;
    }

    public String getIsedit() {
        return isedit;
    }

    public String getInputtype() {
        return inputtype;
    }

    public String getOption() {
        return option;
    }
}
