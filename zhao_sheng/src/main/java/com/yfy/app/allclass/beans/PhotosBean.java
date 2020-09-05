package com.yfy.app.allclass.beans;

import java.io.Serializable;

/**
 * Created by yfy1 on 2017/3/17.
 */

public  class PhotosBean implements Serializable{
    /**
     * detailPhoto : http://www.cdeps.sc.cn/uploadfile/web/31462/201703130518131370.jpg
     * simplePhoto : http://www.cdeps.sc.cn/uploadfile/web/31462/201703130518131370_m.jpg
     */

    private String detailPhoto;
    private String simplePhoto;

    public String getDetailPhoto() {
        return detailPhoto;
    }

    public void setDetailPhoto(String detailPhoto) {
        this.detailPhoto = detailPhoto;
    }

    public String getSimplePhoto() {
        return simplePhoto;
    }

    public void setSimplePhoto(String simplePhoto) {
        this.simplePhoto = simplePhoto;
    }

    @Override
    public String toString() {
        return "PhotosBean{" +
                "detailPhoto='" + detailPhoto + '\'' +
                ", simplePhoto='" + simplePhoto + '\'' +
                '}';
    }
}
