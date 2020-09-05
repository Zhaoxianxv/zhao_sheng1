package com.yfy.app.notice.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

public class ChildBean implements Parcelable {

    //group
    private String depid;
    private String depname;
    private String departid;
    private String departname;
    private String selectuser;
    private int allNum;
    private int selectNum;
    private boolean groupChick=false;//整组选择
    private boolean expand=false;//展开|收索
    private int group_index;//用于group刷新
    private List<Integer> child_indexs;//用于child刷新

    private int type=TagFinal.ONE_INT;//parent|child


    //child
    private String HeadPic;
    private String Phone1;
    private String Phone2;
    private String userid;
    private String username;
    private boolean isChick=false;
    private List<String> group_tag;//group标签


    public ChildBean(int type) {
        this.type = type;
    }

    public ChildBean(String depid, String depname, int type) {
        this.depid = depid;
        this.depname = depname;
        this.type = type;
    }

    public ChildBean(String depid, int type, String headPic, String userid, String username, List<String> group_tag) {
        this.depid = depid;
        this.type = type;
        this.HeadPic = headPic;
        this.userid = userid;
        this.username = username;
        this.group_tag = group_tag;
    }


    public int getGroup_index() {
        return group_index;
    }

    public void setGroup_index(int group_index) {
        this.group_index = group_index;
    }

    public String getDepid() {
        return depid;
    }

    public void setDepid(String depid) {
        this.depid = depid;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
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


    public List<Integer> getChild_indexs() {
        return child_indexs;
    }

    public void setChild_indexs(List<Integer> child_indexs) {
        this.child_indexs = child_indexs;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHeadPic() {
        return HeadPic;
    }

    public void setHeadPic(String headPic) {
        HeadPic = headPic;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.depid);
        dest.writeString(this.depname);
        dest.writeString(this.departid);
        dest.writeString(this.departname);
        dest.writeString(this.selectuser);
        dest.writeInt(this.allNum);
        dest.writeInt(this.selectNum);
        dest.writeByte(this.groupChick ? (byte) 1 : (byte) 0);
        dest.writeByte(this.expand ? (byte) 1 : (byte) 0);
        dest.writeInt(this.group_index);
        dest.writeList(this.child_indexs);
        dest.writeInt(this.type);
        dest.writeString(this.HeadPic);
        dest.writeString(this.Phone1);
        dest.writeString(this.Phone2);
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeByte(this.isChick ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.group_tag);
    }

    protected ChildBean(Parcel in) {
        this.depid = in.readString();
        this.depname = in.readString();
        this.departid = in.readString();
        this.departname = in.readString();
        this.selectuser = in.readString();
        this.allNum = in.readInt();
        this.selectNum = in.readInt();
        this.groupChick = in.readByte() != 0;
        this.expand = in.readByte() != 0;
        this.group_index = in.readInt();
        this.child_indexs = new ArrayList<Integer>();
        in.readList(this.child_indexs, Integer.class.getClassLoader());
        this.type = in.readInt();
        this.HeadPic = in.readString();
        this.Phone1 = in.readString();
        this.Phone2 = in.readString();
        this.userid = in.readString();
        this.username = in.readString();
        this.isChick = in.readByte() != 0;
        this.group_tag = in.createStringArrayList();
    }

    public static final Creator<ChildBean> CREATOR = new Creator<ChildBean>() {
        @Override
        public ChildBean createFromParcel(Parcel source) {
            return new ChildBean(source);
        }

        @Override
        public ChildBean[] newArray(int size) {
            return new ChildBean[size];
        }
    };
}
