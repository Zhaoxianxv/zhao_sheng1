package com.yfy.app.net.atten;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.ATTENDANCE_GET_AUDITOR_LIST +"Response")
public class AttenGetAuditorListRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.ATTENDANCE_GET_AUDITOR_LIST +"Result", required = false)
    public String result;
}
