package com.yfy.app.net.authentication;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.AUTHENTICATION_GET_STU, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {"stuid"})
public class AuthenticationGetDataReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "stuid", required = false)
    private String stuid;

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }
}
