/**
 *
 */
package com.yfy.app.pro;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.greendao.User;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;

/**
 * @author yfy1
 * @Date 2016年1月13日
 * @version 1.0
 * @Desprition
 */
public class AddSupplyActivity extends WcfActivity implements OnClickListener {

	// string addsupplies(string session_key, string name, string bz);

	private final static String TAG = AddSupplyActivity.class.getSimpleName();

	private TextView headTitle;
	private TextView add_tv;
	private EditText supply_name_et;
	private EditText remark_et;

	private LoadingDialog loadingDialog;

	private final String method = "addsupplies";
	private final User userInfo = Base.user;
	private String supplyName = "";
	private String remark = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_supply);
		initAll();
	}

	private void initAll() {
		initViews();
	}

	private void initViews() {
		headTitle = (TextView) findViewById(R.id.head_title);
		add_tv = (TextView) findViewById(R.id.right_tv);
		supply_name_et = (EditText) findViewById(R.id.supply_name);
		remark_et = (EditText) findViewById(R.id.remark_et);
		add_tv.setText("完成");

		setOnClickListener(mActivity, R.id.left_rela);
		setOnClickListener(mActivity, add_tv);


		headTitle.setVisibility(View.VISIBLE);
		headTitle.setText("种类添加");

		loadingDialog = new LoadingDialog(this);
	}

	private boolean isCanUpload() {
		supplyName = supply_name_et.getText().toString().trim();
		remark = remark_et.getText().toString().trim();
		if (TextUtils.isEmpty(supplyName)) {
			toastShow("名称不能为空");
			return false;
		}
		return true;
	}

	private void addSupply() {
		Object[] params = new Object[] { userInfo.getSession_key(), supplyName,
				remark };
		ParamsTask addTask = new ParamsTask(params, method, loadingDialog);
		execute(addTask);
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
		toastShow("网络异常,添加失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_rela:
				finish();
				break;
			case R.id.right_tv:
				if (isCanUpload()) {
					addSupply();
				}
				break;
		}
	}
}
