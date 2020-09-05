package com.yfy.app.tea_evaluate.bean;

import java.util.List;

public class ResultJudge {
    private String result;
    private String error_code;
    private String attachment;
    private String issubmit;
    private String state;
    private String reason;
    private String score;
    private List<ParamBean> judge_record;

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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getIssubmit() {
        return issubmit;
    }

    public void setIssubmit(String issubmit) {
        this.issubmit = issubmit;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<ParamBean> getJudge_record() {
        return judge_record;
    }

    public void setJudge_record(List<ParamBean> judge_record) {
        this.judge_record = judge_record;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
