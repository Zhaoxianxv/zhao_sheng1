package com.yfy.app.net.base;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.BASE_GET_TOKEN +Base.RESPONSE)
public class BaseGetTokenRes {

    @Attribute(name = Base.XMLNS, empty = Base.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.BASE_GET_TOKEN +Base.RESULT, required = false)
    public String result;
}
