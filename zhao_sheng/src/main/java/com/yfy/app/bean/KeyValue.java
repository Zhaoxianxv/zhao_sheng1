package com.yfy.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yfy.final_tag.dialog.CPWBean;

import java.util.ArrayList;
import java.util.List;

public class KeyValue implements Parcelable {
    private String key;//描述值

    private String type="";//用于判断value取那个字段
    private String user_type;
    private String activity_type;
    private boolean required=true;//用于判断是否必填
    private String value;//值
    private String name;//值名称
    private String title;//
    private String left_title;//
    private String right;//
    private String right_key;//
    private String right_name;//
    private String right_value="";//
    private String id;
    private String max;
    private String min;
    private String group_id;
    private String second_group_id;
    private int view_type;
    private int res_image;
    private int num;
    private int res_color;
    private int span_size;
    private int item_id;
    private List<String> listValue=new ArrayList<String>();

    private List<CPWBean> cpwBeanArrayList =new ArrayList<>();


    public KeyValue() {

    }

    public KeyValue(String id) {
        this.id = id;
    }

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValue(String type, List<String> listValue) {
        this.type = type;
        this.listValue = listValue;
    }
    public KeyValue(String name, String value, int view_type) {
        this.name = name;
        this.value = value;
        this.view_type = view_type;
    }
    public KeyValue(String key, String value, String name) {
        this.key = key;
        this.value = value;
        this.name = name;
    }
    public KeyValue(String key, String value, String name, int view_type) {
        this.key = key;
        this.value = value;
        this.name = name;
        this.view_type = view_type;
    }
    public KeyValue(String key, String value, String name, String id) {
        this.key = key;
        this.value = value;
        this.name = name;
        this.id = id;
    }

    public void setKeyValueLeftRight(String left_title,String right_key,String right_name,String right_value){
        this.left_title = left_title;
        this.right_key = right_key;
        this.right_name = right_name;
        this.right_value = right_value;

    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setNameValue(String name, String value){
        this.value = value;
        this.name = name;
    }


    public List<CPWBean> getCpwBeanArrayList() {
        return cpwBeanArrayList;
    }

    public void setCpwBeanArrayList(List<CPWBean> cpwBeanArrayList) {
        this.cpwBeanArrayList = cpwBeanArrayList;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getLeft_title() {
        return left_title;
    }

    public void setLeft_title(String left_title) {
        this.left_title = left_title;
    }

    public String getRight_key() {
        return right_key;
    }

    public void setRight_key(String right_key) {
        this.right_key = right_key;
    }

    public String getRight_name() {
        return right_name;
    }

    public void setRight_name(String right_name) {
        this.right_name = right_name;
    }

    public String getRight_value() {
        return right_value;
    }

    public void setRight_value(String right_value) {
        this.right_value = right_value;
    }



    public KeyValue(int view_type) {
        this.view_type = view_type;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getRes_color() {
        return res_color;
    }

    public void setRes_color(int res_color) {
        this.res_color = res_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRes_image() {
        return res_image;
    }

    public void setRes_image(int res_image) {
        this.res_image = res_image;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getListValue() {
        return listValue;
    }

    public void setListValue(List<String> listValue) {
        this.listValue = listValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSpan_size() {
        return span_size;
    }

    public void setSpan_size(int span_size) {
        this.span_size = span_size;
    }


    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getSecond_group_id() {
        return second_group_id;
    }

    public void setSecond_group_id(String second_group_id) {
        this.second_group_id = second_group_id;
    }
    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.type);
        dest.writeString(this.user_type);
        dest.writeString(this.activity_type);
        dest.writeByte(this.required ? (byte) 1 : (byte) 0);
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.left_title);
        dest.writeString(this.right);
        dest.writeString(this.right_key);
        dest.writeString(this.right_name);
        dest.writeString(this.right_value);
        dest.writeString(this.id);
        dest.writeString(this.max);
        dest.writeString(this.min);
        dest.writeString(this.group_id);
        dest.writeString(this.second_group_id);
        dest.writeInt(this.view_type);
        dest.writeInt(this.res_image);
        dest.writeInt(this.num);
        dest.writeInt(this.res_color);
        dest.writeInt(this.span_size);
        dest.writeInt(this.item_id);
        dest.writeStringList(this.listValue);
        dest.writeTypedList(this.cpwBeanArrayList);
    }

    protected KeyValue(Parcel in) {
        this.key = in.readString();
        this.type = in.readString();
        this.user_type = in.readString();
        this.activity_type = in.readString();
        this.required = in.readByte() != 0;
        this.value = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.left_title = in.readString();
        this.right = in.readString();
        this.right_key = in.readString();
        this.right_name = in.readString();
        this.right_value = in.readString();
        this.id = in.readString();
        this.max = in.readString();
        this.min = in.readString();
        this.group_id = in.readString();
        this.second_group_id = in.readString();
        this.view_type = in.readInt();
        this.res_image = in.readInt();
        this.num = in.readInt();
        this.res_color = in.readInt();
        this.span_size = in.readInt();
        this.item_id = in.readInt();
        this.listValue = in.createStringArrayList();
        this.cpwBeanArrayList = in.createTypedArrayList(CPWBean.CREATOR);
    }

    public static final Creator<KeyValue> CREATOR = new Creator<KeyValue>() {
        @Override
        public KeyValue createFromParcel(Parcel source) {
            return new KeyValue(source);
        }

        @Override
        public KeyValue[] newArray(int size) {
            return new KeyValue[size];
        }
    };
}
