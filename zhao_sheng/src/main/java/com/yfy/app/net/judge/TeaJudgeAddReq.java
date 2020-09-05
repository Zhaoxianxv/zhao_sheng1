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
@Root(name = TagFinal.TEA_JUDGE_ADD, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"realname",Base.id,"year","pid",Base.content,"images","image_file"})
public class TeaJudgeAddReq {

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key= Base.user.getSession_key();


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "realname", required = false)
    private String realname;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private String id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "year", required = false)
    private int year;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "pid", required = false)
    private int pid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.content, required = false)
    private String content;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "images", required = false)
    private String images;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "image_file", required = false)
    private String image_file;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}
