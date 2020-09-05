/**
 *
 */
package com.yfy.app.pro;

import android.os.Bundle;
import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.beans.Supply;
import com.yfy.greendao.User;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfy1
 * @Date 2016年1月13日
 * @version 1.0
 * @Desprition
 */
public class SupplyTypeActivity extends WcfActivity  {

	private final static String TAG = SupplyTypeActivity.class.getSimpleName();

	private List<Supply> supplyList = new ArrayList<Supply>();

	private final String method = "getsupplieslist";
	private User userInfo = Base.user;
	private ParamsTask dataTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swip_recycler_main);
		initAll();
	}

	private void initAll() {
		initTask();

	}



	private void initTask() {
		Object[] params = new Object[] { userInfo.getSession_key() };
		dataTask = new ParamsTask(params, method, "dataTask");
	}





	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {

		String taskName = wcfTask.getName();
		if (taskName.equals("dataTask")) {
			supplyList = JsonParser.getSupplyList(result);
		}
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {
		toastShow("网络异常,请求物品列表失败");
	}


}
