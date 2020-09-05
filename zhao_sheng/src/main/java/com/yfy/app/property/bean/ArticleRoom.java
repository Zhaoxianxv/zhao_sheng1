package com.yfy.app.property.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2018/3/1.
 */

public class ArticleRoom implements Parcelable {
    private String name;
    private String id;
    private String all_num;
    private String good_num;
    private String defi_num;
    private List<BadObj> bad_num;

    public ArticleRoom(String name, String id, String all_num, String defi_num, String good_num) {
        this.name = name;
        this.id = id;
        this.all_num = all_num;
        this.defi_num = defi_num;
        this.good_num = good_num;
    }

    public List<BadObj> getBad_num() {
        return bad_num;
    }

    public void setBad_num(List<BadObj> bad_num) {
        this.bad_num = bad_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAll_num() {
        return all_num;
    }

    public void setAll_num(String all_num) {
        this.all_num = all_num;
    }

    public String getDefi_num() {
        return defi_num;
    }

    public void setDefi_num(String defi_num) {
        this.defi_num = defi_num;
    }


    public String getGood_num() {
        return good_num;
    }

    public void setGood_num(String good_num) {
        this.good_num = good_num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.all_num);
        dest.writeString(this.good_num);
        dest.writeString(this.defi_num);
        dest.writeTypedList(this.bad_num);
    }

    protected ArticleRoom(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.all_num = in.readString();
        this.good_num = in.readString();
        this.defi_num = in.readString();
        this.bad_num = in.createTypedArrayList(BadObj.CREATOR);
    }

    public static final Parcelable.Creator<ArticleRoom> CREATOR = new Parcelable.Creator<ArticleRoom>() {
        @Override
        public ArticleRoom createFromParcel(Parcel source) {
            return new ArticleRoom(source);
        }

        @Override
        public ArticleRoom[] newArray(int size) {
            return new ArticleRoom[size];
        }
    };
}
