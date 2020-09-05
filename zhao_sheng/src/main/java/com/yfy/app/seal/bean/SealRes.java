package com.yfy.app.seal.bean;

import java.util.List;

public class SealRes {

    /**
     * result : true
     * error_code :
     */

    private String result;
    private String error_code;
    private String count;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

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

    //------------signets--apply seal type ----------------

    private List<SealState> signets;

    public List<SealState> getSignets() {
        return signets;
    }

    public void setSignets(List<SealState> signets) {
        this.signets = signets;
    }

    //--------------------user-------------------------

    private List<Approval> Approvallist;

    public List<Approval> getApprovallist() {
        return Approvallist;
    }

    public void setApprovallist(List<Approval> approvallist) {
        Approvallist = approvallist;
    }

    //-------------------main-bean---------------
    private List<SealMainBean> signetlist;

    public List<SealMainBean> getSignetlist() {
        return signetlist;
    }

    public void setSignetlist(List<SealMainBean> signetlist) {
        this.signetlist = signetlist;
    }

    //-------------------borrowlist-------------

    private List<SealTag> borrowlist;

    public List<SealTag> getBorrowlist() {
        return borrowlist;
    }

    public void setBorrowlist(List<SealTag> borrowlist) {
        this.borrowlist = borrowlist;
    }
}
