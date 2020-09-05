package com.yfy.app.process;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.process.ProcessAdminSetClassReq;
import com.yfy.app.net.process.ProcessAdminSetStateReq;
import com.yfy.app.process.adapter.ProcessDoingAdapter;
import com.yfy.app.process.bean.ProBeanChild;
import com.yfy.app.process.bean.ProcessMainRes;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.ZoomImage;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProcessAdminDoingActivity extends BaseActivity  {

    private static final String TAG = ProcessAdminDoingActivity.class.getSimpleName();
    private ProcessDoingAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initRecycler();
        getData();
        initSQToolbar();
        initTypeDialog();

    }

    private CPWBean cpwBean;

    private String id,set_user_type="";
    private void getData(){
        cpwBean=getIntent().getParcelableExtra(Base.data);
        id=getIntent().getStringExtra(Base.id);
        set_user_type=getIntent().getStringExtra(Base.type);
        adapter.setId(id);
        initView();
    }

    private List<KeyValue> adapter_data=new ArrayList<>();
    private void initView(){
        adapter_data.clear();
        if (StringJudge.isEmpty(cpwBean.getValue())){

        }else{
            List<String> list=StringUtils.listToStringSplitCharacters(cpwBean.getValue(),",");
            for (String s:list){
                KeyValue keyValue=new KeyValue();
                keyValue.setType(s);
                List<String> names=StringUtils.listToStringSplitCharacters(s, "_");
                switch (names.get(0)){
                    case "content":
                        keyValue.setView_type(TagFinal.TYPE_REFRECH);
                        keyValue.setKey("请填写审批意见");
                        keyValue.setName("备注");
                        break;
                    case "image":
                        if (names.size()>1){
                            keyValue.setMax(names.get(1));
                        }else{
                            keyValue.setMax("0");
                        }

                        keyValue.setView_type(TagFinal.TYPE_IMAGE);
                        keyValue.setName("图片");
                        break;
                    case "star":
                        if (names.size()>1){
                            keyValue.setMax(names.get(1));
                        }else{
                            keyValue.setMax("0");
                        }
                        keyValue.setView_type(TagFinal.TYPE_STAR_TITLE);
                        keyValue.setName("评分");
                        break;
                    case "":
                        keyValue.setView_type(TagFinal.TYPE_REFRECH);
                        keyValue.setKey("请填写审批意见");
                        keyValue.setName("备注");
                        break;

                        default:
                            keyValue.setView_type(TagFinal.TYPE_REFRECH);
                            keyValue.setKey("请填写审批意见");
                            keyValue.setName("备注");
                            break;
                }
                adapter_data.add(keyValue);
            }
        }




        adapter.setDataList(adapter_data);
        adapter.setLoadState(TagFinal.LOADING_END);

        if (set_user_type.equalsIgnoreCase("selectuser")){
            List<KeyValue> addItem=new ArrayList<>();
            KeyValue keyValue=new KeyValue();
            List<String> names=StringUtils.listToStringSplitCharacters(cpwBean.getId(), "_");
            if (names.get(2).equalsIgnoreCase("1")){
                keyValue.setView_type(TagFinal.TYPE_SELECT_SINGLE);
            }else{
                keyValue.setView_type(TagFinal.TYPE_SELECT_GROUP);
            }

            keyValue.setKey("审批人");
            keyValue.setName("请选择");
            keyValue.setTitle(cpwBean.getName());
            keyValue.setId(cpwBean.getId());

            addItem.add(keyValue);
            adapter.addItemData(0,addItem);
        }

    }
    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(cpwBean.getName());
        toolbar.addMenuText(TagFinal.ONE_INT,"提交");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (set_user_type.equalsIgnoreCase("selectuser")){
                    setClass();

                }else{
                    setState();
                }

            }
        });

    }



    private ConfirmAlbumWindow album_select;
    private void initTypeDialog() {
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










    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ProcessDoingAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setSealChoice(new ProcessDoingAdapter.SealChoice() {
            @Override
            public void refresh(KeyValue bean, int pos_index) {
                position_index=pos_index;
                multi_bean=bean;
            }
        });
    }



    private KeyValue multi_bean;
    private int position_index;



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
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
                case TagFinal.UI_SELECT_USER_SINGLE:
                    int adapter_position=data.getIntExtra(Base.index,0);
                    KeyValue valueSingle=adapter.getDataList().get(adapter_position);
                    ProBeanChild childBean=data.getParcelableExtra(Base.data);
                    if (childBean.getView_type()==TagFinal.TYPE_CHILD){
                        valueSingle.setValue(StringUtils.getTextJoint("%1$s_%2$s",childBean.getDepartid(),childBean.getUserid()));
                        valueSingle.setName(childBean.getUsername());
                    }else{
                        valueSingle.setName(childBean.getDepartname());
                        valueSingle.setValue(StringUtils.getTextJoint("%1$s_%2$s",childBean.getDepartid(),"0"));
                    }
                    adapter.notifyItemChanged(adapter_position,valueSingle);
                    break;
                case TagFinal.UI_SELECT_USER_MORE:

                    break;

            }
        }
    }









    /**
     * ----------------------------retrofit-----------------------
     */


    private void saveImg(String flie_string){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SaveImgReq req = new SaveImgReq();
        //获取参数
        req.setImage_file(flie_string);
        req.setFileext("jpg");
        reqBody.saveImgReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnv);
        call.enqueue(this);
    }



    public void setState() {
        closeKeyWord();
        List<String> ids=StringUtils.listToStringSplitCharacters(cpwBean.getId(),"_");
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessAdminSetStateReq req = new ProcessAdminSetStateReq();
        req.setVoice("");
        req.setContent("");
        req.setImage("");
        req.setStar("-1");
        req.setId(id);
        req.setState(ConvertObjtect.getInstance().getInt(ids.get(1)));
        List<KeyValue> data=adapter.getDataList();
        for (KeyValue bean:data){
            List<String> names=StringUtils.listToStringSplitCharacters(bean.getType(), "_");
            switch (names.get(0)){
                case "content":
                    req.setContent(bean.getValue());
                    break;
                case "image":
                    req.setImage(StringUtils.stringToArraysGetString(bean.getListValue(),","));
                    break;
                case "star":
                    req.setStar(bean.getValue());
                    break;
            }
        }
        //获取参数
        reqBody.processAdminSetStateReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_admin_set_state(evn);
        call.enqueue(this);
        showProgressDialog("");
        Logger.e(evn.toString());
    }



    public void setClass() {
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessAdminSetClassReq req = new ProcessAdminSetClassReq();
        //获取参数
        req.setId(String.valueOf(id));
        req.setVoice("");
        req.setContent("");
        req.setImage("");
        req.setDepartuserid("");

        List<KeyValue> data=adapter.getDataList();
        for (KeyValue bean:data){
            List<String> names=StringUtils.listToStringSplitCharacters(bean.getType(), "_");
            switch (names.get(0)){
                case "content":
                    req.setContent(bean.getValue());
                    break;
                case "image":
                    req.setImage(StringUtils.stringToArraysGetString(bean.getListValue(),","));
                    break;
            }
            if (bean.getView_type()==TagFinal.TYPE_SELECT_SINGLE||bean.getView_type()==TagFinal.TYPE_SELECT_GROUP){
                req.setDepartuserid(bean.getValue());
            }
        }


        reqBody.processAdminSetClassReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_admin_set_class(evn);
        call.enqueue(this);
        showProgressDialog("");
        Logger.e(evn.toString());
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
            if (b.processAdminSetStateRes!=null){
                String result = b.processAdminSetStateRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    toast(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toast(res.getError_code());
                }
            }
            if (b.processAdminSetClassRes!=null){
                String result = b.processAdminSetClassRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    toast(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toast(res.getError_code());
                }
            }
            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    List<String> list_c=multi_bean.getListValue();
                    list_c.add(Base.RETROFIT_URI+res.getImg());
                    adapter.notifyItemChanged(position_index,multi_bean);
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
        toast(R.string.fail_do_not);
        dismissProgressDialog();
    }




    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
























    public void addMult(String uri){
        if (uri==null) return;
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
                base64_list.add(ZoomImage.fileToBase64Str(s));
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
