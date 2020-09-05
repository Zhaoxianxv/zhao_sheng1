package com.yfy.app.net.base;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.USER_RESET_PASSWORD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {"userinfo"})
public class UserResetPasswordReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "userinfo", required = false)
    private String userinfo;

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }
}
