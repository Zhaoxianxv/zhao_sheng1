package com.yfy.greendao;

import com.yfy.final_tag.TagFinal;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class KeyValueDb {
    @NotNull
    private String type="";//用于判断数据类型
    @NotNull
    private String model_type="";//用于判断了个功能木快5.7
    @NotNull
    private String parent_id="";//7.9
    @NotNull
    private String child_id="";
    @NotNull
    private boolean required=true;//用于判断是否必填
    @NotNull
    private int view_type=TagFinal.TYPE_ITEM;
    @NotNull
    private String key_value_id="";//id
    @NotNull
    private String key="";//描述值
    @NotNull
    private String value="";//值
    @NotNull
    private String name="";//值名
    @NotNull
    private String image="";//图片
    @NotNull
    private int num=-1;
    @Id
    private Long id;
    @Generated(hash = 123966101)
    public KeyValueDb(@NotNull String type, @NotNull String model_type,
            @NotNull String parent_id, @NotNull String child_id, boolean required,
            int view_type, @NotNull String key_value_id, @NotNull String key,
            @NotNull String value, @NotNull String name, @NotNull String image,
            int num, Long id) {
        this.type = type;
        this.model_type = model_type;
        this.parent_id = parent_id;
        this.child_id = child_id;
        this.required = required;
        this.view_type = view_type;
        this.key_value_id = key_value_id;
        this.key = key;
        this.value = value;
        this.name = name;
        this.image = image;
        this.num = num;
        this.id = id;
    }

    public KeyValueDb(String key, String value, String name) {
        this.key = key;
        this.value = value;
        this.name = name;
    }
    public KeyValueDb(String key, String value, String name,int view_type) {
        this.key = key;
        this.value = value;
        this.name = name;
        this.view_type = view_type;
    }



    public KeyValueDb(Long id) {
        this.id = id;
    }

    @Generated(hash = 980486890)
    public KeyValueDb() {
    }
    public String getKey_value_id() {
        return this.key_value_id;
    }
    public void setKey_value_id(String key_value_id) {
        this.key_value_id = key_value_id;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public boolean getRequired() {
        return this.required;
    }
    public void setRequired(boolean required) {
        this.required = required;
    }
    public int getView_type() {
        return this.view_type;
    }
    public void setView_type(int view_type) {
        this.view_type = view_type;
    }
    public String getModel_type() {
        return this.model_type;
    }
    public void setModel_type(String model_type) {
        this.model_type = model_type;
    }

    public String getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getChild_id() {
        return this.child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }






}
