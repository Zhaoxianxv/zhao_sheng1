package com.yfy.app.net.maintain;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name =  TagFinal.MAINTENANCE_GET_MAIN_LIST_ADMIN +Base.RESPONSE)
public class MaintainGetAdminListRes {

    @Attribute(name = Base.XMLNS, empty = Base.NAMESPACE, required = false)
    private String nameSpace;


    @Element(name =  TagFinal.MAINTENANCE_GET_MAIN_LIST_ADMIN +Base.RESULT, required = false)
    public String result;
}
