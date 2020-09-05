package com.yfy.app.net.seal;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SEAL_GET_ALL, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {TagFinal.session_key,"signetid","date"})
public class SealAllDetailReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();     ///
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "signetid", required = false)
    private int signetid;     ///
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "date", required = false)
    private String date;     ///


    public void setSignetid(int signetid) {
        this.signetid = signetid;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
