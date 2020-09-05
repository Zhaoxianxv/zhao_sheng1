package com.yfy.app.net.ebook;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.BOOK_GET_USER_LIST+"Response")
public class BookGetUserListRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.BOOK_GET_USER_LIST+"Result", required = false)
    public String result;
}
