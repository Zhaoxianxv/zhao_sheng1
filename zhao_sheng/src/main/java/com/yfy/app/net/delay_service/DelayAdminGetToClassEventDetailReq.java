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
@Root(name = TagFinal.DELAY_ADMIN_get_TO_CLASS_EVENT_DETAIL, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"Electiveid","teacherid",Base.date})
public class DelayAdminGetToClassEventDetailReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electiveid", required = false)
    private int Electiveid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "teacherid", required = false)
    private int teacherid;


    public void setTeacherid(int teacherid) {
        this.teacherid = teacherid;
    }

    public void setElectiveid(int electiveid) {

        Electiveid = electiveid;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
