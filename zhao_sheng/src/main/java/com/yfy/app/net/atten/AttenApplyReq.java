package com.yfy.app.net.atten;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.ATTENDANCE_APPLY, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {
        Base.session_key,
        "userid",
        "start_time",
        "duration",
        Base.reason,
        "table_plan",
        "image_file",
        Base.image,
        "kqtype"
})
public class AttenApplyReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.reason, required = false)
    private String reason;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.image, required = false)
    private String image;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "userid", required = false)
    private int userid;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "start_time", required = false)
    private String start_time;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "duration", required = false)
    private String duration;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "table_plan", required = false)
    private String table_plan;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "image_file", required = false)
    private String image_file;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "kqtype", required = false)
    private String kqtype;

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setTable_plan(String table_plan) {
        this.table_plan = table_plan;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public void setKqtype(String kqtype) {
        this.kqtype = kqtype;
    }
}
