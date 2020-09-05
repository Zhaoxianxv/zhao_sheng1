package com.yfy.app.allclass.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yfy1 on 2017/3/17.
 */

public class ShapeBean implements Serializable{


    /**
     * dynamic_content_text : 请大家分享
     * dynamic_id : 442138
     * headPic : http://www.cdeps.sc.cn/head.png
     * ispraise : 否
     * name : 刘晓虹
     * photos : [{"detailPhoto":"http://www.cdeps.sc.cn/uploadfile/web/31462/201703130518131370.jpg","simplePhoto":"http://www.cdeps.sc.cn/uploadfile/web/31462/201703130518131370_m.jpg"}]
     * praise : 梁晋,张速,苏琴,陈晓燕,钟辉霞,钟键,伍丹,杨栩,奉小刚
     */

    private String dynamic_content_text;
    private String dynamic_id;
    private String headPic;
    private String ispraise;
    private String name;
    private List<ReplyaBean> replya;

    private String praise;
    private String time;
    private List<PhotosBean> photos;

    public List<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosBean> photos) {
        this.photos = photos;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ReplyaBean> getReplyas() {
        return replya;
    }

    public void setReplyas(List<ReplyaBean> replyas) {
        this.replya = replyas;
    }



    public String getDynamic_content_text() {
        return dynamic_content_text;
    }

    public void setDynamic_content_text(String dynamic_content_text) {
        this.dynamic_content_text = dynamic_content_text;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getIspraise() {
        return ispraise;
    }

    public void setIspraise(String ispraise) {
        this.ispraise = ispraise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }


    @Override
    public String toString() {
        return "ShapeBean{" +
                "praise='" + praise + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", headPic='" + headPic + '\'' +
                ", dynamic_id='" + dynamic_id + '\'' +
                ", dynamic_content_text='" + dynamic_content_text + '\'' +
                ", ispraise='" + ispraise + '\'' +
                '}';
    }
}
