package com.yfy.app.net.goods;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.GOODS_NUM_DO_STATE, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"state","content"})
public class GoodNumDoReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "state", required = false)
    private int state;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "content", required = false)
    private String content;


    public void setId(String id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
