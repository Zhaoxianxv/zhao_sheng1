package com.yfy.app.cyclopedia.beans;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/10.
 */

public class CyclopediaType {

    private String result;

    private String errorCode;

    private List<AncyclopediaList> ancyclopedia_list;
    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return result;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorCode() {
        return errorCode;
    }

    public void setAncyclopediaList (List<AncyclopediaList> ancyclopedia_list ) {
        this.ancyclopedia_list  = ancyclopedia_list ;
    }
    public List<AncyclopediaList> getAncyclopediaList () {
        return ancyclopedia_list ;
    }

}
