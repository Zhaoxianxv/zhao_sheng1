package com.yfy.app.net.check;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.CHECK_ADD_PARENT, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"groupid","date","mobile", "illdate","inspecttypeid","illtype",
        "userid","ids","contents","usertype"})
public class CheckAddParentReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();     ///


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "groupid", required = false)
    private int groupid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "date", required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "mobile", required = false)
    private String mobile;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "illdate", required = false)
    private String illdate;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "inspecttypeid", required = false)
    private int inspecttypeid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "illtype", required = false)
    private String illtype;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "userid", required = false)
    private String userid;

//    @Namespace(reference = TagFinal.NAMESPACE)
//    @ElementList
//    private String[] ids;
//    @Namespace(reference = TagFinal.NAMESPACE)
//    @ElementList
//    private String[] contents;
    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] ids;
    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] contents;




    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "usertype", required = false)
    private String usertype= Base.user_check_type;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIlldate(String illdate) {
        this.illdate = illdate;
    }

    public void setInspecttypeid(int inspecttypeid) {
        this.inspecttypeid = inspecttypeid;
    }

    public void setIlltype(String illtype) {
        this.illtype = illtype;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }


}
