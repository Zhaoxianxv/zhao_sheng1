package com.yfy.app.event;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.event.bean.EventRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.event.EventDelImageReq;
import com.yfy.app.net.event.EventDelReq;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.event.bean.Dep;
import com.yfy.app.event.bean.EventBean;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAlterActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = EventAlterActivity.class.getSimpleName();

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
    @Bind(R.id.event_del_mult)
    MultiPictureView del_mult;


    @Bind(R.id.event_del_mult_layout)
    RelativeLayout layout;
    private EventBean eventBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_alter);
        getData();
        initSQtoolbar();
        initAbsListView();
        initView();
        initTypeDialog();
    }


    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        super.onPageBack();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void getData(){
        eventBean=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
    }

    private void initSQtoolbar(){
        assert toolbar!=null;
        toolbar.setTitle("编辑");
        toolbar.addMenuText(TagFinal.ONE_INT,"修改");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        if (isSned()){
                            addEvent();
                        }
                        break;
                }
            }
        });
    }

    private void initView(){

        if (StringJudge.isNull(eventBean)){
            toastShow("数据出错");
            return;
        }
        time_view.setText(DateUtils.changeDate(eventBean.getDate(), "yyyy年MM月dd"));
        site_edit.setText(eventBean.getAddress());
        dep_view.setText(eventBean.getDepartmentname());
        user_edeit.setText(eventBean.getLiableuser());
        content_edit.setText(eventBean.getContent() );
        del_mult.clearItem();
        List<String> photos= new ArrayList<>();
        if (!StringJudge.isEmpty(eventBean.getImage())){
            photos.addAll(StringUtils.listToStringSplitCharacters(eventBean.getImage(),"|"));
        }
        if (StringJudge.isEmpty(photos)){
            layout.setVisibility(View.GONE);
            del_mult.addItem(photos);
        }else{
            layout.setVisibility(View.VISIBLE);
            del_mult.addItem(photos);
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

    private int del_index;
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
        del_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, final int index) {
                del_index=index;
                //
                DialogTools.getInstance().showDialog(mActivity, "", "是否删除图片！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> photos= new ArrayList<>();
                        if (!StringJudge.isEmpty(eventBean.getImage())){
                            photos.addAll(StringUtils.listToStringSplitCharacters(eventBean.getImage(),"|"));
                        }
                        delImage(eventBean.getEventid(),photos.get(del_index) );
                    }
                });
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
        del_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
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
                eventBean.setDate(DateUtils.getDate(year, month, dayOfMonth));
                time_view.setText(DateUtils.getDate2(year, month, dayOfMonth));
            }
        });
    }

    @OnClick(R.id.event_del_item)
    void setDelEvent(){
        DialogTools.getInstance().showDialog(mActivity, "", "是否删除此记录！", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delEvent(eventBean.getEventid());
            }
        });
    }

    @OnClick(R.id.event_add_dep)
    void setPro(){
        Intent intent=new Intent(mActivity,EventDepActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }


    private boolean isSned(){
        eventBean.setAddress( site_edit.getText().toString().trim());
        eventBean.setContent(content_edit.getText().toString().trim());
        eventBean.setLiableuser(user_edeit.getText().toString().trim());
        if (eventBean.getDate().isEmpty()){
            toastShow(R.string.please_choose_time);
            return false;
        }
        if (eventBean.getAddress().isEmpty()){
            toastShow("请输入地点");
            return false;
        }
        if (eventBean.getDepartmentid().isEmpty()){
            toastShow("请选择部门");
            return false;
        }
        if (eventBean.getLiableuser().isEmpty()){
            toastShow("请填写负责人");
            return false;
        }
        if (eventBean.getContent().isEmpty()){
            toastShow("请填写大事内容");
            return false;
        }
        return true;
    }
    public void addEvent() {
        Object[] params = new Object[]{
                Base.user.getSession_key(),
                eventBean.getLiableuser(),
                eventBean.getEventid(),//大事记id，新增0
                eventBean.getDepartmentid(),//
                eventBean.getDate(),
                eventBean.getContent(),
                eventBean.getAddress(),
                "",
                ""

        };

        ParamsTask  refreshTask = new ParamsTask(params, TagFinal.EVENT_ADD, TagFinal.EVENT_ADD);
        ExtraRunTask wrapTask = new ExtraRunTask(refreshTask);
        wrapTask.setExtraRunnable(extraRunnable);
        showProgressDialog("");
        execute(wrapTask);
    }



    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
                p.setPath(uri);
                list.add(p);
            }
            params[7] = Base64Utils.getZipTitle2(list);
            params[8] = Base64Utils.getZipBase64Str(list);
            return params;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch(requestCode){
                case TagFinal.UI_ADMIN://dep_id
                    Dep dep=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    dep_view.setText(dep.getDepartname());
                    dep_view.setTextColor(ColorRgbUtil.getBaseText());
                    eventBean.setDepartmentid(dep.getDepartid());
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

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        String name=wcfTask.getName();
        Logger.e(result );
        if (name.equals(TagFinal.EVENT_ADD)){
            if (StringJudge.isSuccess(gson,result )){
                toastShow(R.string.success_do);
                finish();
            }
        }


        return false;
    }



    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
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



    /**
     * ----------------------------retrofit-----------------------
     */

    public void delEvent(String id) {
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventDelReq req = new EventDelReq();
        //获取参数
        req.setId(ConvertObjtect.getInstance().getInt(id));
        reqBody.eventDelReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_del(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }



    public void delImage(String id,String paht) {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        EventDelImageReq req = new EventDelImageReq();
        //获取参数
        req.setImage(paht);
        req.setId(ConvertObjtect.getInstance().getInt(id));
        reqBody.eventDelImageReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().event_dep_image(reqEnv);
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
            if (b.eventDelImageRes !=null){
                String result=b.eventDelImageRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class );
                if (StringJudge.isSuccess(gson,result )){
                    toastShow(R.string.success_do);
                    del_mult.removeItem(del_index,true);
                    List<String> photos= new ArrayList<>();
                    if (!StringJudge.isEmpty(eventBean.getImage())){
                        photos.addAll(StringUtils.listToStringSplitCharacters(eventBean.getImage(),"|"));
                    }
                    photos.remove(del_index);
                    eventBean.setImage(StringUtils.stringToArraysGetString(photos,"|"));
                }else{
                    toastShow(R.string.success_not_do);
                }
            }
            if (b.eventDelRes !=null){
                String result=b.eventDelRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class );
                if (StringJudge.isSuccess(gson,result )){
                    toastShow(R.string.success_do);
                    finish();
                }else{
                    toast("失败");
                }
            }
        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
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








    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
