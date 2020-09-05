package com.yfy.app.net.event;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.EVENT_DEL_IMAGE+Base.RESPONSE)
public class EventDelImageRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.EVENT_DEL_IMAGE+Base.RESULT, required = false)
    public String result;
}
