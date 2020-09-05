package com.yfy.app.appointment.bean;

import java.util.List;

/**
 * Created by yfy1 on 2017/2/14.
 */

public class OrderRes {
    /**
     * result : true
     * error_code :
     * my_funcRoom : []
     */

    private String result;
    private String error_code;
    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

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
    private List<MyFunRooom> funcRoom;
    public List<MyFunRooom> getMy_funcRoom() {
        return funcRoom;
    }
    public void setMy_funcRoom(List<MyFunRooom> my_funcRoom) {
        this.funcRoom = funcRoom;
    }
    /**
     *  =================================
     */
    private List<Rooms> rooms;
    public List<Rooms> getRooms() {
        return rooms;
    }
    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }
    /**
     *  =================================
     */
    private List<OrderBean> my_funcRoom;
    public List<OrderBean> getAdmin() {
        return my_funcRoom;
    }
    public void setAdmin(List<OrderBean> my_funcRoom) {
        this.my_funcRoom = my_funcRoom;
    }

    /**
     * ===========================
     */
    private List<RoomType> funcRoom_type;
    public List<RoomType> getFuncRoom_type() {
        return funcRoom_type;
    }
    public void setFuncRoom_type(List<RoomType> funcRoom_type) {
        this.funcRoom_type = funcRoom_type;
    }
}
