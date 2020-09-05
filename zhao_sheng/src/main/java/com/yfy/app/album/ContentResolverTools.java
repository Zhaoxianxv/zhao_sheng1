package com.yfy.app.album;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by yfyandr on 2018/5/7.
 */

public class ContentResolverTools {

    public void test(Context context){
        ContentResolver v =context.getContentResolver();
        // ContactsContract.Contacts.CONTENT_URI,联系人的Uri
        Cursor c=v.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID}, null, null, null);
        if (c!=null){
            while (c.moveToNext()){
                int id=c.getInt(c.getColumnIndex("_ID"));
                //根据id查询电话号码
                Cursor c1= v.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                        null, null);
                if (c1!=null){
                    while (c1.moveToNext()){
                        int type=c1.getInt(c1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    }
                    c1.close();//一定要清理掉；
                }
                //根据id查询邮箱
                Cursor c2=v.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE}, ContactsContract.CommonDataKinds.Email._ID,null,null);
                if (c2!=null){
                    while (c2.moveToNext()){
                        int email=c2.getInt(c2.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                    }
                    c2.close();
                }
            }
            c.close();//一定要清理掉；
        }
    }
    public void testinsert(Context context){
        ContentResolver v=context.getContentResolver();
        ContentValues values=new ContentValues();
        //首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的raw_Contact_Id
        Uri uri=v.insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long raw_contact_id= ContentUris.parseId(uri);
        values.clear();
        values.put(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, raw_contact_id);
        values.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "啊啊啊");//名字：
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        uri=v.insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        values.put(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,raw_contact_id);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,"");
        values.put(ContactsContract.CommonDataKinds.Phone.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        uri=v.insert(ContactsContract.Data.CONTENT_URI,values);
    }
}
