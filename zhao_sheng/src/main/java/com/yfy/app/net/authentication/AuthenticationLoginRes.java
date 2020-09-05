package com.yfy.app.net.authentication;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.AUTHENTICATION_LOGIN +"Response")
public class AuthenticationLoginRes {

    @Attribute(name = "xmlns", empty = Base.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.AUTHENTICATION_LOGIN +"Result", required = false)
    public String result;
}
