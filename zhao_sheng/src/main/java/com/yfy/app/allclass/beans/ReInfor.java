package com.yfy.app.allclass.beans;

import java.util.List;

/**
 * Created by yfy1 on 2017/3/16.
 */

public class ReInfor {

    /**
     * result : true
     * error_code :
     * tags : [{"tag_name":"人文","tag_id":"1"},{"tag_name":"开学校园","tag_id":"2"}]
     */
    private String result;
    private String error_code;
    private List<ShapeKind> tags;


    public List<ShapeKind> getTags() {
        return tags;
    }

    public void setTags(List<ShapeKind> tags) {
        this.tags = tags;
    }

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

    /**
     * shape main list
     */

    private String count;
    private List<ShapeMainList> wbs;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ShapeMainList> getWbs() {
        return wbs;
    }

    public void setWbs(List<ShapeMainList> wbs) {
        this.wbs = wbs;
    }

    /**
     * replay list
     */
    private List<ReplyaBean> replys;

    public List<ReplyaBean> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplyaBean> replys) {
        this.replys = replys;
    }


}
