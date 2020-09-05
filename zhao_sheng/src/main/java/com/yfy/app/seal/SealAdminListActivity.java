package com.yfy.app.seal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
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
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealAdminListActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealAdminListActivity.class.getSimpleName();


    private SealAdminAdapter adapter;
    private List<SealMainBean> sealMainBeans=new ArrayList<>();
    @Bind(R.id.seal_admin_top)
    TextView top_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seal_admin_main);
        initRecycler();
        initSQtoobar("用章审核");
        top_txt.setText("全部");
        getAdminList(true, TagFinal.REFRESH);
        initTeaDialog();

    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private String state_value="-1";
    @OnClick(R.id.seal_admin_top)
    void setTop_txt(){
        setSealTypeData();
    }

    private CPWListView cpwListView;
    private List<String> dialog_name_list=new ArrayList<>();
    private List<KeyValue> keyValues=new ArrayList<>();
    private void initTeaDialog(){
        //-1所有，100，1校级领导审核通过，3已取章，5已还章、已盖章
        keyValues.add(new KeyValue("全部", "-1"));
        keyValues.add(new KeyValue("待审核", "0"));
        keyValues.add(new KeyValue("校级领导审核通过", "1"));
        keyValues.add(new KeyValue("已取章", "3"));
        keyValues.add(new KeyValue("已还章/已盖章", "5"));
        keyValues.add(new KeyValue("未通过", "100"));
        cpwListView = new CPWListView(mActivity);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
            @Override
            public void onClick(int index, String type) {
                switch (type){
                    case "type":
                        state_value=keyValues.get(index).getValue();
                        top_txt.setText(keyValues.get(index).getKey());
                        getAdminList(false, TagFinal.REFRESH);
                        cpwListView.dismiss();
                        break;
                }
            }
        });
    }
    private void setSealTypeData(){
        cpwListView.setType("type");
        if (StringJudge.isEmpty(keyValues)){
            return;
        }else{
            dialog_name_list.clear();
            for(KeyValue s:keyValues){
                dialog_name_list.add(s.getKey());
            }
        }
        closeKeyWord();
        cpwListView.setDatas(dialog_name_list);
        cpwListView.showAtCenter();
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
                getAdminList(false, TagFinal.REFRESH);
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
        adapter.setDataList(sealMainBeans);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        adapter.setDo_tpe("admin");

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
                    getAdminList(false, TagFinal.REFRESH);
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
        req.setPage(page);
        req.setState(ConvertObjtect.getInstance().getInt(state_value));
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
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealAdminListRes!=null) {
//                String result =StringUtils.string2Json(b.sealAdminListRes.result) ;
                String result =b.sealAdminListRes.result;

                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
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
