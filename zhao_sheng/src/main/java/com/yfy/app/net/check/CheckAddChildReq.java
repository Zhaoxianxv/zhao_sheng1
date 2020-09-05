package com.yfy.app.net.check;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;


@Root(name = TagFinal.CHECK_ADD_CHILD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"illid","isreturn","returndate","ids","contents","usertype"})
public class CheckAddChildReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();     ///

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "illid", required = false)
    private String illid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "isreturn", required = false)
    private int isreturn;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "returndate", required = false)
    private String returndate;



    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] ids;
    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] contents;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "usertype", required = false)
    private String usertype= Base.user_check_type;

    public void setIllid(String illid) {
        this.illid = illid;
    }

    public void setIsreturn(int isreturn) {
        this.isreturn = isreturn;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }


    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public void setContents(String[] contents) {
        this.contents = contents;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
