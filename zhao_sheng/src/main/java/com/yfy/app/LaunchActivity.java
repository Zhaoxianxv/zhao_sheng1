package com.yfy.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import com.example.zhao_sheng.R;
import com.yfy.base.InitUtils;
import com.yfy.base.activity.BaseActivity;
import com.yfy.jpush.ExampleUtil;
import com.yfy.jpush.LocalBroadcastManager;


public class LaunchActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		init();
	}

	private void init() {
		InitUtils.init(this);
		registerMessageReceiver();
		ImageView imageView= (ImageView) findViewById(R.id.index_image);
		imageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				//初始化登录信息
				Intent intent = new Intent(mActivity, HomeNewActivity.class);
//				Intent intent = new Intent(mActivity, MainSetViewActivity.class);
				startActivity(intent);

				onPageBack();
			}
		}, 1000);

	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return false;
	}

	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}

		private void setCostomMsg(String string) {
			// TODO Auto-generated method stub
//			if (null != msgText) {
//				msgText.setText(msg);
//				msgText.setVisibility(android.view.View.VISIBLE);
//			}
		}
	}
}
