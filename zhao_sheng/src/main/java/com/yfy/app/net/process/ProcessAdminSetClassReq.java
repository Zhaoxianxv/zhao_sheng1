package com.yfy.app.net.process;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.PROCESS_ADMIN_SET_CLASS, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.id,"departuserid",Base.content,Base.image,Base.voice})
public class ProcessAdminSetClassReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key=Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.image, required = false)
    private String image;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.voice, required = false)
    private String voice;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "departuserid", required = false)
    private String departuserid;


    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public void setDepartuserid(String departuserid) {
        this.departuserid = departuserid;
    }
}
