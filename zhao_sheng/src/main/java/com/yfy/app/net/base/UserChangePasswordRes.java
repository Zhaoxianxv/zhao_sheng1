package com.yfy.app.net.base;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.USER_CHANGE_PASSWORD +"Response")
public class UserChangePasswordRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.USER_CHANGE_PASSWORD +"Result", required = false)
    public String result;
}
