package com.yfy.app.appointment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderSetScoreReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDoScroeActivity extends BaseActivity implements Callback<ResEnv> {


    private static final String TAG = AdminDoScroeActivity.class.getSimpleName();


    @Bind(R.id.score_rating_content)
    EditText rating_edit;
    @Bind(R.id.score_rating)
    RatingBar ratingbar;


    private String devicecontent="";
    private float score=0;
    private int item_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_admin_scroe);
        getData();
        initSQToolbar();

    }

    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b,"id")){
            item_id=b.getInt("id");
        }

    }
    private  void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("评价");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (isSend()){

                    setScore();
                }
            }
        });
    }

    public boolean isSend(){
        devicecontent=rating_edit.getText().toString();

        if (StringJudge.isEmpty(devicecontent)){
            toastShow("请填写内容");
            return false;
        }
        score=ratingbar.getRating();
        if (score<=0){
            toastShow("分数不能为 0！");
            return false;
        }
        return true;


    }



    public int floatToInt(float f){
        int i = 0;
        if(f>0) //正数
            i = (int)(f*10 + 5)/10;
        else if(f<0) //负数
            i = (int)(f*10 - 5)/10;
        else i = 0;
        return i;

    }





    /**
     * ----------------------------retrofit-----------------------
     */





    public void setScore(){



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderSetScoreReq req = new OrderSetScoreReq();
        //获取参数
        req.setId(item_id);
        req.setIslogistics(1);
        req.setLogisticsscore(floatToInt(score));
        req.setLogisticscontent(devicecontent);
        req.setIsdevice(0);
        req.setDevicescore(0);
        req.setDevicecontent("");

        reqBody.orderSetScoreReq= req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_set_score(reqEnv);
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
            if (b.orderSetScoreRes!=null){
                String result=b.orderSetScoreRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes res=gson.fromJson(result,OrderRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow("成功");
                    setResult(RESULT_OK);
                    onPageBack();
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
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
