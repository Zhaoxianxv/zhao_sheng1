package com.yfy.app.attennew.bean;

import com.yfy.app.attennew.bean.Subject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class JsonRootBean implements Serializable{
    private List<Subject> approve_user;
    public void setSubject(List<Subject> subject) {
        this.approve_user = approve_user;
    }
    public List<Subject> getSubject() {
        return approve_user;
    }
}
