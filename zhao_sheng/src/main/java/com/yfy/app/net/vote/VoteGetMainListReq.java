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
@Root(name = TagFinal.VOTE_GET_MAIN_LIST, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {Base.session_key,Base.page,Base.size})
public class VoteGetMainListReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "size", required = false)
    private int size;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "page", required = false)
    public int page;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
