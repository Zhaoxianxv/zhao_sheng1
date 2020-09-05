package com.yfy.app.net.process;

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
@Root(name = TagFinal.PROCESS_ADD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,Base.type,"termid","groupid","idlist","contentlist"})
public class ProcessApplyDetailReq {


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "termid", required = false)
    private int termid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "groupid", required = false)
    private int groupid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] idlist;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] contentlist;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTermid(int termid) {
        this.termid = termid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public void setIdlist(String[] idlist) {
        this.idlist = idlist;
    }

    public void setContentlist(String[] contentlist) {
        this.contentlist = contentlist;
    }
}

