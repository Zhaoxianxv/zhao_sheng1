package com.yfy.app.net.base;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.USER_SIGN, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"appid","andios"})
public class UserSignReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key = Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "appid", required = false)
    private String appid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "andios", required = false)
    private String andios;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setAndios(String andios) {
        this.andios = andios;
    }
}
