package com.yfy.app.check;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.CheckTjParentAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.IllGroup;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckTjParentActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckTjParentActivity.class.getSimpleName();
    private CheckTjParentAdapter adapter;


    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initSQToolbar();
        initRecycler();
        getData();
    }


    private ArrayList<IllGroup> data_list=new ArrayList<>();
    private void getData(){
        data_list=getIntent().getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
        dateBean=getIntent().getParcelableExtra(Base.date);
//        Logger.e(dateBean.getName());
        adapter.setDateBean(dateBean);
        adapter.setDataList(data_list);
        adapter.setLoadState(TagFinal.LOADING_END);
    }
    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("年级统计");

    }


    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                ColorRgbUtil.getGainsboro()));
        adapter=new CheckTjParentAdapter(mActivity);
        recyclerView.setAdapter(adapter);

    }


    /**
     * ----------------------------retrofit-----------------------
     */

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkTjListRes!=null) {
                String result = b.checkTjListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                CheckRes res=gson.fromJson(result,CheckRes.class);
                adapter.setDataList(res.getGrouplist());
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
            }
        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
