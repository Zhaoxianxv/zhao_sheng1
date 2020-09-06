package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.example.zhao_sheng.R;
import com.yfy.app.goods.adapter.TagAdapter;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.goods.bean.GoodsType;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodSearchGetStationeryTypeReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.ClearEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoodsTagActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = GoodsTagActivity.class.getSimpleName();
    private TagAdapter adapter;
    private List<GoodsType> goodsTypes=new ArrayList<>();
    @Bind(R.id.clear_et)
    ClearEditText edit;

    @Bind(R.id.goods_tag_no)
    TextView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_srech);
        initRecycler();
        getData();
        initSQtoobar();

        edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String name=s.toString();
                refresh(name);
            }
        });

    }


    private boolean is_bottom,is_show;
    private void getData(){
        is_bottom=getIntent().getBooleanExtra(TagFinal.TYPE_TAG,false );
        is_show=getIntent().getBooleanExtra(Base.type,false );
        if (is_bottom){
            bottom.setVisibility(View.GONE);
        }else{
            bottom.setVisibility(View.VISIBLE);
        }
        adapter.setIs_show(is_show);
    }
    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("物品类型");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    if (edit!=null){
                        if (StringJudge.isNotEmpty(edit.getText().toString().trim())){
                            refresh(edit.getText().toString().trim());
                        }
                    }
                    break;
            }
        }
    }

    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.notice_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new TagAdapter(this);
        recyclerView.setAdapter(adapter);


    }




    @OnClick(R.id.goods_tag_no)
    void setChoice(){

        GoodsType type=new GoodsType();
        type.setId("0");
        type.setName("未找到所需物品");
        type.setCanreply(TagFinal.TYPE_TAG);
        Intent data=new Intent();
        data.putExtra(TagFinal.OBJECT_TAG,type);
        setResult(RESULT_OK,data);
        finish();
    }


    /**
     * ----------------------------retrofit-----------------------
     */

    /**
     */
    public void refresh(String s){



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodSearchGetStationeryTypeReq req = new GoodSearchGetStationeryTypeReq();
        req.setContent_text(s);
        //获取参数

        reqBody.goodSearchGetStationeryTypeReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_search_get_stationery_type(reqEnv);
        call.enqueue(this);
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
            if (b.goodSearchGetStationeryTypeRes !=null){
                String result=b.goodSearchGetStationeryTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                goodsTypes.clear();
                GoodsRes info=gson.fromJson(result,GoodsRes.class );
                if (info.getResult().equals(TagFinal.FALSE)){
                    adapter.setDataList(goodsTypes);
                    adapter.setLoadState(2);
                    return ;
                }
                goodsTypes=info.getOffice_supply_class();
                adapter.setDataList(goodsTypes);
                adapter.setLoadState(2);
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
        Logger.e( "onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
