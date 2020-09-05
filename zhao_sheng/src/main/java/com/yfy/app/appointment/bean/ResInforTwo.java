package com.yfy.app.appointment.bean;

import java.util.List;

/**
 * Created by yfy1 on 2017/2/14.
 */

public class ResInforTwo {
    /**
     * result : true
     * error_code :
     * my_funcRoom : []
     */

    private String result;
    private String error_code;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }


    /**
     *  =================================
     */
    private List<RoomDetail> my_funcRoom;

    public List<RoomDetail> getMy_funcRoom() {
        return my_funcRoom;
    }

    public void setMy_funcRoom(List<RoomDetail> my_funcRoom) {
        this.my_funcRoom = my_funcRoom;
    }
}
