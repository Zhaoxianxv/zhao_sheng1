package com.yfy.app.net.vote;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.VOTE_GET_MAIN_LIST+"Response")
public class VoteGetMainListRes {

    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.VOTE_GET_MAIN_LIST+"Result", required = false)
    public String result;

}
