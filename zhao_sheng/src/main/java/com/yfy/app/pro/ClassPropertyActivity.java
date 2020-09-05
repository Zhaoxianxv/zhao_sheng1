/**
 * 
 */
package com.yfy.app.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;

import com.yfy.app.pro.adapter.ClassSupplyAdapter;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.beans.Property;
import com.yfy.greendao.User;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;

import java.util.ArrayList;
import java.util.List;


/**
 * @version 1.0
 * @Desprition
 */
public class ClassPropertyActivity extends WcfActivity implements OnClickListener {

	private final static String TAG = "zxx";

	private RelativeLayout right_rela;
	private TextView head_title;
	private ImageView add_iv;
	private TextView empty_tv;

	private XListView xlist;
	private ClassSupplyAdapter adapter;
	private List<Property> propertyList = new ArrayList<Property>();

	private final String method = "getxclist";
	private final String deleteMethod = "delxclist";
	private User userInfo = Base.user;
	private ParamsTask dataTask;

	private String classId;
	private String title;

	private boolean isTasking = false;
	private int mPositionCur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_class_supply);
		initAll();
	}

	private void initAll() {
		getDataFromIntent();
		initViews();
		initTask();
	}



	private void getDataFromIntent() {
		Bundle b = getIntent().getExtras();
		classId = b.getString("classId");
		title = b.getString("title");
	}

	private void initViews() {
		right_rela = (RelativeLayout) findViewById(R.id.right_rela);
		empty_tv = (TextView) findViewById(R.id.empty_tv);
		head_title = (TextView) findViewById(R.id.head_title);
		head_title.setText(title);
		head_title.setVisibility(View.VISIBLE);

		xlist = (XListView) findViewById(R.id.refresh_lv);

		adapter = new ClassSupplyAdapter(this, propertyList);



		setOnClickListener(mActivity, R.id.left_rela);
	}

	private void initTask() {
		Object[] params = new Object[] { userInfo.getSession_key(), classId };
		dataTask = new ParamsTask(params, method, "dataTask");
	}

	private void getDataFromNet() {
		if (isTasking) {
			return;
		}
		isTasking = true;
		execute(dataTask);
	}

	private void deleteSupply() {
		if (isTasking) {
			return;
		}
		isTasking = true;
		Object[] params = new Object[] { userInfo.getSession_key(),
				propertyList.get(mPositionCur).getId() };
		ParamsTask deleteTask = new ParamsTask(params, deleteMethod,
				"deleteTask");
		execute(deleteTask);
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {

		}
	};

	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {
		Logger.e( result);
		isTasking = false;
		String taskName = wcfTask.getName();
		if (taskName.equals("dataTask")) {
			propertyList = JsonParser.getPropertyList(result);
			adapter.notifyDataSetChanged(propertyList);

			if (propertyList.size() == 0) {
				empty_tv.setVisibility(View.VISIBLE);
			} else {
				empty_tv.setVisibility(View.GONE);
			}
		} else if (taskName.equals("deleteTask")) {
			propertyList.remove(mPositionCur);
			adapter.notifyDataSetChanged(propertyList);
		}
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {
		isTasking = false;
		toastShow(getString(R.string.request_failed));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_rela:
			finish();
			break;
		case R.id.right_rela:
			Intent intent = new Intent(this, AddPropertyActivity.class);
			Bundle b = new Bundle();
			b.putString("classId", classId);
			intent.putExtras(b);
			startActivityForResult(intent, 1);
			break;
		}
	}
}
