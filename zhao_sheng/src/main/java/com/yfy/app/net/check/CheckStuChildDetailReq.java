package com.yfy.app.net.check;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.CHECK_GET_STU_ALL_DETAIL, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"userid","usertype"})
public class CheckStuChildDetailReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();     ///


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "userid", required = false)
    private int userid;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "usertype", required = false)
    private String usertype= Base.user_check_type;

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
