package com.yfy.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.login.bean.UserRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.base.UserResetPasswordReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.rsa.AES;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.RegexUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordToHavePasswordActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = ResetPasswordToHavePasswordActivity.class.getSimpleName();
    @Bind(R.id.reset_edit_new_password)
    EditText edit_new_pass;
    @Bind(R.id.reset_alter_edit_new_password)
    EditText alter_edit_pass;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_reset_password_to_have_password);
        getData();
        initSQToolbar();
    }



    private String se_key="";
    public void getData(){
        Intent intent=getIntent();
        se_key=intent.getStringExtra(Base.id);
    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("重置密码");
    }





    @OnClick(R.id.reset_password_submit)
    void setBtn(){
        closeKeyWord();
        resetPassWord();
    }




    /**
     * ----------------------------retrofit-----------------------
     */


    private void resetPassWord() {
        //登陆时传的Constants.APP_ID：

        String first_password = edit_new_pass.getText().toString().trim();
        String alter_password=alter_edit_pass.getText().toString().trim();
        if (StringJudge.isEmpty(first_password)){
            toast(R.string.please_edit_password);
            return;
        }
        if(!RegexUtils.isCharAndNumbar(first_password)){
            toast("密码必须是六位以上且包含字母、数字");
            return;
        }

        if (StringJudge.isEmpty(alter_password)){
            toast("请再次输入新密码");
            return;
        }

        if (!first_password.equals(alter_password)){
            toast("新密码输入不一致");
            return ;
        }

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserResetPasswordReq req = new UserResetPasswordReq();
        //获取参数
        Logger.e(se_key+"_"+first_password);
        req.setUserinfo(AES.decrypTToString(se_key+"_"+first_password));
        reqBody.userResetPasswordReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_reset_password(reqEnv);
        call.enqueue(this);
        Logger.e(reqEnv.toString());
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
            if (b.userResetPasswordRes !=null){
                String result=b.userResetPasswordRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                UserRes res= gson.fromJson(result,UserRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    toast(R.string.success_do);
//                    Intent intent=new Intent(mActivity,LoginActivity.class);
//                    startActivity(intent);
                    finish();
                }else{
                    toast(res.getError_code());
                }
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (isActivity())return;
        dismissProgressDialog();
        Logger.e("onFailure  :"+call.request().headers().toString());
        toastShow(R.string.fail_do_not);

    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
