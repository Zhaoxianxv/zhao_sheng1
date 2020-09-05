package com.yfy.app.info_submit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.WebActivity;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.utils.JsonStrParser;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.authentication.AuthenticationGetDataReq;
import com.yfy.app.net.authentication.AuthenticationLoginReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public  class AuthenticationActivity extends BaseActivity {

	private static final String TAG = AuthenticationActivity.class.getSimpleName();
	@Bind(R.id.baobao_edit)
	EditText baobao_et;
	@Bind(R.id.mishi_et)
	EditText mishi_et;

	private String baobaoName;
	private String tel_number;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_authentication);
		initSQtoolbar();
		initDialog();
	}
	private void initSQtoolbar() {
		assert toolbar != null;
		toolbar.setTitle("招生");

	}


	private ConfirmContentWindow confirmContentWindow;
	private void initDialog(){

		confirmContentWindow = new ConfirmContentWindow(mActivity);
		confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
			@Override
			public void onClick(View view) {

				switch (view.getId()){
					case R.id.pop_dialog_title:
						break;
					case R.id.pop_dialog_content:
						break;
					case R.id.pop_dialog_ok:
						break;
				}
			}
		});
	}

	private void showDialog(String title,String content,String ok){
		if (confirmContentWindow==null)return;
		confirmContentWindow.setTitle_s(title,content,ok);
		closeKeyWord();
		confirmContentWindow.showAtCenter();
	}


	@OnClick(R.id.point)
	void setWeb(){
		Intent intent = new Intent(mActivity, WebActivity.class);
		Bundle b = new Bundle();
		b.putString(TagFinal.URI_TAG, Base.POINT_PATH);
		b.putString(TagFinal.TITLE_TAG, "招生须知");
		intent.putExtras(b);
		startActivity(intent);
	}
	@OnClick(R.id.authente)
	void setSubmit(){
		baobaoName = baobao_et.getText().toString().trim();
		tel_number = mishi_et.getText().toString().trim();

		if (StringJudge.isEmpty(baobaoName) || StringJudge.isEmpty(tel_number)) {
			Toast.makeText(AuthenticationActivity.this, "请输入完整信息",Toast.LENGTH_SHORT).show();;
		} else {
			stuLoginCode();
		}
	}






	private void stuLoginCode(){

		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		AuthenticationLoginReq req = new AuthenticationLoginReq();
		//获取参数
		req.setName(baobaoName);
		req.setPhone(tel_number);

		reqBody.authenticationLoginReq= req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().authentication_login(reqEnv);
		call.enqueue(this);
		showProgressDialog("");
	}
	private void getStuData(String id){

		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		AuthenticationGetDataReq req = new AuthenticationGetDataReq();
		//获取参数
		req.setStuid(id);

		reqBody.authenticationGetDataReq= req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().authentication_get_data(reqEnv);
		call.enqueue(this);
	}



	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		if (!isActivity())return;
        dismissProgressDialog();
		ResEnv respEnvelope = response.body();
		List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
		String name=names.get(names.size()-1);
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;
			if (b.authenticationLoginRes!=null) {
				String result = b.authenticationLoginRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				BaseRes res=gson.fromJson(result,BaseRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					showDialog(baobaoName, res.getClassname(), "确定");
				}else{
					toast(res.getError_code());
				}
			}
			if (b.authenticationGetDataRes!=null) {
				String result = b.authenticationGetDataRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				InfosConstant.grade = JsonStrParser.getFenBan(result);
				showDialog(baobaoName, JsonStrParser.getFenBan(result), "确定");
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
		Logger.e("onFailure  :"+call.request().headers().toString());
		toast(R.string.fail_do_not);
		dismissProgressDialog();

	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}
}
