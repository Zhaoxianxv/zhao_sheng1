/**
 * 
 */
package com.yfy.app.attennew;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.attennew.bean.AttenRes;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.atten.AttenApplyReq;
import com.yfy.app.net.atten.AttenGetTypeReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.attennew.bean.AttenType;
import com.yfy.app.attennew.bean.Subject;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.json.JsonParser;
import com.yfy.final_tag.Base64Utils;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * @author yfy1
 * @version 1.0
 * @Date 2016年1月14日
 * @Desprition
 */
public class AttenAddActivity extends BaseActivity implements Callback<ResEnv>{
	private static final String TAG = AttenAddActivity.class.getSimpleName();

	@Bind(R.id.atten_admin_type)//审批人
	TextView admin_name;
	@Bind(R.id.leave_date)//开始时间
	TextView leave_date;
	@Bind(R.id.atten_type)//类型
	TextView atten_type;
	@Bind(R.id.leave_duration)//天数
	EditText leave_duration;
	@Bind(R.id.leave_reason)//内容
	EditText leave_reason;
	@Bind(R.id.atten_add_mult)
	MultiPictureView add_mult;

	private final static int USERID = 9;
	private DateBean dateBean;
	private int userid = -1;

	private List<AttenType> leaveTypeList = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atten_add_new);
		dateBean=new DateBean();
		dateBean.setValue_long(System.currentTimeMillis(),false);
		initAll();
	}


	private void initAll() {
		initSQtoolbar();
		initViews();
		initAbsListView();
		initDialog();
		initDateDialog();
		initListDialog();
	}



	private void initSQtoolbar() {
		assert toolbar != null;
		toolbar.setTitle("请假");
		toolbar.addMenuText(1, "提交");
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				if (isCanUpload()) {
					mtask=new MyAsyncTask();
					mtask.execute();
				}
			}
		});
	}

	private void initViews() {
		leave_date.setText(dateBean.getName());

		admin_name.setText("请选择审批人");
		admin_name.setTextColor(Color.rgb(183,183,183));
		atten_type.setText("未选择请假类型");
		atten_type.setTextColor(Color.rgb(183,183,183));


	}





	private CPWListView cpwListView;
	List<String> txts=new ArrayList<>();
	private void initData(){
		if (StringJudge.isEmpty(leaveTypeList)){
			toast(R.string.success_not_details);
			return;
		}else{
			txts.clear();
			for(AttenType s:leaveTypeList){
				txts.add(s.getTypename());
			}
		}
		closeKeyWord();
		cpwListView.setDatas(txts);
		cpwListView.showAtCenter();

	}
	private void initListDialog(){
		cpwListView = new CPWListView(mActivity);
		cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
			@Override
			public void onClick(int index,String type) {
				kqtype = leaveTypeList.get(index).getTypeid();
				atten_type.setText(leaveTypeList.get(index).getTypename());
				atten_type.setTextColor(Color.rgb(24,24,24));
				cpwListView.dismiss();
			}
		});
	}



	private boolean isCanUpload() {
		duration = leave_duration.getText().toString().trim();
		if (userid == -1) {
			toastShow("请选择审批人");
			return false;
		}
		if (TextUtils.isEmpty(duration)) {
			toastShow("请填写请假天数");
			return false;
		}
		if (TextUtils.isEmpty(kqtype)) {
			toastShow("请选择请假类型");
			return false;
		}
		reason = leave_reason.getText().toString().trim();
		if (TextUtils.isEmpty(reason)) {
			toastShow("请填写请假理由");
			return false;
		}
		return true;
	}
	private String reason = "";
	private String kqtype = "";
	private String duration = "";




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case TagFinal.CAMERA:
					addMult(FileCamera.photo_camera);
					break;
				case TagFinal.PHOTO_ALBUM:
					ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
					if (photo_a==null)return;
					if (photo_a.size()==0)return;
					setMultList(photo_a);
					break;
				case USERID:
					Subject su=data.getParcelableExtra(TagFinal.OBJECT_TAG);
					admin_name.setText(su.getUser_name());
					admin_name.setTextColor(Color.rgb(44,44,44));
					userid = Integer.parseInt(su.getUser_id());
					break;

			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	//==============add icon============


	public void addMult(String uri){
		if (uri==null) return;
		add_mult.addItem(uri);
	}
	public void setMultList(List<Photo> list){
		for (Photo photo:list){
			if (photo==null) continue;
			addMult(photo.getPath());
		}
	}

	private void initAbsListView() {

		add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
			@Override
			public void onAddClick(View view) {
				Logger.e(TagFinal.ZXX, "onAddClick: ");
				closeKeyWord();
				album_select.showAtBottom();
			}
		});

		add_mult.setClickable(false);

		add_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
			@Override
			public void onDeleted(@NotNull View view, int index) {
				add_mult.removeItem(index,true);
//                Logger.e(TAG, "onDeleted: "+add_mult.getList().size() );
			}
		});
		add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
			@Override
			public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
				Logger.e(TagFinal.ZXX, "onItemClicked: "+index );
				Intent intent=new Intent(mActivity, SingePicShowActivity.class);
				Bundle b=new Bundle();
				b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}


	private ConfirmAlbumWindow album_select;
	private void initDialog() {
		album_select = new ConfirmAlbumWindow(mActivity);
		album_select.setOne_select(getResources().getString(R.string.take_photo));
		album_select.setTwo_select(getResources().getString(R.string.album));
		album_select.setName(getResources().getString(R.string.upload_type));
		album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.popu_select_one:
						PermissionTools.tryCameraPerm(mActivity);
						break;
					case R.id.popu_select_two:
						PermissionTools.tryWRPerm(mActivity);
						break;
				}
			}
		});
	}

	private ConfirmDateAndTimeWindow date_dialog;
	private void initDateDialog(){
		date_dialog = new ConfirmDateAndTimeWindow(mActivity);
//		date_dialog.setCancelName("无");
		date_dialog.setOnPopClickListenner(new ConfirmDateAndTimeWindow.OnPopClickListenner() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.set:
						dateBean.setName(date_dialog.getTimeName());
						dateBean.setValue(date_dialog.getTimeValue());
						leave_date.setText(dateBean.getName());
						date_dialog.dismiss();
						break;
					case R.id.cancel:
						date_dialog.dismiss();
						break;
				}

			}
		});
	}

	@OnClick(R.id.atten_admin_type_layout)
	void setChoiceAdmin(){
		Intent intent = new Intent(this, SelectMasterActivity.class);
		startActivityForResult(intent, USERID);
	}
	@OnClick(R.id.leave_date)
	void setChoiceTime(){
		closeKeyWord();
		date_dialog.showAtBottom();
	}


	@OnClick(R.id.atten_type_layout)
	void setChoiceType(){
		if (leaveTypeList.size() > 0) {
			initData();
		} else {
			getLeaveType();
		}
	}







	@PermissionSuccess(requestCode = TagFinal.CAMERA)
	private void takePhoto() {
		FileCamera camera = new FileCamera(mActivity);
		startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
	}

	@PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
	private void photoAlbum() {
		Intent intent = new Intent(mActivity, AlbumOneActivity.class);
		Bundle b = new Bundle();
		b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
		b.putBoolean(TagFinal.ALBUM_SINGLE, false);
		intent.putExtras(b);
		startActivityForResult(intent, TagFinal.PHOTO_ALBUM);
	}

	@PermissionFail(requestCode = TagFinal.CAMERA)
	private void showCamere() {
		Toast.makeText(getApplicationContext(), R.string.permission_fail_camere, Toast.LENGTH_SHORT).show();
	}

	@PermissionFail(requestCode = TagFinal.PHOTO_ALBUM)
	private void showTip1() {
		Toast.makeText(getApplicationContext(), R.string.permission_fail_album, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}

	/**
	 * --------------------------retrofit--------------------------
	 */

	public void getLeaveType()  {

		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		AttenGetTypeReq req = new AttenGetTypeReq();
		//获取参数

		reqBody.attenGetTypeReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_get_type(reqEnv);
		call.enqueue(this);
	}



	private void addLeave() {

		ReqEnv reqEnvelop = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		AttenApplyReq req = new AttenApplyReq();
		//获取参数
		req.setUserid(userid);
		req.setStart_time(dateBean.getValue());
		req.setDuration(duration);
		req.setReason(reason);
		req.setTable_plan("");
		req.setImage_file(content_s);
		req.setImage(name_s);
		req.setKqtype(kqtype);

		reqBody.attenApplyReq= req;
		reqEnvelop.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_apply(reqEnvelop);
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
			if (b.attenGetTypeRes !=null){
				String result=b.attenGetTypeRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				AttenRes res= gson.fromJson(result, AttenRes.class);
				leaveTypeList=res.getKqtype();
				if (StringJudge.isEmpty(leaveTypeList)){
					toast(R.string.success_not_details);
				}else{
					initData();
				}
			}
			if (b.attenApplyRes!=null){
				String result=b.attenApplyRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				AttenRes res= gson.fromJson(result, AttenRes.class);
				if (StringJudge.isSuccess(gson,result)) {
					toastShow("申请成功,请等待审核");
					setResult(RESULT_OK);
					onPageBack();
				} else {
					toastShow(JsonParser.getErrorCode(result));
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




	private MyAsyncTask mtask;
	private String name_s="",content_s="";
	private List<Photo> photo_list=new ArrayList<>();
	public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
		//内部执行后台任务,不可在此方法内修改UI
		@Override
		protected Void doInBackground(String... arg0) {
			if (isCancelled()) {
				return null;
			}
			int i=0;
			for (String uri:add_mult.getList()){
				Photo p=new Photo();
				i++;
				String picName = String.valueOf(System.currentTimeMillis())+String.valueOf(i);
				p.setFileName(picName+".jpg");
				p.setPath(uri);
				photo_list.add(p);
			}
			if (StringJudge.isEmpty(photo_list)){
			}else{
				name_s= Base64Utils.getZipTitleNotice(photo_list);
				content_s=  Base64Utils.filesToZipBase64Notice(photo_list);
			}
			return null;
		}
		//onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			addLeave();
		}
		//onProgressUpdate方法用于更新进度信息
		protected void onProgressUpdate(Integer... integers) {
			super.onProgressUpdate(integers);

		}
		//onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog("");
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//判断AsyncTask不为null且Status.RUNNING在运行状态
		if (mtask!=null&&mtask.getStatus()==AsyncTask.Status.RUNNING) {//为mtask标记cancel(true)状态
			mtask.cancel(true);
		}
	}

}

