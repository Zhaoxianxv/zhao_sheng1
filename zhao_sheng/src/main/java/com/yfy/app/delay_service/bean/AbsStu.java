package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.List;

public class AbsStu implements Parcelable {

    /**
     * id : 1
     * stuname : 池睿
     * stuid : 5629
     * phonenumber : 13551182899
     * stuheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20190619113418.jpg
     * classid : 104
     * classname : 二年级一班
     * addusername : 管理员
     * adduserheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * adddate : 2020/05/22 14:17
     * type : 请假
     * content : 测试
     * image : []
     */

    private String id;
    private String stuname;
    private String stuid;
    private String phonenumber;
    private String stuheadpic;
    private String classid;
    private String classname;
    private String addusername;
    private String adduserheadpic;
    private String adddate;
    private String type;
    private String content;
    private  List<String> image;


    //--------------tea--------
    private int view_type=TagFinal.TYPE_ITEM;

    private String teachername;
    private String teacherid;
    private String checktype;
    private String substituteteacher;
    private String substituteteacherid;
    private String teacherheadpic;
    private String stuarrive;


    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
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

    public String getSubstituteteacher() {
        return substituteteacher;
    }

    public void setSubstituteteacher(String substituteteacher) {
        this.substituteteacher = substituteteacher;
    }

    public String getSubstituteteacherid() {
        return substituteteacherid;
    }

    public void setSubstituteteacherid(String substituteteacherid) {
        this.substituteteacherid = substituteteacherid;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public AbsStu() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.stuname);
        dest.writeString(this.stuid);
        dest.writeString(this.phonenumber);
        dest.writeString(this.stuheadpic);
        dest.writeString(this.classid);
        dest.writeString(this.classname);
        dest.writeString(this.addusername);
        dest.writeString(this.adduserheadpic);
        dest.writeString(this.adddate);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeStringList(this.image);
        dest.writeInt(this.view_type);
        dest.writeString(this.teachername);
        dest.writeString(this.teacherid);
        dest.writeString(this.checktype);
        dest.writeString(this.substituteteacher);
        dest.writeString(this.substituteteacherid);
        dest.writeString(this.teacherheadpic);
        dest.writeString(this.stuarrive);
    }

    protected AbsStu(Parcel in) {
        this.id = in.readString();
        this.stuname = in.readString();
        this.stuid = in.readString();
        this.phonenumber = in.readString();
        this.stuheadpic = in.readString();
        this.classid = in.readString();
        this.classname = in.readString();
        this.addusername = in.readString();
        this.adduserheadpic = in.readString();
        this.adddate = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.image = in.createStringArrayList();
        this.view_type = in.readInt();
        this.teachername = in.readString();
        this.teacherid = in.readString();
        this.checktype = in.readString();
        this.substituteteacher = in.readString();
        this.substituteteacherid = in.readString();
        this.teacherheadpic = in.readString();
        this.stuarrive = in.readString();
    }

    public static final Creator<AbsStu> CREATOR = new Creator<AbsStu>() {
        @Override
        public AbsStu createFromParcel(Parcel source) {
            return new AbsStu(source);
        }

        @Override
        public AbsStu[] newArray(int size) {
            return new AbsStu[size];
        }
    };
}
