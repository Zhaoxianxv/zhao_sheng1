package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.goods.bean.GoodNumBean;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodNumGetByIdReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodNumByidActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = GoodNumByidActivity.class.getSimpleName();

    private String id="";

    @Bind(R.id.good_num_item_head)
    ImageView head;
    @Bind(R.id.good_num_item_name)
    TextView user_name;
    @Bind(R.id.good_num_item_time)
    TextView user_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_num_by_id);
        getData();
        initSQtoolbar();

        initView();


    }

    private void initView(){
        GlideTools.chanCircle(mActivity,bean.getAdduserheadpic() ,head , R.drawable.head_user);
        user_name.setText(bean.getAddluser());
        user_time.setText(bean.getAdddate());

    }

    private GoodNumBean bean;
    private boolean is_edit=false;
    public void getData(){
        Intent intent=getIntent();
        bean=intent.getParcelableExtra(TagFinal.OBJECT_TAG);
        id=bean.getId();
        is_edit=intent.getBooleanExtra(TagFinal.TYPE_TAG, false);
        getItemDetail(id);

    }
    private TextView one_menu;
    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("物品");
        one_menu=toolbar.addMenuText(TagFinal.ONE_INT,"");

        switch (bean.getState()){
            case "待审核":
                one_menu.setVisibility(View.VISIBLE);
                break;
            case "已通过":
                one_menu.setVisibility(View.GONE);
                break;
            case "已拒绝":
                one_menu.setVisibility(View.GONE);
                break;
        }
        if (is_edit){
            one_menu.setText("审核");
        }else{
            one_menu.setText("编辑");
        }
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent;
                switch (position){
                    case TagFinal.ONE_INT:
                        if (is_edit){
                            intent=new Intent(mActivity,GoodNumDoActivity.class);
                            intent.putExtra(TagFinal.ID_TAG,id);
                            startActivityForResult(intent,TagFinal.UI_REFRESH);

                        }else{
                            intent=new Intent(mActivity,GoodsSchoolAddActivity.class);
                            intent.putExtra(TagFinal.ID_TAG,id);
                            startActivityForResult(intent,TagFinal.UI_REFRESH);
                        }
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getItemDetail(id);
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */



    public void getItemDetail(String id){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodNumGetByIdReq req = new GoodNumGetByIdReq();
        //获取参数

        req.setId(id);
        reqBody.goodNumGetByIdReq= req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_num_byid(reqEnvelop);
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

            if (b.goodNumGetByIdRes!=null) {
                String result = b.goodNumGetByIdRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    bean=res.getOffice_supply_list().get(0);
                    switch (bean.getState()){
                        case "待审核":
                            one_menu.setVisibility(View.VISIBLE);
                            break;
                        case "已通过":
                            one_menu.setVisibility(View.GONE);
                            break;
                        case "已拒绝":
                            one_menu.setVisibility(View.GONE);
                            break;
                    }
                }else{
                    toastShow(res.getError_code());
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


}
