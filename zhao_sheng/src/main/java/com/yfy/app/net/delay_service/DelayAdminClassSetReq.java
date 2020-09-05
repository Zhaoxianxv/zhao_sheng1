package com.yfy.app.net.delay_service;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.DELAY_ADMIN_CLASS_SET, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.date,"Electivedetail","leavedetail","Electivedetailpic","leavedetailpic"})
public class DelayAdminClassSetReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electivedetail", required = false)
    private String Electivedetail;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "leavedetail", required = false)
    private String leavedetail;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electivedetailpic", required = false)
    private String Electivedetailpic;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "leavedetailpic", required = false)
    private String leavedetailpic;


    public void setLeavedetail(String leavedetail) {
        this.leavedetail = leavedetail;
    }

    public void setElectivedetail(String electivedetail) {

        Electivedetail = electivedetail;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setElectivedetailpic(String electivedetailpic) {
        Electivedetailpic = electivedetailpic;
    }

    public void setLeavedetailpic(String leavedetailpic) {
        this.leavedetailpic = leavedetailpic;
    }
}
