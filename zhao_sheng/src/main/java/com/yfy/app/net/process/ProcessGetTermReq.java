package com.yfy.app.net.process;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.PROCESS_GET_TERM, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"groups"})
public class ProcessGetTermReq {


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "groups", required = false)
    private String groups;

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
}

