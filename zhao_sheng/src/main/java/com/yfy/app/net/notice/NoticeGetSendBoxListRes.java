package com.yfy.app.net.notice;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.NOTICE_GET_SEND_BOX_LIST +"Response")
public class NoticeGetSendBoxListRes {
    @Attribute(name = "xmlns", empty = Base.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.NOTICE_GET_SEND_BOX_LIST +"Result", required = false)
    public String result;
}
