package com.yfy.app.net.judge;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.TEA_JUDGE_GET_ADD_TYPE +"Response")
public class TeaJudgeGetAddTypeRes {
    @Attribute(name = "xmlns", empty = TagFinal.NAMESPACE, required = false)
    public String nameSpace;

    @Element(name = TagFinal.TEA_JUDGE_GET_ADD_TYPE +"Result", required = false)
    public String result;
}
