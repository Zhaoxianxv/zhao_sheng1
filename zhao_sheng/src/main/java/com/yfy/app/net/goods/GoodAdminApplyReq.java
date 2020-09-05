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
@Root(name = TagFinal.GOODS_ADMIN_APPLY, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"goodsid","surpluscount"})
public class GoodAdminApplyReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "goodsid", required = false)
    private int goodsid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "surpluscount", required = false)
    private int surpluscount;

    public void setSurpluscount(int surpluscount) {
        this.surpluscount = surpluscount;
    }

    public void setGoodsid(int goodsid) {

        this.goodsid = goodsid;
    }

    public void setId(String id) {
        this.id = id;
    }
}
