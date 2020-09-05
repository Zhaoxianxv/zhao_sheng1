/**
 * 
 */
package com.yfy.app.process;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.process.ProcessApplyDetailReq;
import com.yfy.app.net.process.ProcessGetDetailReq;
import com.yfy.app.process.adapter.ProcessRoomAddAdapter;
import com.yfy.app.process.bean.ProBeanChild;
import com.yfy.app.process.bean.ProDetailInfo;
import com.yfy.app.process.bean.ProDetailRes;
import com.yfy.app.process.bean.ProLayerOneData;
import com.yfy.app.process.bean.ProLayerTwoData;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.ZoomImage;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;


/**
 * @author yfy1
 * @version 1.0
 * @Date 2016年1月14日
 * @Desprition
 */
public class ProcessRoomAddActivity extends BaseActivity {

	private static final String TAG = ProcessRoomAddActivity.class.getSimpleName();
	private DateBean dateBean;
	private ProcessRoomAddAdapter addAdapter;


	@Bind(R.id.public_recycler_del)
	Button del_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_recycler_del_view);
		getData();
		dateBean = new DateBean();
		dateBean.setValue_long(System.currentTimeMillis(),false);
		del_button.setVisibility(View.GONE);
		initRecycler();
		getDetail();
		initSQtoolbar();

	}


	private String id;
	private void getData(){
		id=getIntent().getStringExtra(Base.id);
	}




	private void initSQtoolbar() {
		assert toolbar != null;
		toolbar.setTitle(Base.process_title);
		toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				if (isCanSubmit()){
					applyDetail();
				}

			}
		});


	}



	private List<String> ids=new ArrayList<>();
	private List<String> contents=new ArrayList<>();
	private boolean isCanSubmit(){
		ids.clear();
		contents.clear();
		closeKeyWord();
		List<ProLayerOneData> adapterShowData=addAdapter.getDataList();
		for (ProLayerOneData oneData:adapterShowData){
				List<String> names=StringUtils.listToStringSplitCharacters(oneData.getValuetype(), "_");
				switch (names.get(0)){
					case "image":
						ids.add(String.valueOf((long)oneData.getId()));
						List<String> images=oneData.getKeyValue().getListValue();
						if (StringJudge.isEmpty(images)&&isCanNUll(oneData.getCannull())){
							toast(StringUtils.getTextJoint("%1$s为必填项",oneData.getTitle()));
							return false;
						}
						contents.add(StringUtils.stringToArraysGetString(images,","));
						break;
					case "group":
						ids.add(String.valueOf((long)oneData.getId()));
						contents.add("");
						if (StringJudge.isEmpty(oneData.getRecordlinkvalue()))continue;
						for (ProLayerTwoData twoData:oneData.getRecordlinkvalue()){
							ids.add(String.valueOf((long)twoData.getId()));

							String group_content=twoData.getKeyValue().getRight_value();
							if (StringJudge.isEmpty(group_content)&&isCanNUll(twoData.getCannull())){
								toast(StringUtils.getTextJoint("%1$s为必填项",twoData.getTitle()));
								return false;
							}

							contents.add(twoData.getKeyValue().getRight_value());
						}
						break;
					case "check":
						ids.add(String.valueOf((long)oneData.getId()));
						String check=oneData.getKeyValue().getRight_value();
						if (StringJudge.isEmpty(check)&&isCanNUll(oneData.getCannull())){
							toast(StringUtils.getTextJoint("%1$s为必填项",oneData.getTitle()));
							return false;
						}
						contents.add(check);
						if (check.equalsIgnoreCase(TagFinal.TRUE)){
							if (StringJudge.isEmpty(oneData.getRecordlinkvalue()))continue;
							for (ProLayerTwoData twoData:oneData.getRecordlinkvalue()){
								ids.add(String.valueOf((long)twoData.getId()));

								String check_content=twoData.getKeyValue().getRight_value();
								if (StringJudge.isEmpty(check_content)&&isCanNUll(twoData.getCannull())){
									toast(StringUtils.getTextJoint("%1$s为必填项",twoData.getTitle()));
									return false;
								}
								contents.add(check_content);
							}
						}else{
							if (StringJudge.isEmpty(oneData.getRecordlinkvalue()))continue;
							for (ProLayerTwoData twoData:oneData.getRecordlinkvalue()){
								ids.add(String.valueOf((long)twoData.getId()));
								contents.add("");
							}
						}
						break;
					case "datetime":
					case "select":
					case "list":
					case "selectuser":
					case "decimal":
					case "text":
					case "longtext":
					case "int":
						ids.add(String.valueOf((long)oneData.getId()));
						String content=oneData.getKeyValue().getRight_value();
						if (StringJudge.isEmpty(content)&&isCanNUll(oneData.getCannull())){
							toast(StringUtils.getTextJoint("%1$s为必填项",oneData.getTitle()));
							return false;
						}
						contents.add(content);
						break;
				}
		}
		return true;
	}



	private boolean isCanNUll(String data){
		if (data.equalsIgnoreCase("0")){
			return true;
		}else{
			return false;
		}
	}


	public RecyclerView recyclerView;
	public void initRecycler(){
		recyclerView = findViewById(R.id.public_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		//添加分割线
		recyclerView.addItemDecoration(new RecycleViewDivider(
				mActivity,
				LinearLayoutManager.HORIZONTAL,
				1,
				getResources().getColor(R.color.gray)));
		addAdapter=new ProcessRoomAddAdapter(this);
		recyclerView.setAdapter(addAdapter);
		addAdapter.setSealChoice(new ProcessRoomAddAdapter.SealChoice() {
			@Override
			public void refresh(ProLayerOneData bean, int pos_index) {
				position_index=pos_index;
				multi_bean=bean;
			}
		});


	}
	private ProLayerOneData multi_bean;
	private int position_index;



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case TagFinal.CAMERA:
					mtask=new MyAsyncTask();
					mtask.execute(FileCamera.photo_camera);
					break;
				case TagFinal.PHOTO_ALBUM:
					ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
					if (photo_a==null)return;
					if (photo_a.size()==0)return;
					Logger.e(photo_a.get(0).getPath());
					List<String> list_a=new ArrayList<>();
					for (Photo photo:photo_a){
						if (photo==null) continue;
						list_a.add(photo.getPath());
					}
					mtask=new MyAsyncTask();
					mtask.execute(StringUtils.arraysToListString(list_a));
					break;
				case TagFinal.UI_TAG:
					int position=data.getIntExtra(Base.index,0);
					List<ProLayerTwoData> adapterData=data.getParcelableArrayListExtra(Base.data);
					List<ProLayerOneData> adapterShowData=addAdapter.getDataList();
					for (ProLayerOneData oneData:adapterShowData){
						if(oneData.getValuetype().equalsIgnoreCase("group")){
							for (ProLayerTwoData twoData:oneData.getRecordlinkvalue()){
								for (ProLayerTwoData dataTwo:adapterData){
									if (twoData.getId()==dataTwo.getId()){
										twoData.setKeyValue(dataTwo.getKeyValue());
									}
								}
							}

							addAdapter.notifyItemChanged(position,oneData);
						}
					}

					break;
				case TagFinal.UI_SELECT_USER_SINGLE:
					int adapter_position=data.getIntExtra(Base.index,0);
					ProLayerOneData valueList=addAdapter.getDataList().get(adapter_position);
					valueList.setIs_select(true);
					ProBeanChild childBean=data.getParcelableExtra(Base.data);
					if (childBean.getView_type()==TagFinal.TYPE_CHILD){
						valueList.getKeyValue().setRight_value(StringUtils.getTextJoint("%1$s_%2$s",childBean.getDepartid(),"0"));
						valueList.getKeyValue().setRight_name(childBean.getUsername());
					}else{
						valueList.getKeyValue().setName(childBean.getDepartname());
						valueList.getKeyValue().setRight_value(StringUtils.getTextJoint("%1$s_%2$s",childBean.getDepartid(),childBean.getUserid()));
					}
					addAdapter.notifyItemChanged(adapter_position,valueList);
					break;
				case TagFinal.UI_SELECT_USER_MORE:
					int adapter_position_more=data.getIntExtra(Base.index,0);
					ProLayerOneData valueListMore=addAdapter.getDataList().get(adapter_position_more);
					valueListMore.setIs_select(true);
					List<ProBeanChild> childBeans=data.getParcelableArrayListExtra(Base.data);
					valueListMore.getKeyValue().setName(StringUtils.getTextJoint("已选中:%1$s\t项",childBeans.size()));
					for (ProBeanChild childBeanUser:childBeans){


					}
					addAdapter.notifyItemChanged(adapter_position_more,valueListMore);
					break;

			}
		}
	}




	private void setKeyValueData(KeyValue data,String left_title,String right_key,String right_name,String right_value){
		data.setLeft_title(left_title);
		data.setRight_key(right_key);
		data.setRight_name(right_name);
		data.setRight_value(right_value);

	}

	private void  initViews(ProDetailInfo info){

		List<ProLayerOneData> list=info.getRecordvalue();
		for (ProLayerOneData valueList:list){
			if (StringJudge.isNotEmpty(valueList.getRecordlinkvalue())){
				for (ProLayerTwoData linkBean:valueList.getRecordlinkvalue()){
					KeyValue value=new KeyValue();

					if(StringJudge.isEmpty(linkBean.getContent())){
						setKeyValueData(value,linkBean.getTitle(),"未完成","","");
					}else{
                        linkBean.setIs_select(true);
						setKeyValueData(value,linkBean.getTitle(),"",linkBean.getDisplaycontent(),linkBean.getContent());
					}


					value.setType(linkBean.getValuetype());
					linkBean.setKeyValue(value);
				}
			}

			KeyValue key=new KeyValue(String.valueOf(valueList.getId()));

			if(StringJudge.isEmpty(valueList.getContent())){
				setKeyValueData(key,valueList.getTitle(),"未完成","","");
			}else{
                valueList.setIs_select(true);
				setKeyValueData(key,valueList.getTitle(),"",valueList.getDisplaycontent(),valueList.getContent());
			}
			List<String> names=StringUtils.listToStringSplitCharacters(valueList.getValuetype(), "_");
			switch (names.get(0)){
				case "datetime":
					valueList.setView_type(TagFinal.TYPE_DATE);
					break;
				case "select":
				case "list":
					valueList.setView_type(TagFinal.TYPE_ITEM);
					break;
				case "selectuser":
					valueList.setView_type(TagFinal.TYPE_FOOTER);
					break;
				case "longtext":
					key.setRight_key(StringUtils.getTextJoint("请输入%1$s",valueList.getTitle()));
					valueList.setView_type(TagFinal.TYPE_REFRECH);
					break;
				case "image":
                    if(!StringJudge.isEmpty(valueList.getContent())){
                        key.setListValue(StringUtils.listToStringSplitCharacters(valueList.getContent(),","));
                    }
					valueList.setView_type(TagFinal.TYPE_IMAGE);
					break;
				case "check":
					valueList.setView_type(TagFinal.TYPE_CHECK);
					break;
				case "group":
					valueList.setView_type(TagFinal.TYPE_GROUP);
					break;
				case "decimal":
				case "text":
				case "int":
					valueList.setView_type(TagFinal.TYPE_PARENT);
					key.setKey(StringUtils.getTextJoint("请输入%1$s",valueList.getTitle()));
					break;
					default:
						valueList.setView_type(TagFinal.TYPE_DATE);
						break;
			}
			valueList.setKeyValue(key);
		}
		addAdapter.setDataList(list);
		addAdapter.setLoadState(TagFinal.LOADING_END);
	}



	/**
	 * --------------------------retrofit--------------------------
	 */
	public void getDetail() {
		ReqEnv evn = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		ProcessGetDetailReq req = new ProcessGetDetailReq();
		//获取参数
		req.setId(id);
		reqBody.processGetDetailReq = req;
		evn.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_detail(evn);
		Logger.e(evn.toString());
		call.enqueue(this);
		showProgressDialog("");
	}


	private void saveImg(String flie_string){
		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		SaveImgReq req = new SaveImgReq();
		//获取参数
		req.setImage_file(flie_string);
		req.setFileext("jpg");
		reqBody.saveImgReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnv);
		call.enqueue(this);
	}
	private void applyDetail(){
		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		ProcessApplyDetailReq req = new ProcessApplyDetailReq();
		//获取参数
		req.setSession_key(Base.user.getSession_key());
		req.setId(id);
		req.setType(Base.process_type);
		req.setTermid(0);

		req.setGroupid(0);
		req.setContentlist(StringUtils.arraysToListString(contents));
		req.setIdlist(StringUtils.arraysToListString(ids));

		reqBody.processApplyDetailReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_apply(reqEnv);
		call.enqueue(this);
		Logger.e(reqEnv.toString());
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
			if (b.processGetDetailRes!=null) {
				String result = b.processGetDetailRes.result;
//				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				ProDetailRes res=gson.fromJson(result,ProDetailRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					initViews(res.getData());
				}else{
					toast(res.getError_code());
				}
			}
			if (b.processApplyDetailRes!=null) {
				String result = b.processApplyDetailRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				ProDetailRes res=gson.fromJson(result,ProDetailRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					toast(R.string.success_do);
					setResult(RESULT_OK);
					finish();

				}else{
					toast(res.getError_code());
				}
			}
			if (b.saveImgRes!=null) {
				String result = b.saveImgRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				SealRes res=gson.fromJson(result,SealRes.class );
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					List<String> list_c=multi_bean.getKeyValue().getListValue();
					list_c.add(Base.RETROFIT_URI+res.getImg());
					addAdapter.notifyItemChanged(position_index,multi_bean );
				}else{
					toastShow(res.getError_code());
				}
				if (num==1){
					dismissProgressDialog();
				}else{
					num--;
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
	private int num=0;
	private List<String> base64_list=new ArrayList<>();
	public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
		//内部执行后台任务,不可在此方法内修改UI
		@Override
		protected Void doInBackground(String... arg0) {
			if (isCancelled()) {
				return null;
			}
			List<String> list = Arrays.asList(arg0);
			base64_list.clear();
			num=0;
			for (String s:list){
				base64_list.add(ZoomImage.fileToBase64Str(s));
				num++;
			}
			return null;
		}
		//onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (StringJudge.isEmpty(base64_list)){
				toastShow("没有数据");
			}
			for (String s:base64_list){
				saveImg(s);
			}
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

}

