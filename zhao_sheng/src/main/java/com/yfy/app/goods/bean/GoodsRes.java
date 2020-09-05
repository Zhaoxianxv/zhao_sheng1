package com.yfy.app.goods.bean;

import com.yfy.app.GType;

import java.util.List;

public class GoodsRes {

    /**
     * result : true
     * error_code :
     * page_count : 0
     * office_supply_record : []
     */

    private String result;
    private String error_code;
    private String page_count;

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

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }
    //需要处理次数
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    /**
     * ---------type-------------
     */

    private List<GoodsType> office_supply_class;

    public List<GoodsType> getOffice_supply_class() {
        return office_supply_class;
    }

    public void setOffice_supply_class(List<GoodsType> office_supply_class) {
        this.office_supply_class = office_supply_class;
    }

    /**
     * ------------------mainbean------------------
     */
    private List<GoodsBean> office_supply_record;

    public List<GoodsBean> getOffice_supply_record() {
        return office_supply_record;
    }

    public void setOffice_supply_record(List<GoodsBean> office_supply_record) {
        this.office_supply_record = office_supply_record;
    }

    /**
     * -----------------------office_supply_content------------------
     */
    private List<BeanItem> office_supply_content;

    public List<BeanItem> getOffice_supply_content() {
        return office_supply_content;
    }

    public void setOffice_supply_content(List<BeanItem> office_supply_content) {
        this.office_supply_content = office_supply_content;
    }

    /**
     * -----------------------------------------
     */
    private List<GType> office_supply_type;

    public List<GType> getOffice_supply_type() {
        return office_supply_type;
    }

    public void setOffice_supply_type(List<GType> office_supply_type) {
        this.office_supply_type = office_supply_type;
    }


    private List<GType> office_supply_master;

    public List<GType> getOffice_supply_master() {
        return office_supply_master;
    }

    public void setOffice_supply_master(List<GType> office_supply_master) {
        this.office_supply_master = office_supply_master;
    }

    //-----------------num_user_list--------------------

//    private List<MasterBean> office_supply_record;

    private List<GoodNumBean> office_supply_list;

    public List<GoodNumBean> getOffice_supply_list() {
        return office_supply_list;
    }

    public void setOffice_supply_list(List<GoodNumBean> office_supply_list) {
        this.office_supply_list = office_supply_list;
    }
}
