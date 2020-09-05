package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DelayTeaBean implements Parcelable {

    /**
     * id : 2
     * teachername : 管理员
     * teacherid : 1
     * checktype : tea
     * substituteteacher : 无
     * substituteteacherid : 0
     * teacherheadpic : https://www.cdeps.sc.cn/uploadfile/photo/20191114095058.jpg
     * type :
     * stuarrive : 47
     * content :
     * image : []
     */

    private String id;
    private String teachername;
    private String teacherid;
    private String checktype;
    private String substituteteacher;
    private String substituteteacherid;
    private String teacherheadpic;
    private String type;
    private String stuarrive;
    private String content;
    private  List<String> image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStuarrive() {
        return stuarrive;
    }

    public void setStuarrive(String stuarrive) {
        this.stuarrive = stuarrive;
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

    public DelayTeaBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.teachername);
        dest.writeString(this.teacherid);
        dest.writeString(this.checktype);
        dest.writeString(this.substituteteacher);
        dest.writeString(this.substituteteacherid);
        dest.writeString(this.teacherheadpic);
        dest.writeString(this.type);
        dest.writeString(this.stuarrive);
        dest.writeString(this.content);
        dest.writeStringList(this.image);
    }

    protected DelayTeaBean(Parcel in) {
        this.id = in.readString();
        this.teachername = in.readString();
        this.teacherid = in.readString();
        this.checktype = in.readString();
        this.substituteteacher = in.readString();
        this.substituteteacherid = in.readString();
        this.teacherheadpic = in.readString();
        this.type = in.readString();
        this.stuarrive = in.readString();
        this.content = in.readString();
        this.image = in.createStringArrayList();
    }

    public static final Creator<DelayTeaBean> CREATOR = new Creator<DelayTeaBean>() {
        @Override
        public DelayTeaBean createFromParcel(Parcel source) {
            return new DelayTeaBean(source);
        }

        @Override
        public DelayTeaBean[] newArray(int size) {
            return new DelayTeaBean[size];
        }
    };
}
