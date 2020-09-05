package com.yfy.app.notice.bean;

import java.util.List;

public class NoticeRes {
    private List<ContactsGroup> userinfotx;
    private List<ContactsGroup> data;


    public List<ContactsGroup> getData() {
        return data;
    }

    public void setData(List<ContactsGroup> data) {
        this.data = data;
    }

    public List<ContactsGroup> getUserinfotx() {
        return userinfotx;
    }

    public void setUserinfotx(List<ContactsGroup> userinfotx) {
        this.userinfotx = userinfotx;
    }


    private List<SendNotice> noticelist;

    public List<SendNotice> getNoticelist() {
        return noticelist;
    }

    public void setNoticelist(List<SendNotice> noticelist) {
        this.noticelist = noticelist;
    }




    /**
     * --------noticestate---------
     */
    private List<ReadState> noticestate;

    public List<ReadState> getNoticestate() {
        return noticestate;
    }

    public void setNoticestate(List<ReadState> noticestate) {
        this.noticestate = noticestate;
    }
    /**
     * ------------------notice-bean-----------
     */

    private List<NoticeBean> mynotice;

    public List<NoticeBean> getMynotice() {
        return mynotice;
    }

    public void setMynotice(List<NoticeBean> mynotice) {
        this.mynotice = mynotice;
    }
}
