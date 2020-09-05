/**
 * 
 */
package com.yfy.net.loading.task;


import com.yfy.net.loading.interf.Dialog;
import com.yfy.net.loading.interf.Indicator;
import com.yfy.net.loading.interf.WcfTask;

/**
 * @author yfy1
 * @Date 2015��11��30��
 * @version 1.0
 * @Desprition
 */
public class ExtraRunTask implements WcfTask {

	private WcfTask wcfTask;

	private ExtraRunnable extraRunnable;

	public ExtraRunTask(WcfTask wcfTask) {
		this.wcfTask = wcfTask;
	}

	@Override
	public String call() throws Exception {
		if (extraRunnable != null) {
			extraRunnable.onExecute(wcfTask.getParams());
		}
		String result = wcfTask.call();
		return result;
	}

	@Override
	public Indicator getIndicator() {
		return wcfTask.getIndicator();
	}

	@Override
	public Dialog getDialog() {
		return wcfTask.getDialog();
	}

	@Override
	public String getResult() {
		return wcfTask.getResult();
	}

	@Override
	public void setResult(String result) {
		wcfTask.setResult(result);
	}

	@Override
	public String getName() {
		return wcfTask.getName();
	}

	public static interface ExtraRunnable {
		public Object[] onExecute(Object[] params);
	}

	public void setExtraRunnable(ExtraRunnable extraRunnable) {
		this.extraRunnable = extraRunnable;
	}

	@Override
	public Object[] getParams() {
		return wcfTask.getParams();
	}

	@Override
	public int getId() {
		return wcfTask.getId();
	}

}
