package com.yfy.app.net.process;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.PROCESS_ADMIN_SET_CLASS +"Response")
public class ProcessAdminSetClassRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.PROCESS_ADMIN_SET_CLASS +"Result", required = false)
    public String result;
}
