package com.yfy.app.net.footbook;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.FOOT_BOOK_GET_POPULAR, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.date,Base.type,Base.page,Base.size})
public class FootBookGetPopularReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.size, required = false)
    private int size=TagFinal.TEN_FIVE;

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
}
