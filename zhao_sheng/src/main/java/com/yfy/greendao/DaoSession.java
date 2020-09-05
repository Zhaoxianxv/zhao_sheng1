package com.yfy.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.yfy.greendao.KeyValueDb;
import com.yfy.greendao.AdminDb;
import com.yfy.greendao.MainIndex;
import com.yfy.greendao.User;

import com.yfy.greendao.KeyValueDbDao;
import com.yfy.greendao.AdminDbDao;
import com.yfy.greendao.MainIndexDao;
import com.yfy.greendao.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig keyValueDbDaoConfig;
    private final DaoConfig adminDbDaoConfig;
    private final DaoConfig mainIndexDaoConfig;
    private final DaoConfig userDaoConfig;

    private final KeyValueDbDao keyValueDbDao;
    private final AdminDbDao adminDbDao;
    private final MainIndexDao mainIndexDao;
    private final UserDao userDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        keyValueDbDaoConfig = daoConfigMap.get(KeyValueDbDao.class).clone();
        keyValueDbDaoConfig.initIdentityScope(type);

        adminDbDaoConfig = daoConfigMap.get(AdminDbDao.class).clone();
        adminDbDaoConfig.initIdentityScope(type);

        mainIndexDaoConfig = daoConfigMap.get(MainIndexDao.class).clone();
        mainIndexDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        keyValueDbDao = new KeyValueDbDao(keyValueDbDaoConfig, this);
        adminDbDao = new AdminDbDao(adminDbDaoConfig, this);
        mainIndexDao = new MainIndexDao(mainIndexDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(KeyValueDb.class, keyValueDbDao);
        registerDao(AdminDb.class, adminDbDao);
        registerDao(MainIndex.class, mainIndexDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        keyValueDbDaoConfig.clearIdentityScope();
        adminDbDaoConfig.clearIdentityScope();
        mainIndexDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
    }

    public KeyValueDbDao getKeyValueDbDao() {
        return keyValueDbDao;
    }

    public AdminDbDao getAdminDbDao() {
        return adminDbDao;
    }

    public MainIndexDao getMainIndexDao() {
        return mainIndexDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}
