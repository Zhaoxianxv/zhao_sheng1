package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.adapter.TeaDetailAdapter;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayDelStuItemReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.CallPhone;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DelayServiceStuDetailActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = DelayServiceStuDetailActivity.class.getSimpleName();

    private EventClass bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_service_detail);
        getData();
        initDialog();
    }


    private String canedit=TagFinal.FALSE;
    private void getData(){
        Bundle b=getIntent().getExtras();

        if (StringJudge.isContainsKey(b,TagFinal.CONTENT_TAG)){
            canedit=b.getString(TagFinal.CONTENT_TAG);
        }
        if (StringJudge.isContainsKey(b,TagFinal.OBJECT_TAG )){
            bean=b.getParcelable(TagFinal.OBJECT_TAG);
            initSQtoobar(bean.getStuname());
            initRecyclerView();
            adapter.setBean(bean);
            adapter.setDataList(bean.getDetail());
            adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        }else{
            toastShow("数据错误");
            initSQtoobar("");
        }


    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
    }



    private RecyclerView recyclerView;
    TeaDetailAdapter adapter;
    private void initRecyclerView(){
        recyclerView=findViewById(R.id.event_detail_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                0,
                ColorRgbUtil.getGrayText()));

        adapter = new TeaDetailAdapter(this);
        adapter.setTell(new TeaDetailAdapter.Tell() {
            @Override
            public void tell(String phone) {
                phone_tell=phone;
                showDialog("拨打电话",phone ,"确定" );
            }
            @Override
            public void del(String id) {
                del_teaDetail(id);
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private String phone_tell="";



    private ConfirmContentWindow confirmContentWindow;
    private void initDialog(){

        confirmContentWindow = new ConfirmContentWindow(mActivity);
        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        PermissionTools.tryTellPhone(mActivity);
                        break;
                }
            }
        });
    }

    private void showDialog(String title,String content,String ok){
        if (confirmContentWindow==null)return;
        confirmContentWindow.setTitle_s(title,content,ok);
        confirmContentWindow.showAtCenter();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    }

    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        if (StringJudge.isEmpty(phone_tell))return;
        CallPhone.callDirectly(mActivity, phone_tell);
    }
    @PermissionFail(requestCode = TagFinal.CALL_PHONE)
    public void callFail(){
        Toast.makeText(getApplicationContext(), R.string.permission_fail_call, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }











    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


    private void del_teaDetail(String id){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayDelStuItemReq req = new DelayDelStuItemReq();
        //获取参数

        req.setId(ConvertObjtect.getInstance().getInt(id));
        reqBody.delayDelStuItemReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_del_stu_item(reqEnv);
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

            if (b.delayDelStuItemRes !=null) {
                String result = b.delayDelStuItemRes.result;
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
        toast(R.string.fail_do_not);
    }



}
