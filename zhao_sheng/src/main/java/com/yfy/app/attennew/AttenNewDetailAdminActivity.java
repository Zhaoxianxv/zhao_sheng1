package com.yfy.app.attennew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.attennew.adapter.DetailAdapter;
import com.yfy.app.attennew.bean.AttenBean;
import com.yfy.app.attennew.bean.AttenFlow;
import com.yfy.app.attennew.bean.AttenRes;
import com.yfy.app.bean.DateBean;
import com.yfy.app.maintainnew.MaintainEditActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.atten.AttenAdminDoingReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttenNewDetailAdminActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = AttenNewDetailAdminActivity.class.getSimpleName();
    private AttenBean bean;

    @Bind(R.id.maintiain_detail_list)
    RecyclerView listView;
    private DetailAdapter adapter;
    private List<AttenFlow> flowBeenS=new ArrayList<>();
    private String id="";
    private int dealstate;
    private LoadingDialog loadingDialog;
    @Bind(R.id.maintiain_detail_bottom)
    LinearLayout layout;
    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atten_new_detail);
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        loadingDialog=new LoadingDialog(mActivity);
        layout.setVisibility(View.GONE);
        getData();
        initSQtoolbar();
    }

    public void initSQtoolbar(){
        assert toolbar!=null;
        toolbar.setTitle("请假详情");
    }

    public void getData(){

        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle, TagFinal.OBJECT_TAG)){
            bean=bundle.getParcelable(TagFinal.OBJECT_TAG);
        }
        if (bean==null)return;
        if (bean.getAttendance_info()!=null){
            flowBeenS=bean.getAttendance_info();
            intiListView();
        }
    }




    public void intiListView(){

        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new DetailAdapter(mActivity,flowBeenS,bean,false);
        adapter.setDoAdmin(new DetailAdapter.DoAdmin() {
            @Override
            public void onClickDo(String id_bean, String tag) {
                id=id_bean;
                dealstate=tag.equals("1")?1:3;
                Intent intent=new Intent(mActivity,MaintainEditActivity.class);
                intent.putExtra(TagFinal.OBJECT_TAG,tag.equals("1")?"同意请假":"驳回请假");
                startActivityForResult(intent, TagFinal.UI_CONTENT);
            }
        });
        listView.setAdapter(adapter);

    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_CONTENT:
                    String content=data.getStringExtra(TagFinal.OBJECT_TAG);
                    if (StringJudge.isEmpty(content))content="";
                    choiceState(content);
                    break;
            }
        }
    }







    /**
     * ------------------------retrofit-------------------
     * @return
     */


    public void getItemDetail(String id)  {
//        ReqEnv evn = new ReqEnv();
//        ReqBody reqBody = new ReqBody();
//        AttenItemDetailReq request = new AttenItemDetailReq();
//        //获取参数
//        request.setId(id);
//        reqBody.attenItemDetailReq = request;
//        evn.body = reqBody;
//        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_item(evn);
//        call.enqueue(this);
//        Logger.e(reqEnvelop.toString());
    }


    private void choiceState(String content){



        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        AttenAdminDoingReq req = new AttenAdminDoingReq();
        //获取参数
        req.setId(id);
        req.setReply(StringUtils.stringToUpJson(content));
        req.setTable_plan("");
        req.setReview_result(dealstate);
        reqBody.attenAdminDoingReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_admin_set_state(evn);
        call.enqueue(this);
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
            if (b.attenAdminDoingRes!=null){
                String result=b.attenAdminDoingRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                AttenRes res=gson.fromJson(result,AttenRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(R.string.success_not_do);
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
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
