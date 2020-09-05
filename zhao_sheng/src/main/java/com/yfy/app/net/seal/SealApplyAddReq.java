package com.yfy.app.net.seal;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SEAL_SUBMIT, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"signetid","typeid","approve",
        "startdate","enddate","tableid","tablecontent"})
public class SealApplyAddReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "startdate", required = false)
    private String startdate;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "enddate", required = false)
    private String enddate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name ="signetid", required = false)
    private int signetid=-1;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "typeid", required = false)
    private int typeid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "approve", required = false)
    private int approve;


    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] tableid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] tablecontent;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setSignetid(int signetid) {
        this.signetid = signetid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public void setTableid(String[] tableid) {
        this.tableid = tableid;
    }

    public void setTablecontent(String[] tablecontent) {
        this.tablecontent = tablecontent;
    }
}
