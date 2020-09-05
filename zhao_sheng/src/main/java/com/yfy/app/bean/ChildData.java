package com.yfy.app.bean;


import com.yfy.form.annotation.SmartColumn;

import java.util.List;

/**
 * Created by huang on 2017/11/1.
 */

public class ChildData {

    @SmartColumn(id =5,name = "子类",autoCount = true)
    private String child;
    @SmartColumn(id =7,name = "子类",autoCount = true)
    private List<String> dataList;

    public ChildData(String child) {
        this.child = child;
    }

    public ChildData(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}
