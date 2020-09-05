package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.judge.TeaJudgeDelItemReq;
import com.yfy.app.net.judge.TeaJudgeGetInfoDetailReq;
import com.yfy.app.tea_evaluate.adpter.DetailAdapter;
import com.yfy.app.tea_evaluate.bean.ParamBean;
import com.yfy.app.tea_evaluate.bean.ResultJudge;
import com.yfy.app.tea_evaluate.bean.TeaDetail;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class ChartDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = ChartDetailActivity.class.getSimpleName();
    private  int idi;
    private DetailAdapter adapter;
    private List<TeaDetail> lis=new ArrayList<>();
    private String isSubmit="";
    @Bind(R.id.public_deleted_text)
    TextView delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_delete_main);
        delete.setVisibility(View.GONE);
        delete.setTextColor(ColorRgbUtil.getBaseColor());
        initRecycler();
        getData();
    }

    private TextView menu;
    private void initSQToolbar(String title){
        assert toolbar!=null;
        toolbar.setTitle(title);
        menu=toolbar.addMenuText(TagFinal.ONE_INT,"编辑");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,RedactActivity.class);
                intent.putExtra(TagFinal.ID_TAG,idi);
                startActivityForResult(intent,TagFinal.UI_REFRESH);
            }
        });
        menu.setVisibility(View.GONE);
    }

    private boolean isShow=false;

    private void getData(){

        String id=getIntent().getStringExtra(TagFinal.ID_TAG);
        isShow=getIntent().getBooleanExtra(TagFinal.TYPE_TAG,false);
        String title=getIntent().getStringExtra("title");
        idi = ConvertObjtect.getInstance().getInt(id);
        getInfoData(true,idi);
        title=title.split(" ")[0];
        initSQToolbar(title);
    }



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_delete_recycler);
        swipeRefreshLayout = findViewById(R.id.public_deleted_swipe);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getInfoData(false,idi);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线

        adapter=new DetailAdapter(this,lis);
        recyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.public_deleted_text)
    void setDelete(){
         deleteItem(idi);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getInfoData(false,idi);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ----------------------------retrofit-----------------------
     */


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



    private void initData(ResultJudge info){

        List<TeaDetail> list=new ArrayList<>();
        TeaDetail oneDetail=new TeaDetail();
        oneDetail.setHead_image(Base.user.getHeadPic());
        oneDetail.setTitle(Base.user.getName());
        oneDetail.setIssubmit(info.getIssubmit());
        oneDetail.setState(info.getState());
        if (isShow){
            delete.setVisibility(View.VISIBLE);
            menu.setVisibility(View.VISIBLE);

        }

        oneDetail.setType("head");
        list.add(oneDetail);
        if (StringJudge.isNotEmpty(info.getJudge_record())){
            for (ParamBean bean:info.getJudge_record()){
                TeaDetail detail=new TeaDetail();
                detail.setTitle(bean.getTitle());
                if (bean.getType().equals("multifile")){
                    detail.setContent("");
                }else{
                    detail.setContent(bean.getContent());
                }

                detail.setType("text");
                list.add(detail);
            }
        }
        TeaDetail detaiScore=new TeaDetail();
        detaiScore.setTitle("个人得分");
        detaiScore.setContent(info.getScore());
        detaiScore.setType("text");
        list.add(detaiScore);
        if (StringJudge.isEmpty(info.getReason())){

        }else{
            TeaDetail detaireason=new TeaDetail();
            detaireason.setTitle("审批备注");
            detaireason.setContent(info.getReason());
            detaireason.setType("text");
            list.add(detaireason);
        }

        TeaDetail detai=new TeaDetail();
        detai.setTitle("");
        detai.setContent("");
        detai.setType("text");
        list.add(detai);
        if (StringJudge.isNotEmpty(info.getAttachment())){
            String[] s=info.getAttachment().split(Pattern.quote(","));
            List<String> name=Arrays.asList(s);
            for (int i=0; i<name.size();i++){
                String s1=name.get(i);
                TeaDetail detail=new TeaDetail();
                detail.setIcon(i+1+"");
                detail.setContent(s1);
                detail.setType("icon");
                list.add(detail);
            }
        }
        adapter.setDataList(list);
        adapter.setLoadState(2);
    }














    /**
     * --------------------------retrofit---------------------
     */


    public void deleteItem(int id_){



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeDelItemReq req = new TeaJudgeDelItemReq();
        //获取参数
        req.setId(String.valueOf(id_));

        reqBody.teaJudgeDelItemReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_del_item(reqEnv);
        call.enqueue(this);

        showProgressDialog("正在删除");
    }


    public void getInfoData(boolean is,int id_){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetInfoDetailReq req = new TeaJudgeGetInfoDetailReq();
        //获取参数
        req.setId(id_);

        reqBody.teaJudgeGetInfoDetailReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_info_detail(reqEnv);
        call.enqueue(this);
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
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetInfoDetailRes != null) {
                String result = b.teaJudgeGetInfoDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultJudge info=gson.fromJson(result, ResultJudge.class);
                if (info.getResult().equals(TagFinal.TRUE)){
                    initData(info);
                }

            }
            if (b.teaJudgeDelItemRes!=null){
                String result = b.teaJudgeDelItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultJudge res=gson.fromJson(result, ResultJudge.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    setResult(RESULT_OK);
                    finish();
                }else{
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
        Logger.e("error  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();

    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }















}
