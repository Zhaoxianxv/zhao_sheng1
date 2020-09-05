package com.yfy.app.net.affiche;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.SCHOOL_GET_NEWS_LIST, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {"no","searchkey","fxid",Base.page,Base.pagesize})
public class SchoolGetNewsListReq {
    @Namespace(reference = Base.NAMESPACE)
    @Element(name ="no", required = false)
    private String no;     ///

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "fxid", required = false)
    private int fxid;     ///

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.page, required = false)
    private int page;     ///

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.pagesize, required = false)
    private int pagesize;     ///

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "searchkey", required = false)
    private String searchkey="";     ///


    public void setNo(String no) {
        this.no = no;
    }

    public void setFxid(int fxid) {
        this.fxid = fxid;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }
}
