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
@Root(name = TagFinal.PROCESS_GET_USER_COUNT, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.type})
public class ProcessGetUserCountReq {


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;

    public void setType(String type) {
        this.type = type;
    }
}

