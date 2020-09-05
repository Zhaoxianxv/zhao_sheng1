package com.yfy.app.seal.bean;

import java.util.List;

public class SealType {

    /**
     * typeid : 1
     * typename : 盖章
     */

    private String typeid;
    private String typename;
    private List<SealApply>  tables;

    public List<SealApply> getTables() {
        return tables;
    }

    public void setTables(List<SealApply> tables) {
        this.tables = tables;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
