package com.yfy.app.process.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class ProBeanChild implements Parcelable {


    private int allNum;
    private int selectNum;
    private boolean groupChick=false;//整组选择
    private boolean expand=false;//展开|收索
    private int group_index;//用于group刷新
    private List<Integer> child_indexs;//用于child刷新
    private int view_type=TagFinal.ONE_INT;//parent|child
    private boolean isChick=false;
    private List<String> group_tag;//group标签
    //group
    private String departid;
    private String departname;
    private String selectuser;



    //child
    private String userid;
    private String username;
    private String userHeadPic;
    private String userSex;
    private String Phone1;
    private String Phone2;
    private String usertype;




    public ProBeanChild(int type) {
        this.view_type = type;
    }

    public ProBeanChild(String depid, String depname, int type) {
        this.departid = depid;
        this.departname = depname;
        this.view_type = type;
    }

    public ProBeanChild(String depid, int type, String headPic, String userid, String username, List<String> group_tag) {
        this.departid = depid;
        this.view_type = type;
        this.userHeadPic = headPic;
        this.userid = userid;
        this.username = username;
        this.group_tag = group_tag;
    }


    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getSelectNum() {
        return selectNum;
    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }

    public boolean isGroupChick() {
        return groupChick;
    }

    public void setGroupChick(boolean groupChick) {
        this.groupChick = groupChick;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public int getGroup_index() {
        return group_index;
    }

    public void setGroup_index(int group_index) {
        this.group_index = group_index;
    }

    public List<Integer> getChild_indexs() {
        return child_indexs;
    }

    public void setChild_indexs(List<Integer> child_indexs) {
        this.child_indexs = child_indexs;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public boolean isChick() {
        return isChick;
    }

    public void setChick(boolean chick) {
        isChick = chick;
    }

    public List<String> getGroup_tag() {
        return group_tag;
    }

    public void setGroup_tag(List<String> group_tag) {
        this.group_tag = group_tag;
    }

    public String getDepartid() {
        return departid;
    }

    public void setDepartid(String departid) {
        this.departid = departid;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    public String getSelectuser() {
        return selectuser;
    }

    public void setSelectuser(String selectuser) {
        this.selectuser = selectuser;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHeadPic() {
        return userHeadPic;
    }

    public void setUserHeadPic(String userHeadPic) {
        this.userHeadPic = userHeadPic;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        Phone1 = phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
        Phone2 = phone2;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.allNum);
        dest.writeInt(this.selectNum);
        dest.writeByte(this.groupChick ? (byte) 1 : (byte) 0);
        dest.writeByte(this.expand ? (byte) 1 : (byte) 0);
        dest.writeInt(this.group_index);
        dest.writeList(this.child_indexs);
        dest.writeInt(this.view_type);
        dest.writeByte(this.isChick ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.group_tag);
        dest.writeString(this.departid);
        dest.writeString(this.departname);
        dest.writeString(this.selectuser);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.userHeadPic);
        dest.writeString(this.userSex);
        dest.writeString(this.Phone1);
        dest.writeString(this.Phone2);
        dest.writeString(this.usertype);
    }

    protected ProBeanChild(Parcel in) {
        this.allNum = in.readInt();
        this.selectNum = in.readInt();
        this.groupChick = in.readByte() != 0;
        this.expand = in.readByte() != 0;
        this.group_index = in.readInt();
        this.child_indexs = new ArrayList<Integer>();
        in.readList(this.child_indexs, Integer.class.getClassLoader());
        this.view_type = in.readInt();
        this.isChick = in.readByte() != 0;
        this.group_tag = in.createStringArrayList();
        this.departid = in.readString();
        this.departname = in.readString();
        this.selectuser = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.userHeadPic = in.readString();
        this.userSex = in.readString();
        this.Phone1 = in.readString();
        this.Phone2 = in.readString();
        this.usertype = in.readString();
    }

    public static final Creator<ProBeanChild> CREATOR = new Creator<ProBeanChild>() {
        @Override
        public ProBeanChild createFromParcel(Parcel source) {
            return new ProBeanChild(source);
        }

        @Override
        public ProBeanChild[] newArray(int size) {
            return new ProBeanChild[size];
        }
    };
}
