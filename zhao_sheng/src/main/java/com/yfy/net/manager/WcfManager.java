/**
 * 
 */
package com.yfy.net.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.Dialog;
import com.yfy.net.loading.interf.Indicator;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.single.SingleArrayDeque;
import com.yfy.net.single.SingleCallable;
import com.yfy.net.single.SingleFutureTask;
import com.yfy.net.single.SingleRunnable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class WcfManager {

	// private static final String LOG_TAG = WcfManager.class.getSimpleName();
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_POOL_SIZE = 128;
	private static final int KEEP_ALIVE = 1;

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "WcfManager #" + mCount.getAndIncrement());
		}
	};

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);

	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sPoolWorkQueue, sThreadFactory);

	public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

	private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;

	private final InternalHandler sHandler = new InternalHandler();

	private static class SerialExecutor implements Executor {
		@SuppressLint("NewApi")
		final SingleArrayDeque<Runnable> mTasks = new SingleArrayDeque<Runnable>();
		Runnable mActive;

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@SuppressLint("NewApi")
		public synchronized void execute(final Runnable r) {
			mTasks.offer(new SingleRunnable(r) {
				public void run() {
					try {
						r.run();
					} finally {
						scheduleNext();
					}
				}
			});
			if (mActive == null) {
				scheduleNext();
			}
		}

		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		protected synchronized void scheduleNext() {
			if ((mActive = mTasks.poll()) != null) {
				THREAD_POOL_EXECUTOR.execute(mActive);
			}
		}
	}

	public static void setDefaultExecutor(Executor exec) {
		sDefaultExecutor = exec;
	}

	public static void resetExecutor() {
		sDefaultExecutor = SERIAL_EXECUTOR;
	}

	public WcfManager(OnWcfTaskListener onWcfTaskListener) {
		sHandler.setOnWcfTaskListener(onWcfTaskListener);
	}

	public void execute(final WcfTask wcfTask) {
		if (wcfTask == null) {
			return;
		}

		Callable<String> task = new SingleCallable<String>(wcfTask) {

			@Override
			public String call() throws Exception {
				postResult(Status.BEGIN, wcfTask);
				String result="wcf_error";
				try {
					result = wcfTask.call();
				} catch (Exception e) {
					Logger.e("赵");
				}
				wcfTask.setResult(result);
				postResultIfNotInvoked(wcfTask);
				return result;
			}

		};

		FutureTask<String> mFuture = new SingleFutureTask<String>(task);

		sDefaultExecutor.execute(mFuture);

	}

	private void postResultIfNotInvoked(final WcfTask wcfTask) {
		if (wcfTask == null) {
			return;
		}
		final String result = wcfTask.getResult();

		if (result.equals("wcf_error")) {
			postResult(Status.ERROR, wcfTask);
			return;
		}

		postResult(Status.SUCCESS, wcfTask);

	}

	private synchronized void postResult(Status status, WcfTask wcfTask) {
		Message message = sHandler.obtainMessage(status.value, wcfTask);
		message.sendToTarget();
	}

	public enum Status {

		BEGIN(0x1), SUCCESS(0x2), ERROR(0x3);

		private int value = 0;

		private Status(int value) {
			this.value = value;
		}

		public static Status valueOf(int value) {
			switch (value) {
			case 0x1:
				return BEGIN;
			case 0x2:
				return SUCCESS;
			case 0x3:
				return ERROR;
			}
			return null;
		}

		public int value() {
			return this.value;
		}
	}

	private static class InternalHandler extends Handler {

		private OnWcfTaskListener onWcfTaskListener;

		public InternalHandler() {
			super(Looper.getMainLooper());
		}

		@Override
		public void handleMessage(Message msg) {
			WcfTask wcfTask = (WcfTask) msg.obj;
			Indicator indicator = wcfTask.getIndicator();
			Dialog dialog = wcfTask.getDialog();
			Status status = Status.valueOf(msg.what);
			switch (status) {
			case BEGIN:
				if (indicator != null) {
					indicator.show();
					indicator.loading();
				} else if (dialog != null) {
					dialog.show();
				}
				break;
			case SUCCESS:
				if (onWcfTaskListener == null) {
					return;
				}
				boolean isEmpty = onWcfTaskListener.onSuccess(wcfTask.getResult(), wcfTask);

				if (dialog != null) {
					dialog.dismiss();
				}
				if (indicator == null) {
					break;
				}
				if (isEmpty) {
					indicator.emptyResult();
				} else {
					indicator.dismiss();
					indicator.valueResult();
				}

				break;
			case ERROR:
				if (wcfTask.getResult().equals("wcf_error")) {
					if (onWcfTaskListener != null) {
						onWcfTaskListener.onError(wcfTask);
					}
				}
				if (indicator != null) {
					indicator.dismiss();
					indicator.fail();
				} else if (dialog != null) {
					dialog.dismiss();
				}
				break;
			default:
				break;
			}
		}

		public void removeListener() {
			onWcfTaskListener = null;
		}

		public void setOnWcfTaskListener(OnWcfTaskListener onWcfTaskListener) {
			this.onWcfTaskListener = onWcfTaskListener;
		}
	}

	public void setOnWcfTaskListener(OnWcfTaskListener onWcfTaskListener) {
		sHandler.setOnWcfTaskListener(onWcfTaskListener);
	}

	public void removeAllListener() {
		sHandler.removeListener();
	}

	public static interface OnWcfTaskListener {

		public boolean onSuccess(String result, WcfTask wcfTask);

		public void onError(WcfTask wcfTask);

	}
}
