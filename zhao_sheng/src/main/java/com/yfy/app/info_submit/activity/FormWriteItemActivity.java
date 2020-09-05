package com.yfy.app.info_submit.activity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.frament.AbstractFragment;
import com.yfy.app.info_submit.frament.DatePickerFragment;
import com.yfy.app.info_submit.frament.EditAndButtonFragment;
import com.yfy.app.info_submit.frament.EditTextFragment;
import com.yfy.app.info_submit.frament.PhoneChangeFragment;
import com.yfy.app.info_submit.frament.SingleSelectFragment;
import com.yfy.app.info_submit.infos.ItemType;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.example.zhao_sheng.R;
import com.yfy.base.fragment.BaseFragmentActivity;

public class FormWriteItemActivity extends BaseFragmentActivity {

	private FragmentManager manager;
	private FragmentTransaction transaction;
	private AbstractFragment fragment;

	private RelativeLayout back;
	private TextView title, ok;

	private WriteItem writeItem;

	private Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_form_write_item);
		initView();
	}

	@SuppressLint("NewApi")
	private void initView() {
		resources = getResources();

		Bundle b = getIntent().getExtras();
		int position1 = b.getInt("position1");
		int position2 = b.getInt("position2");
		writeItem = InfosConstant.totalList.get(position1).get(position2);
		ItemType itemType = writeItem.getItemType();
		switch (itemType.getFragmentType()) {
		case 0:
			fragment = new EditTextFragment();
			break;
		case 1:
			fragment = new SingleSelectFragment();
			break;
		case 2:
			fragment = new DatePickerFragment();
			break;
		case 3:
			fragment = new EditAndButtonFragment();
			break;
		case 4:
			fragment = new PhoneChangeFragment();
			break;
		}
		fragment.setArguments(b);
		manager = getSupportFragmentManager();
		transaction = manager.beginTransaction();
		if (fragment != null) {
			transaction.replace(R.id.replace_frame, fragment);
			transaction.commit();
		}

		title = (TextView) findViewById(R.id.center_tv);
		back = (RelativeLayout) findViewById(R.id.left_rela);
		ok = (TextView) findViewById(R.id.ok);
		title.setText(writeItem.getItemName());
		back.setOnClickListener(onClickListener);
		ok.setOnClickListener(onClickListener);

		if (writeItem.getItemValue()!=null||writeItem.getItemValue().equals("")) {
			ok.setTextColor(resources.getColor(R.color.White));
		}
	}

	public void setOkClicked(boolean b) {
		ok.setClickable(b);
		if (b) {
			ok.setTextColor(resources.getColor(R.color.White));
		} else {
			ok.setTextColor(resources.getColor(R.color.Gray));
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.ok:
				if (ok.isClickable()) {
					String result = fragment.getData();
					if (!LegalityJudger.judge(FormWriteItemActivity.this, result, writeItem)) {
						return;
					}
					writeItem.setItemValue(result);
					finish();
				}
				break;
			case R.id.left_rela:
				finish();
				break;
			}
		}
	};

}
