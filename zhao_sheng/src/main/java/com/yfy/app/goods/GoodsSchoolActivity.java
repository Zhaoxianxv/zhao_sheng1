package com.yfy.app.goods;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.goods.adapter.GoodsAdminAdapter;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodGetRecordListSchoolReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodsSchoolActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = GoodsSchoolActivity.class.getSimpleName();
    private GoodsAdminAdapter adapter;

    private List<GoodsBean> users=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQtoobar();
        initRecycler();
        getSchoollist(TagFinal.REFRESH);


    }


    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("物品类型");

    }


    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getSchoollist(TagFinal.REFRESH);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new GoodsAdminAdapter(mActivity);
        adapter.setIs_master(TagFinal.TYPE_TAG);
        adapter.setNo_state("115");
        adapter.setYes_state("110");

        recyclerView.setAdapter(adapter);

    }





    /**
     * ----------------------------retrofit-----------------------
     */


    /**
     */


    private int page=0;
    public void getSchoollist(String type) {
        if (type.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetRecordListSchoolReq req = new GoodGetRecordListSchoolReq();
        req.setPage(page);
        //获取参数

        reqBody.goodGetRecordListSchoolReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_record_list_school(reqEnv);
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

            if (b.goodGetRecordListSchoolRes !=null){
                String result=b.goodGetRecordListSchoolRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result, GoodsRes.class);
                if (StringJudge.isNotNull(res)){
                    GoodsRes info=gson.fromJson(result,GoodsRes.class);
                    if (info.getOffice_supply_record().size()<TagFinal.TEN_FIVE){

                    }
                    if (page==0){
                        users=info.getOffice_supply_record();
                        adapter.setDataList(users);
                        adapter.setLoadState(2);
                    }else{
                        users.addAll(info.getOffice_supply_record());
                        adapter.setDataList(users);
                        adapter.setLoadState(2);
                    }

                }
            }



        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e(TagFinal.ZXX, "error  "+call.request().headers().toString() );
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
