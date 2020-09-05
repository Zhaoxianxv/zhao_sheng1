/**
 * 
 */
package com.gy.aac;

/**
 * @author yfy1
 * @Date 20151224
 * @version 1.0
 * @Desprition
 */
public abstract class AbstractRecord implements IRecorder {

	protected State state = State.NO_ACTION;

	protected OnRecordListener listener;

	public void setRecordListener(OnRecordListener listener) {
		this.listener = listener;
	}

	public static interface OnRecordListener {
		public void onRecordProgress(long duration);

		public void onEnd(long result, State state);

	}

	public enum State {
		PAUSE, END, RUN, NO_ACTION
	}

}
