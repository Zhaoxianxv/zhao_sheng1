package com.yfy.app.net.judge;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/22.
 */
@Root(name = TagFinal.TEA_JUDGE_GET_INFO_YEAR_LIST, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"year",Base.id,Base.page,Base.size})
public class TeaJudgeGetInfoYearListReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "year", required = false)
    private int year;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.size, required = false)
    private int size;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
