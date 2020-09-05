package com.yfy.app.footbook;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.footbook.FootBookAddSuggestReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FootSuggestActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = FootSuggestActivity.class.getSimpleName();
    @Bind(R.id.foot_suggest_edit)
    EditText suggest_edit;
    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_suggset);
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        getData();
        initSQToolbar();
    }


    private String foot_id;
    public void getData(){
        foot_id=getIntent().getStringExtra("id");
    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.suggest);
        toolbar.addMenuText(1,R.string.finish);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isfail()){
                    submit();
                }
            }
        });
    }
    private String content;
    public boolean isfail(){
        content = suggest_edit.getText().toString();
        if (StringJudge.isEmpty(content)){
            return false;
        }
        return true;
    }






    /**
     * ----------------------------retrofit-----------------------
     */
    public void submit(){


        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        FootBookAddSuggestReq req = new FootBookAddSuggestReq();
        //获取参数

        req.setSession_key(Base.user.getSession_key());
        req.setDate(dateBean.getValue());
        req.setContent(content);
        req.setId(foot_id);

        reqBody.footBookAddSuggestReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().footbook_add_suggest(env);
        call.enqueue(this);
        showProgressDialog("");
    }





    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.footBookAddSuggestRes !=null){
                String result=b.footBookAddSuggestRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                FootRes res=gson.fromJson(result, FootRes.class);
                if (res.getResult().equals("true")){
                    toastShow(R.string.success_do);
                    onPageBack();
                }else{
                    toastShow(R.string.success_not_do);
                    onPageBack();
                }
            }
        }else{
            Logger.e(name+"---ResEnv:null");
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

