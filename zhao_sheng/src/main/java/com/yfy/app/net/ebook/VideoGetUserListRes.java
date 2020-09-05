package com.yfy.app.net.ebook;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.VIDEO_GET_MAIN_LIST+"Response")
public class VideoGetUserListRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name =  TagFinal.VIDEO_GET_MAIN_LIST+"Result", required = false)
    public String result;
}
