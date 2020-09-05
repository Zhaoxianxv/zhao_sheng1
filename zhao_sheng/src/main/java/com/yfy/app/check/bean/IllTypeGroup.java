package com.yfy.app.check.bean;

import java.util.List;

public class IllTypeGroup  {


    /**
     * illtypegroupname : 传染病
     */

    private String illtypegroupname;
    private List<IllType> illtypegroup;




    public List<IllType> getIlltypegroup() {
        return illtypegroup;
    }

    public void setIlltypegroup(List<IllType> illtypegroup) {
        this.illtypegroup = illtypegroup;
    }

    public String getIlltypegroupname() {
        return illtypegroupname;
    }

    public void setIlltypegroupname(String illtypegroupname) {
        this.illtypegroupname = illtypegroupname;
    }
}
