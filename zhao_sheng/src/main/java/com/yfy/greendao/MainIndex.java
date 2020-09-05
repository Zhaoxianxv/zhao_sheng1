package com.yfy.greendao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MainIndex {
    @Id
    private Long id;
    @NotNull
    private String key;
    @NotNull
    private int num;
    @Generated(hash = 1806263387)
    public MainIndex(Long id, @NotNull String key, int num) {
        this.id = id;
        this.key = key;
        this.num = num;
    }
    @Generated(hash = 1181495789)
    public MainIndex() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }


}
