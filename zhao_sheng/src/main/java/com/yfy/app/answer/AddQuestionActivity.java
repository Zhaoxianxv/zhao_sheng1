package com.yfy.app.answer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.answer.bean.AnswerResult;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.FileTools;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;

import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
public class AddQuestionActivity extends WcfActivity {

    private static final String TAG = AddQuestionActivity.class.getSimpleName();
    @Bind(R.id.question_title_bar)
    Toolbar toolbar;
    @Bind(R.id.question_add_edit)
    EditText add_question;
    @Bind(R.id.question_add_pic)
    ImageView add_pic;



    private String question;
    private String ADD_QUESTION="QA_add_question";
    private Photo photo;
    private LoadingDialog dialog;
    private MyDialog typeDialog;
    private List<Photo> photoList = new ArrayList<Photo>();
    public static String path = Environment.getExternalStorageDirectory().toString() + "/notice/";
    public static String temp_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_add_question);
        dialog=new LoadingDialog(mActivity);
        initToolbar();
        initDialog();
        add_pic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (photo==null){
                    return true;
                }else{
                    photo=null;
                    Glide.with(mActivity).load(R.drawable.add_pic)
                            .into(add_pic);
                    return true;
                }
            }
        });
    }


    //菜单 返回true(显示) false（）
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.submit_question:
                if (isSubmit()){
                    submitQuestion();
                }else{
                    toastShow("请输入提问内容");
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        AnswerResult infor = gson.fromJson(result, AnswerResult.class);
        if (infor.getResult().equals("true")){
            toastShow("添加成功");
            onPageBack();
        }else{
            toastShow(infor.getError_code());
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(R.string.fail_do_not);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case  TagFinal.CAMERA:
                    temp_path=FileCamera.photo_camera;
                    photo= new Photo("", temp_path, "", FileTools.getFileSize(temp_path), true);
                    Glide.with(mActivity).load(temp_path)
                            .into(add_pic);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    photo = photo_a.get(0);
                    Glide.with(mActivity).load(photo.getPath())
                            .into(add_pic);
                    break;


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.question_add_pic)
    void setAddPic(){
        if (photo!=null){
            Intent intent=new Intent(mActivity, SingePicShowActivity.class);
            Bundle b=new Bundle();
            b.putString(TagFinal.ALBUM_SINGE_URI,photo.getPath());
            intent.putExtras(b);
            startActivity(intent);
        }else{
            typeDialog.showAtBottom();
        }
    }

    public void initToolbar(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.app_head_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    private int ischangeimage;
    public boolean isSubmit(){
        closeKeyWord();
        question=add_question.getText().toString();
        if (StringJudge.isEmpty(question)){
            toastShow("请输入内容");
            return false;
        }

        if (photo!=null){
            ischangeimage=1;

        }else{
            ischangeimage=0;
        }
        return true;
    }


    public void submitQuestion(){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                0,
                question,
                ischangeimage,//有传 1，没得0
                "",
                ""
        };
        ParamsTask refreshTask = new ParamsTask(params, ADD_QUESTION, ADD_QUESTION,dialog);
        ExtraRunTask wrapTask = new ExtraRunTask(refreshTask);
        wrapTask.setExtraRunnable(extraRunnable);
        execute(wrapTask);
    }

    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            Photo p=new Photo();
//            String path= CompressUtils.compressFileStringSample(photo.getPath());
//            if (path==null){
                p.setPath(photo.getPath());
//            }else{
//                p.setPath(path);
//            }
            list.add(p);
            if (photo==null){
                params[4] = "";
                params[5] = "";
            }else{
                params[4] = Base64Utils.fileToBase64Str(p.getPath());
                params[5] = Base64Utils.getFirstPic(list);
            }
            return params;

        }
    };

    private void initDialog() {
        typeDialog= new MyDialog(mActivity,
                R.layout.dialog_getpic_type_popup,
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle },
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle });
        typeDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        typeDialog.setOnCustomDialogListener(new AbstractDialog.OnCustomDialogListener() {
            @Override
            public void onClick(View v, AbstractDialog dialog) {
                switch (v.getId()) {
                    case R.id.take_photo:
                        PermissionTools.tryCameraPerm(mActivity);
                        dialog.dismiss();
                        break;
                    case R.id.alnum:
                        PermissionTools.tryWRPerm(mActivity);
                        dialog.dismiss();
                        break;
                    case R.id.cancle:
                        dialog.dismiss();
                        break;
                }
            }
        });

    }



    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        FileCamera camera=new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }
    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent;
        intent = new Intent(mActivity, AlbumOneActivity.class);
        Bundle b = new Bundle();
        b.putInt("position", 0);
        b.putBoolean("single", true);//true 单选;多选false
        intent.putExtras(b);
        startActivityForResult(intent,TagFinal.PHOTO_ALBUM);
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
