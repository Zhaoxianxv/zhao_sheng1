package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class EventClass implements Parcelable {

    //------------class
    private String Electivename;
    private String Electiveid;
    /**
     * Electivename : 测试1
     * Electiveid : 1590
     * stuname : 学生1
     * stuid : 11621
     * stuheadpic : https://www.cdpxjj.com/ClientBin/head.png
     * classid : 684
     * classname : 一年级1班
     * isabsent : false
     * detail : []
     */

//-------------------stu
    private String stuname;
    private String stuid;
    private String stuheadpic;
    private String stuphonenumber;
    private String phonenumber;
    private String classid;
    private String classname;
    private String isabsent;
    private List<DetailBean> detail;


    private int view_type;

    private boolean expand=false;

    //---------------absent
    private String id;
    private String addusername;
    private String adduserheadpic;
    private String adddate;
    private String content;
    private String type;
    private  List<String> image;


    //--------------tea
    private String teachername;
    private String teacherid;
    private String checktype;
    private String teacherheadpic;
    private String stuarrive;
    private String adduserphonenumber;
    private String substituteteacher;
    private String substituteteacherid;

    private boolean isCheck=false;
    private int span_size=4;

    public int getSpan_size() {
        return span_size;
    }

    public void setSpan_size(int span_size) {
        this.span_size = span_size;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getSubstituteteacherid() {
        return substituteteacherid;
    }

    public void setSubstituteteacherid(String substituteteacherid) {
        this.substituteteacherid = substituteteacherid;
    }

    public String getSubstituteteacher() {
        return substituteteacher;
    }

    public void setSubstituteteacher(String substituteteacher) {
        this.substituteteacher = substituteteacher;
    }

    public String getAdduserphonenumber() {
        return adduserphonenumber;
    }

    public void setAdduserphonenumber(String adduserphonenumber) {
        this.adduserphonenumber = adduserphonenumber;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getChecktype() {
        return checktype;
    }

    public void setChecktype(String checktype) {
        this.checktype = checktype;
    }

    public String getTeacherheadpic() {
        return teacherheadpic;
    }

    public void setTeacherheadpic(String teacherheadpic) {
        this.teacherheadpic = teacherheadpic;
    }

    public String getStuarrive() {
        return stuarrive;
    }

    public void setStuarrive(String stuarrive) {
        this.stuarrive = stuarrive;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddusername() {
        return addusername;
    }

    public void setAddusername(String addusername) {
        this.addusername = addusername;
    }

    public String getAdduserheadpic() {
        return adduserheadpic;
    }

    public void setAdduserheadpic(String adduserheadpic) {
        this.adduserheadpic = adduserheadpic;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getElectivename() {
        return Electivename;
    }

    public void setElectivename(String Electivename) {
        this.Electivename = Electivename;
    }

    public String getElectiveid() {
        return Electiveid;
    }

    public void setElectiveid(String Electiveid) {
        this.Electiveid = Electiveid;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }

    public String getStuheadpic() {
        return stuheadpic;
    }

    public void setStuheadpic(String stuheadpic) {
        this.stuheadpic = stuheadpic;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getIsabsent() {
        return isabsent;
    }

    public void setIsabsent(String isabsent) {
        this.isabsent = isabsent;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }


    public String getStuphonenumber() {
        return stuphonenumber;
    }

    public void setStuphonenumber(String stuphonenumber) {
        this.stuphonenumber = stuphonenumber;
    }

    public EventClass() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Electivename);
        dest.writeString(this.Electiveid);
        dest.writeString(this.stuname);
        dest.writeString(this.stuid);
        dest.writeString(this.stuheadpic);
        dest.writeString(this.stuphonenumber);
        dest.writeString(this.phonenumber);
        dest.writeString(this.classid);
        dest.writeString(this.classname);
        dest.writeString(this.isabsent);
        dest.writeTypedList(this.detail);
        dest.writeInt(this.view_type);
        dest.writeByte(this.expand ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.addusername);
        dest.writeString(this.adduserheadpic);
        dest.writeString(this.adddate);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeStringList(this.image);
        dest.writeString(this.teachername);
        dest.writeString(this.teacherid);
        dest.writeString(this.checktype);
        dest.writeString(this.teacherheadpic);
        dest.writeString(this.stuarrive);
        dest.writeString(this.adduserphonenumber);
        dest.writeString(this.substituteteacher);
        dest.writeString(this.substituteteacherid);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeInt(this.span_size);
    }

    protected EventClass(Parcel in) {
        this.Electivename = in.readString();
        this.Electiveid = in.readString();
        this.stuname = in.readString();
        this.stuid = in.readString();
        this.stuheadpic = in.readString();
        this.stuphonenumber = in.readString();
        this.phonenumber = in.readString();
        this.classid = in.readString();
        this.classname = in.readString();
        this.isabsent = in.readString();
        this.detail = in.createTypedArrayList(DetailBean.CREATOR);
        this.view_type = in.readInt();
        this.expand = in.readByte() != 0;
        this.id = in.readString();
        this.addusername = in.readString();
        this.adduserheadpic = in.readString();
        this.adddate = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.image = in.createStringArrayList();
        this.teachername = in.readString();
        this.teacherid = in.readString();
        this.checktype = in.readString();
        this.teacherheadpic = in.readString();
        this.stuarrive = in.readString();
        this.adduserphonenumber = in.readString();
        this.substituteteacher = in.readString();
        this.substituteteacherid = in.readString();
        this.isCheck = in.readByte() != 0;
        this.span_size = in.readInt();
    }

    public static final Creator<EventClass> CREATOR = new Creator<EventClass>() {
        @Override
        public EventClass createFromParcel(Parcel source) {
            return new EventClass(source);
        }

        @Override
        public EventClass[] newArray(int size) {
            return new EventClass[size];
        }
    };
}
