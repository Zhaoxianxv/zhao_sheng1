package com.yfy.app.net.maintain;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.MAINTENANCE_GET_SECTION +"Response")
public class MaintainGetSectionRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.MAINTENANCE_GET_SECTION +"Result", required = false)
    public String result;
}
