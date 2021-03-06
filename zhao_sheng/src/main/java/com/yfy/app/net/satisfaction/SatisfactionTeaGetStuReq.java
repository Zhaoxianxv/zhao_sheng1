package com.yfy.app.net.satisfaction;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SATISFACTION_TEA_GET_STU, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {TagFinal.session_key,"classid","termid"})
public class SatisfactionTeaGetStuReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();     ///


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "classid", required = false)
    private int classid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "termid", required = false)
    private int termid;


    public void setTermid(int termid) {
        this.termid = termid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }
}
