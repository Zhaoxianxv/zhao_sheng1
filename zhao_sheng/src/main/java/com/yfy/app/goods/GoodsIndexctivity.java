package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.example.zhao_sheng.R;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.goods.GoodNumCountReq;
import com.yfy.app.net.goods.GoodGetCountAdminReq;
import com.yfy.app.net.goods.GoodGetCountSchoolReq;
import com.yfy.app.net.goods.GoodGetCountMasterReq;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.app.slide.CheckMainActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodsIndexctivity extends BaseActivity  implements Callback<ResEnv> {

    private static final String TAG = GoodsIndexctivity.class.getSimpleName();


    @Bind(R.id.oa_base_one_posh)
    AppCompatTextView one_tag;
    @Bind(R.id.oa_base_five_posh)
    AppCompatTextView five_tag;
    @Bind(R.id.oa_base_six_posh)
    AppCompatTextView six_tag;
    @Bind(R.id.oa_base_ten_posh)
    AppCompatTextView ten_tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_index);
        getData();
        getSchoolCount();
        getcount();
        getMastercount();
        getNumCount();
    }

    private String admin_right="",
            admin_add=TagFinal.FALSE,
            admin_manage=TagFinal.FALSE,
            issupplycount=TagFinal.FALSE,
            isoffice_supply=TagFinal.FALSE,
            isoffice_supply_master=TagFinal.FALSE;
    public void getData(){
        Intent intent=getIntent();
        admin_right=intent.getStringExtra(Base.content);
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    admin_add=TagFinal.TRUE;
                    break;
                case "admin":
                    admin_manage=TagFinal.TRUE;
                    break;
                case "issupplycount":
                    issupplycount=TagFinal.TRUE;
                    break;
                case "isoffice_supply":
                        isoffice_supply=TagFinal.TRUE;
                    break;
                case "isoffice_supply_master":
                    isoffice_supply_master=TagFinal.TRUE;
                    break;
            }
        }
    }

    //申领
    @OnClick(R.id.oa_base_two_layout)
    void setTwo(){
        if (issupplycount.equals(TagFinal.TRUE)){
            toast("没有权限");
            return;
        }
        Intent intent=new Intent(mActivity,GoodsAddActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADD);
    }
    //申领记录
    @OnClick(R.id.oa_base_one_layout)
    void setOne(){
//        if (UserPreferences.getInstance().getUserAdmin().getIssupplycount().equals(TagFinal.TRUE)){
//            toast("没有权限");
//            return;
//        }
        Intent intent=new Intent(mActivity,GoodsMainActivity.class);
        startActivity(intent);
    }
    //申领审核
    @OnClick(R.id.oa_base_three_layout)
    void setThree(){
        if (isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent=new Intent(mActivity,GoodsADminActivity.class);
        startActivityForResult(intent,TagFinal.UI_ADMIN);
    }
    //分校申领审核
    @OnClick(R.id.oa_base_sex_layout)
    void setSix(){
        if (isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent=new Intent(mActivity,GoodsSchoolActivity.class);
        startActivity(intent);
    }
    //新增种类
    @OnClick(R.id.oa_base_four_layout)
    void setFour(){
        if (isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent=new Intent(mActivity,GoodsAddGoodsActivity .class);
        startActivityForResult(intent,TagFinal.UI_ADD );
    }
    //采购审核
    @OnClick(R.id.oa_base_five_layout)
    void setFive(){
        if (isoffice_supply_master.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent inte=new Intent(mActivity,GoodsMasterActivity.class);
        startActivityForResult(inte,TagFinal.UI_CONTENT);
    }
    //库存修改/记录
    @OnClick(R.id.oa_base_eight)
    void setEight(){
        if (issupplycount.equals(TagFinal.FALSE)&& isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent=new Intent(mActivity,GoodsSchoolAddActivity.class);
        intent.putExtra(TagFinal.ID_TAG,"0" );
        startActivity(intent);
    }
    @OnClick(R.id.oa_base_nine)
    void setNine(){
        if (issupplycount.equals(TagFinal.FALSE)&& isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent = new Intent(mActivity, GoodNumUserListActivity.class);
        startActivity(intent);
    }
    //库存审核
    @OnClick(R.id.oa_base_ten)
    void setTen(){
        if (isoffice_supply.equals(TagFinal.FALSE)){
            toastShow("没有权限");
            return;
        }
        Intent intent = new Intent(mActivity, GoodNumAdminListActivity.class);
        startActivity(intent);
    }












    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getSchoolCount();
                    getcount();
                    getMastercount();
                    break;
            }
        }

    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getSchoolCount() {
        if (isoffice_supply.equals(TagFinal.FALSE))return;
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetCountSchoolReq request = new GoodGetCountSchoolReq();
        //获取参数
        reqBody.goodGetCountSchoolReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_count_school(reqEnvelop);
        call.enqueue(this);

    }

    public void getcount() {
        if (isoffice_supply.equals(TagFinal.FALSE))return;
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetCountAdminReq request = new GoodGetCountAdminReq();
        //获取参数

        reqBody.goodGetCountAdminReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_count_admin(reqEnvelop);
        call.enqueue(this);

    }

    public void getNumCount() {
        if (isoffice_supply.equals(TagFinal.FALSE))return;
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodNumCountReq req = new GoodNumCountReq();
        //获取参数
        reqBody.goodNumCountReq= req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_num_count(reqEnvelop);
        call.enqueue(this);
    }


    public void getMastercount() {
        if (isoffice_supply_master.equals(TagFinal.FALSE))return;
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodGetCountMasterReq request = new GoodGetCountMasterReq();
        //获取参数

        reqBody.goodGetCountMasterReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_get_count_master(reqEnvelop);
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

            if (b.goodGetCountAdminRes !=null) {
                String result = b.goodGetCountAdminRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    one_tag.setText("");
                }else{
                    if (res.getCount().equals("0")){
                        one_tag.setText("");
                    }else{
                        one_tag.setText(res.getCount());
                    }
                    if (res.getCount().length()>2) one_tag.setText("99");
                }
            }
            if (b.goodGetCountMasterRes !=null) {
                String result = b.goodGetCountMasterRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    five_tag.setText("");
                }else{
                    if (res.getCount().equals("0")){
                        five_tag.setText("");
                    }else{
                        five_tag.setText(res.getCount());
                    }
                    if (res.getCount().length()>2) five_tag.setText("99");
                }
            }
            if (b.goodGetCountSchoolRes !=null) {
                String result = b.goodGetCountSchoolRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    six_tag.setText("");
                }else{
                    if (res.getCount().equals("0")){
                        six_tag.setText("");
                    }else{
                        six_tag.setText(res.getCount());
                    }
                    if (res.getCount().length()>2) six_tag.setText("99");
                }
            }
            if (b.goodNumCountRes!=null) {
                String result = b.goodNumCountRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (StringJudge.isEmpty(res.getCount())){
                    ten_tag.setText("");
                }else{
                    if (res.getCount().equals("0")){
                        ten_tag.setText("");
                    }else{
                        ten_tag.setText(res.getCount());
                    }
                    if (res.getCount().length()>2) ten_tag.setText("99");
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));        }
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






    @OnClick(R.id.oa_base_two_zxx)
    void setTag(){
//        Intent intent = new Intent(mActivity, SealMainListActivity.class);
        Intent intent = new Intent(mActivity, CheckMainActivity.class);
        startActivity(intent);
    }
}
