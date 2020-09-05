/**
 * 
 */
package com.yfy.net.loading.task;


import android.util.SparseArray;

import com.yfy.net.SoapAccessor;
import com.yfy.net.loading.interf.Dialog;
import com.yfy.net.loading.interf.Indicator;
import com.yfy.net.loading.interf.WcfTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class ParamsTask implements WcfTask {

	public final static String ERROR = "wcf_error";

	private Object[] params;
	private int id;
	private String method;
	private String name;
	private String result;
	private Indicator indicator;
	private Dialog loadingDialog;
	private SoapAccessor accessor = SoapAccessor.getInstance();
	private SparseArray<Object> sparseObject = new SparseArray<Object>();

	public ParamsTask(Object[] params, String method) {
		this.params = params;
		this.method = method;
	}

	public ParamsTask(Object[] params, String method, String name) {
		this.params = params;
		this.method = method;
		this.name = name;
	}

	public ParamsTask(Object[] params, String method, int id) {
		this.params = params;
		this.method = method;
		this.id = id;
	}

	public ParamsTask(Object[] params, String method, Dialog loadingDialog) {
		this.params = params;
		this.method = method;
		this.loadingDialog = loadingDialog;
	}

	public ParamsTask(Object[] params, String method, String name,
					  Indicator indicator) {
		this.params = params;
		this.method = method;
		this.name = name;
		this.indicator = indicator;
	}

	public ParamsTask(Object[] params, String method, String name,
					  Dialog loadingDialog) {
		this.params = params;
		this.method = method;
		this.name = name;
		this.loadingDialog = loadingDialog;
	}

	public ParamsTask(Object[] params, String method, int id,
					  Dialog loadingDialog) {
		this.params = params;
		this.method = method;
		this.id = id;
		this.loadingDialog = loadingDialog;
	}

	public ParamsTask(Object[] params, String method, String name, int id,
					  Dialog loadingDialog) {
		this.params = params;
		this.method = method;
		this.name = name;
		this.id = id;
		this.loadingDialog = loadingDialog;
	}

	@Override
	public String call() {
		String result = ERROR;
		try {
			result = accessor.loadResult(params, method);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResult(result);
		return result;
	}

	@Override
	public Indicator getIndicator() {
		return indicator;
	}

	@Override
	public String getResult() {
		return result;
	}

	@Override
	public void setResult(String result) {
		this.result = result;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Dialog getDialog() {
		return loadingDialog;
	}

	@Override
	public Object[] getParams() {
		return params;
	}

	public void setTag(Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			sparseObject.put(i, objects[i]);
		}
	}

	public Object getTag() {
		return getTag(0);
	}

	public Object getTag(int key) {
		return sparseObject.get(key);
	}

}
