package com.yfy.exafunction;



import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.manager.WcfManager;
import com.yfy.net.manager.WcfManager.OnWcfTaskListener;


public class WcfFunction implements AddFunction {

	private WcfManager manager;
	private OnWcfTaskListener onWcfTaskListener;

	public WcfFunction(OnWcfTaskListener onWcfTaskListener) {
		this.onWcfTaskListener = onWcfTaskListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		manager = new WcfManager(onWcfTaskListener);
	}

	public void execute(WcfTask wcfTask) {
		if (manager == null) {
			manager = new WcfManager(onWcfTaskListener);
		}
		manager.execute(wcfTask);
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onRestart() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onDestroy() {
		if (manager != null) {
			manager.removeAllListener();
		}
	}

	@Override
	public boolean isLeagal() {
		return true;
	}

	@Override
	public void onDetach() {

	}

	@Override
	public void onAttach(Activity activity) {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void onActivityCreate(Bundle savedInstanceState) {

	}

	@Override
	public void onDestroyView() {

	}
}
