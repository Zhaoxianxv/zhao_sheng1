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
@Root(name = TagFinal.AUTHENTICATION_LOGIN, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {Base.name,Base.phone})
public class AuthenticationLoginReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.name, required = false)
    private String name;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.phone, required = false)
    private String phone;

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
