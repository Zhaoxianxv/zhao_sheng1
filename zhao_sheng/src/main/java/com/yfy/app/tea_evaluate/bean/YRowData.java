package com.yfy.app.tea_evaluate.bean;

import com.yfy.app.bean.YearData;

import java.util.List;

public class YRowData {
    private String name ;
    private String name_data ;
    private List<YearData> yearData;

    public YRowData(String name, String name_data, List<YearData> yearData) {
        this.name = name;
        this.name_data = name_data;
        this.yearData = yearData;
    }

    public String getName_data() {
        return name_data;
    }

    public void setName_data(String name_data) {
        this.name_data = name_data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<YearData> getYearData() {
        return yearData;
    }

    public void setYearData(List<YearData> yearData) {
        this.yearData = yearData;
    }
}
