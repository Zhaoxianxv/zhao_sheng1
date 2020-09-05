package com.yfy.app.net.ebook;

import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.VIDEO_GET_TAG, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
public class VideoGetTypeReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "session_key", required = false)
    private String session_key;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }
}
