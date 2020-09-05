package com.yfy.app.net.vote;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.VOTE_SUBMIT, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"realname","examid","votecontent"})
public class VoteSubmitReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "realname", required = false)
    private String realname;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "examid", required = false)
    private String examid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "votecontent", required = false)
    private String votecontent;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public void setVotecontent(String votecontent) {
        this.votecontent = votecontent;
    }
}
