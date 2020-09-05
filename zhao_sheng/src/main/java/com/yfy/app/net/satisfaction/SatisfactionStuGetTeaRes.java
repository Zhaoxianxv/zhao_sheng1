package com.yfy.app.net.satisfaction;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SATISFACTION_STU_GET_TEAS +"Response")
public class SatisfactionStuGetTeaRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.SATISFACTION_STU_GET_TEAS +"Result", required = false)
    public String result;
}
