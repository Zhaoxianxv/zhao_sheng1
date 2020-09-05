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
@Root(name = TagFinal.EVENT_GET_WEEK_LIST, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"weekid","depid"})
public class EventGetWeekListReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key = Base.user.getSession_key();


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "weekid", required = false)
    private int weekid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "depid", required = false)
    private int depid;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setWeekid(int weekid) {
        this.weekid = weekid;
    }

    public void setDepid(int depid) {
        this.depid = depid;
    }
}
