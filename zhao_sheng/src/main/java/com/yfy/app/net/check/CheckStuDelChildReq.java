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
@Root(name = TagFinal.CHECK_DEL_CHILD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"illid","illstateid","usertype"})
public class CheckStuDelChildReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();     ///
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "illid", required = false)
    private String illid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "illstateid", required = false)
    private String illstateid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "usertype", required = false)
    private String usertype=Base.user_check_type;

    public void setIllstateid(String illstateid) {
        this.illstateid = illstateid;
    }

    public void setIllid(String illid) {

        this.illid = illid;
    }
}
