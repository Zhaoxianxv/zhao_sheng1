package com.yfy.app.seal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.seal.SealApplyAddReq;
import com.yfy.app.net.seal.SealApplyMasterUserReq;
import com.yfy.app.net.seal.SealGetSealTypeReq;
import com.yfy.app.seal.adapter.SealEditResetApplyAdapter;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.app.seal.bean.SealState;
import com.yfy.app.seal.bean.SealTable;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.GreenDaoManager;
import com.yfy.greendao.KeyValueDb;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealEditResetApplyActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealEditResetApplyActivity.class.getSimpleName();

    private SealEditResetApplyAdapter adapter;
    private DateBean dateBean;
    @Bind(R.id.public_recycler_del)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_del_view);
        GreenDaoManager.getInstance().clearKeyValue();
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        initRecycler();
        initSQtoobar("修改用章时间");
        button.setText("提交");
        initData();

        getApplyMasterUser();
        getSealType();

    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);


    }

    private KeyValueDb apply_user,apply_date,apply_master;
    private SealMainBean bean;
    private void initData(){
        bean=getIntent().getParcelableExtra(Base.data);

        apply_user = new KeyValueDb();
        apply_user.setKey("申请人:");
        apply_user.setValue(Base.user.getSession_key());
        apply_user.setName(bean.getProposername());
        apply_user.setView_type(TagFinal.TYPE_TXT);
        apply_user.setType("base");
        top_data.add(apply_user);

        apply_date = new KeyValueDb();
        apply_date.setKey("申请时间:");
        apply_date.setValue( bean.getAdddate());
        apply_date.setName( bean.getAdddate());
        apply_date.setView_type(TagFinal.TYPE_TXT);
        apply_date.setType("base");
        top_data.add(apply_date);

        apply_master=new KeyValueDb();
        apply_master.setKey("审批人:");
        apply_master.setValue(bean.getApprovalid());
        apply_master.setName(bean.getApprovalname());
        apply_master.setView_type(TagFinal.TYPE_CHOICE);
        apply_master.setType("person");
        top_data.add(apply_master);


        KeyValueDb two=new KeyValueDb();
        two.setKey("印章类型:");
        two.setValue(bean.getSignetid());
        two.setName(bean.getSignetname());
        two.setView_type(TagFinal.TYPE_CHOICE);
        two.setType("seal_type");
        two.setKey_value_id(bean.getSignetid());
        top_data.add(two);

        KeyValueDb three = new KeyValueDb();
        three.setKey("用章类型:");
        three.setValue(bean.getTypeid());
        three.setName(bean.getTypename());
        three.setView_type(TagFinal.TYPE_CHOICE);
        three.setType("do_type");
        three.setParent_id(bean.getSignetid());
        three.setKey_value_id(bean.getTypeid());
        top_data.add(three);

        if (StringJudge.isEmpty(bean.getEnddate())){
            KeyValueDb four = new KeyValueDb();
            four.setKey("用章时间:");
            four.setValue(bean.getStartdate());
            four.setName(bean.getStartdate());
            four.setView_type(TagFinal.TYPE_DATE_TIME);

            four.setParent_id(bean.getSignetid());
            four.setChild_id(bean.getTypeid());
            four.setType("date");
            top_data.add(four);
        }else{

            KeyValueDb five = new KeyValueDb();
            five.setKey("开始时间:");
            five.setValue(bean.getStartdate());
            five.setName(bean.getStartdate());
            five.setView_type(TagFinal.TYPE_DATE_TIME);
            five.setParent_id(bean.getSignetid());
            five.setChild_id(bean.getTypeid());
            five.setType("date_start");
            top_data.add(five);

            KeyValueDb six = new KeyValueDb();
            six.setKey("还章时间:");
            six.setValue(bean.getEnddate());
            six.setName(bean.getEnddate());
            six.setView_type(TagFinal.TYPE_DATE_TIME);
            six.setParent_id(bean.getSignetid());
            six.setChild_id(bean.getTypeid());
            six.setType("date_end");
            top_data.add(six);
        }

        for (SealTable table:bean.getTables()){
            List<String> listOne=StringUtils.listToStringSplitCharacters(table.getValuetype(), "_");
            KeyValueDb keyValue = new KeyValueDb();

            keyValue.setKey(StringUtils.getTextJoint("%1$s:", table.getTablename()));
            keyValue.setValue(table.getValue());
            keyValue.setName(table.getValue());
            keyValue.setKey_value_id(table.getTableid());

            keyValue.setParent_id(bean.getSignetid());
            keyValue.setChild_id(bean.getTypeid());
            keyValue.setType(table.getValuetype());
            switch (listOne.get(0)){
                case "txt":
                    keyValue.setView_type(TagFinal.TYPE_TXT_EDIT);
                    break;
                case "float":
                    keyValue.setView_type(TagFinal.TYPE_TXT_EDIT);
                    break;
                case "longtxt":
                    keyValue.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                    break;
                case "img":
                    keyValue.setView_type(TagFinal.TYPE_IMAGE);
                    keyValue.setImage(table.getValue());
                    break;
                default:
                    keyValue.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                    break;
            }
            top_data.add(keyValue);

        }
        adapter.setDataList(top_data);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);

        GreenDaoManager.getInstance().saveAllKeyValueDb(top_data);
    }

    private List<KeyValueDb> top_data=new ArrayList<>();
    public List<SealState> sealStateList =new ArrayList<>();



    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new SealEditResetApplyAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setSealChoice(new SealEditResetApplyAdapter.SealChoice() {
            @Override
            public void refresh(KeyValueDb bean, int pos_index) {
                position_index=pos_index;
                multi_bean=bean;
            }
        });


    }

    private KeyValueDb multi_bean;
    private int position_index;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.CAMERA:
//                    List<String> list_c=multi_bean.getDo_seal_time().getListValue();
////                    addMult(FileCamera.photo_camera);
//                    list_c.add(FileCamera.photo_camera);
//                    adapter.notifyItemChanged(position_index,multi_bean );
                    showProgressDialog("");
                    mtask=new MyAsyncTask();
                    mtask.execute(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    Logger.e(photo_a.get(0).getPath());
//                    setMultList(photo_a);
                    List<String> list_a=new ArrayList<>();
                    for (Photo photo:photo_a){
                        if (photo==null) continue;
                        list_a.add(photo.getPath());
                    }
//                    adapter.notifyItemChanged(position_index,multi_bean );
                    showProgressDialog("");
                    mtask=new MyAsyncTask();
                    mtask.execute(StringUtils.arraysToListString(list_a));
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */

    public void getApplyMasterUser() {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealApplyMasterUserReq req = new SealApplyMasterUserReq();
        //获取参数
        reqBody.sealApplyMasterUserReq= req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_apply_master_user(reqEnvelop);
        call.enqueue(this);
    }


    private void saveImg(String flie_string){
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SaveImgReq request = new SaveImgReq();
        //获取参数
//		request.setFile("PNG"+Base64Utils.fileToBase64Str("/storage/emulated/0/DCIM/Camera/IMG_20200402_162459.jpg"));
        request.setImage_file(flie_string);
        request.setFileext("jpg");
        reqBody.saveImgReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnvelop);
        call.enqueue(this);
        Logger.e(reqEnvelop.toString());
    }

    public void getSealType() {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealGetSealTypeReq req = new SealGetSealTypeReq();
        //获取参数
        reqBody.sealGetSealTypeReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_seal_type(reqEnvelop);
//        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
    }
    @OnClick(R.id.public_recycler_del)
    void setTag(){
       addSubmit();
    }
    public void addSubmit() {


        List<KeyValueDb> list=adapter.getDataList();
        List<String> ids=new ArrayList<>();
        List<String> values=new ArrayList<>();

        String start_time="",end_time="",person_id="",signet_id="",type_id="";


        for (KeyValueDb apply:list){
            if (StringJudge.isEmpty(apply.getValue())) {
                toast(apply.getName()+"未完成");
                return;
            }
           switch (apply.getType()){
               case "base":
                   break;
               case "person":
                   person_id=apply.getValue();
                   break;
               case "seal_type":
                   signet_id=apply.getValue();
                   break;
               case "do_type":
                   type_id=apply.getValue();
                   break;
               case "date":
                   start_time=apply.getValue();
                   break;
               case "date_start":
                   start_time=apply.getValue();
                   break;
               case "date_end":
                   end_time=apply.getValue();
                   break;
               default:
                   ids.add(apply.getKey_value_id());
                   values.add(apply.getValue());
                   break;
           }
        }

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealApplyAddReq req = new SealApplyAddReq();
        //获取参数
        if (StringJudge.isEmpty(end_time)){
        }else{
            long start=DateUtils.stringToLong(start_time, "yyyy/MM/dd HH:mm");
            long end=DateUtils.stringToLong(end_time, "yyyy/MM/dd HH:mm");
            if (start>=end){
                toastShow(StringUtils.getTextJoint("开始时间大于结束时间"));
                return;
            }
        }
        req.setStartdate(start_time);
        req.setEnddate(end_time);
        req.setId(bean.getId());
        req.setTypeid(ConvertObjtect.getInstance().getInt(type_id));
        req.setSignetid(ConvertObjtect.getInstance().getInt(signet_id));
        req.setApprove(ConvertObjtect.getInstance().getInt(person_id));
        req.setTableid(StringUtils.arraysToListString(ids));
        req.setTablecontent(StringUtils.arraysToListString(values));
        reqBody.sealApplyAddReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_submit(reqEnvelop);
        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
        showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealApplyAddRes!=null) {
                String result = b.sealApplyAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.sealApplyMasterUserRes!=null) {
                dismissProgressDialog();
                String result = b.sealApplyMasterUserRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setApprovallist(res.getApprovallist());
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.sealGetSealTypeRes!=null) {
                dismissProgressDialog();
                String result = b.sealGetSealTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    sealStateList=res.getSignets();
                    adapter.setSealStateList(sealStateList);
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    List<String> list_c=new ArrayList<>();
                    if(StringJudge.isEmpty(multi_bean.getValue())){

                    }else{
                        list_c.addAll(StringUtils.listToStringSplitCharacters(multi_bean.getValue(),","));
                    }
                    list_c.add(res.getImg());
                    multi_bean.setValue(StringUtils.stringToArraysGetString(list_c,"," ));
                    multi_bean.setImage(StringUtils.stringToArraysGetString(list_c,"," ));
                    adapter.notifyItemChanged(position_index,multi_bean );
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
            Logger.e(name+"---ResEnv:null");
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
}
