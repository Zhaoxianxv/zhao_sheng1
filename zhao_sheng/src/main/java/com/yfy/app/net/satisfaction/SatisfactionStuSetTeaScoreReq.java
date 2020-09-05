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
@Root(name = TagFinal.SATISFACTION_STU_SET_TEA_SCORE, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {TagFinal.session_key,"teacherid","coureseid","evaluateid",Base.content})
public class SatisfactionStuSetTeaScoreReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();     ///

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;     ///

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "teacherid", required = false)
    private int teacherid;     //

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "coureseid", required = false)
    private int coureseid;     //

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "evaluateid", required = false)
    private String evaluateid;     //


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public void setCoureseid(int coureseid) {
        this.coureseid = coureseid;
    }

    public void setEvaluateid(String evaluateid) {
        this.evaluateid = evaluateid;
    }
}
