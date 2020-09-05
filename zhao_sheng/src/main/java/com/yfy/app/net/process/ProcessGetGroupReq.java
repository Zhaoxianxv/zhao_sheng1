package com.yfy.app.net.process;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.PROCESS_GET_GROUP, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,Base.type,Base.id,"value"})
public class ProcessGetGroupReq {


    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.type, required = false)
    private String type;


    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @ElementArray(entry="arr:string")
    private String[] value;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public void setValue(String[] value) {
        this.value = value;
    }
}

