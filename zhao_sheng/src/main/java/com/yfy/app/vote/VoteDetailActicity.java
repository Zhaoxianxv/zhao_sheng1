/**
 *
 */
package com.yfy.app.vote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.vote.VoteGetItemDetailReq;
import com.yfy.app.net.vote.VoteSubmitReq;
import com.yfy.app.vote.adapter.VoteOperationAdapter;
import com.yfy.app.vote.adapter.VoteOperationAdapter.CheckBoxOnclick;
import com.yfy.app.vote.bean.VoteBean;
import com.yfy.app.vote.bean.VoteInfo;
import com.yfy.app.vote.bean.VoteRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.MyDialog;
import com.yfy.view.MyDialog.OnCustomDialogListener;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yfy1
 * @Date 2015年11月26日
 * @version 1.0
 * @Desprition
 */
public class VoteDetailActicity extends BaseActivity implements Callback<ResEnv> {

	private final static String TAG = VoteDetailActicity.class.getSimpleName();

	private ExpandableListView listView;
	private MyDialog continue_dialog;

	private VoteOperationAdapter adapter;
	private List<VoteInfo> voteDetailList = new ArrayList<>();

	private boolean isVoted;

	private String voteId = "";
	private String content = "";
	private int position;


	@Bind(R.id.in_fu)
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vote_detail);
		initData();
		initViews();
		initDialog();
		initSQToolbar();
		refresh();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (continue_dialog.isShowing()) {
			continue_dialog.dismiss();
		}
	}

	private void initSQToolbar() {
		assert toolbar!=null;
		toolbar.setTitle("");
		toolbar.addMenuText(1,"提交");
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				closeKeyWord();
				view.setFocusableInTouchMode(true);
				if (isCanSend()) {
					continue_dialog.showAtCenter();
				}else{

				}
			}
		});
	}



	private void initDialog() {
		continue_dialog = new MyDialog(
				this,
				R.layout.layout_votedetail_dialog,
				new int[] { R.id.cancle, R.id.continue_bt },
				new int[] {R.id.cancle, R.id.continue_bt });

		continue_dialog.setOnCustomDialogListener(listener);
	}

	private void initData() {
		Bundle b = getIntent().getExtras();
		position = b.getInt("position");
		voteId = b.getString("voteId");
		isVoted = b.getBoolean("isVoted");

	}




	private void initViews() {
		listView =  findViewById(R.id.listView);


		listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

				return true;
			}
		});


	}

	private CheckBoxOnclick click=new CheckBoxOnclick() {
		@Override
		public void oniBox(int group) {
			adapter.setVoteDetailList(adapter.getVoteDetailList());

			listView.collapseGroup(group);
			listView.expandGroup(group);
		}
	};


	private OnCustomDialogListener listener = new OnCustomDialogListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.cancle:
					continue_dialog.dismiss();
					break;
				case R.id.continue_bt:
					vote();
					continue_dialog.dismiss();
					break;
			}
		}

	};

	private boolean isCanSend() {
		if (isVoted) {
			toastShow("已经投过,无法再次投票");
			return false;
		}
		List<VoteInfo> voteResult = adapter.getVoteDetailList();
		StringBuffer sb = new StringBuffer();
		for (VoteInfo vote : voteResult) {
			if(vote.getType().equals("3")){
				if (StringJudge.isEmpty(vote.getReply())) {
					toastShow("问答题全部完成才能提交");
					return false;
				}else{
					sb.append(vote.getId().toString())
							.append("^").append("0")
							.append("^").append(vote.getReply());

				}
			}else if(vote.getType().equals("1")){
				if (vote.getMaxsize()==1){
					boolean is=false;
					for (VoteBean bean:vote.getVotecontent()){
						is=bean.getIsselect().equals("true")?true:false;
						if (is){
							sb.append(vote.getId())
									.append("^").append(bean.getId())
									.append("^").append("true");
							break;
						}

					}
					if (!is){
						toastShow("选择题全部完成才能提交");
						return false;
					}
				}else{
					boolean frist=false;
					boolean is=false;
					for (VoteBean bean:vote.getVotecontent()){
						is=bean.getIsselect().equals("true")?true:false;
						if (is){
							if (frist){
								sb.append("|").append(vote.getId().toString())
										.append("^").append(bean.getId().toString())
										.append("^").append("true");
							}else{
								sb.append(vote.getId())
										.append("^").append(bean.getId().toString())
										.append("^").append("true");
							}
							frist=is;
							continue;
						}
					}
					if (!frist){
						toastShow("多选题全部完成才能提交");
						return false;
					}
				}
			}
			sb.append("|");
		}

		content = sb.toString().substring(0, sb.length() - 1);
		Logger.e("zxx", "" + content);
		return true;
	}











	/**
	 * -------------------retrofit---------------------
	 */




	private void refresh() {



		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		VoteGetItemDetailReq req = new VoteGetItemDetailReq();
		//获取参数
		req.setId(voteId);
//
		reqBody.voteGetItemDetailReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().vote_get_detail(reqEnv);
		call.enqueue(this);
		showProgressDialog("");
	}

	private void vote() {


		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		VoteSubmitReq req = new VoteSubmitReq();
		//获取参数
		req.setRealname(Base.user.getName());
		req.setExamid(voteId);
		req.setVotecontent(content);
//
		reqBody.voteSubmitReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().vote_do_submit(reqEnv);
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
			if (b.voteGetItemDetailRes !=null){
				String result=b.voteGetItemDetailRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));

				VoteRes res=gson.fromJson(result, VoteRes.class);
				voteDetailList=res.getVotedetail();
				if (voteDetailList.size()== TagFinal.ZERO_INT){
					if (StringJudge.isEmpty(res.getError_code())){
						toastShow(R.string.success_not_details);
					}else{
						toastShow(res.getError_code());
					}
				}
				adapter = new VoteOperationAdapter(mActivity, voteDetailList,isVoted);
				listView.setAdapter(adapter);
				adapter.setClick(click);
				for (int i=0; i<voteDetailList.size(); i++) {
					listView.expandGroup(i);
				}

			}
			if (b.voteSubmitRes !=null){
				String result=b.voteSubmitRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				VoteRes res=gson.fromJson(result, VoteRes.class);
				toastShow(result);
				isVoted = true;
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();
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
		toast(R.string.fail_do_not);
		dismissProgressDialog();
	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}

}
