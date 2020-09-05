package com.yfy.app.net.applied_order;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/10.
 */
@Root(name = TagFinal.ORDER_ADMIN_SET, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {TagFinal.session_key,Base.id,"status","reply"})
public class OrderAdminSetReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "status", required = false)
    private int status;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "reply", required = false)
    private String reply;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}
