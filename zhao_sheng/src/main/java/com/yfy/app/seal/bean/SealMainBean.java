package com.yfy.app.seal.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.bean.KeyValue;

import java.util.List;

public class SealMainBean implements Parcelable {


    /**
     * id : 2
     * signetid : 2
     * signetname : 实物章
     * typeid : 2
     * typename : 盖章
     * status : 待审核
     * startdate : 2020/04/22
     * enddate :
     * adddate : 2020/04/22
     * proposerid : 1
     * proposername : 管理员
     * proposerheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * Approvalid : 1
     * Approvalname : 管理员
     * Approvalheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * title : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     */

    private String id;
    private String signetid;
    private String canedit;
    private String signetname;
    private String typeid;
    private String typename;
    private String status;
    private String startdate;
    private String enddate;
    private String adddate;
    private String proposerid;
    private String proposername;
    private String proposerheadpic;
    private String Approvalid;
    private String Approvalname;
    private String Approvalheadpic;
    private String title;
    private List<KeyValue> keyValues;
    private List<Opear> opear;
    private List<SealMainState> statuslist;
    private List<SealTable> tables;


    public String getCanedit() {
        return canedit;
    }

    public void setCanedit(String canedit) {
        this.canedit = canedit;
    }

    public List<SealTable> getTables() {
        return tables;
    }

    public void setTables(List<SealTable> tables) {
        this.tables = tables;
    }

    public List<SealMainState> getStatuslist() {
        return statuslist;
    }

    public void setStatuslist(List<SealMainState> statuslist) {
        this.statuslist = statuslist;
    }

    public List<Opear> getOpear() {
        return opear;
    }

    public void setOpear(List<Opear> opear) {
        this.opear = opear;
    }

    public List<KeyValue> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<KeyValue> keyValues) {
        this.keyValues = keyValues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignetid() {
        return signetid;
    }

    public void setSignetid(String signetid) {
        this.signetid = signetid;
    }

    public String getSignetname() {
        return signetname;
    }

    public void setSignetname(String signetname) {
        this.signetname = signetname;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getProposerid() {
        return proposerid;
    }

    public void setProposerid(String proposerid) {
        this.proposerid = proposerid;
    }

    public String getProposername() {
        return proposername;
    }

    public void setProposername(String proposername) {
        this.proposername = proposername;
    }

    public String getProposerheadpic() {
        return proposerheadpic;
    }

    public void setProposerheadpic(String proposerheadpic) {
        this.proposerheadpic = proposerheadpic;
    }

    public String getApprovalid() {
        return Approvalid;
    }

    public void setApprovalid(String Approvalid) {
        this.Approvalid = Approvalid;
    }

    public String getApprovalname() {
        return Approvalname;
    }

    public void setApprovalname(String Approvalname) {
        this.Approvalname = Approvalname;
    }

    public String getApprovalheadpic() {
        return Approvalheadpic;
    }

    public void setApprovalheadpic(String Approvalheadpic) {
        this.Approvalheadpic = Approvalheadpic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SealMainBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.signetid);
        dest.writeString(this.canedit);
        dest.writeString(this.signetname);
        dest.writeString(this.typeid);
        dest.writeString(this.typename);
        dest.writeString(this.status);
        dest.writeString(this.startdate);
        dest.writeString(this.enddate);
        dest.writeString(this.adddate);
        dest.writeString(this.proposerid);
        dest.writeString(this.proposername);
        dest.writeString(this.proposerheadpic);
        dest.writeString(this.Approvalid);
        dest.writeString(this.Approvalname);
        dest.writeString(this.Approvalheadpic);
        dest.writeString(this.title);
        dest.writeTypedList(this.keyValues);
        dest.writeTypedList(this.opear);
        dest.writeTypedList(this.statuslist);
        dest.writeTypedList(this.tables);
    }

    protected SealMainBean(Parcel in) {
        this.id = in.readString();
        this.signetid = in.readString();
        this.canedit = in.readString();
        this.signetname = in.readString();
        this.typeid = in.readString();
        this.typename = in.readString();
        this.status = in.readString();
        this.startdate = in.readString();
        this.enddate = in.readString();
        this.adddate = in.readString();
        this.proposerid = in.readString();
        this.proposername = in.readString();
        this.proposerheadpic = in.readString();
        this.Approvalid = in.readString();
        this.Approvalname = in.readString();
        this.Approvalheadpic = in.readString();
        this.title = in.readString();
        this.keyValues = in.createTypedArrayList(KeyValue.CREATOR);
        this.opear = in.createTypedArrayList(Opear.CREATOR);
        this.statuslist = in.createTypedArrayList(SealMainState.CREATOR);
        this.tables = in.createTypedArrayList(SealTable.CREATOR);
    }

    public static final Creator<SealMainBean> CREATOR = new Creator<SealMainBean>() {
        @Override
        public SealMainBean createFromParcel(Parcel source) {
            return new SealMainBean(source);
        }

        @Override
        public SealMainBean[] newArray(int size) {
            return new SealMainBean[size];
        }
    };
}
