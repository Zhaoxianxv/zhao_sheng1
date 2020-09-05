package com.yfy.app.net.notice;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/22.
 */
@Root(name = TagFinal.NOTICE_SEND, strict = false)
@Namespace(reference = Base.NAMESPACE)
@Order(elements = {
        Base.session_key,
        Base.name,
        "receiverid",
        "receiveruser",
        Base.title,
        Base.content,
        "pictures_file",
        "pictures_content",
        "voice_file",
        "voice_content"
})
public class NoticeAddReq {

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.name, required = false)
    private String name;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "receiverid", required = false)
    private String receiverid;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "receiveruser", required = false)
    private String receiveruser;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "pictures_file", required = false)
    private String pictures_file;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "pictures_content", required = false)
    private String pictures_content;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "voice_file", required = false)
    private String voice_file="";

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = "voice_content", required = false)
    private String voice_content="";

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.title, required = false)
    private String title;

    @Namespace(reference = Base.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    public void setName(String name) {
        this.name = name;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public void setReceiveruser(String receiveruser) {
        this.receiveruser = receiveruser;
    }

    public void setPictures_file(String pictures_file) {
        this.pictures_file = pictures_file;
    }

    public void setPictures_content(String pictures_content) {
        this.pictures_content = pictures_content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
