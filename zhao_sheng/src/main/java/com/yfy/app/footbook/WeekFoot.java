package com.yfy.app.footbook;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/5.
 */

public class WeekFoot {

    /**
     * date : 2017/7/11
     * week : 星期二
     */
    private String date;
    private String week;


    private List<Foot> foods;

    public List<Foot> getFoods() {
        return foods;
    }

    public void setFoods(List<Foot> foods) {
        this.foods = foods;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public String getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return date.replace("/","-")+" "+week;
    }
}
