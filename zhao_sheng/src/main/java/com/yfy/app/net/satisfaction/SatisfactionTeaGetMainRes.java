package com.yfy.app.net.satisfaction;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SATISFACTION_TEA_GET_MAIN +"Response")
public class SatisfactionTeaGetMainRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.SATISFACTION_TEA_GET_MAIN +"Result", required = false)
    public String result;
}
