package com.yfy.app.notice.cyc;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.notice.NoticeGetUserDetailReq;
import com.yfy.app.net.notice.NoticeReadReq;
import com.yfy.app.notice.bean.NoticeBean;
import com.yfy.app.notice.bean.NoticeDetail;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeDetailsActivity extends BaseActivity implements Callback<ResEnv>{


	private static final String TAG = NoticeDetailsActivity.class.getSimpleName();
	private String time,title,details,sender,id,state;
	private List<String> urls=new ArrayList<String>();
	@Bind(R.id.read_state_tag)
	ImageView state_tag;
	private MultiPictureView multi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_details);
		getData();
		getDetail(id);
		initView();
		initSQToolbar();
		readNotice(id);
	}



	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

	private void initSQToolbar() {
		assert toolbar!=null;
		toolbar.setTitle("通知详情");
		toolbar.setOnNaviClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}



	/**
	 * @description
	 */
	private void getData() {
		NoticeBean bean = getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
		time=bean.getDate();
		title=bean.getTitle();
		details=bean.getContent();
		sender=bean.getSender();
		id=bean.getId();
		state=bean.getState();
	}
	private void initView() {
		// TODO Auto-generated method stub

		TextView head= findViewById(R.id.notice_title);
		TextView date= findViewById(R.id.notice_time);
		TextView detail= findViewById(R.id.notice_details);
		head.setText(title);
		date.setText(sender+" "+time);

		detail.setText("\t\t\t"+details);

		multi= findViewById(R.id.notice_icon_gallery);
		multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
			@Override
			public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
				Intent intent=new Intent(mActivity, MultPicShowActivity.class);
				Bundle b=new Bundle();
				b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
				b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}

	private void initImage(List<String> urls) {
		if (urls.size()==0) {
			multi.setVisibility(View.INVISIBLE);
			return;
		}else{
			multi.setVisibility(View.VISIBLE);
		}
		multi.setList(urls);
	}






	/**
	 * ---------------------------retrofit-----------------------
	 */

	private void getDetail(String id) {
		ReqEnv reqenv=new ReqEnv();
		ReqBody reqbody=new ReqBody();
		NoticeGetUserDetailReq req=new NoticeGetUserDetailReq();
		//参数
		req.setId(id);

		reqbody.noticeGetUserDetailReq =req;
		reqenv.body=reqbody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_user_detail(reqenv);
		call.enqueue(this);
	}




	private void readNotice(String id) {
		ReqEnv reqenv=new ReqEnv();
		ReqBody reqbody=new ReqBody();
		NoticeReadReq req_state=new NoticeReadReq();
		//参数
		req_state.setId(ConvertObjtect.getInstance().getInt(id));

		reqbody.noticeReadReq=req_state;
		reqenv.body=reqbody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_read(reqenv);
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
			if (b.noticeGetUserDetailRes !=null){
				String result=b.noticeGetUserDetailRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				NoticeDetail detail = JsonParser.getReceiveNoticeicon(result.replaceAll("null", "\"\""));
				urls=detail.getImages();
				initImage(urls);
			}
			if (b.noticeReadRes !=null){
				String result=b.noticeReadRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
			}
		}else{
			toast("数据出差了");
			Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
		}
	}

	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		if (!isActivity())return;
		Logger.e("onFailure  :"+call.request().headers().toString());
		dismissProgressDialog();
		toast(R.string.fail_do_not);
	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}


}
