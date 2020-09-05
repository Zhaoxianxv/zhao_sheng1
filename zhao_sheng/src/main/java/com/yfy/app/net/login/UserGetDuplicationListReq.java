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
@Root(name = TagFinal.USER_GET_DUPLICATION_LIST, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {"username","password","role_id","appid","andios","firsttoken"})
public class UserGetDuplicationListReq {

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "username", required = false)
    public String username;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "password", required = false)
    public String password;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "role_id", required = false)
    public String role_id;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "appid", required = false)
    public String appid;
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "andios", required = false)
    public String andios="and";
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "firsttoken", required = false)
    public String firsttoken;


    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public void setAndios(String andios) {
        this.andios = andios;
    }

    public void setFirsttoken(String firsttoken) {
        this.firsttoken = firsttoken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
