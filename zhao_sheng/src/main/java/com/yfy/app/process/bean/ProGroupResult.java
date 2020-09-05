package com.yfy.app.process.bean;

import java.util.List;

public class ProGroupResult {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;

    /**
     * id : 14.0
     * step : 0.0
     * HeadPic :
     * content :
     * valuetype : select
     * adddate :
     * title : 预约时间
     * cannull : 0
     * unit :
     * min : 0
     * max : 1
     * selects : 上午第一节,上午第二节,上午第三节,上午第四节,中午午休,下午第一节,下午第二节,下午第三节,下午第四节
     * isshow : 1
     * recordlinkvalue : []
     * recordvaluearray : []
     */

    private double id;
    private double step;
    private String HeadPic;
    private String content;
    private String valuetype;
    private String adddate;
    private String title;
    private String cannull;
    private String unit;
    private String min;
    private String max;
    private String selects;
    private String isshow;
    private List<ProLayerTwoData> recordlinkvalue;
    private List<ProLinkListBean> recordvaluearray;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCannull() {
        return cannull;
    }

    public void setCannull(String cannull) {
        this.cannull = cannull;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getSelects() {
        return selects;
    }

    public void setSelects(String selects) {
        this.selects = selects;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

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

    public List<ProLayerTwoData> getRecordlinkvalue() {
        return recordlinkvalue;
    }

    public void setRecordlinkvalue(List<ProLayerTwoData> recordlinkvalue) {
        this.recordlinkvalue = recordlinkvalue;
    }

    public List<ProLinkListBean> getRecordvaluearray() {
        return recordvaluearray;
    }

    public void setRecordvaluearray(List<ProLinkListBean> recordvaluearray) {
        this.recordvaluearray = recordvaluearray;
    }
}
