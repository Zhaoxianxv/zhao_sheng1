package com.yfy.app.net.seal;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SEAL_DO, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"opearid","content"})
public class SealSetDoReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "opearid", required = false)
    private int opearid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "content", required = false)
    private String content;

    public void setId(String id) {
        this.id = id;
    }

    public void setOpearid(int opearid) {
        this.opearid = opearid;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
