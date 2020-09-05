package com.yfy.app.net.login;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.USER_LOGOUT, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {Base.session_key})
public class UserLogoutReq {

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();     ///


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }


}
