package com.yfy.app.net.atten;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.ATTENDANCE_GET_COUNT +"Response")
public class AttenGetCountRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.ATTENDANCE_GET_COUNT +"Result", required = false)
    public String result;
}
