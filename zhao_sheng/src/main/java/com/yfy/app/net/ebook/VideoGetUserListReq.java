package com.yfy.app.net.ebook;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.VIDEO_GET_MAIN_LIST, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"tagid",Base.page,Base.size})
public class VideoGetUserListReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "tagid", required = false)
    private int tagid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.size, required = false)
    private int size=TagFinal.TEN_FIVE;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
