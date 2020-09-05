package com.yfy.exafunction;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface AddFunction {

	public void onCreate(Bundle savedInstanceState);

	public void onStart();

	public void onRestart();

	public void onResume();

	public void onPause();

	public void onStop();

	public void onDestroy();

	// fragment
	public void onAttach(Activity activity);

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState);

	public void onActivityCreate(Bundle savedInstanceState);

	public void onDestroyView();

	public void onDetach();

	public boolean isLeagal();

}
