package com.yfy.app.delay_service.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DetailBean implements Parcelable {

    /**
     {
     "id": "85",
     "teachername": "周芳羽",
     "teacherid": "1263",
     "phonenumber": "13688029610",
     "teacherheadpic": "https://www.cdpxjj.com/ClientBin/head.png",
     "type": "老师留下辅导",
     "content": "特殊",
     "image": ["https://www.cdpxjj.com/uploadfile/evenet/0/15687334309891.jpg"]
     }
     */

    private String id;
    private String teachername;
    private String teacherid;
    private String phonenumber;
    private String teacherheadpic;
    private String content;
    private String type;
    private List<String> image;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

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

    public String getTeacherheadpic() {
        return teacherheadpic;
    }

    public void setTeacherheadpic(String teacherheadpic) {
        this.teacherheadpic = teacherheadpic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        dest.writeString(this.phonenumber);
        dest.writeString(this.teacherheadpic);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeStringList(this.image);
    }

    public DetailBean() {
    }

    protected DetailBean(Parcel in) {
        this.id = in.readString();
        this.teachername = in.readString();
        this.teacherid = in.readString();
        this.phonenumber = in.readString();
        this.teacherheadpic = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.image = in.createStringArrayList();
    }

    public static final Creator<DetailBean> CREATOR = new Creator<DetailBean>() {
        @Override
        public DetailBean createFromParcel(Parcel source) {
            return new DetailBean(source);
        }

        @Override
        public DetailBean[] newArray(int size) {
            return new DetailBean[size];
        }
    };
}
