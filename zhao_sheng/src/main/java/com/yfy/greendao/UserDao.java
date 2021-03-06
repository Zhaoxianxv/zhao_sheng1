package com.yfy.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Username = new Property(2, String.class, "username", false, "USERNAME");
        public final static Property IdU = new Property(3, String.class, "idU", false, "ID_U");
        public final static Property Session_key = new Property(4, String.class, "session_key", false, "SESSION_KEY");
        public final static Property HeadPic = new Property(5, String.class, "headPic", false, "HEAD_PIC");
        public final static Property Pwd = new Property(6, String.class, "pwd", false, "PWD");
        public final static Property Token = new Property(7, String.class, "token", false, "TOKEN");
        public final static Property Classid = new Property(8, String.class, "classid", false, "CLASSID");
        public final static Property Fxid = new Property(9, String.class, "fxid", false, "FXID");
        public final static Property IsDuplication = new Property(10, String.class, "isDuplication", false, "IS_DUPLICATION");
        public final static Property Rightlist = new Property(11, String.class, "rightlist", false, "RIGHTLIST");
        public final static Property Usertype = new Property(12, String.class, "usertype", false, "USERTYPE");
        public final static Property Date = new Property(13, java.util.Date.class, "date", false, "DATE");
    }


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"NAME\" TEXT NOT NULL ," + // 1: name
                "\"USERNAME\" TEXT NOT NULL ," + // 2: username
                "\"ID_U\" TEXT NOT NULL ," + // 3: idU
                "\"SESSION_KEY\" TEXT NOT NULL ," + // 4: session_key
                "\"HEAD_PIC\" TEXT NOT NULL ," + // 5: headPic
                "\"PWD\" TEXT NOT NULL ," + // 6: pwd
                "\"TOKEN\" TEXT NOT NULL ," + // 7: token
                "\"CLASSID\" TEXT NOT NULL ," + // 8: classid
                "\"FXID\" TEXT NOT NULL ," + // 9: fxid
                "\"IS_DUPLICATION\" TEXT NOT NULL ," + // 10: isDuplication
                "\"RIGHTLIST\" TEXT NOT NULL ," + // 11: rightlist
                "\"USERTYPE\" TEXT NOT NULL ," + // 12: usertype
                "\"DATE\" INTEGER);"); // 13: date
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getUsername());
        stmt.bindString(4, entity.getIdU());
        stmt.bindString(5, entity.getSession_key());
        stmt.bindString(6, entity.getHeadPic());
        stmt.bindString(7, entity.getPwd());
        stmt.bindString(8, entity.getToken());
        stmt.bindString(9, entity.getClassid());
        stmt.bindString(10, entity.getFxid());
        stmt.bindString(11, entity.getIsDuplication());
        stmt.bindString(12, entity.getRightlist());
        stmt.bindString(13, entity.getUsertype());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(14, date.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getName());
        stmt.bindString(3, entity.getUsername());
        stmt.bindString(4, entity.getIdU());
        stmt.bindString(5, entity.getSession_key());
        stmt.bindString(6, entity.getHeadPic());
        stmt.bindString(7, entity.getPwd());
        stmt.bindString(8, entity.getToken());
        stmt.bindString(9, entity.getClassid());
        stmt.bindString(10, entity.getFxid());
        stmt.bindString(11, entity.getIsDuplication());
        stmt.bindString(12, entity.getRightlist());
        stmt.bindString(13, entity.getUsertype());
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(14, date.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // name
            cursor.getString(offset + 2), // username
            cursor.getString(offset + 3), // idU
            cursor.getString(offset + 4), // session_key
            cursor.getString(offset + 5), // headPic
            cursor.getString(offset + 6), // pwd
            cursor.getString(offset + 7), // token
            cursor.getString(offset + 8), // classid
            cursor.getString(offset + 9), // fxid
            cursor.getString(offset + 10), // isDuplication
            cursor.getString(offset + 11), // rightlist
            cursor.getString(offset + 12), // usertype
            cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)) // date
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setUsername(cursor.getString(offset + 2));
        entity.setIdU(cursor.getString(offset + 3));
        entity.setSession_key(cursor.getString(offset + 4));
        entity.setHeadPic(cursor.getString(offset + 5));
        entity.setPwd(cursor.getString(offset + 6));
        entity.setToken(cursor.getString(offset + 7));
        entity.setClassid(cursor.getString(offset + 8));
        entity.setFxid(cursor.getString(offset + 9));
        entity.setIsDuplication(cursor.getString(offset + 10));
        entity.setRightlist(cursor.getString(offset + 11));
        entity.setUsertype(cursor.getString(offset + 12));
        entity.setDate(cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
