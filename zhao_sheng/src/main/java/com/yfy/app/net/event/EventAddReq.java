package com.yfy.app.net.event;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.EVENT_ADD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"liableuser",Base.id,"depid",Base.date,Base.content,"address","images","image_file"})
public class EventAddReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "liableuser", required = false)
    private String liableuser;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "depid", required = false)
    private int depid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "address", required = false)
    private String address;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "images", required = false)
    private String images;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "image_file", required = false)
    private String image_file;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setLiableuser(String liableuser) {
        this.liableuser = liableuser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDepid(int depid) {
        this.depid = depid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}
