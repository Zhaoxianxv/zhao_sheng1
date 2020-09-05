package com.yfy.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.zhao_sheng.R;
import com.yfy.base.App;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.FileTools;
import com.yfy.final_tag.Logger;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;




public class  UpdateManager {

	private Context mContext;
	private String apkUrl = "";
	private Dialog downloadDialog;
	private static final String saveFileName = Environment.getExternalStorageDirectory().toString() + "/yfy/"+ System.currentTimeMillis() + ".apk";

	private ProgressBar mProgress;
	private TextView text;


	private static final int DOWN_UPDATE = 1;

	private static final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWN_UPDATE:
					mProgress.setProgress(progress);
					break;
				case DOWN_OVER:
					
					installApk();
					break;
				default:
					break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}


	public void checkUpdateInfo(){
		showDownloadDialog();
	}



	private void showDownloadDialog(){
		MyAlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("");

		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.upload_progress, null);
		mProgress = (ProgressBar)v.findViewById(R.id.progress);
		text = (TextView)v.findViewById(R.id.loading);

		text.setText("");
		builder.setView(v);
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});

		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			if (!Thread.currentThread().isInterrupted()){
				try {
					apkUrl=UploadDataService.getUrl();
					URL url = new URL(apkUrl);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setRequestProperty("conn","Keep-Alive");
					conn.connect();
					long length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					String apkFile = saveFileName;
					FileTools.createFile(apkFile);
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do{
						int numread = is.read(buf);
						count += numread;
						progress =(int)(((float)count / length) * 100);
						
						if (!Thread.currentThread().isInterrupted()){
							mHandler.sendEmptyMessage(DOWN_UPDATE);
							if(numread <= 0){
								mHandler.sendEmptyMessage(DOWN_OVER);
								break;
							}
						}
						fos.write(buf,0,numread);
					}while(!interceptFlag);
					Logger.e("zxx","-conn-3-");
					fos.close();
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch(IOException e){
					e.printStackTrace();
				}

			}
		}

	};

	/**
	 *
	 * @param
	 */

	private void downloadApk(){
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	/**
	 * apk
	 * @param
	 */
	private void installApk(){
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		//判断是否是AndroidN以及更高的版本
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			if (mContext==null){
				Logger.e("zxx","mContext null");
			}
			Uri contentUri = FileProvider.getUriForFile(App.getApp().getApplicationContext(), Base.AUTHORITY, apkfile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");

		} else {
			Uri contentUri=Uri.fromFile(apkfile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		}
		if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
			mContext.startActivity(intent);
		}

	}
}
