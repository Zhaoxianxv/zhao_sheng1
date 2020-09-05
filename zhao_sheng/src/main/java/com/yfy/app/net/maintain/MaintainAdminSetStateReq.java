package com.yfy.app.net.maintain;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.MAINTENANCE_ADMIN_SET_STATE, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {
        Base.id,
        Base.session_key,
        "realname",
        Base.content,
        Base.date,
        "dealstate",
        "isout",
        "money",
        "pictures",
        "fileContext",
        "delpictures"})
public class MaintainAdminSetStateReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "realname", required = false)
    private String realname;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "dealstate", required = false)
    private String dealstate;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "isout", required = false)
    private int isout;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "money", required = false)
    private String money;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "pictures", required = false)
    private String pictures;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "fileContext", required = false)
    private String fileContext;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "delpictures", required = false)
    private String delpictures;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setFileContext(String fileContext) {
        this.fileContext = fileContext;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDealstate(String dealstate) {
        this.dealstate = dealstate;
    }

    public void setIsout(int isout) {
        this.isout = isout;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setDelpictures(String delpictures) {
        this.delpictures = delpictures;
    }
}
