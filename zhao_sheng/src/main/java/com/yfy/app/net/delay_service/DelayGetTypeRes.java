package com.yfy.app.net.delay_service;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.DELAY_GET_TYPE+"Response")
public class DelayGetTypeRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.DELAY_GET_TYPE+"Result", required = false)
    public String result;
}
