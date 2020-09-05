package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.zhao_sheng.R;
import com.yfy.app.goods.adapter.GoodsDetailAdapter;
import com.yfy.app.goods.bean.BeanItem;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodDelUserItemReq;
import com.yfy.app.net.goods.GoodGetItemDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.List;


public class GoodsDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = GoodsDetailActivity.class.getSimpleName();



    private String id="";
    private GoodsDetailAdapter adapter;
    @Bind(R.id.public_deleted_text)
    TextView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        getData();
        initSQtoolbar();
        initRecycler();
        delete.setText("撤销");
        delete.setTextColor(ColorRgbUtil.getBaseColor());

    }

    public void getData(){
        Intent intent=getIntent();
        id=intent.getStringExtra(TagFinal.ID_TAG);
        String type=intent.getStringExtra(TagFinal.TYPE_TAG);
        getItemDetail(id);
        if (type.equals("已提交")){
            delete.setVisibility(View.VISIBLE);
        }else{
            delete.setVisibility(View.GONE);
        }

    }
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("物品");
    }

    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_delete_recycler);
        swipeRefreshLayout =  findViewById(R.id.public_deleted_swipe);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getItemDetail(id);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new GoodsDetailAdapter(mActivity);
        recyclerView.setAdapter(adapter);

    }
    @OnClick(R.id.public_deleted_text)
    void setDelete(){
        deleteItem(id);
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




    /**
     * ----------------------------retrofit-----------------------
     */


    public void getItemDetail(String id){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetItemDetailReq  req = new GoodGetItemDetailReq();
        req.setId(ConvertObjtect.getInstance().getInt(id));
        //获取参数

        reqBody.goodGetItemDetailReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_item_detail(reqEnv);
        call.enqueue(this);
    }

    public void deleteItem(String id){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodDelUserItemReq req = new GoodDelUserItemReq();
        req.setId(ConvertObjtect.getInstance().getInt(id));
        //获取参数

        reqBody.goodDelUserItemReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_del_item(reqEnv);
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
            if (b.goodGetItemDetailRes !=null){
                String result=b.goodGetItemDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes info=gson.fromJson(result,GoodsRes.class);
                List<BeanItem> list=info.getOffice_supply_content();
                if (StringJudge.isEmpty(list)){
                    return ;
                }
                adapter.setDataList(list.get(0));
                adapter.setLoadState(2);
            }
            if (b.goodDelUserItemRes !=null){
                String result=b.goodDelUserItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                setResult(RESULT_OK);
                finish();
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e( "onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
