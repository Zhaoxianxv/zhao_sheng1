package com.yfy.app.bean;

/**
 * Created by yfyandr on 2017/6/9.
 */
public class TermEntity {
    /**
     * datemax : 2016/9/1
     * datemin : 2017/1/1
     * name : 2016-2017上期
     * id : 2
     */
    private String datemax;
    private String datemin;
    private String name;
    private String id;


    public void setDatemax(String datemax) {
        this.datemax = datemax;
    }

    public void setDatemin(String datemin) {
        this.datemin = datemin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatemax() {
        return datemax;
    }

    public String getDatemin() {
        return datemin;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
