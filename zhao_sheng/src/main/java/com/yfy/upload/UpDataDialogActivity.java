package com.yfy.upload;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.base.App;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;

import java.util.Timer;
import java.util.TimerTask;

import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;


public class UpDataDialogActivity extends BaseActivity {
    private UpdateManager mUpdateManager;
    private  LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_data_dialog);
        initView();
    }

    private void initView() {
        TextView upData= (TextView) findViewById(R.id.updata_app);
        TextView exit= (TextView) findViewById(R.id.updata_exit);
        layout= (LinearLayout) findViewById(R.id.updata_app_layout);
        upData.setOnClickListener(view);
        exit.setOnClickListener(view);


    }
    private View.OnClickListener view=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.updata_app:
                    //        launchAppDetail(getPackageName(),"");
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        //先判断是否有安装未知来源应用的权限
                        boolean haveInstallPermission = getPackageManager().canRequestPackageInstalls();
                        if(!haveInstallPermission){
                            toInstallPermissionSettingIntent();
                        }else{
                            PermissionTools.tryWRPerm(mActivity);
                        }
                    }else{
                        PermissionTools.tryWRPerm(mActivity);
                    }
                break;
                case R.id.updata_exit:
                   exitBy2Click();
                break;

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent() {
        Uri packageURI = Uri.parse("package:"+getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }

    /**
     * 
     */
    private static Boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this,R.string.again_click_exit,Toast.LENGTH_LONG).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            App.getApp().onTerminate();
        }
    }


    public void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {

    }

    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
//        launchAppDetail(getPackageName(),"");
        mUpdateManager = new UpdateManager(this);
        layout.setVisibility(View.GONE);
        mUpdateManager.checkUpdateInfo();
        stopService(new Intent(App.getApp().getApplicationContext(),UploadDataService.class));
    }
    @PermissionFail(requestCode = TagFinal.CAMERA)
    private void showCamere() {
        Toast.makeText(getApplicationContext(), R.string.permission_fail_camere, Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = TagFinal.PHOTO_ALBUM)
    private void showTip1() {
        Toast.makeText(getApplicationContext(), R.string.permission_fail_album, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


}
