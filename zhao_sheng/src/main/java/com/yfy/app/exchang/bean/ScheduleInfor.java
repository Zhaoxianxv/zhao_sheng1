package com.yfy.app.exchang.bean;





import java.util.List;

/**
 * Created by yfyandr on 2017/8/23.
 */

public class ScheduleInfor {
    /**
     * date : 2017/8/21
     * dayid : 1
     * day : 星期一
     */
    private String date;
    private String dayid;
    private String day;
    private List<CourseInfor> course;

    public List<CourseInfor> getCourse() {
        return course;
    }

    public void setCourse(List<CourseInfor> course) {
        this.course = course;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDayid(String dayid) {
        this.dayid = dayid;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public String getDayid() {
        return dayid;
    }

    public String getDay() {
        return day;
    }
}
