package com.yfy.app.footbook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.login.LoginActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.footbook.FootBookPraiseReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FootDetailActivity extends BaseActivity implements Callback<ResEnv> {
	private static final String TAG = FootDetailActivity.class.getSimpleName();
	@Bind(R.id.foot_detail_image)
	ImageView image;
	@Bind(R.id.foot_detail_content)
	TextView text_conten;
	@Bind(R.id.foot_add_reting_image)
	ImageView reting_icon;
	@Bind(R.id.foot_add_reting_button)
	TextView reting_button;

	private String url;
	private String content;
	private String title;
	private String id;
	private String ispraise;
	private String praise;

	private DateBean dateBean;
	private LoadingDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foot_book_detail);
		dateBean=new DateBean();
		dateBean.setValue_long(System.currentTimeMillis(),true);
		dialog=new LoadingDialog(mActivity);
		getData();
		initSQtoolbar();
		initView();

	}


	/**
	 * @description
	 */
	private void getData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			if (b.containsKey("url")) {
				url = b.getString("url");
			}
			if (b.containsKey("content")) {
				content = b.getString("content");
			}
			if (b.containsKey("title")) {
				title = b.getString("title");
			}
			if (b.containsKey("id")) {
				id = b.getString("id");
			}
			if (b.containsKey("ispraise")) {
				ispraise = b.getString("ispraise");
			}
			if (b.containsKey("praise")) {
				praise = b.getString("praise");
			}
		}
		if (StringJudge.isEmpty(title)){
			title="";
		}
		if (StringJudge.isEmpty(url)){
			url="";
		}
		if (StringJudge.isEmpty(content)){
			content="";
		}
		if (StringJudge.isEmpty(ispraise)){
			ispraise="false";
		}
		if (StringJudge.isEmpty(praise)){
			praise="0";
		}
	}



	@OnClick(R.id.foot_add_suggest_button)
	void setAddSuggest(){
		if (Base.user !=null){
			Intent intent=new Intent(mActivity, FootSuggestActivity.class);
			intent.putExtra("id",id);
			startActivity(intent);
		}else{
			startActivity(new Intent(mActivity, LoginActivity.class));
		}
	}
	@OnClick(R.id.foot_add_reting_image)
	void setImage(){
		if (Base.user !=null){
			if (ispraise.equals("true")){
				ispriase(0);
			}else{
				ispriase(1);
			}
		}else{
			startActivity(new Intent(mActivity, LoginActivity.class));
		}
	}


	private void initSQtoolbar() {
		assert toolbar!=null;
		toolbar.setTitle(title);
	}

	public void initView(){
		GlideTools.loadImage(mActivity,url,image);
		text_conten.setText(content);
		reting_button.setText(praise);
		if (ispraise.equals("true")){
			Glide.with(mActivity).load(R.drawable.graded_dynamic_praise_selected).into(reting_icon);
		}else{
			Glide.with(mActivity).load(R.drawable.graded_dynamic_praise_unselected).into(reting_icon);
		}

	}











	/**
	 * ----------------------------retrofit-----------------------
	 */


	public void ispriase(int state){


		ReqEnv env = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		FootBookPraiseReq req = new FootBookPraiseReq();
		//获取参数

		req.setSession_key(Base.user.getSession_key());
		req.setDate(dateBean.getValue());
		req.setState(state);
		req.setId(String.valueOf(id));

		reqBody.footBookPraiseReq = req;
		env.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().footbook_praise(env);
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
			if (b.footBookPraiseRes !=null){
				String result=b.footBookPraiseRes.result;
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
