package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.ChoiceGetStuToIllAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckGetStuToIllReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckGetStuToIllActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckGetStuToIllActivity.class.getSimpleName();


    private ChoiceGetStuToIllAdapter adapter;
    @Bind(R.id.public_deleted_text)
    TextView delete_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        delete_button.setVisibility(View.GONE);
        getData();
        initRecycler();

    }





    private DateBean dateBean;
    private String statisticsid="",statisticstypeid="";
    private void getData(){
        Intent intent=getIntent();
        dateBean=intent.getParcelableExtra(Base.date);
        String title=intent.getStringExtra(Base.title);
        statisticsid=intent.getStringExtra(Base.id);
        statisticstypeid=intent.getStringExtra(Base.type);

        initSQtoolbar(title);
        getTypeStu();
    }
    private void initSQtoolbar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_delete_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_deleted_swipe);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getTypeStu();
            }
        });
//        GridLayoutManager manager = new GridLayoutManager(mActivity,4, LinearLayoutManager.VERTICAL,false);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return select_stu.get(position).getSpan_size();
//            }
//        });
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ChoiceGetStuToIllAdapter(mActivity);
        recyclerView.setAdapter(adapter);

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


    public void getTypeStu() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckGetStuToIllReq req = new CheckGetStuToIllReq();
        //获取参数

        req.setStatisticsid(ConvertObjtect.getInstance().getInt(statisticsid));
        req.setStatisticstypeid(ConvertObjtect.getInstance().getInt(statisticstypeid));
        req.setDate(dateBean.getValue());
        reqBody.checkGetStuToIllReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_stu_to_ill(evn);
        call.enqueue(this);
        showProgressDialog("");
//        Logger.e(evn.toString());

    }
    private List<CheckStu> check_stu=new ArrayList<>();

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkGetStuToIllRes!=null) {
                String result = b.checkGetStuToIllRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    check_stu.clear();
                    check_stu.addAll(res.getIlluserlist());
                    adapter.setDateBean(dateBean);
                    adapter.setDataList(check_stu);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }

        }else{
            Logger.e(name+"---ResEnv:null");
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
