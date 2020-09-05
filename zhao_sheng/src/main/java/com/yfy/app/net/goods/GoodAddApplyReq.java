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
@Root(name = TagFinal.GOODS_ADD_APPLY, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"realname",Base.id,"masterid","images","image_file",Base.count,"remark"})
public class GoodAddApplyReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.count, required = false)
    private int count;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "masterid", required = false)
    private int masterid;


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "realname", required = false)
    private String realname;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "images", required = false)
    private String images;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "image_file", required = false)
    private String image_file;
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "remark", required = false)
    private String remark;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMasterid(int masterid) {
        this.masterid = masterid;
    }



    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
