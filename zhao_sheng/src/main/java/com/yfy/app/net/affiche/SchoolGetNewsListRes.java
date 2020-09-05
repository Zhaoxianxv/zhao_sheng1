package com.yfy.app.net.affiche;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SCHOOL_GET_NEWS_LIST +"Response")
public class SchoolGetNewsListRes {

    @Attribute(name = "xmlns", empty = "http://tempuri.org/", required = false)
    public String nameSpace;

    @Element(name = TagFinal.SCHOOL_GET_NEWS_LIST +"Result", required = false)
    public String result;
}
