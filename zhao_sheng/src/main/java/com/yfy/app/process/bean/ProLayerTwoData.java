package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.app.bean.KeyValue;

import java.util.List;

public class ProLayerTwoData implements Parcelable {


    private int view_type;
    private boolean is_select;

    private KeyValue keyValue;
    /**
     * id : 12.0
     * step : 0.0
     * HeadPic :
     * content :
     * valuetype : list
     * adddate :
     * title : 功能室
     * cannull : 0
     * unit :
     * min : 0
     * max : 0
     * selects :
     * isshow : 1
     * linkstate : 7
     * linkvalue :
     */

    private double id;
    private double step;
    private String HeadPic;
    private String content;
    private String displaycontent;
    private String valuetype;
    private String adddate;
    private String title;
    private String cannull;
    private String unit;
    private String min;
    private String max;
    private String selects;
    private String isshow;
    private int linkstate;
    private String linkvalue;
    private List<ProLayerTwoData> recordlinkvalues;
    private List<ProLinkListBean> recordvaluearray;


    public String getDisplaycontent() {
        return displaycontent;
    }

    public void setDisplaycontent(String displaycontent) {
        this.displaycontent = displaycontent;
    }

    public boolean isIs_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public int getView_type() {

        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public List<ProLinkListBean> getRecordvaluearray() {
        return recordvaluearray;
    }

    public void setRecordvaluearray(List<ProLinkListBean> recordvaluearray) {
        this.recordvaluearray = recordvaluearray;
    }

    public List<ProLayerTwoData> getRecordlinkvalues() {
        return recordlinkvalues;
    }

    public void setRecordlinkvalues(List<ProLayerTwoData> recordlinkvalues) {
        this.recordlinkvalues = recordlinkvalues;
    }

    public KeyValue getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(KeyValue keyValue) {
        this.keyValue = keyValue;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String HeadPic) {
        this.HeadPic = HeadPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getValuetype() {
        return valuetype;
    }

    public void setValuetype(String valuetype) {
        this.valuetype = valuetype;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCannull() {
        return cannull;
    }

    public void setCannull(String cannull) {
        this.cannull = cannull;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getSelects() {
        return selects;
    }

    public void setSelects(String selects) {
        this.selects = selects;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public int getLinkstate() {
        return linkstate;
    }

    public void setLinkstate(int linkstate) {
        this.linkstate = linkstate;
    }

    public String getLinkvalue() {
        return linkvalue;
    }

    public void setLinkvalue(String linkvalue) {
        this.linkvalue = linkvalue;
    }

    public ProLayerTwoData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.view_type);
        dest.writeByte(this.is_select ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.keyValue, flags);
        dest.writeDouble(this.id);
        dest.writeDouble(this.step);
        dest.writeString(this.HeadPic);
        dest.writeString(this.content);
        dest.writeString(this.displaycontent);
        dest.writeString(this.valuetype);
        dest.writeString(this.adddate);
        dest.writeString(this.title);
        dest.writeString(this.cannull);
        dest.writeString(this.unit);
        dest.writeString(this.min);
        dest.writeString(this.max);
        dest.writeString(this.selects);
        dest.writeString(this.isshow);
        dest.writeInt(this.linkstate);
        dest.writeString(this.linkvalue);
        dest.writeTypedList(this.recordlinkvalues);
        dest.writeTypedList(this.recordvaluearray);
    }

    protected ProLayerTwoData(Parcel in) {
        this.view_type = in.readInt();
        this.is_select = in.readByte() != 0;
        this.keyValue = in.readParcelable(KeyValue.class.getClassLoader());
        this.id = in.readDouble();
        this.step = in.readDouble();
        this.HeadPic = in.readString();
        this.content = in.readString();
        this.displaycontent = in.readString();
        this.valuetype = in.readString();
        this.adddate = in.readString();
        this.title = in.readString();
        this.cannull = in.readString();
        this.unit = in.readString();
        this.min = in.readString();
        this.max = in.readString();
        this.selects = in.readString();
        this.isshow = in.readString();
        this.linkstate = in.readInt();
        this.linkvalue = in.readString();
        this.recordlinkvalues = in.createTypedArrayList(ProLayerTwoData.CREATOR);
        this.recordvaluearray = in.createTypedArrayList(ProLinkListBean.CREATOR);
    }

    public static final Creator<ProLayerTwoData> CREATOR = new Creator<ProLayerTwoData>() {
        @Override
        public ProLayerTwoData createFromParcel(Parcel source) {
            return new ProLayerTwoData(source);
        }

        @Override
        public ProLayerTwoData[] newArray(int size) {
            return new ProLayerTwoData[size];
        }
    };
}
