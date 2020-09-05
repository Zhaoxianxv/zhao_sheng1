package com.yfy.app.seal;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.seal.SealApplyAddReq;
import com.yfy.app.net.seal.SealApplyMasterUserReq;
import com.yfy.app.net.seal.SealGetSealTypeReq;
import com.yfy.app.seal.adapter.SealApplyAdapter;
import com.yfy.app.seal.bean.Approval;
import com.yfy.app.seal.bean.SealApply;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.app.seal.bean.SealState;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
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
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealApplyActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealApplyActivity.class.getSimpleName();

    private SealApplyAdapter adapter;
    private DateBean dateBean;

    @Bind(R.id.public_recycler_del)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_del_view);
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        initTeaDialog();
        getApplyMasterUser();
        getSealType();
        initRecycler();
        initSQtoobar("新增申请");
        initData();
        button.setText("提交");
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    SealMainBean bean;
    private String seal_id="";
    private TextView menu_one;
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        menu_one=toolbar.addMenuText(TagFinal.ONE_INT, "印章情况");
//        menu_one.setVisibility(View.GONE);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,SealTagActivity.class);
//                intent.putExtra(TagFinal.ID_TAG,seal_id );
                startActivity(intent);
            }
        });
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private SparseArray<KeyValue> top_data=new SparseArray<>();
    private List<SealApply> adapter_list=new ArrayList();
    private SealApply sealApplytop=new SealApply(TagFinal.TYPE_TOP);
    private void initData(){
        KeyValue one=new KeyValue(Base.user.getName(),Base.user.getSession_key());
        KeyValue two=new KeyValue(dateBean.getName(),dateBean.getValue());
//        KeyValue five=new KeyValue("产看印章使用情况","");
        KeyValue three=new KeyValue("请选择审批人","");
        KeyValue four=new KeyValue("请选择印章类型","");
        top_data.put(0,one );
        top_data.put(1,two );
        top_data.put(2,three );
        top_data.put(3,four);
//        top_data.put(4,five);



        sealApplytop.setTop_value(top_data);
        sealApplytop.setType("top");
        adapter_list.add(sealApplytop);
        adapter.setDataList(adapter_list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        adapter.setSealApplytop(sealApplytop);


    }






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
        adapter=new SealApplyAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setSealChoice(new SealApplyAdapter.SealChoice() {
            @Override
            public void choiceUser(SealApply bean) {
                if (StringJudge.isEmpty(dialog_list_beans)){
                    toastShow("未获取到审核人");
                    return;
                }
                setUserData(dialog_list_beans);
            }

            @Override
            public void choiceSealType() {
                if (StringJudge.isEmpty(sealStateList)){
                    toastShow("未获取印章信息");
                    return;
                }
                setSealStateData(sealStateList);
            }

            @Override
            public void refresh(SealApply bean, int pos_index) {
                position_index=pos_index;
                multi_bean=bean;
            }
        });

    }



    private CPWListView cpwListView;
    private List<String> dialog_name_list=new ArrayList<>();
    private List<Approval> dialog_list_beans =new ArrayList<>();
    public List<SealState> sealStateList =new ArrayList<>();
    private Approval select_bean;
    private void initTeaDialog(){
        cpwListView = new CPWListView(mActivity);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
            @Override
            public void onClick(int index, String type) {
                adapter_list=adapter.getDataList();
                SealApply apply=adapter_list.get(0);
                switch (type){
                    case "user":
                        select_bean= dialog_list_beans.get(index);

                        apply.getTop_value().put(2, new KeyValue(select_bean.getApprovalname(),select_bean.getApprovalid()));
                        adapter.notifyItemChanged(0,apply);
                        cpwListView.dismiss();
                        break;
                    case "seal_type":


                        SealState state=sealStateList.get(index);
                        apply.getTop_value().put(3, new KeyValue(state.getSignetname(),state.getSignetid()));
                        adapter.notifyItemChanged(0,apply);

//                        menu_one.setVisibility(View.VISIBLE);
                        seal_id=state.getSignetid();
                        initCreateSealType(state);

                        cpwListView.dismiss();

                        break;
                }
            }
        });
    }

    private void initCreateSealType(SealState state){
        if (StringJudge.isEmpty(state.getTypes())){
            toastShow("没有状态");
            return;
        }

        if (adapter_list.size()>1){
            adapter_list.clear();
            adapter_list.add(sealApplytop);
        }
        SealApply seal_type=new SealApply(TagFinal.TYPE_ITEM);
        KeyValue keyValue=new KeyValue(StringUtils.getTextJoint("请选择%1$s","用章类型"),"","用章类型:");
        seal_type.setDo_seal_time(keyValue);
        seal_type.setType("seal_type");
        adapter_list.add(seal_type);

        adapter.setTypes(state.getTypes());
        adapter.setDataList(adapter_list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }


    private void setUserData(List<Approval> dialog_beans){
        cpwListView.setType("user");
        if (StringJudge.isEmpty(dialog_beans)){
            toastShow(R.string.success_not_details);
            return;
        }else{
            dialog_name_list.clear();
            for(Approval s:dialog_beans){
                dialog_name_list.add(s.getApprovalname());
            }
        }
        closeKeyWord();
        cpwListView.setDatas(dialog_name_list);
        cpwListView.showAtCenter();
    }
    private void setSealStateData(List<SealState> dialog_beans){
        cpwListView.setType("seal_type");
        if (StringJudge.isEmpty(dialog_beans)){
            toastShow(R.string.success_not_details);
            return;
        }else{
            dialog_name_list.clear();
            for(SealState s:dialog_beans){
                dialog_name_list.add(s.getSignetname());
            }
        }
        closeKeyWord();
        cpwListView.setDatas(dialog_name_list);
        cpwListView.showAtCenter();
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
//        showProgressDialog("");
        List<SealApply> list=adapter.getDataList();
        List<String> ids=new ArrayList<>();
        List<String> values=new ArrayList<>();
        String master_id="",signetid="",typeid="",start_time="",end_time="";
        master_id=list.get(0).getTop_value().get(2).getValue();
        signetid=list.get(0).getTop_value().get(3).getValue();
        if (StringJudge.isEmpty(master_id)){
            toastShow("请选择审批人");
            dismissProgressDialog();
            return;
        }
        if (StringJudge.isEmpty(signetid)){
            toastShow("请选择印章");
            dismissProgressDialog();
            return;
        }
        for (SealApply apply:list){
            if (apply.isList()){
                String type=apply.getDo_seal_time().getType().split("_")[0];
                Logger.e(type);
                switch (type){
                    case Base.DATA_TYPE_IMG:
                        if (StringJudge.isEmpty(apply.getDo_seal_time().getListValue())){
                            if (apply.getCanbenull().equalsIgnoreCase(TagFinal.TRUE)){
                                toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getTablename()));
                                dismissProgressDialog();
                                return;
                            }
                        }else{
                            ids.add(apply.getTableid());
                            values.add(StringUtils.stringToArraysGetString(apply.getDo_seal_time().getListValue(),"," ));
                        }
                        break;
                        default:
                            if (StringJudge.isEmpty(apply.getDo_seal_time().getValue())){
                                if (apply.getCanbenull().equalsIgnoreCase(TagFinal.TRUE)){
                                    toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getTablename()));
                                    dismissProgressDialog();
                                    return;
                                }
                            }else{
                                ids.add(apply.getTableid());
                                values.add(apply.getDo_seal_time().getValue());
                            }
                            break;
                }
            }else{
                switch (apply.getType()){
                    case "time":
                        if (StringJudge.isEmpty(apply.getDo_seal_time().getValue())){
                            toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getDo_seal_time().getKey()));
                            dismissProgressDialog();
                            return;
                        }
                        if (apply.getDo_seal_time().getId().equals("1")){
                            start_time=apply.getDo_seal_time().getValue();
                        }else{
                            end_time=apply.getDo_seal_time().getValue();
                        }
                        break;
                    case "seal_type":
                        if (StringJudge.isEmpty(apply.getDo_seal_time().getValue())){
                            toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getDo_seal_time().getKey()));
                            dismissProgressDialog();
                            return;
                        }
                        typeid=apply.getDo_seal_time().getValue();
                        break;
                }
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
                dismissProgressDialog();
                return;
            }
        }
        req.setStartdate(start_time);
        req.setEnddate(end_time);
        req.setId("0");
        req.setTypeid(ConvertObjtect.getInstance().getInt(typeid));
        req.setSignetid(ConvertObjtect.getInstance().getInt(signetid));
        req.setApprove(ConvertObjtect.getInstance().getInt(master_id));
        req.setTableid(StringUtils.arraysToListString(ids));

        req.setTablecontent(StringUtils.arraysToListString(values));
        reqBody.sealApplyAddReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_submit(reqEnvelop);
        Logger.e(reqEnvelop.toString());
        call.enqueue(this);

    }


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

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealGetSealTypeRes!=null) {
                dismissProgressDialog();
                String result = b.sealGetSealTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    sealStateList=res.getSignets();
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
                    dialog_list_beans=res.getApprovallist();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.sealApplyAddRes!=null) {
                dismissProgressDialog();
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
            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    List<String> list_c=multi_bean.getDo_seal_time().getListValue();
                    list_c.add(res.getImg());
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
        toastShow(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }




    private SealApply multi_bean;
    private int position_index;
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
