package com.yfy.app.seal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.seal.SealAdminListReq;
import com.yfy.app.seal.adapter.SealAdminAdapter;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealMasterListActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealMasterListActivity.class.getSimpleName();


    private SealAdminAdapter adapter;
    private List<SealMainBean> sealMainBeans=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
        initSQtoobar("领导审核");
        getAdminList(true,TagFinal.REFRESH);

    }


    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
    }





    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getAdminList(false,TagFinal.REFRESH);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                getAdminList(false,TagFinal.REFRESH_MORE);
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
        adapter=new SealAdminAdapter(this);
        recyclerView.setAdapter(adapter);




//        initAdapterData();

        adapter.setDataList(sealMainBeans);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        adapter.setDo_tpe("master");

    }



    public void closeSwipeRefresh(){
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    SealMainBean bean=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    sealMainBeans.remove(bean);
                    adapter.setDataList(sealMainBeans);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);

                    break;
            }
        }
    }


    /**
     * ----------------------------retrofit-----------------------
     */

    private int page=0;
    public void getAdminList(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            page=0;
        }else{
            page++;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealAdminListReq req = new SealAdminListReq();
        //获取参数
        req.setPage(0);
        reqBody.sealAdminListReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_admin_list(reqEnvelop);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealAdminListRes!=null) {
                String result = b.sealAdminListRes.result;
                Logger.e(call.request().headers().toString().trim()+result );
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    if (page==0){
                        sealMainBeans=res.getSignetlist();
                    }else{
                        sealMainBeans.addAll(res.getSignetlist());
                    }
                    adapter.setDataList(sealMainBeans);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }
        }else{
            List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
            String name=names.get(names.size()-1);
            Logger.e(name+"--------er");
        }
    }





    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
