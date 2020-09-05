package com.yfy.app.maintainnew;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.maintainnew.bean.MainRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.maintain.MaintainAdminSetStateReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.DateUtils;
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

import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

public class MaintainDoingActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = MaintainDoingActivity.class.getSimpleName();
    @Bind(R.id.edit_edit_text)
    EditText edit_content;
    @Bind(R.id.maintian_do_item)
    MultiPictureView add_mult;

    @Bind(R.id.maintian_do_ing)
    TextView do_ing;
    @Bind(R.id.maintian_do_ok)
    TextView do_ok;
    @Bind(R.id.maintian_do_cancle)
    TextView do_cancle;
    @Bind(R.id.maintian_do_ing_icon)
    ImageView icon_ing;
    @Bind(R.id.maintian_do_ok_icon)
    ImageView icon_ok;
    @Bind(R.id.maintian_do_cnacle_icon)
    ImageView icon_cancle;

    private String dealstate="",id="";
    private DateBean dateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_do_edit_sy);
        dateBean=new DateBean(DateUtils.getCurrentTimeName(), DateUtils.getCurrentTimeValue()) ;
        getData();
        initSQToolbar();
        initDialog();
        initAbsListView();
        initView();

    }



    private void getData(){
        id=getIntent().getStringExtra("data");
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("处理报修");
        toolbar.addMenuText(1,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (StringJudge.isEmpty(dealstate)){
                    toastShow("请选择维修状态");
                    return;
                }
                String content=edit_content.getText().toString();
                if (StringJudge.isEmpty(content)){
                    toastShow("请填写维修描述");
                }else{
                    mtask=new MyAsyncTask();
                    mtask.execute();
                }

            }
        });
    }




    ConfirmAlbumWindow album_select;
    private void initDialog() {
        album_select = new ConfirmAlbumWindow(mActivity);
        album_select.setTwo_select(getResources().getString(R.string.album));
        album_select.setOne_select(getResources().getString(R.string.take_photo));
        album_select.setName(getResources().getString(R.string.upload_type));
        album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.popu_select_one:
                        PermissionTools.tryCameraPerm(mActivity);
                        break;
                    case R.id.popu_select_two:
                        PermissionTools.tryWRPerm(mActivity);
                        break;
                }
            }
        });
    }


    private void initAbsListView() {
        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                closeKeyWord();
                album_select.showAtBottom();
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
//				Log.e(TAG, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }



    private void initView() {
        edit_content.setText(UserPreferences.getInstance().getContent());
    }



    @OnClick(R.id.maintian_do_cancle)
    void setCancel(){
        dealstate="2";
        changeIng();
        do_cancle.setTextColor(Color.rgb(44,44,44));
        icon_cancle.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.maintian_do_ok)
    void setOk(){
        dealstate="1";
        changeIng();
        do_ok.setTextColor(Color.rgb(44,44,44));
        icon_ok.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.maintian_do_ing)
    void setDoing(){
        dealstate="3";
        changeIng();
        do_ing.setTextColor(Color.rgb(44,44,44));
        icon_ing.setVisibility(View.VISIBLE);
    }

    public void changeIng(){
        do_ing.setTextColor(Color.rgb(128,128,128));
        do_ok.setTextColor(Color.rgb(128,128,128));
        do_cancle.setTextColor(Color.rgb(128,128,128));
        icon_cancle.setVisibility(View.GONE);
        icon_ok.setVisibility(View.GONE);
        icon_ing.setVisibility(View.GONE);
    }




    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
        b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
        b.putBoolean(TagFinal.ALBUM_SINGLE, false);
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

















    /**
     * ----------------------------retrofit-----------------------
     */







    private void choiceState(String content){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainAdminSetStateReq req = new MaintainAdminSetStateReq();
        //获取参数

        req.setId(id);
        req.setDate(dateBean.getValue());
        req.setRealname(Base.user.getName());
        req.setContent(StringUtils.stringToUpJson(content));
        req.setDealstate(dealstate);
        req.setIsout(0);
        req.setMoney("0");
        req.setPictures(name_s);
        req.setFileContext(content_s);
        req.setDelpictures("");

        reqBody.maintainAdminSetStateReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_admin_set_state(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
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

            if (b.maintainAdminSetStateRes !=null){
                String result=b.maintainAdminSetStateRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                if (StringJudge.isSuccess(gson,result)){
                    toast(R.string.success_do);
                    finish();
                }else{
                    MainRes res=gson.fromJson(result,MainRes.class );
                    toastShow(res.getError_code());
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
        toast(R.string.fail_do_not);
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
            String content=edit_content.getText().toString();
            choiceState(content);
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
