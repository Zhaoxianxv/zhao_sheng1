package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.goods.bean.GoodsType;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodNumAddReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.SQToolBar;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodsSchoolAddActivity extends BaseActivity   implements Callback<ResEnv> {


    private static final String TAG = GoodsSchoolAddActivity.class.getSimpleName();
    @Bind(R.id.goods_add_type)
    TextView type;
    @Bind(R.id.goods_num_add_value)
    TextView add_value;


    @Bind(R.id.goods_add_num)
    EditText num_edit;



    private boolean isbox=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_num_add);
        getDate();
        initSQtoolbar();
        num_edit.setText("");
        num_edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>10){
                    s.delete(0, 10);
                }
            }
        });
    }



    private String id="0";
    private void getDate(){
        id=getIntent().getStringExtra(TagFinal.ID_TAG);
    }


    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("新增入库");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        closeKeyWord();
                        submitGoods(true);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.goods_add_type)
    void setChioceType(){
        Intent intent=new Intent(mActivity,GoodsTagActivity.class);
        intent.putExtra(TagFinal.TYPE_TAG,true);
        intent.putExtra(Base.type,true);
        startActivityForResult(intent, TagFinal.UI_CONTENT);
    }



    private GoodsType goodsType;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.UI_CONTENT:
                    goodsType = data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    type.setText(goodsType.getName());
                    add_value.setText(StringUtils.getTextJoint("库存:%1$s %2$s",goodsType.getSurpluscount(),goodsType.getUnit()));
                    type.setTextColor(ColorRgbUtil.getBaseText());
                    break;


            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    /**
     * ----------------------------retrofit-----------------------
     */

    public void submitGoods(boolean is) {

        if (StringJudge.isNull(goodsType)){
            toastShow("请选择物品（没有找到联系管理员）");
            return;
        }
        String num_s=num_edit.getText().toString().trim();
        if (StringJudge.isEmpty(num_s)){
            toastShow("请输入数量");
            return;
        }

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodNumAddReq request = new GoodNumAddReq();
        //获取参数
        request.setId(id);
        request.setSurpluscount(ConvertObjtect.getInstance().getInt(num_s));
        request.setGoodsid(ConvertObjtect.getInstance().getInt(goodsType.getId()));
        reqBody.goodNumAddReq = request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_num_add(evn);
        call.enqueue(this);
        if (is)showProgressDialog("正在加载");

    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.goodNumAddRes !=null){
                String result=b.goodNumAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
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
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}





