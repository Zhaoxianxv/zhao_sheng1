package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.yfy.app.net.goods.GoodGetItemDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.List;


public class GoodsAdminDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = GoodsAdminDetailActivity.class.getSimpleName();



    private String id="";
    private GoodsDetailAdapter adapter;

    private boolean is=true;
    @Bind(R.id.goods_admin_do)
    TextView bottom;

    @Bind(R.id.goods_admin_layout)
    RelativeLayout layout;
    private String is_master;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_admin_detail);
        getData();
        initSQtoolbar();
        initRecycler();

    }

    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        super.onPageBack();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    public void getData(){
        Intent intent=getIntent();
        id=intent.getStringExtra(TagFinal.ID_TAG);
        yes_state=intent.getStringExtra("yes_state");
        no_state=intent.getStringExtra("no_state");
        is_master=intent.getStringExtra(TagFinal.TYPE_TAG );
        getItemDetail(id);

    }
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("物品");
    }

    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
        adapter=new GoodsDetailAdapter(mActivity);
        recyclerView.setAdapter(adapter);

    }

    /**
     */





    private String yes_state,no_state;
    @OnClick(R.id.goods_admin_do)
    void setAdmin(){

        Intent intent=new Intent(mActivity,GoodsDoActivity.class);
        intent.putExtra(TagFinal.ID_TAG, id);
        intent.putExtra(TagFinal.TYPE_TAG, is);
        intent.putExtra("no_state",no_state);
        intent.putExtra("yes_state",yes_state);

        startActivityForResult(intent,TagFinal.UI_ADMIN );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADMIN:
                    getItemDetail(id);
                    break;
            }
        }
    }




    /**
     * ----------------------------retrofit-----------------------
     */


    public void getItemDetail(String id){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetItemDetailReq req = new GoodGetItemDetailReq();
        req.setId(ConvertObjtect.getInstance().getInt(id));
        //获取参数

        reqBody.goodGetItemDetailReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_item_detail(reqEnv);
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
            if (b.goodGetItemDetailRes !=null){
                String result=b.goodGetItemDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                List<BeanItem> list=res.getOffice_supply_content();
                if (StringJudge.isEmpty(list)){
                    return ;
                }

                adapter.setDataList(list.get(0));
                adapter.setLoadState(2);

                switch (list.get(0).getStatus()){
                    case "已提交":
                        switch (is_master){
                            case TagFinal.TRUE:
                                is=list.get(0).getType().equals("新物品提交");
                                break;
                            case TagFinal.FALSE:
                                is=list.get(0).getType().equals("新物品提交");
                                break;
                            case TagFinal.TYPE_TAG:
                                is=true;
                                break;
                        }
                        bottom.setVisibility(View.VISIBLE);
                        break;
                    case "同意采购":
                        switch (is_master){
                            case TagFinal.TRUE:
                                is=false;
                                layout.setVisibility(View.GONE);
                                break;
                            case TagFinal.FALSE:
                                is=false;
                                layout.setVisibility(View.VISIBLE);
                                break;
                            case TagFinal.TYPE_TAG:
                                layout.setVisibility(View.GONE);
                                break;
                        }

                        bottom.setVisibility(View.VISIBLE);
                        break;
                    case "准备中":
                        switch (is_master){
                            case TagFinal.TRUE:
                                is=false;
                                layout.setVisibility(View.GONE);
                                break;
                            case TagFinal.FALSE:
                                is=false;
                                layout.setVisibility(View.VISIBLE);
                                break;
                            case TagFinal.TYPE_TAG:
                                layout.setVisibility(View.GONE);
                                break;
                        }

                        bottom.setVisibility(View.VISIBLE);
                        break;
                    case "已准备":
                        is=false;
                        bottom.setVisibility(View.VISIBLE);
                        break;
                    default:
                        bottom.setVisibility(View.GONE);
                        break;
                }
            }
            if (b.goodDelUserItemRes !=null){
                String result=b.goodDelUserItemRes.result;
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
        Logger.e( "onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
