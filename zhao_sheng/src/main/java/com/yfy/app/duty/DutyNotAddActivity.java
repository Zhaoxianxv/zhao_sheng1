package com.yfy.app.duty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.adpater.DutyNotAddAdapter;
import com.yfy.app.duty.bean.AddBean;
import com.yfy.app.duty.bean.Addinfo;
import com.yfy.app.duty.bean.DutyRes;
import com.yfy.app.duty.bean.PlaneA;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.duty.DutyGetReportDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DutyNotAddActivity extends BaseActivity {
    private static final String TAG = DutyNotAddActivity.class.getSimpleName();

    private DutyNotAddAdapter adapter;
    private List<Addinfo> list=new ArrayList<>();
    private List<AddBean> addBeans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initRecycler();
        getData();
    }


    private void getData(){
        Intent intent=getIntent();
        PlaneA beanA=intent.getParcelableExtra(TagFinal.OBJECT_TAG);
        initSQToolbar(beanA.getDate());

        AddBean bean=new AddBean(true);
        bean.setType_name(beanA.getDutyreporttype());
        addBeans.add(bean);
        adapter.setDataList(addBeans);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);

        getPlane(beanA.getDate(),beanA.getTypeid());

    }
    private void initSQToolbar(String title){
        assert toolbar!=null;
        toolbar.setTitle(title);

    }



    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new DutyNotAddAdapter(this);
        recyclerView.setAdapter(adapter);
    }






    /**
     * ----------------------------retrofit-----------------------
     */


    public void getPlane(String date,String typeid){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DutyGetReportDetailReq req = new DutyGetReportDetailReq();
        //获取参数
        req.setDate(date);
        req.setTypeid(ConvertObjtect.getInstance().getInt(typeid));
        reqBody.dutyGetReportDetailReq=req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().duty_get_report_detail(reqEnv);
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
            if (b.dutyGetReportDetailRes !=null){
                String result=b.dutyGetReportDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DutyRes info=gson.fromJson(result,DutyRes.class );
                if (StringJudge.isEmpty(info.getDutyreport_type())){
                    toastShow("数据出差");
                }else{
                    list = info.getDutyreport_type();
                    if (list.size()==TagFinal.ONE_INT){
                        //直接赋值
                        addBeans.addAll(list.get(0).getDutyreport_content());
                        adapter.setDataList(addBeans);
                        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                    }
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
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }
    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }




}
