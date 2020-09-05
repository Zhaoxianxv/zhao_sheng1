package com.yfy.app.check;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.zhao_sheng.R;
import com.yfy.app.check.adapter.ChoiceTjStuAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.IllGroup;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckTjStuActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckTjStuActivity.class.getSimpleName();


    private ChoiceTjStuAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initRecycler();
        getData();


    }
    private IllGroup bean;
    private void getData(){
        bean=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        initSQtoolbar(bean.getGroupname());
        if (StringJudge.isEmpty(bean.getIlluserlist()))return;
        adapter.setDataList(bean.getIlluserlist());
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }
    private void initSQtoolbar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
    }

    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
        GridLayoutManager manager = new GridLayoutManager(mActivity,4, LinearLayoutManager.VERTICAL,false);
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return select_stu.get(position).getSpan_size();
//            }
//        });
        recyclerView.setLayoutManager(manager);
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
        adapter=new ChoiceTjStuAdapter(mActivity);
        recyclerView.setAdapter(adapter);


    }





    /**
     * ----------------------------retrofit-----------------------
     */





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

            if (b.checkGetStuRes!=null) {
                String result = b.checkGetStuRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);

            }


        }else{
            Logger.e("evn: null" +call.request().headers().toString());
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
