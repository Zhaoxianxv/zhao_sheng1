package com.yfy.app.integral.beans;

/**
 * Created by yfyandr on 2017/6/6.
 */

public class IntegralGrow {

    /**
     * result :
     * tagname : 身高(cm)
     * isedit : false
     * tagid : 1
     * inputtype : 小数
     */
    private String result;
    private String tagname;
    private String isedit;
    private String tagid;
    private String inputtype;
    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public void setIsedit(String isedit) {
        this.isedit = isedit;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public void setInputtype(String inputtype) {
        this.inputtype = inputtype;
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

    public String getTagid() {
        return tagid;
    }

    public String getInputtype() {
        return inputtype;
    }
}
