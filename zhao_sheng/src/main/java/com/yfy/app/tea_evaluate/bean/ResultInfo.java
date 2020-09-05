package com.yfy.app.tea_evaluate.bean;

import com.yfy.app.bean.YearData;

import java.util.List;

public class ResultInfo {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }


    /**
     * ------------------奖项类型-------------------
     */
    private List<JudgeBean> judge_statistics_class;


    public List<JudgeBean> getJudge_statistics_class() {
        return judge_statistics_class;
    }

    public void setJudge_statistics_class(List<JudgeBean> judge_statistics_class) {
        this.judge_statistics_class = judge_statistics_class;
    }

    /**
     * ------------------奖项类型-------------------
     */
    private List<ChartInfo> judge_statistics;


    public List<ChartInfo> getJudge_statistics() {
        return judge_statistics;
    }

    public void setJudge_statistics(List<ChartInfo> judge_statistics) {
        this.judge_statistics = judge_statistics;
    }

    /**
     * ------------------奖项类型-------------------
     */
    private  List<EvaluateMain> judge_class;

    public void setJudge_class(List<EvaluateMain> judge_class) {
        this.judge_class = judge_class;
    }

    public List<EvaluateMain> getJudge_class() {
        return judge_class;
    }
    /**
     * ------------param--------------
     */
    private List<AddPararem> judge_parameter;

    public List<AddPararem> getJudge_parameter() {
        return judge_parameter;
    }

    public void setJudge_parameter(List<AddPararem> judge_parameter) {
        this.judge_parameter = judge_parameter;
    }
    /**
     * ---------------year--------------------
     */
    private List<YearData> judge_year;

    public List<YearData> getJudge_year() {
        return judge_year;
    }

    public void setJudge_year(List<YearData> judge_year) {
        this.judge_year = judge_year;
    }
    /**
     * ------------------------year------------------------
     */
    private List<ItemBean> judge_record;

    public List<ItemBean> getJudge_record() {
        return judge_record;
    }

    public void setJudge_record(List<ItemBean> judge_record) {
        this.judge_record = judge_record;
    }


    /**
     * -------------------详情----------------
     */

}
