package com.yfy.db;

import android.util.Log;

import com.yfy.base.App;
import com.yfy.final_tag.TagFinal;
import com.yfy.greendao.AdminDb;
import com.yfy.greendao.AdminDbDao;
import com.yfy.greendao.KeyValueDb;
import com.yfy.greendao.KeyValueDbDao;
import com.yfy.greendao.MainIndex;
import com.yfy.greendao.MainIndexDao;
import com.yfy.greendao.User;
import com.yfy.greendao.UserDao;
import com.yfy.final_tag.Logger;


import java.util.List;


/**
 * Created by yfy1 on 2016/12/30.
 */

public class GreenDaoManager {


    public static GreenDaoManager manager;


    public synchronized static GreenDaoManager getInstance() {
        if (manager == null) {
            manager=new GreenDaoManager();
        }
        return manager;
    }




    private UserDao getUserDao() {
        // 通过 BaseApplication 类提供的 getDaoSession() 获取具体 Dao
        return ((App) App.getApp().getApplicationContext()).getDaoSession().getUserDao();
    }
    private MainIndexDao getMainIndexDao() {
        return ((App) App.getApp().getApplicationContext()).getDaoSession().getMainIndexDao();
    }

    private KeyValueDbDao getKeyValueDbDao() {

        return ((App) App.getApp().getApplicationContext()).getDaoSession().getKeyValueDbDao();
    }
    private AdminDbDao getAdminDbDao() {
        return ((App) App.getApp().getApplicationContext()).getDaoSession().getAdminDbDao();
    }


    /**
     * 根据id,删除数据
     * @param id      用户id
     */
    public void deleteNote(long id){
        getUserDao().deleteByKey(id);
        Log.i(TagFinal.ZXX, "delete");
    }
    /**
     * 根据用户类,删除信息
     * @param user    用户信息类
     */
    public void deleteNote(User user){
        getUserDao().delete(user);
    }
    public long saveUser(User user){
        return getUserDao().insertOrReplace(user);
    }
    public void clearUser() {
        getUserDao().deleteAll();
    }

    public List<User> loadAllUser(){
        return getUserDao().loadAll();
    }
    public User getUser(String  session_key){
        return getUserDao().queryRaw("where idU = \""+session_key+"\"").get(0);
    }

    public List<User> queryNote(String where, String... params){
        return getUserDao().queryRaw(where, params);
    }

    /**
     * 批量插入或修改用户信息
     * @param list      用户信息列表
     */
    public void saveNoteLists(final List<User> list){
        if(list == null || list.isEmpty()){
            return;
        }
        getUserDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    User user = list.get(i);
                    getUserDao().insertOrReplace(user);
                }
            }
        });

    }


    public List<MainIndex> loadAllMainIndex(){
        return getMainIndexDao().loadAll();
    }


    public long saveMainIndex(MainIndex user){
        return getMainIndexDao().insertOrReplace(user);
    }


    public MainIndex geMainIndex(String  key){
        return getMainIndexDao().queryRaw("where key = \""+key+"\"").get(0);
    }
    /**清除 用户*/
    public void clearMainIndex() {
        getMainIndexDao().deleteAll();
    }


    /**
     * ------------------key value-------------
     */
    public void clearKeyValue() {
        getKeyValueDbDao().deleteAll();
    }

    public List<KeyValueDb> loadAllKeyValueDb(){
        return getKeyValueDbDao().loadAll();
    }

    public long saveKeyValueDb(KeyValueDb keyValueDb){
        return getKeyValueDbDao().insertOrReplace(keyValueDb);
    }

    //"where key = \""+key+"\""
    public List<KeyValueDb> getKeyValueDbList(String  key){
        Logger.e(key);
        return getKeyValueDbDao().queryRaw(key);
    }
    public List<KeyValueDb> getKeyValueDbList(String where, String... params){
        return getKeyValueDbDao().queryRaw(where,params);
    }

    public void saveAllKeyValueDb(final List<KeyValueDb> list){
        if(list == null || list.isEmpty()){
            return;
        }
        getKeyValueDbDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    KeyValueDb astir = list.get(i);
//                    Logger.e(i+"");
                    getKeyValueDbDao().insertOrReplace(astir);
                }
            }
        });

    }

    /**
     * -----------------------adminDb---------------------
     */
    public long saveAdminDb(AdminDb adminDb){
        return getAdminDbDao().insertOrReplace(adminDb);
    }
    public void clearAdminDb() {
        getAdminDbDao().deleteAll();
    }
    public AdminDb getAdminDb(){
        return getAdminDbDao().loadAll().get(0);
    }
    public List<AdminDb> loadAdminDb(){
        return getAdminDbDao().loadAll();
    }
}
