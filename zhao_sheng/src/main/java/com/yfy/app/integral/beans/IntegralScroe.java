package com.yfy.app.integral.beans;

/**
 * Created by yfyandr on 2017/6/14.
 */

public class IntegralScroe {

    /**
     * score : 2
     * id : 2
     * state : 通过
     * type : 签到
     */
    private String score;
    private String id;
    private String state;
    private String type;

    public void setScore(String score) {
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }
}
