package com.yfy.app.school.bean;

import com.yfy.final_tag.TagFinal;

import java.util.List;

public class SchoolNews {

    /**
     * newsComment_size : 0
     * news_info_detailed : https://www.cdpxjj.com/detail.aspx?id=257582
     * newslist_head : 成都市泡桐树小学境界分校章程
     * newslist_image : https://www.cdpxjj.com/clientbin/logo.png
     * newslist_point : （2017年1月6日经教职工代表大会审议，2017年1月9日经学校校长办公会议通过。）序言学校发展沿革：泡桐树小学境界分校创办于1940年，前身是梵音寺小学，1996年改名为成都市...
     * newslist_time : 2017/5/11 14:51:00
     */



    private int view_load=TagFinal.TYPE_ITEM;


    private String news_info_detailed;
    private String newslist_head;
    private String newslist_image;
    private String newslist_point;
    private String newslist_time;


    private List<Newsbanner> list;




    public SchoolNews(int view_load) {
        this.view_load = view_load;
    }

    public List<Newsbanner> getList() {
        return list;
    }

    public void setList(List<Newsbanner> list) {
        this.list = list;
    }

    public int getView_load() {
        return view_load;
    }

    public void setView_load(int view_load) {
        this.view_load = view_load;
    }

    public String getNews_info_detailed() {
        return news_info_detailed;
    }

    public void setNews_info_detailed(String news_info_detailed) {
        this.news_info_detailed = news_info_detailed;
    }


    public String getNewslist_head() {
        return newslist_head;
    }

    public void setNewslist_head(String newslist_head) {
        this.newslist_head = newslist_head;
    }

    public String getNewslist_image() {
        return newslist_image;
    }

    public void setNewslist_image(String newslist_image) {
        this.newslist_image = newslist_image;
    }

    public String getNewslist_point() {
        return newslist_point;
    }

    public void setNewslist_point(String newslist_point) {
        this.newslist_point = newslist_point;
    }

    public String getNewslist_time() {
        return newslist_time;
    }

    public void setNewslist_time(String newslist_time) {
        this.newslist_time = newslist_time;
    }
}
