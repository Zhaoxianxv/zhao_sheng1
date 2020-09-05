package com.yfy.app.net.delay_service;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 */
@Root(name = TagFinal.DELAY_TEA_ADD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {
        Base.session_key,
        "ids",
        "Electiveid",
        "teacherid",
        "stuids",
        Base.content,
        Base.date,
        Base.type,
        Base.image,
        "image_file",
        "stucount",
        "substituteteacher",
        "checktype"})
public class DelayTeaAddReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] ids;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] stuids;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] image;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electiveid", required = false)
    private int Electiveid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "teacherid", required = false)
    private int teacherid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "stucount", required = false)
    private int stucount;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "substituteteacher", required = false)
    private int substituteteacher;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "image_file", required = false)
    private String image_file;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "checktype", required = false)
    private String checktype;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public void setStuids(String[] stuids) {
        this.stuids = stuids;
    }

    public void setImage(String[] image) {
        this.image = image;
    }

    public void setElectiveid(int electiveid) {
        Electiveid = electiveid;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public void setStucount(int stucount) {
        this.stucount = stucount;
    }

    public void setSubstituteteacher(int substituteteacher) {
        this.substituteteacher = substituteteacher;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public void setChecktype(String checktype) {
        this.checktype = checktype;
    }
}
