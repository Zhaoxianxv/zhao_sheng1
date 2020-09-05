package com.yfy.app.integral.beans;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/6.
 */

public class IntegralResult {

    /**
     * result : true
     * score : 0
     * error_code :
     * info : 9岁
     */
    private String result;
    private String score;
    private String error_code;
    private String info;

    public void setResult(String result) {
        this.result = result;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public String getScore() {
        return score;
    }

    public String getError_code() {
        return error_code;
    }

    public String getInfo() {
        return info;
    }



    /**
     *   grow,shuju
     */
    private List<IntegralGrow> Results;

    public List<IntegralGrow> getResults() {
        return Results;
    }

    public void setResults(List<IntegralGrow> results) {
        Results = results;
    }

    /**
     * 积分详情
     */

    private String points_count;
    private String pagecount;
    private List<IntegralScroe> points;
    public String getPoints_count() {
        return points_count;
    }
    public void setPoints_count(String points_count) {
        this.points_count = points_count;
    }
    public List<IntegralScroe> getPoints() {
        return points;
    }
    public void setPoints(List<IntegralScroe> points) {
        this.points = points;
    }
    public String getPagecount() {
        return pagecount;
    }
    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    /**
     * honor_type
     */
    private List<String> rewardtypes;

    public List<String> getRewardtypes() {
        return rewardtypes;
    }

    public void setRewardtypes(List<String> rewardtypes) {
        this.rewardtypes = rewardtypes;
    }
    /**
     * get_stu_reward
     */
    private List<HonorStu> reward;

    public List<HonorStu> getReward() {
        return reward;
    }

    public void setReward(List<HonorStu> reward) {
        this.reward = reward;
    }
    /**
     * get_award_class
     */
    private List<ClassName> classes;

    public List<ClassName> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassName> classes) {
        this.classes = classes;
    }

    /**
     * TagFinal.AWARD_TEA_CLASS_LIST
     */
    private List<com.yfy.app.integral.subjcet.ClassName> courseclass;


    public List<com.yfy.app.integral.subjcet.ClassName> getCourseclass() {
        return courseclass;
    }

    public void setCourseclass(List<com.yfy.app.integral.subjcet.ClassName> courseclass) {
        this.courseclass = courseclass;
    }

    /**
     * teawords
     */
    private List<TermCommt> teawords;

    public List<TermCommt> getTeawords() {
        return teawords;
    }

    public void setTeawords(List<TermCommt> teawords) {
        this.teawords = teawords;
    }

    /**
     * SubjectStu
     */
    private List<SubjectStu> award;

    public List<SubjectStu> getAward() {
        return award;
    }

    public void setAward(List<SubjectStu> award) {
        this.award = award;
    }
    /**
     * courses
     */
    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }



    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
