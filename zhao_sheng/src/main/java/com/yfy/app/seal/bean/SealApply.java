package com.yfy.app.seal.bean;

import android.util.SparseArray;

import com.yfy.app.bean.KeyValue;

public class SealApply {

    private int view_type;
    private SparseArray<KeyValue> top_value;
    private KeyValue do_seal_time;
    private String type;
    /**
     * tableid : 6
     * tablename : 文件份数
     * defaultvalue :
     * valuetype : float_1
     */

    private String tableid;
    private String tablename;
    private String defaultvalue;
    private String valuetype;
    private String canbenull;
    private boolean isList=false;

    public SealApply(int view_type) {
        this.view_type = view_type;
    }

    public SealApply(int view_type, String type,KeyValue do_seal_time) {
        this.view_type = view_type;
        this.do_seal_time = do_seal_time;
        this.type = type;
    }
    public SealApply(int view_type, String type,KeyValue do_seal_time,boolean isList) {
        this.view_type = view_type;
        this.do_seal_time = do_seal_time;
        this.type = type;
        this.isList = isList;
    }


    public String getCanbenull() {
        return canbenull;
    }

    public void setCanbenull(String canbenull) {
        this.canbenull = canbenull;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }


    public KeyValue getDo_seal_time() {
        return do_seal_time;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDo_seal_time(KeyValue do_seal_time) {
        this.do_seal_time = do_seal_time;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public SparseArray<KeyValue> getTop_value() {
        return top_value;
    }

    public void setTop_value(SparseArray<KeyValue> top_value) {
        this.top_value = top_value;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getDefaultvalue() {
        return defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }
}
