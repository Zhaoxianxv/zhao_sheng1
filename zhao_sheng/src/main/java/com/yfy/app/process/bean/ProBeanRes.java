package com.yfy.app.process.bean;

import com.yfy.app.notice.bean.NoticeBean;
import com.yfy.app.notice.bean.ReadState;
import com.yfy.app.notice.bean.SendNotice;

import java.util.List;

public class ProBeanRes {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;
    private List<ProBeanGroup> data;

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


    public List<ProBeanGroup> getData() {
        return data;
    }

    public void setData(List<ProBeanGroup> data) {
        this.data = data;
    }
}
