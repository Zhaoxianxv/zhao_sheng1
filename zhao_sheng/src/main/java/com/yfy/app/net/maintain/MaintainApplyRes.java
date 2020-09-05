package com.yfy.app.net.maintain;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name =  TagFinal.MAINTENANCE_ADD +Base.RESPONSE)
public class MaintainApplyRes {

    @Attribute(name = Base.XMLNS, empty = Base.NAMESPACE, required = false)
    private String nameSpace;


    @Element(name =  TagFinal.MAINTENANCE_ADD +Base.RESULT, required = false)
    public String result;
}
