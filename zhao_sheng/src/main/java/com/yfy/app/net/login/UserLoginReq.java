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
@Root(name = TagFinal.USER_LOGIN, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {"username","password","role_id","appid","andios","firsttoken"})
public class UserLoginReq {

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "username", required = false)
    public String username;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "password", required = false)
    public String password;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "role_id", required = false)
    private String role_id;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "appid", required = false)
    private String appid;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "andios", required = false)
    private String andios="and";

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "firsttoken", required = false)
    private String firsttoken;


    public void setFirsttoken(String firsttoken) {
        this.firsttoken = firsttoken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
