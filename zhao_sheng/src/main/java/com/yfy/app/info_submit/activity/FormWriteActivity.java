package com.yfy.app.info_submit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.yfy.app.info_submit.adapter.FormWriteAdapter;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.MyPopupWindow;
import com.yfy.final_tag.dialog.MyPopupWindow.OnPopClickListenner;
import com.yfy.final_tag.TagFinal;
import com.example.zhao_sheng.R;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;

public class FormWriteActivity extends BaseActivity {

	private ListView listView;
	private MyPopupWindow popupWindow;

	private ArrayList<WriteItem> itemList;
	private FormWriteAdapter adapter;

	private Bundle bundle = null;
	private WriteItem writeItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_form_write);
		initSQtoolbar();
		initView();
	}


	private TextView title;

	private void initSQtoolbar(){
		assert toolbar!=null;
		title=toolbar.setTitle("");
		toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok );
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				finish();
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.write_lv);
		initData();
	}

	private void initData() {
		bundle = getIntent().getExtras();
		itemList = InfosConstant.totalList.get(bundle.getInt("position1"));
		adapter = new FormWriteAdapter(this, itemList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(onItemClickListener);
		title.setText(InfosConstant.SUBMIT_ITEM_PARENT[bundle.getInt("position1")]);

	}

	private OnPopClickListenner onClickListener = new OnPopClickListenner() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sfz_type:
				writeItem.getItemType().setSysmbol(1);
				startActivityToNext(bundle);
				popupWindow.dismiss();
				break;
			case R.id.other_type:
				writeItem.getItemType().setSysmbol(0);
				startActivityToNext(bundle);
				popupWindow.dismiss();
				break;
			case R.id.cancle:
				popupWindow.dismiss();
				break;
			}
		}
	};

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			writeItem = itemList.get(position);
			if (writeItem.getItemKey().equals("stuname")) {
				return;
			}
			bundle.putInt("position2", position);
			if (writeItem.getItemKey().equals("sfz")) {
				intPopuWindows();
				popupWindow.showAtLocation(FormWriteActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				return;
			}

			startActivityToNext(bundle);
		}
	};

	private void intPopuWindows() {
		int[] listennerId = new int[] {
				R.id.sfz_type,
				R.id.other_type,
				R.id.cancle };
		popupWindow = new MyPopupWindow(this, R.layout.layout_sfz_popu, listennerId);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnPopClickListenner(onClickListener);
	}

	private void startActivityToNext(Bundle bundle) {
		Intent intent = new Intent(FormWriteActivity.this, FormWriteItemActivity.class);
		intent.putExtras(bundle);
		FormWriteActivity.this.startActivity(intent);
	}
}
