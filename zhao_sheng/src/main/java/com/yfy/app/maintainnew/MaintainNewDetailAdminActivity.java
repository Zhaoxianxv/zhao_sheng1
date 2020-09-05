package com.yfy.app.maintainnew;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import android.widget.Toast;
import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.maintain.MaintainGetSectionReq;
import com.yfy.app.maintainnew.bean.DepTag;
import com.yfy.app.maintainnew.bean.MainResult;
import com.yfy.app.maintainnew.adapter.DetailAdapter;
import com.yfy.app.maintainnew.bean.FlowBean;
import com.yfy.app.maintainnew.bean.MainBean;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.maintain.MaintainSetSectionReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintainNewDetailAdminActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = MaintainNewDetailAdminActivity.class.getSimpleName();
    private MainBean bean;

    @Bind(R.id.maintiain_detail_list)
    RecyclerView listView;
    private DetailAdapter adapter;
    private List<FlowBean> flowBeenS=new ArrayList<>();
    private String id="",dealstate="";
    private LoadingDialog loadingDialog;
    @Bind(R.id.maintiain_detail_bottom)
    LinearLayout layout;


    private DateBean dateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_new_detail);
        loadingDialog=new LoadingDialog(mActivity);
        dateBean=new DateBean(DateUtils.getCurrentDateName(), DateUtils.getCurrentDateValue());
        getData();
        initSQtoolbar();
    }

    public void initSQtoolbar(){
        assert toolbar!=null;
        toolbar.setTitle("报修详情");
    }
    public void getData(){

        Bundle bundle=getIntent().getExtras();
        layout.setVisibility(View.GONE);
        if (StringJudge.isContainsKey(bundle, TagFinal.OBJECT_TAG)){
            bean=bundle.getParcelable(TagFinal.OBJECT_TAG);
        }
        if (bean==null)return;
        if (bean.getMaintain_info()!=null){
            flowBeenS=bean.getMaintain_info();
            intiListView();
        }
        if (bean.getDealstate().equals("完成维修")||bean.getDealstate().equals("拒绝维修")){
            layout.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.VISIBLE);
        }

    }




    public void intiListView(){
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        listView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new DetailAdapter(mActivity,flowBeenS,bean,false);

        listView.setAdapter(adapter);
        mLayoutManager.scrollToPositionWithOffset(adapter.getItemCount()-1, 0);
        mLayoutManager.setStackFromEnd(true);

    }



    private AlertDialog.Builder  listDialog;
    private List<DepTag> depTags=new ArrayList<>();
    private String type_id;//
    private void initDialog() {
        List<String> txts=new ArrayList<>();
        for(DepTag s:depTags){
            txts.add(s.getName());
        }
        String[] strArr = new String[txts.size()];
        txts.toArray(strArr);
        listDialog = new AlertDialog.Builder(mActivity);
//		builder.setIcon(R.drawable.ic_launcher);
        listDialog.setTitle("请选择");
        //    设置一个单项选择下拉框
        /**
         * 第一个参数指定我们要显示的一组下拉单选框的数据集合
         * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
         * 第三个参数给每一个单选项绑定一个监听器
         */
        listDialog.setSingleChoiceItems(strArr, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                type_id=depTags.get(which).getId();
            }
        });
        listDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rollOut();
                dialog.dismiss();
            }
        });
        listDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        listDialog.show();
    }

    private void choiceState(String content){
        Object[] params = new Object[] {
                id,
                Base.user.getSession_key(),
                Base.user.getName(),
                content,
                dateBean.getValue(),//date
                dealstate,//1,完成2，拒绝
                0,//int
                "0",//money
                "",//pictures
                "",//fileContext
                "",//delpictures
        };
        ParamsTask choice = new ParamsTask(params, TagFinal.MAINTENANCE_ADMIN_SET_STATE,loadingDialog);
        execute(choice);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_CONTENT:
                    String content=data.getStringExtra(TagFinal.OBJECT_TAG);
                    choiceState(content);
                    break;
                case TagFinal.ONE_INT:
                    onPageBack();
                    break;

            }
        }
    }

    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        super.onPageBack();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e(TagFinal.ZXX,  "onSuccess: "+result );
        String taskName = wcfTask.getName();

        if (StringJudge.isSuccess(gson,result)){
            onPageBack();
        }else{
            toastShow(R.string.success_not_do);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }




    //转交
    @OnClick(R.id.detail_item_choice)
    void setChicoe(){
        id=bean.getId();
        if (StringJudge.isEmpty(depTags)){
            getSectionList();
        }else{
            initDialog();
        }
    }
    //处理
    @OnClick(R.id.detail_item_yes)
    void setDo(){
        id=bean.getId();
        Intent intent=new Intent(mActivity,MaintainDoingActivity.class);
        intent.putExtra("data",id);
        startActivityForResult(intent,TagFinal.ONE_INT);
    }



    /**
     * ----------------------------retrofit-----------------------
     */


    public void getSectionList()  {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainGetSectionReq req = new MaintainGetSectionReq();
        //获取参数

        reqBody.maintainGetSectionReq = req;
        reqEnv.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_get_section(reqEnv);
        call.enqueue(this);
        showProgressDialog("");

    }


    private void rollOut() {



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainSetSectionReq req = new MaintainSetSectionReq();
        //获取参数

        req.setId(ConvertObjtect.getInstance().getInt(id));
        req.setClassid(type_id);
        reqBody.maintainSetSectionReq = req;
        reqEnv.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_set_section(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
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
            if (b.maintainGetSectionRes !=null) {
                String result = b.maintainGetSectionRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                MainResult res=gson.fromJson(result, MainResult.class);
                depTags=res.getMaintainclass();
                if (StringJudge.isEmpty(depTags)){
                    toastShow("暂无数据！");
                }else{
                    initDialog();
                }
            }
            if (b.maintainSetSectionRes!=null){
                String result = b.maintainGetSectionRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                onPageBack();
                return ;
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        Intent myIntentDial=new Intent("android.intent.action.CALL",Uri.parse("tel:"+bean.getMobile()));
        startActivity(myIntentDial);
    }
    @PermissionFail(requestCode = TagFinal.CALL_PHONE)
    public void callFail(){
        Toast.makeText(getApplicationContext(), R.string.permission_fail_call, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
