package com.yfy.app.net.goods;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/27.
 */
@Root(name = TagFinal.GOODS_ADD_STATIONERY_TYPE, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"parentid","isreply",Base.name,"searchkey","unit",Base.count})
public class GoodAddStationeryTypeReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.name, required = false)
    private String name;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.count, required = false)
    private int count;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "parentid", required = false)
    private int parentid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "isreply", required = false)
    private int isreply;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "searchkey", required = false)
    private String searchkey;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "unit", required = false)
    private String unit;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public void setIsreply(int isreply) {
        this.isreply = isreply;
    }

    public void setSearchkey(String searchkey) {
        this.searchkey = searchkey;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
