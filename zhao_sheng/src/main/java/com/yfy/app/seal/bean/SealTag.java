package com.yfy.app.seal.bean;

public class SealTag {

    /**
     * borrowuser : 管理员
     * borrowheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * borrowsdate : 2020/04/23 16:20
     * borrowedate : 2020/04/23 16:25
     * status : 待审核
     */

    private String borrowuser;
    private String borrowheadpic;
    private String borrowsdate;
    private String borrowedate;
    private String status;

    public String getBorrowuser() {
        return borrowuser;
    }

    public void setBorrowuser(String borrowuser) {
        this.borrowuser = borrowuser;
    }

    public String getBorrowheadpic() {
        return borrowheadpic;
    }

    public void setBorrowheadpic(String borrowheadpic) {
        this.borrowheadpic = borrowheadpic;
    }

    public String getBorrowsdate() {
        return borrowsdate;
    }

    public void setBorrowsdate(String borrowsdate) {
        this.borrowsdate = borrowsdate;
    }

    public String getBorrowedate() {
        return borrowedate;
    }

    public void setBorrowedate(String borrowedate) {
        this.borrowedate = borrowedate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
