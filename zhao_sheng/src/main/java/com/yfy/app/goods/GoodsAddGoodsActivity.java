package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.zhao_sheng.R;
import com.yfy.app.GType;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodAddStationeryTypeReq;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.*;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.view.SQToolBar;

import java.util.List;

public class GoodsAddGoodsActivity extends WcfActivity  implements Callback<ResEnv> {


    private static final String TAG = GoodsAddGoodsActivity.class.getSimpleName();
    @Bind(R.id.goods_type_add)
    TextView type_name;
    @Bind(R.id.goods_is_check_text)
    TextView check_text;

    @Bind(R.id.goods_is_check_icon)
    AppCompatImageView check_icon;

    @Bind(R.id.goods_name_add)
    EditText goods_name;
    @Bind(R.id.goods_search_add)
    EditText goods_search;
    @Bind(R.id.goods_unit_add)
    EditText goods_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_add_goods);

        initSQtoolbar();


    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("新增物品");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:

                        submit();
                        break;
                }
            }
        });
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



    @OnClick(R.id.goods_type_add)
    void setChioceType(){
        Intent intent=new Intent(mActivity,GoodsAddTypeActivity.class);
        startActivityForResult(intent, TagFinal.UI_CONTENT);
    }


    private boolean is_check=false;

    @OnClick(R.id.goods_is_check_icon)
    void setCheckIcon(){
        if (!is_check){
            is_check=true;
            check_text.setText("是否可以备注：是");
            check_icon.setImageResource(R.drawable.ic_check_selected);
        }else{
            is_check=false;
            check_text.setText("是否可以备注：否");
            check_icon.setImageResource(R.drawable.ic_check_unselect);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.UI_CONTENT:
                    GType gType=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    type_name.setText(gType.getTypename());
                    parentid=gType.getTypeid();
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }







    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        GoodsRes res=gson.fromJson(result,GoodsRes.class);
        if (res.getResult().equals(TagFinal.TRUE)){
            toastShow("新增物品成功");
            setResult(RESULT_OK);
            onPageBack();
        }else{
            toastShow(res.getError_code());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
    }



    /**
     * ----------------------------retrofit-----------------------
     */

    private String parentid;


    public void submit(){

        if (StringJudge.isEmpty(parentid)){
            toastShow("选择物品类型");
            return;
        }

        String name=goods_name.getText().toString().trim();
        if (StringJudge.isEmpty(name)){
            toastShow("请输入物品名称");
            return;
        }
        String searchkey=goods_search.getText().toString().trim();
        if (StringJudge.isEmpty(searchkey)){
            searchkey="";
        }
        String unit=goods_unit.getText().toString().trim();
        if (StringJudge.isEmpty(unit)){
            toastShow("请输入物品单位");
            return;
        }
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodAddStationeryTypeReq req = new GoodAddStationeryTypeReq();
        req.setId(0);
        req.setParentid(ConvertObjtect.getInstance().getInt(parentid));
        req.setIsreply(is_check?1:0);
        req.setUnit(unit);
        req.setName(name);
        req.setSearchkey(searchkey);
        req.setCount(0);
        //获取参数

        reqBody.goodAddStationeryTypeReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_add_stationery_type(reqEnv);
        call.enqueue(this);

        showProgressDialog("");
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
            if (b.goodAddStationeryTypeRes !=null){
                String result=b.goodAddStationeryTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                toastShow("新增物品成功");
                setResult(RESULT_OK);
                finish();
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
