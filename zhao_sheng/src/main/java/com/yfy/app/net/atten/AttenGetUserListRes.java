package com.yfy.app.net.atten;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.ATTENDANCE_GET_USER_LIST +"Response")
public class AttenGetUserListRes {
    @Attribute(name = "xmlns", empty = Base.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.ATTENDANCE_GET_USER_LIST +"Result", required = false)
    public String result;
}
