package com.yfy.app.seal.bean;

import java.util.List;

public class SealState {

    /**
     * signetname : 电子章
     * signetid : 1
     */

    private String signetname;
    private String signetid;
    private List<SealType> types;

    public List<SealType> getTypes() {
        return types;
    }

    public void setTypes(List<SealType> types) {
        this.types = types;
    }

    public String getSignetname() {
        return signetname;
    }

    public void setSignetname(String signetname) {
        this.signetname = signetname;
    }

    public String getSignetid() {
        return signetid;
    }

    public void setSignetid(String signetid) {
        this.signetid = signetid;
    }
}
