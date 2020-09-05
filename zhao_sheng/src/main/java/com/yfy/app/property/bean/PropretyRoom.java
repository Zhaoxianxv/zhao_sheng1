package com.yfy.app.property.bean;

import butterknife.Bind;

/**
 * Created by yfyandr on 2018/1/18.
 */

public class PropretyRoom {
    String name;
    String id;
    boolean is_roll;
    BindUser bindUser;
    BindUser rooluser;

    public PropretyRoom(String name, boolean is_roll, String id, BindUser bindUser, BindUser rooluser) {
        this.name = name;
        this.is_roll = is_roll;
        this.id = id;
        this.bindUser = bindUser;
        this.rooluser = rooluser;
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

    public BindUser getBindUser() {
        return bindUser;
    }

    public void setBindUser(BindUser bindUser) {
        this.bindUser = bindUser;
    }

    public boolean is_roll() {
        return is_roll;
    }

    public void setIs_roll(boolean is_roll) {
        this.is_roll = is_roll;
    }

    public BindUser getRooluser() {
        return rooluser;
    }

    public void setRooluser(BindUser rooluser) {
        this.rooluser = rooluser;
    }
}
