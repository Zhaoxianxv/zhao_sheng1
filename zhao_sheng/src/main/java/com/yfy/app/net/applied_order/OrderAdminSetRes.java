package com.yfy.app.net.applied_order;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/10.
 */
@Root(name =  TagFinal.ORDER_ADMIN_SET +Base.RESPONSE)
public class OrderAdminSetRes {
    @Attribute(name = Base.XMLNS, empty = Base.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.ORDER_ADMIN_SET +Base.RESULT, required = false)
    public String result;
}
