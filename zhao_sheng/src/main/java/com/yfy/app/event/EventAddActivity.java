package com.yfy.app.event;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventAddReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.event.bean.Dep;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAddActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = EventAddActivity.class.getSimpleName();


    @Bind(R.id.event_add_time)
    TextView time_view;
    @Bind(R.id.event_edit_user)
    EditText user_edeit;
    @Bind(R.id.event_add_dep)
    TextView dep_view;

    @Bind(R.id.event_add_site)
    EditText site_edit;
    @Bind(R.id.event_add_content)
    EditText content_edit;

    @Bind(R.id.event_add_mult)
    MultiPictureView add_mult;

    private String user_id,depid,content,site;

    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_add);
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        initSQtoolbar();
        initAbsListView();
        initView();
        initTypeDialog();
    }

    private void initSQtoolbar(){
        assert toolbar!=null;
        toolbar.setTitle("新增");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        if (isSned()){
                            mtask=new MyAsyncTask();
                            mtask.execute();
                        }
                        break;
                }
            }
        });
    }

    private void initView(){
        time_view.setText(dateBean.getName());
        time_view.setTextColor(ColorRgbUtil.getBaseText());
        user_edeit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String edit_String=s.toString();
                if (edit_String.length()>10)
                    s.delete(10,edit_String.length());//限制2位小数.
            }
        });

        content_edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String edit_String=s.toString();
                if (edit_String.length()>70){
                    s.delete(70,edit_String.length());//限制2位小数.
                    toastShow("字数限制");
                }

            }
        });
    }





    public void addMult(String uri){
        if (uri==null) return;
        add_mult.addItem(uri);
    }
    public void setMultList(List<Photo> list){
        for (Photo photo:list){
            if (photo==null) continue;
            addMult(photo.getPath());
        }
    }

    private void initAbsListView() {
        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                typeDialog.showAtBottom();
            }
        });
        add_mult.setClickable(false);
        add_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int index) {
                add_mult.removeItem(index,true);
            }
        });
        add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    @OnClick(R.id.event_add_time)
    void setTime(){

        DialogTools.getInstance().showDatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateBean.setDateYMD(year,month,dayOfMonth);
                time_view.setText(dateBean.getName());
            }
        });
    }


    @OnClick(R.id.event_add_dep)
    void setPro(){
        Intent intent=new Intent(mActivity,EventDepActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }


    private boolean isSned(){
         site=site_edit.getText().toString().trim();
         content=content_edit.getText().toString().trim();
         user_id=user_edeit.getText().toString().trim();
        if (StringJudge.isEmpty(site)){
            toastShow("请输入地点");
            return false;
        }
        if (StringJudge.isEmpty(depid)){
            toastShow("请选择部门");
            return false;
        }
        if (StringJudge.isEmpty(user_id)){
            toastShow("请选择负责人");
            return false;
        }
        if (StringJudge.isEmpty(content)){
            toastShow("请填写大事内容");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch(requestCode){
                case TagFinal.UI_ADMIN://dep_id
                    Dep dep=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    dep_view.setText(dep.getDepartname());
                    dep_view.setTextColor(ColorRgbUtil.getBaseText());
                    depid=dep.getDepartid();
                    break;
                case TagFinal.UI_TAG://user_id
                    break;
                case TagFinal.UI_ADD:
                    break;
                case TagFinal.CAMERA:
                    addMult(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    setMultList(photo_a);
                    break;
            }
        }
    }










    private MyDialog typeDialog;
    private void initTypeDialog() {
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
        FileCamera camera = new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }

    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent = new Intent(mActivity, AlbumOneActivity.class);
        Bundle b = new Bundle();
        b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
        b.putBoolean(TagFinal.ALBUM_SINGLE, false);
        intent.putExtras(b);
        startActivityForResult(intent, TagFinal.PHOTO_ALBUM);
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










    /**
     * ----------------------------retrofit-----------------------
     */








    public void addEvent() {
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                user_id,
                "0",//大事记id，新增0
                depid,//
                dateBean.getValue(),
                content,
                site,
                "",
                ""

        };

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventAddReq req = new EventAddReq();

        req.setLiableuser(user_id);
        req.setId(0);
        req.setDepid(ConvertObjtect.getInstance().getInt(depid));
        req.setDate(dateBean.getValue());
        req.setContent(StringUtils.stringToUpJson(content));
        req.setAddress(site);
        req.setImages(name_s);
        req.setImage_file(content_s);


        //获取参数
        reqBody.eventAddReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_add(reqEnv);
        call.enqueue(this);
//        Logger.e();
    }





    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.eventAddRes !=null){
                String result=b.eventAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
//                EventRes res=gson.fromJson(result,EventRes.class);
                if (StringJudge.isSuccess(gson,result )){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }




    private MyAsyncTask mtask;
    private String name_s="",content_s="";
    private List<Photo> photo_list=new ArrayList<>();
    public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
        //内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(String... arg0) {
            if (isCancelled()) {
                return null;
            }
            name_s="";
            content_s="";
            int i=0;
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
                i++;
                String picName = String.valueOf(System.currentTimeMillis())+String.valueOf(i);
                p.setFileName(picName+".jpg");
                p.setPath(uri);
                photo_list.add(p);
            }
            if (StringJudge.isEmpty(photo_list)){
            }else{
                name_s= Base64Utils.getZipTitleNotice(photo_list);
                content_s=  Base64Utils.filesToZipBase64Notice(photo_list);
            }
            return null;
        }
        //onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            addEvent();
        }
        //onProgressUpdate方法用于更新进度信息
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);

        }
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }
    }
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //判断AsyncTask不为null且Status.RUNNING在运行状态
        if (mtask!=null&&mtask.getStatus()==AsyncTask.Status.RUNNING) {//为mtask标记cancel(true)状态
            mtask.cancel(true);
        }
    }





}
