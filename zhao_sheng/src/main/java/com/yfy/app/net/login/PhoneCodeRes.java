package com.yfy.app.net.login;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.USER_GET_CODE_TO_SYSTEM_PHONE +"Response")
public class PhoneCodeRes {

    @Attribute(name = "xmlns", empty = "http://tempuri.org/", required = false)
    public String nameSpace;

    @Element(name = TagFinal.USER_GET_CODE_TO_SYSTEM_PHONE +"Result", required = false)
    public String result;
}
