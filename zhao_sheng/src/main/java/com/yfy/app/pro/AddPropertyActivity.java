/**
 *
 */
package com.yfy.app.pro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.greendao.User;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ExtraRunTask.ExtraRunnable;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.Base64Utils;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

/**
 * @author yfy1
 * @Date 2016年1月12日
 * @version 1.0
 * @Desprition
 */
public class AddPropertyActivity extends WcfActivity implements OnClickListener {

	private final static String TAG ="zxx";

	private TextView head_title, submit;
	private TextView supply_name_tv;
	private EditText supply_count, remark_et;
	private List<Photo> photoList = new ArrayList<Photo>();

	private LoadingDialog loadingDialog;


	private ParamsTask sendTask;
	private ExtraRunTask wrapTask;

	private final String method = "addxclist";
	private User userInfo = Base.user;
	private String classId = "";
	private String suppid = "";
	private String number = "";
	private String bz = "";


	private final static int SUPPLY_TYPE = 4;


	@Bind(R.id.notice_add_mult)
	MultiPictureView add_mult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_property);
		initAll();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initAll() {
		getDataFromIntent();
		initViews();
		initDialog();
		initAbsListView();

	}


	private MyDialog typeDialog;
	private void initDialog() {
		loadingDialog = new LoadingDialog(this);
		typeDialog= new MyDialog(mActivity,
				R.layout.dialog_getpic_type_popup,
				new int[] { R.id.take_photo, R.id.alnum, R.id.cancle },
				new int[] { R.id.take_photo, R.id.alnum, R.id.cancle });
		typeDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
		typeDialog.setOnCustomDialogListener(new AbstractDialog.OnCustomDialogListener() {
			@Override
			public void onClick(View v, AbstractDialog dialog) {
				switch (v.getId()) {
					case R.id.take_photo:
						PermissionTools.tryCameraPerm(mActivity);
						dialog.dismiss();
						break;
					case R.id.alnum:
						PermissionTools.tryWRPerm(mActivity);
						dialog.dismiss();
						break;
					case R.id.cancle:
						dialog.dismiss();
						break;
				}
			}
		});

	}


	private void initAbsListView() {

		add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
			@Override
			public void onAddClick(View view) {
				Logger.e(TAG, "onAddClick: ");
				typeDialog.showAtBottom();
			}
		});

		add_mult.setClickable(false);

		add_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
			@Override
			public void onDeleted(@NotNull View view, int index) {
				add_mult.removeItem(index,true);
//                Log.e(TAG, "onDeleted: "+add_mult.getList().size() );
			}
		});
		add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
			@Override
			public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
				Logger.e(TAG, "onItemClicked: "+index );
				Intent intent=new Intent(mActivity, SingePicShowActivity.class);
				Bundle b=new Bundle();
				b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}

	private void getDataFromIntent() {
		classId = getIntent().getExtras().getString("classId");
	}

	private void initViews() {
		supply_name_tv = (TextView) findViewById(R.id.supply_name);
		supply_count = (EditText) findViewById(R.id.supply_count);
		remark_et = (EditText) findViewById(R.id.remark_et);
		head_title = (TextView) findViewById(R.id.head_title);
		submit = (TextView) findViewById(R.id.right_tv);

		head_title.setVisibility(View.VISIBLE);
		head_title.setText("添加校产信息");
		submit.setText("提交");

		setOnClickListener(mActivity, R.id.left_rela, R.id.right_tv, R.id.type_lila);
	}







	private boolean isCanUpload() {
		number = supply_count.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			toastShow("物品数量不能为空");
			return false;
		}
		if (TextUtils.isEmpty(suppid)) {
			toastShow("请选择物品类型");
			return false;
		}

		if (!Base64Utils.canUpload(photoList)) {
			toastShow("内存不足,请减少图片");
			return false;
		}
		bz = remark_et.getText().toString().trim();
		return true;
	}

	private void addProperty() {
		Object[] params = new Object[] { userInfo.getSession_key(), classId,
				suppid, number, bz, photoList, photoList };
		sendTask = new ParamsTask(params, method, loadingDialog);
		wrapTask = new ExtraRunTask(sendTask);
		wrapTask.setExtraRunnable(extraRunnable);
		execute(wrapTask);
	}

	private ExtraRunnable extraRunnable = new ExtraRunnable() {

		@Override
		public Object[] onExecute(Object[] params) {
			List<Photo> list=new ArrayList<>();
			for (String uri:add_mult.getList()){
				Photo p=new Photo();
//				String path= CompressUtils.compressFileStringSample(uri);
//				if (path==null){
					p.setPath(uri);
//				}else{
//					p.setPath(path);
//				}
				list.add(p);
			}
			params[5] = Base64Utils.getZipTitle2(list);
			params[6] = Base64Utils.getZipBase64Str(list);
			return params;
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle b;
			switch (requestCode) {
				case SUPPLY_TYPE:
					b = data.getExtras();
					suppid = b.getString("supplyId");
					String supplyName = b.getString("supplyName");
					supply_name_tv.setText(supplyName);
					remark_et.setText(b.getString("supplyBz"));
					break;
				case TagFinal.CAMERA:
					addMult(FileCamera.photo_camera);
					break;
				case TagFinal.PHOTO_ALBUM:
					ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
					if (photo_a==null)return;
					if (photo_a.size()==0)return;
					setMultList(photo_a);
//					Log.e(TAG, "onActivityResult:imgPath  "+photo_a.get(0).toString());
					break;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {
		if (JsonParser.isSuccess(result)) {
			toastShow("添加成功");
			setResult(RESULT_OK);
			finish();
		} else {
			toastShow(JsonParser.getErrorCode(result));
		}
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {
		toastShow("网络异常,提交失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_rela:
				finish();
				break;
			case R.id.right_tv:
				if (isCanUpload()) {
					addProperty();
				}
				break;
			case R.id.type_lila:
				Intent intent = new Intent(this, SupplyTypeActivity.class);
				startActivityForResult(intent, SUPPLY_TYPE);
				break;
		}
	}




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


	@PermissionSuccess(requestCode = TagFinal.CAMERA)
	private void takePhoto() {
		FileCamera camera=new FileCamera(mActivity);
		startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
	}
	@PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
	private void photoAlbum() {
		Intent intent;
		intent = new Intent(mActivity, AlbumOneActivity.class);
		Bundle b = new Bundle();
		b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
		b.putBoolean(TagFinal.ALBUM_SINGLE, false);
		intent.putExtras(b);
		startActivityForResult(intent,TagFinal.PHOTO_ALBUM);
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
