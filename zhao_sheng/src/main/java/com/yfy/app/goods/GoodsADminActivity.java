package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.ChoiceTypeActivity;
import com.yfy.app.GType;
import com.yfy.app.goods.adapter.GoodsAdminAdapter;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodGetRecordListAdminReq;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class GoodsADminActivity extends WcfActivity  implements Callback<ResEnv> {

    private static final String TAG = GoodsADminActivity.class.getSimpleName();
    private int pager=0;
    private GoodsAdminAdapter adapter;
    private List<GoodsBean> users=new ArrayList<>();

    @Bind(R.id.public_center_txt_add)
    TextView topTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_txt_swip_recycler);
        initSQtoolbar();
        initRecycler();
        refresh(true,TagFinal.REFRESH);
        topTv.setTextColor(ColorRgbUtil.getBaseColor());
        topTv.setText("全部类型");
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("申领审核");
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
                refresh(false,TagFinal.REFRESH);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {


                refresh(false,TagFinal.REFRESH_MORE);
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
        adapter=new GoodsAdminAdapter(this);
        adapter.setIs_master(TagFinal.FALSE);
        adapter.setNo_state("40");
        adapter.setYes_state("30");
        recyclerView.setAdapter(adapter);

    }



    @OnClick(R.id.public_center_txt_add)
    void setTag(){

        Intent intent=new Intent(mActivity,ChoiceTypeActivity.class);
        intent.putExtra(TagFinal.TITLE_TAG, "申领状态");
        ArrayList<GType> gs=new ArrayList<>();
        gs.add(new GType("-1","全部类型"));
        gs.add(new GType("1","准备中"));
        gs.add(new GType("10","已准备"));
        gs.add(new GType("11","已领取"));
        gs.add(new GType("2","驳回"));
//        gs.add(new GType("30","同意"));
//        gs.add(new GType("40","拒绝"));

        intent.putExtra(TagFinal.OBJECT_TAG,gs );
        startActivityForResult(intent,TagFinal.UI_ADMIN);

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
                case TagFinal.UI_ADMIN:
                    GType type=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    topTv.setText(type.getTypename());
                    state=type.getTypeid();
                    refresh(false,TagFinal.REFRESH);
                    break;
                case TagFinal.UI_TAG:

                    refresh(false,TagFinal.REFRESH);
                    break;

            }
        }
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        closeSwipeRefresh();
        String name=wcfTask.getName();
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        GoodsRes info=gson.fromJson(result,GoodsRes.class);
        if (info.getOffice_supply_record().size()<TagFinal.FIFTEEN_INT){

        }
        if (name.equals(TagFinal.REFRESH)){
            users=info.getOffice_supply_record();
            adapter.setDataList(users);
            adapter.setLoadState(2);
            return false;
        }
        if (name.equals(TagFinal.REFRESH_MORE)){

            users.addAll(info.getOffice_supply_record());
            adapter.setDataList(users);
            adapter.setLoadState(2);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    /**
     * ----------------------------retrofit-----------------------
     */

    /**
     */
    private String state="-1";
    public void refresh(boolean is,String loadType){
        if (loadType.equals(TagFinal.REFRESH)){
            pager=0;
        }else{
            pager++;
        }

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetRecordListAdminReq req = new GoodGetRecordListAdminReq();
        //获取参数
        req.setPage(pager);
        req.setSize(TagFinal.FIFTEEN_INT);
        req.setState(ConvertObjtect.getInstance().getInt(state));

        reqBody.goodGetRecordListAdminReq= req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_record_list_admin(reqEnv);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {

        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.goodGetRecordListAdminRes !=null){
                String result=b.goodGetRecordListAdminRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (pager==0){
                        users.clear();
                        users=res.getOffice_supply_record();
                    }else{
                        users.addAll(res.getOffice_supply_record());
                    }
                    adapter.setDataList(users);
                    if (res.getOffice_supply_record().size()!=TagFinal.FIFTEEN_INT){
                        toastShow(R.string.success_loadmore_end);
                        adapter.setLoadState(3);
                    }else{
                        adapter.setLoadState(2);
                    }
                }else {
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
        Logger.e( "onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
