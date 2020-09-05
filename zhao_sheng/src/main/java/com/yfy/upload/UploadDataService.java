package com.yfy.upload;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

import com.yfy.base.App;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yfy1 on 2016/9/20.
 */
public class UploadDataService extends Service{
    private String name,oldname,packagename,packagenameurl;
    private int code,oldcode;
    String lines;
    private static String url="";
    public static String getUrl() {
		return url;
	}
    Handler handler=new Handler();


    @Override
    public void onCreate() {
        super.onCreate();
        getVersion();
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        return super.onStartCommand(intent, flags, startId);
    }

    public String getVersion() {
        try {
            PackageManager manager = App.getApp().getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.getApp().getApplicationContext().getPackageName(), 0);
            oldname = info.versionName;
            oldcode = info.versionCode;
            packagename = info.packageName;
            return oldname;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    private Thread thread=new Thread(new Runnable() {
        @Override
        public void run() {
            StringBuffer sb = new StringBuffer();
            String s="";
            try {
                HttpURLConnection conn = null;
                URL url = new URL(Base.AUTO_UPDATE_URI);
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("conn","Keep-Alive");
                Logger.e("zxx","-conn-1-");
                conn.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((lines = reader.readLine()) != null){
                    sb.append(lines);
                }

                reader.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
          
            try {
            	  
            	if (sb.toString()!=null&&sb.toString().length()!=0) {
                    Logger.e("zxx","-conn-2-");
            		s=sb.toString();
				}else {
					return;
				}
                Logger.e("zxx","-conn-3-"+s);
                JSONObject json= new JSONObject(s);
                name=  json.getString("versionName");
                url=  json.getString("url");
                code=  json.getInt("versionCode");
                packagenameurl=  json.getString("packagename");
                Logger.e("zxx",">>"+name+">>>>"+url);
                if (code>oldcode&&packagename.equals(packagenameurl)){
                    Logger.e("zxx",">>"+name+">>>>");
                    Logger.e("zxx",">>"+oldcode+">>>>");
                    Intent i=new Intent();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setClass(getApplicationContext(),UpDataDialogActivity.class);
                    startActivity(i);
                  
                }else{
                   
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //packagename.equals(packagenameurl)
            
           
        }
    });

  
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
