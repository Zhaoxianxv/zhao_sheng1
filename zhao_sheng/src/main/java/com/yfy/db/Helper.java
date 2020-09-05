package com.yfy.db;

import android.content.Context;

import com.yfy.base.App;

import com.yfy.greendao.DaoMaster;
import com.yfy.greendao.DaoSession;
import com.yfy.greendao.UserDao;
import com.yfy.final_tag.Logger;
import org.greenrobot.greendao.database.Database;

public class Helper extends DaoMaster.OpenHelper{

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;


    public Helper(Context context){
        super(context,App.DB_NAME,null);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        Logger.e(oldVersion + "---先前和更新之后的版本---" + newVersion);
        if (oldVersion < newVersion) {
            Logger.e( oldVersion + "---先前和更新之后的版本---" + newVersion);
            MigrationHelper.migrate(db, UserDao.class);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//            MigrationHelper.migrate(db, UserDao.class,XXDao.class);
        }
    }

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, App.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
