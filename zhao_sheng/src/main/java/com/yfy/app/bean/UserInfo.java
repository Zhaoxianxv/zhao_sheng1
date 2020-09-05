package com.yfy.app.bean;

/**
 * Created by huang on 2017/11/1.
 */
public class UserInfo {
    private String name;
    private String scroe;
    private String mill_scroe;
    private ChildData childData;

    public UserInfo(String name, String scroe, String mill_scroe, ChildData childData) {
        this.name = name;
        this.scroe = scroe;
        this.mill_scroe = mill_scroe;
        this.childData = childData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScroe() {
        return scroe;
    }

    public void setScroe(String scroe) {
        this.scroe = scroe;
    }

    public String getMill_scroe() {
        return mill_scroe;
    }

    public void setMill_scroe(String mill_scroe) {
        this.mill_scroe = mill_scroe;
    }

    public ChildData getChildData() {
        return childData;
    }

    public void setChildData(ChildData childData) {
        this.childData = childData;
    }

}
