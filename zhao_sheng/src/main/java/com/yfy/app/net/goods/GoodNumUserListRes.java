package com.yfy.app.net.goods;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.GOODS_NUM_GET_USER_LIST +"Response")
public class GoodNumUserListRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.GOODS_NUM_GET_USER_LIST +"Result", required = false)
    public String result;
}
