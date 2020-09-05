package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.bean.DelayEventBean;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.delay_service.DelayAdminClassSetReq;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayServiceSetActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayServiceSetActivity.class.getSimpleName();

    @Bind(R.id.delay_admin_set_time)
    TextView choice_date;
    @Bind(R.id.delay_end_reason)
    EditText end_reason;
    @Bind(R.id.delay_service_reason)
    EditText service_reason;

    @Bind(R.id.delay_service_multi)
    MultiPictureView add_multi;
    @Bind(R.id.delay_end_multi)
    MultiPictureView end_multi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_service_set);
        initAlbumDialog();
        initAbsListView();
        getData();
        choice_date.setText(dateBean.getName());
        initSQtoobar("巡查");

    }




    //==============add icon============


    public void addMult(String uri){
        if (uri==null) return;
        add_multi.addItem(uri);
    }
    public void setMultList(List<Photo> list){
        for (Photo photo:list){
            if (photo==null) continue;
            addMult(photo.getPath());
        }
    }


    private String album_type ="";
    private void initAbsListView() {

        add_multi.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                Logger.e(TagFinal.ZXX, "onAddClick: ");
                closeKeyWord();
                album_type ="one";
                album_select.showAtBottom();
            }
        });

        add_multi.setClickable(false);

        add_multi.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int index) {
                add_multi.removeItem(index,true);
            }
        });
        add_multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Logger.e(TagFinal.ZXX, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        end_multi.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                Logger.e(TagFinal.ZXX, "onAddClick: ");
                closeKeyWord();
                album_type ="two";
                album_select.showAtBottom();
            }
        });

        end_multi.setClickable(false);

        end_multi.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int index) {
                end_multi.removeItem(index,true);
            }
        });
        end_multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Logger.e(TagFinal.ZXX, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    private ConfirmAlbumWindow album_select;
    private void initAlbumDialog() {
        album_select = new ConfirmAlbumWindow(mActivity);
        album_select.setOne_select(getResources().getString(R.string.take_photo));
        album_select.setTwo_select(getResources().getString(R.string.album));
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






    private boolean is_add=true;
    private DelayEventBean delayEventBean;
    private void getData(){
        Intent intent=getIntent();
        is_add=intent.getBooleanExtra(Base.type, true);
        if (is_add){
            choice_date.setTextColor(ColorRgbUtil.getBaseText());
            initDialog();
        }else{
            choice_date.setTextColor(ColorRgbUtil.getGray());
            dateBean=intent.getParcelableExtra(Base.date);
            delayEventBean=intent.getParcelableExtra(Base.data);
            service_reason.setText(delayEventBean.getElectivedetail());
            end_reason.setText(delayEventBean.getLeavedetail());

            List<String> end_list=new ArrayList<>();
            List<String> add_list=new ArrayList<>();
            if (StringJudge.isEmpty(delayEventBean.getLeavedetailpic())){
                end_multi.setList(end_list);
            }else{
                end_list=StringUtils.listToStringSplitCharacters(delayEventBean.getLeavedetailpic(), ",");
                end_multi.setList(end_list);
            }
            if (StringJudge.isEmpty(delayEventBean.getElectivedetailpic())){
                add_multi.setList(add_list);
            }else{
                add_list=StringUtils.listToStringSplitCharacters(delayEventBean.getElectivedetailpic(), ",");
                add_multi.setList(add_list);
            }

        }
    }
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        toolbar.addMenuText(TagFinal.ONE_INT, R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                closeKeyWord();
                setContent();
            }
        });
    }

    @OnClick(R.id.delay_admin_set_time)
    void setChoice(){
        if (is_add) {
            closeKeyWord();
            dialog.showAtBottom();
        }
    }
    private DateBean dateBean;
    private ConfirmDateWindow dialog;
    private void initDialog(){
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(), true);
        dialog = new ConfirmDateWindow(mActivity);
        dialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.set:
                        dateBean.setValue(dialog.getTimeValue());
                        dateBean.setName(dialog.getTimeName());
                        choice_date.setText(dateBean.getName());
                        dialog.dismiss();
                        break;
                    case R.id.cancel:
                        dialog.dismiss();
                        break;
                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.CAMERA:
                    mtask=new MyAsyncTask();
                    mtask.execute(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    Logger.e(photo_a.get(0).getPath());
                    List<String> list_a=new ArrayList<>();
                    for (Photo photo:photo_a){
                        if (photo==null) continue;
                        list_a.add(photo.getPath());
                    }
                    mtask=new MyAsyncTask();
                    mtask.execute(StringUtils.arraysToListString(list_a));
                    break;
            }
        }
    }

    /**
     * ----------------------------------------retrofit-------------
     */



    public void setContent(){
        String leave_content=end_reason.getText().toString().trim();
        String service_content=service_reason.getText().toString().trim();
        if (StringJudge.isEmpty(leave_content)) {
            toastShow("组织放学必填内容");
            return;
        }
        if (StringJudge.isEmpty(service_content)) {
            toastShow("课后服务必填内容");
            return;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayAdminClassSetReq req = new DelayAdminClassSetReq();
        //获取参数
        req.setDate(dateBean.getValue());

        req.setElectivedetail(StringUtils.stringToUpJson(service_content));
        req.setLeavedetail(StringUtils.stringToUpJson(leave_content));
        req.setElectivedetailpic(StringUtils.stringToArraysGetString(add_multi.getList(),","));
        req.setLeavedetailpic(StringUtils.stringToArraysGetString(end_multi.getList(),","));



        reqBody.delayAdminClassSetReq =req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_admin_class_set(reqEnvelop);
        call.enqueue(this);
        Logger.e(reqEnvelop.toString());
        showProgressDialog("");
    }


    private void saveImg(String flie_string){
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SaveImgReq request = new SaveImgReq();
        //获取参数
        request.setImage_file(flie_string);
        request.setFileext("jpg");
        reqBody.saveImgReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnvelop);
        call.enqueue(this);
        Logger.e(reqEnvelop.toString());
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayAdminClassSetRes !=null) {
                String result = b.delayAdminClassSetRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    switch (album_type){
                        case "one":
                            add_multi.addItem(res.getImg());
                            break;
                        case "two":
                            end_multi.addItem(res.getImg());
                            break;
                    }
                }else{
                    toastShow(res.getError_code());
                }
                if (num==1){
                    dismissProgressDialog();
                }else{
                    num--;
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
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }





    private MyAsyncTask mtask;
    private int num=0;
    private List<String> base64_list=new ArrayList<>();
    public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
        //内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(String... arg0) {
            if (isCancelled()) {
                return null;
            }
            List<String> list = Arrays.asList(arg0);
            base64_list.clear();
            num=0;
            for (String s:list){
                base64_list.add(Base64Utils.fileToBase64Str(s));
                num++;
            }
            return null;
        }
        //onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (StringJudge.isEmpty(base64_list)){
                toastShow("没有数据");
            }
            for (String s:base64_list){
                saveImg(s);
            }
        }
        //onProgressUpdate方法用于更新进度信息
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);
        }
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            showProgressDialog("");
            super.onPreExecute();
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

}
