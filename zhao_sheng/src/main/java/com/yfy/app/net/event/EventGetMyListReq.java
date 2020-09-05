package com.yfy.app.net.event;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.EVENT_GET_USER_LIST, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"sdate","edate","depid","weekid",Base.page,Base.size})
public class EventGetMyListReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "sdate", required = false)
    private String sdate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "edate", required = false)
    private String edate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "depid", required = false)
    private int depid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "weekid", required = false)
    private int weekid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.size, required = false)
    private int size=TagFinal.TEN_FIVE;


    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public void setDepid(int depid) {
        this.depid = depid;
    }

    public void setWeekid(int weekid) {
        this.weekid = weekid;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
