package com.yfy.app.cyclopedia.beans;

import java.io.Serializable;

/**
 * Created by yfy1 on 2017/1/18.
 */

public class AncyclopediaList implements Serializable{

    /**
     * title : 牵牛花分布范围
     * id : 8
     * keyword : 自然
     * keyword_id : 2
     * tag_title : 生长习性
     * tag_id : 6
     * parentname : 牵牛花
     * parentid : 3
     * count : 2
     * images : [{"url":"https://imgsa.baidu.com/baike/s%3D220/sign=e832c943d11b0ef468e89f5cedc551a1/cefc1e178a82b9010660425e708da9773812ef8a.jpg","title":""}]
     * url : http://www.cdeps.sc.cn/bkdetail.aspx?id=8
     */

    private String title;
    private String id;
    private String keyword;
    private String keyword_id;
    private String tag_title;
    private String tag_id;
    private String parentname;
    private String parentid;
    private String count;
    private String url;
    private String images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword_id() {
        return keyword_id;
    }

    public void setKeyword_id(String keyword_id) {
        this.keyword_id = keyword_id;
    }

    public String getTag_title() {
        return tag_title;
    }

    public void setTag_title(String tag_title) {
        this.tag_title = tag_title;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }




}
