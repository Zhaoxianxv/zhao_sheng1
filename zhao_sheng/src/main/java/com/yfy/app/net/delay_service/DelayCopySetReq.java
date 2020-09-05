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
@Root(name = TagFinal.DELAY_COPY_SET, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"Electiveid","Electiveid1",Base.date})
public class DelayCopySetReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electiveid", required = false)
    private int Electiveid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "Electiveid1", required = false)
    private int Electiveid1;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setElectiveid(int electiveid) {
        Electiveid = electiveid;
    }

    public void setElectiveid1(int electiveid1) {
        Electiveid1 = electiveid1;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
