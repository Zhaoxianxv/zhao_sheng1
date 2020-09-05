package com.yfy.app.tea_event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.ChangeTermActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.satisfaction.SatisfactionTeaGetMainReq;
import com.yfy.app.tea_event.adapter.TeaClassAdapter;
import com.yfy.app.tea_event.bean.TRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//type tea

public class TeaActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaActivity.class.getSimpleName();

    private TeaClassAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQtoolbar();
        initRecycler();

    }
    private String term_id,term_name;
    private TextView oneMenu;
    private void initSQtoolbar() {
        assert toolbar!=null;
        term_id=UserPreferences.getInstance().getTermId();
        term_name=UserPreferences.getInstance().getTermName();
        toolbar.setTitle("评测");
        oneMenu=toolbar.addMenuText(TagFinal.ONE_INT,term_name );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        Intent intent=new Intent(mActivity,ChangeTermActivity.class);
                        startActivityForResult(intent,TagFinal.UI_TAG);
                        break;
                }
            }
        });
        getTeaUser(true);
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
                getTeaUser(false);
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
        adapter=new TeaClassAdapter(mActivity);
        recyclerView.setAdapter(adapter);
        adapter.setTerm_id(term_id);
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









    public void getTeaUser(boolean is) {

        if (StringJudge.isEmpty(term_id)){
            toastShow("没有获取到当前学期信息");
            return;
        }
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SatisfactionTeaGetMainReq request = new SatisfactionTeaGetMainReq();
        //获取参数
        request.setTermid(ConvertObjtect.getInstance().getInt(term_id));
        reqBody.satisfactionTeaGetMainReq = request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().satisfaction_tea_get_main(evn);
        call.enqueue(this);
        Logger.e(evn.toString());
        if (is)showProgressDialog("");
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
            if (b.satisfactionTeaGetMainRes !=null){
                String result=b.satisfactionTeaGetMainRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TRes res=gson.fromJson(result, TRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    adapter.setDataList(res.getEvaluatelist());
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                    adapter.setTerm_id(term_id);
                }else{
                    toastShow(JsonParser.getErrorCode(result));
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG:
                    term_name=data.getStringExtra("data_name");
                    term_id=data.getStringExtra("data_id");
                    oneMenu.setText(term_name);
                    getTeaUser(false);
                    break;

            }
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        Logger.e("onFailure  :"+call.request().headers().toString());
        closeSwipeRefresh();
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
