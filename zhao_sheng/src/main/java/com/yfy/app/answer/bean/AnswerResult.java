package com.yfy.app.answer.bean;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/2.
 */

public class AnswerResult {


    private  String  result;
    private  String  error_code;
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


    /**
     * ************   get_QA_list
     */

    private List<AnswerListBean> QA_list;

    public List<AnswerListBean> getQA_list() {
        return QA_list;
    }
    public void setQA_list(List<AnswerListBean> QA_list) {
        this.QA_list = QA_list;
    }

    /**
     * **************** QA_list
     */
}
