package com.yfy.app.duty.bean;


import java.util.ArrayList;
import java.util.List;

public class AddBean {

    /**
     * id : 1
     * title : 上学时段校园情况是否正常
     * dutyreport_detail : []
     */
    private String id;
    private String title;
    private String detailid;
    private String content="";//小结保存数据(edit)
    private String isnormal="";//one
    private String image="";//one
    private List<String> addImg=new ArrayList<>();//添加数据(icon)

    private String time_name;
    private String type_name;
    private boolean type_top=false;//判断top数据


    public List<String> getAddImg() {
        return addImg;
    }

    public void setAddImg(List<String> addImg) {
        this.addImg = addImg;
    }

    public String getDetailid() {
        return detailid;
    }

    public void setDetailid(String detailid) {
        this.detailid = detailid;
    }

    public String getIsnormal() {
        return isnormal;
    }

    public void setIsnormal(String isnormal) {
        this.isnormal = isnormal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType_top(boolean type_top) {
        this.type_top = type_top;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AddBean(boolean type_top) {
        this.type_top = type_top;
    }

    public String getTime_name() {
        return time_name;
    }

    public void setTime_name(String time_name) {
        this.time_name = time_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }



    public boolean isType_top() {
        return type_top;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
