/**
 * 
 */
package com.yfy.net.loading.interf;


/**
 * @author yfy
 * @date 2015-11-8
 * @version 1.0
 * @description AsychDialog
 */
public class AsycnDialog implements Dialog {

//	private final static String TAG = AsycnDialog.class.getSimpleName();

	private int num = 0;

	private Dialog dialog;

	public AsycnDialog(Dialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public synchronized void show() {
		if (num == 0) {
			dialog.show();
		}
		num++;
	}

	@Override
	public synchronized void dismiss() {
		if (num == 1) {
			dialog.dismiss();
		}
		num--;
	}
}
