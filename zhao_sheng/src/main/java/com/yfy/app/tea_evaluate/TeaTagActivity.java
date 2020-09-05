package com.yfy.app.tea_evaluate;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.zhao_sheng.R;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.SelectYearAdapter;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.bean.YearData;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.judge.TeaJudgeGetYearReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
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

public class TeaTagActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaTagActivity.class.getSimpleName();
    private List<YearData> depTags=new ArrayList<>();
    private SelectYearAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initSQtoobar();
        initRecycler();
        getYear();


    }




    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("选择年份");
    }

    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new SelectYearAdapter(this);
        recyclerView.setAdapter(adapter);

    }






    /**
     * ----------------------------retrofit-----------------------
     */


    public void getYear()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetYearReq request = new TeaJudgeGetYearReq();
        //获取参数

        reqBody.teaJudgeGetYearReq = request;
        reqEnvelop.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_year(reqEnvelop);
        call.enqueue(this);
//        showProgressDialog("正在加载");

    }



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {

        if (!isActivity())return;
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetYearRes != null) {
                String result = b.teaJudgeGetYearRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo res=gson.fromJson(result, ResultInfo.class);
                if (StringJudge.isNotNull(res)){
                    depTags=res.getJudge_year();
                    adapter.setDataList(depTags);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
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
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
