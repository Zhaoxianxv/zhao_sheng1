/**
 * 
 */
package com.yfy.exafunction;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;

/**
 * @author yfy
 * @date 2016-5-1
 * @version 1.0
 * @description FunctionProx
 */
public class FunctionProx implements AddFunction {

	private final HashSet<AddFunction> mAddFunctions;

	public FunctionProx() {
		mAddFunctions = new HashSet<AddFunction>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onCreate(savedInstanceState);
		}
	}

	@Override
	public void onStart() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onStart();
		}
	}

	@Override
	public void onRestart() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onRestart();
		}
	}

	@Override
	public void onResume() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onResume();
		}
	}

	@Override
	public void onPause() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onPause();
		}
	}

	@Override
	public void onStop() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onStop();
		}
	}

	@Override
	public void onDestroy() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onDestroy();
		}
	}

	@Override
	public boolean isLeagal() {
		for (AddFunction mAddFunction : mAddFunctions) {
			if (!mAddFunction.isLeagal()) {
				return false;
			}
		}
		return true;
	}

	public void addFunction(AddFunction addFunction) {
		if (mAddFunctions != null) {
			mAddFunctions.add(addFunction);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onAttach(activity);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = null;
		for (AddFunction mAddFunction : mAddFunctions) {
			view = mAddFunction.onCreateView(inflater, container,
					savedInstanceState);
		}
		return view;
	}

	@Override
	public void onActivityCreate(Bundle savedInstanceState) {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onActivityCreate(savedInstanceState);
		}
	}

	@Override
	public void onDestroyView() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onDestroyView();
		}
	}

	@Override
	public void onDetach() {
		for (AddFunction mAddFunction : mAddFunctions) {
			mAddFunction.onDetach();
		}
	}
}
