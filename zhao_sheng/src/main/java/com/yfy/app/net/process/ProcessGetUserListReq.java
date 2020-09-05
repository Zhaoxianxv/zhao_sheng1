package com.yfy.app.net.process;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.PROCESS_GET_USER_DATA_LIST, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {
        Base.session_key,
        Base.type,
        "searchkey",
        Base.state,
        "groupid",
        "secondgroupid",
        "startdate",
        "enddate",
        Base.page,
        Base.pagesize
})
public class ProcessGetUserListReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.state, required = false)
    private int state;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.pagesize, required = false)
    private int pagesize;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "groupid", required = false)
    private int groupid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "secondgroupid", required = false)
    private int secondgroupid;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "searchkey", required = false)
    private String searchkey;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "startdate", required = false)
    private String startdate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "enddate", required = false)
    private String enddate;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public void setSecondgroupid(int secondgroupid) {
        this.secondgroupid = secondgroupid;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
