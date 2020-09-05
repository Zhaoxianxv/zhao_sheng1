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
@Root(name = TagFinal.BASE_GET_TOKEN, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {"username","password"})
public class BaseGetTokenReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "username", required = false)
    private String username="SRTghhy";

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "password", required = false)
    private String password="DS$dfs@$$%";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
