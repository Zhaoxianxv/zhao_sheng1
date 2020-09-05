package com.yfy.app.net.goods;

import com.yfy.final_tag.TagFinal;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.GOODS_GET_MASTER_USER +"Response")
public class GoodGetMasterUserRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.GOODS_GET_MASTER_USER +"Result", required = false)
    public String result;
}
