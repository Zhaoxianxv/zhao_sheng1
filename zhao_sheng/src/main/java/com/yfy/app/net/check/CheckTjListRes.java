package com.yfy.app.net.check;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.CHECK_GET_TJ +"Response")
public class CheckTjListRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.CHECK_GET_TJ +"Result", required = false)
    public String result;
}
