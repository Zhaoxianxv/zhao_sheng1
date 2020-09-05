package com.yfy.app.info_submit.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.yfy.app.info_submit.adapter.FormShowAdapter;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.yfy.app.info_submit.view.HeaderMenuIndicator;
import com.yfy.app.info_submit.view.MenuIndicator;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.AbstractDialog.OnCustomDialogListener;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.example.zhao_sheng.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.OnClick;

public class FormShowActivity extends WcfActivity {

	private HeaderMenuIndicator menuIndicator;

	private ListView listView;
	private FormShowAdapter adapter;

	private MyDialog pointDialog, classDialog;
	private LoadingDialog loadingDialog;

	private int submitNum = 0;
	private boolean receiveEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_form_show);
		initDialog();
		initView();
		initSQToolbar();
	}


	private void initSQToolbar(){
	    assert toolbar!=null;
	    toolbar.setTitle("");
	    toolbar.setOnNaviClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (submitNum == 0) {
					pointDialog.show();
				} else {
					finish();
				}
			}
		});
    }
	@Override
	public void onResume() {
		super.onResume();
		for (int i = 0; i < InfosConstant.totalList.size(); i++) {
			ArrayList<WriteItem> list = InfosConstant.totalList.get(i);
			WriteItem writeItem;
			int h = 0;
			for (Iterator<WriteItem> iterator = list.iterator(); iterator
					.hasNext();) {
				writeItem = iterator.next();
				if (!StringJudge.isEmpty(writeItem.getItemValue())) {
					h++;
				}
			}
			InfosConstant.COMPLETE_DEGRESS[i] = (h * 100 / list.size()) + "%";
		}

		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		submitNum = 0;
	}

	@Override
	public void finish() {
		if (pointDialog != null)
			pointDialog.dismiss();
		if (classDialog != null)
			classDialog.dismiss();
		if (loadingDialog != null)
			loadingDialog.dismiss();
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			receiveEvent = true;
			if (submitNum == 0) {
				pointDialog.show();
			} else {
				if (pointDialog.isShowing())
					pointDialog.dismiss();
				finish();
			}
		}
		return false;
	}

	protected void initDialog() {
		int[] initId = new int[] { R.id.back, R.id.submit };
		int[] listennerId = new int[] { R.id.back, R.id.submit };
		pointDialog = new MyDialog(this, R.layout.layout_dialog, initId,
				listennerId);
		pointDialog.setOnCustomDialogListener(onCustomDialogListener);
		pointDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (!receiveEvent) {
					dialog.dismiss();
				}
				receiveEvent = false;
				return false;
			}
		});

		initId = new int[] { R.id.fenban_ok, R.id.fenban_result };
		listennerId = new int[] { R.id.fenban_ok };
		classDialog = new MyDialog(this, R.layout.layout_fenban, initId, listennerId);
		classDialog.setOnCustomDialogListener(onCustomDialogListener);

		loadingDialog = new LoadingDialog(this);
	}

	private void initView() {

		item_head_lila = (LinearLayout) findViewById(R.id.item_head_lila);
		form_item_title = (TextView) findViewById(R.id.form_item_title);
		complete_degress = (TextView) findViewById(R.id.complete_degree);

		menuIndicator = (HeaderMenuIndicator) findViewById(R.id.menuIndicator);
		menuIndicator.initIndicatorView();

		menuIndicator.setOnItemclickListener(new MenuIndicator.OnItemclickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case 1:
					classDialog.show();
					classDialog.getView(TextView.class, R.id.fenban_result).setText(InfosConstant.grade);
					break;
				}
			}
		});
		listView = (ListView) findViewById(R.id.form_lv);
		FrameLayout f = new FrameLayout(this);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) (43 *  mDensity));
		f.setLayoutParams(params);
//		listView.addFooterView(f);

		adapter = new FormShowAdapter(this, InfosConstant.totalList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(onItemClickListener);
		listView.setOnScrollListener(OnScrollListener);
	}
//

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> container, View view, int position, long id) {
			Intent intent = new Intent(FormShowActivity.this, FormWriteActivity.class);
			Bundle b = new Bundle();
			b.putInt("position1", position);
			intent.putExtras(b);
			startActivity(intent);
		}
	};

	private LinearLayout item_head_lila;
	private TextView form_item_title;
	private TextView complete_degress;
	private LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT);
	private OnScrollListener OnScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@SuppressLint("NewApi")
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			View v = view.getChildAt(0);
			if (v == null) {
				return;
			}
			int dex = v.getBottom() - item_head_lila.getHeight();

			if (dex <= 0) {
				params.topMargin = dex;
				item_head_lila.setLayoutParams(params);
				form_item_title.setText(InfosConstant.SUBMIT_ITEM_PARENT[firstVisibleItem]);
				complete_degress.setText(InfosConstant.COMPLETE_DEGRESS[firstVisibleItem]);

			} else {
				params.topMargin = 0;
				item_head_lila.setLayoutParams(params);
				form_item_title
						.setText(InfosConstant.SUBMIT_ITEM_PARENT[firstVisibleItem]);
				complete_degress
						.setText(InfosConstant.COMPLETE_DEGRESS[firstVisibleItem]);
			}
		}
	};

	private OnCustomDialogListener onCustomDialogListener = new OnCustomDialogListener() {

		@Override
		public void onClick(View v, AbstractDialog myDialog) {
			switch (v.getId()) {
				case R.id.back:
					pointDialog.dismiss();
					finish();
					break;

				case R.id.submit:
					pointDialog.dismiss();
					submitInfo();

					break;

				case R.id.fenban_ok:
					menuIndicator.selectItem(0);
					classDialog.dismiss();
					break;
			}
		}
	};


	@OnClick(R.id.submit)
	void setSub(){
		submitInfo();
	}



	private void submitInfo() {
		if (LegalityJudger.can_submit(mActivity, InfosConstant.totalList)) {
			for (Iterator<ArrayList<WriteItem>> iterator = InfosConstant.totalList.iterator(); iterator.hasNext();) {
				ArrayList<WriteItem> itemList = iterator.next();
				for (Iterator<WriteItem> iterator2 = itemList.iterator(); iterator2.hasNext();) {
					WriteItem writeItem = iterator2.next();
					InfosConstant.paramsMap.put(writeItem.getItemKey(), writeItem.getItemValue());
				}
			}
			submit();
		}
	}



	private void submit(){
		HashMap<String, String> map = InfosConstant.paramsMap;
		Object[] params = {
				InfosConstant.stuId,
				map.get("birthday"),
				map.get("byyey"),
				map.get("dscz"),
				map.get("fkszd"),
				map.get("fqdh"),
				map.get("fqxm"),
				map.get("fqzd"),
				map.get("fqdw"),
				map.get("fqsj"),
				map.get("fqzw"),
				map.get("mqdh"),
				map.get("mqxm"),
				map.get("mqzd"),
				map.get("mqdw"),
				map.get("mqsj"),
				map.get("mqzw"),
				map.get("ljsw"),
				map.get("qt"),
				map.get("sex"),
				map.get("sg"),
				map.get("stuname"),
				map.get("szdz"),
				map.get("ydjn"),
				map.get("yscn"),
				map.get("yybd"),
				map.get("tz"),
				map.get("sfz")
		};
		ParamsTask sub=new ParamsTask(params,TagFinal.AUTHEN_SET_STU,TagFinal.AUTHEN_SET_STU);
		showProgressDialog("");
		execute(sub);

	}


	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {
		dismissProgressDialog();
		String name=wcfTask.getName();
		if (name.equals(TagFinal.AUTHEN_SET_STU)){
			if (result.equals(TagFinal.FALSE)){
				Toast.makeText(mActivity, "提交失败", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mActivity, "提交成功", Toast.LENGTH_SHORT).show();
			}
		}

		submitNum++;
		onPageBack();
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {
		dismissProgressDialog();
		toastShow(R.string.fail_do_not);
	}
}
