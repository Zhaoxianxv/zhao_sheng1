package com.yfy.app.allclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ExtraRunTask.ExtraRunnable;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar.OnMenuClickListener;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;


public class ShapeDynamicActivity extends WcfActivity {

	private static final String TAG = "zxx";

	private final int CHIOCE_TEPY = 3;
	private EditText sendContent;
	private LoadingDialog loadingDialog;
	private final String addDynamic = TagFinal.SHAPE_DID_ADD;
	private String classId;
	private String content;
	private String tag_id;

	@Bind(R.id.shape_add_dynamic_typy_tag)
	TextView tepy_tag;
	@Bind(R.id.shape_add_mult)
	MultiPictureView add_mult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shape_add_dynamic);
		initAll();
	}
	private void initAll() {
		getData();
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
//				Log.e(TAG, "onAddClick: ");
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

	private void getData() {
		Bundle b = getIntent().getExtras();
		if (b != null && b.containsKey(TagFinal.CLASS_BEAN)) {
			classId = b.getString(TagFinal.CLASS_BEAN);
		}
	}

	private void initViews() {

		assert toolbar!=null;
		toolbar.setTitle( R.string.graded_send_dynamic);
		toolbar.addMenuText(1, R.string.publish);
		toolbar.setOnMenuClickListener(new OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				if (isCanSend()) {
					sendDynamic();
				}
			}
		});

		sendContent = (EditText) findViewById(R.id.sendContent);


	}

	private boolean isCanSend() {
		content = sendContent.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			toastShow(R.string.tips_export_dynamic_content);
			return false;
		}
		if (StringJudge.isEmpty(tag_id)) {
			toastShow("选着类型");
			return false;
		}
		return true;
	}

	private void sendDynamic() {
		Object[] params = new Object[] {
				Base.user.getSession_key(),
				Base.user.getName(),
				0,//班级id
				content,
				"",
				"" ,
				ConvertObjtect.getInstance().getInt(tag_id)};
		ParamsTask task = new ParamsTask(params, addDynamic, loadingDialog);
		ExtraRunTask wrapTask = new ExtraRunTask(task);
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
			params[4] = Base64Utils.getZipTitle2(list);
			params[5] = Base64Utils.getZipBase64Str(list);
			return params;
		}
	};





	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {
		if (JsonParser.isSuccess(result)) {
			toastShow(R.string.tips_publish_success);
			setResult(RESULT_OK);
			onPageBack();

		} else {
			toastShow(JsonParser.getErrorCode(result));
		}
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {

		toastShow(R.string.tips_net_error_send_dynamic_fail);
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
			case CHIOCE_TEPY:
				tag_id=data.getStringExtra("shape_kind")==null?"":data.getStringExtra("shape_kind");
				tepy_tag.setText(data.getStringExtra("shape_name")==null?"":data.getStringExtra("shape_name"));
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@OnClick(R.id.shape_add_dynamic_typy_tag)
	void setChioce(){
		Intent ent = new Intent(mActivity, ShapeChioceTagActivity.class);
		startActivityForResult(ent,CHIOCE_TEPY);
	}


	@Override
	public void onPageBack() {
		super.onPageBack();
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
