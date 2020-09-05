package com.yfy.app.integral.beans;

/**
 * Created by yfyandr on 2017/9/4.
 */

public class TermCommt {

    /**
     * date : 2017/5/5 0:00:00
     * termid : 2
     * teacherid : 1
     * teachername : 管理员
     * term : 2016-2017上期
     * id : 1
     * title : 管理员老师的话
     * content : 学生很努力，希望下学期继续加油
     */
    private String date;
    private String termid;
    private String teacherid;
    private String teachername;
    private String term;
    private String id;
    private String title;
    private String content;

    public void setDate(String date) {
        this.date = date;
    }

    public void setTermid(String termid) {
        this.termid = termid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public String getTermid() {
        return termid;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public String getTeachername() {
        return teachername;
    }

    public String getTerm() {
        return term;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
