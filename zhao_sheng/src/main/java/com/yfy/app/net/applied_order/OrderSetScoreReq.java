package com.yfy.app.net.applied_order;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/5/10.
 */
@Root(name = TagFinal.ORDER_SET_SCORE, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {
        TagFinal.session_key,Base.id,
        "islogistics","logisticsscore",
        "logisticscontent","isdevice",
        "devicescore","devicecontent"})
public class OrderSetScoreReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.id, required = false)
    private int id;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "islogistics", required = false)
    private int islogistics;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "logisticsscore", required = false)
    private int logisticsscore;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "logisticscontent", required = false)
    private String logisticscontent;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "isdevice", required = false)
    private int isdevice;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "devicescore", required = false)
    private int devicescore;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "devicecontent", required = false)
    private String devicecontent;

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIslogistics(int islogistics) {
        this.islogistics = islogistics;
    }

    public void setLogisticsscore(int logisticsscore) {
        this.logisticsscore = logisticsscore;
    }

    public void setLogisticscontent(String logisticscontent) {
        this.logisticscontent = logisticscontent;
    }

    public void setIsdevice(int isdevice) {
        this.isdevice = isdevice;
    }

    public void setDevicescore(int devicescore) {
        this.devicescore = devicescore;
    }

    public void setDevicecontent(String devicecontent) {
        this.devicecontent = devicecontent;
    }
}
