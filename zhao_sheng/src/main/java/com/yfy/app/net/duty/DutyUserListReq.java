package com.yfy.app.net.duty;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.DUTY_GET_USER, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {TagFinal.session_key,"sdate","edate",TagFinal.page,TagFinal.size})
public class DutyUserListReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "sdate", required = false)
    private String sdate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "edate", required = false)
    private String edate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = TagFinal.page, required = false)
    private int page;;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = TagFinal.size, required = false)
    private int size=TagFinal.TEN_INT;

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
