package com.yfy.app.net.judge;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/22.
 */
@Root(name = TagFinal.TEA_JUDGE_GET_INFO_DETAIL, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id})
public class TeaJudgeGetInfoDetailReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();
    @Element(name = "id", required = false)
    private int id;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(int id) {
        this.id = id;
    }
}
