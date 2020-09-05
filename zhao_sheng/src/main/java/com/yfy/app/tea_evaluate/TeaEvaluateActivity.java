package com.yfy.app.tea_evaluate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.adpter.EvaluateMainAdapter;
import com.yfy.app.tea_evaluate.bean.EvaluateMain;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetAddTypeReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.Variables;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeaEvaluateActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = TeaEvaluateActivity.class.getSimpleName();
    private EvaluateMainAdapter adapter;
    private List<EvaluateMain> dataList=new ArrayList<>();
    private int year=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initSQToolbar();
        initRecycler();
        showDialog();
    }

    private TextView menu;
    private  void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("教师考评");
        menu=toolbar.addMenuText(TagFinal.ONE_INT,"选择年份");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,TeaTagActivity.class);
                startActivityForResult(intent,TagFinal.UI_TAG);
            }
        });

    }


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_recycler);
        swipeRefreshLayout = findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                refresh(false, year);
            }
        });
//        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
//            @Override
//            public void onLoadMore() {
//
//                refresh(false,TagFinal.REFRESH_MORE);
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new EvaluateMainAdapter(this,dataList);
        recyclerView.setAdapter(adapter);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_CONTENT:
                    break;
                case TagFinal.UI_TAG:
                    Bundle b=data.getExtras();
                    if (StringJudge.isContainsKey(b,TagFinal.NAME_TAG)){
                        menu.setText(b.getString(TagFinal.NAME_TAG));
                        Variables.year=b.getString(TagFinal.NAME_TAG);
                        year= ConvertObjtect.getInstance().getInt(b.getString(TagFinal.NAME_TAG));
                        refresh(true, year);
                    }
                    break;
            }
        }
    }


    /**
     * --------------------retrofit-------------
     */

    private void showDialog(){
        DialogTools.getInstance().showDialog(mActivity, "", "请选择年份", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent=new Intent(mActivity,TeaTagActivity.class);
                startActivityForResult(intent,TagFinal.UI_TAG);

            }
        });
    }

    public void refresh(boolean is,int year){

        if (year==0){
           showDialog();
           closeSwipeRefresh();
           return;
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetAddTypeReq request = new TeaJudgeGetAddTypeReq();
        //获取参数

        request.setYear(year);

        reqBody.teaJudgeGetAddTypeReq = request;
        reqEnvelop.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_add_type(reqEnvelop);
        call.enqueue(this);
        if (is) showProgressDialog("正在加载");

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



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetAddTypeRes != null) {
                String result = b.teaJudgeGetAddTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=gson.fromJson(result, ResultInfo.class);
                adapter.setDataList(info.getJudge_class());
                adapter.setLoadState(2);
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
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
    }



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
