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
@Root(name = TagFinal.ATTENDANCE_GET_ADMIN_LIST, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {
        Base.session_key,
        "userid",
        "key",
        Base.type,
        "status",
        Base.page,
        Base.size})
public class AttenGetAdminListReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "userid", required = false)
    private int userid=0;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "key", required = false)
    private String key="";

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.type, required = false)
    private int type=0;//

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "status", required = false)
    private int state= 0;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name =Base.page, required = false)
    private int page;//

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.size, required = false)
    private int size=TagFinal.TEN_INT;//


    public void setPage(int page) {
        this.page = page;
    }


}
