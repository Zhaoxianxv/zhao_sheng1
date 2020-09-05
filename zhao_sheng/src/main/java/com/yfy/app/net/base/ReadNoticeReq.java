package com.yfy.app.net.base;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.READNOTICE, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.type})
public class ReadNoticeReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();
    ///
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    public String type;     ///

    public void setType(String type) {
        this.type = type;
    }
}
