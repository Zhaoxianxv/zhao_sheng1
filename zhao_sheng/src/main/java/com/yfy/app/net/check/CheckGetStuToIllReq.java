package com.yfy.app.net.check;

import com.yfy.final_tag.Base;
import com.yfy.final_tag.TagFinal;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

/**
 * Created by yfyandr on 2018/4/26.
 */
@Root(name = TagFinal.CHECK_GET_STU_TO_TYPE_ILL, strict = false)
@Namespace(reference = TagFinal.NAMESPACE)
@Order(elements = {Base.session_key,"statisticsid","statisticstypeid",Base.date})
public class CheckGetStuToIllReq {
    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.session_key, required = false)
    private String session_key= Base.user.getSession_key();     ///

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = Base.date, required = false)
    private String date;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "statisticsid", required = false)
    private int statisticsid;

    @Namespace(reference = TagFinal.NAMESPACE)
    @Element(name = "statisticstypeid", required = false)
    private int statisticstypeid;



    public void setDate(String date) {
        this.date = date;
    }

    public void setStatisticsid(int statisticsid) {
        this.statisticsid = statisticsid;
    }

    public void setStatisticstypeid(int statisticstypeid) {
        this.statisticstypeid = statisticstypeid;
    }
}
