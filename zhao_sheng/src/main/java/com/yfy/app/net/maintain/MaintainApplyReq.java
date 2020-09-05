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
@Root(name = TagFinal.MAINTENANCE_ADD, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {
        Base.session_key,
        Base.date,
        "nr",
        "address",
        "mobile",
        "username",
        "pictures",
        "fileContext",
        "classid"})
public class MaintainApplyReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "nr", required = false)
    private String nr;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "address", required = false)
    private String address;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "mobile", required = false)
    private String mobile;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "username", required = false)
    private String username;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "pictures", required = false)
    private String pictures;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "fileContext", required = false)
    private String fileContext;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "classid", required = false)
    private String classid;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public void setFileContext(String fileContext) {
        this.fileContext = fileContext;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }
}
