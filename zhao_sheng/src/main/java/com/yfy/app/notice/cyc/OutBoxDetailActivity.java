package com.yfy.app.notice.cyc;

import android.os.Bundle;
import android.widget.GridView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.notice.NoticeGetSendBoxDetailReq;
import com.yfy.app.notice.adapter.ReadStateAdapter;
import com.yfy.app.notice.bean.ReadState;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutBoxDetailActivity extends BaseActivity implements Callback<ResEnv>{

	private final static String TAG = OutBoxDetailActivity.class.getSimpleName();
	private GridView gridView;

	private List<ReadState> readStateList = new ArrayList();
	private ReadStateAdapter adapter;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_outbox_detail);
		initAll();
		initSQToolbar();
	}

	private void initSQToolbar() {

		assert toolbar!=null;
		toolbar.setTitle("消息回执");
	}

	private void initAll() {
		getData();
		gridView =  findViewById(R.id.gridView);
		adapter = new ReadStateAdapter(this, readStateList);
		gridView.setAdapter(adapter);
	}

	/**
	 * @description
	 */
	private void getData() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			if (b.containsKey(TagFinal.ID_TAG)) {
				id = b.getString(TagFinal.ID_TAG);
				getReadStateTask();
			}
		}
	}





	/**
	 * ---------------------------retrofit-----------------------
     */

	private void getReadStateTask(){
		ReqEnv reqenv=new ReqEnv();
		ReqBody reqbody=new ReqBody();
		NoticeGetSendBoxDetailReq state=new NoticeGetSendBoxDetailReq();

		state.setId(ConvertObjtect.getInstance().getInt(id));
		reqbody.noticeGetSendBoxDetailReq =state;
		reqenv.body=reqbody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_send_detail(reqenv);
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
			if (b.noticeGetSendBoxDetailRes !=null){
				String result=b.noticeGetSendBoxDetailRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				readStateList = JsonParser.getReadStateList(result);
				adapter.notifyDataSetChanged(readStateList);
			}
		}else{
			Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
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
