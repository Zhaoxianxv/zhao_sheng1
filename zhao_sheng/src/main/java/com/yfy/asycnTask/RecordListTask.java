/**
 *
 */
package com.yfy.asycnTask;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Handler;

import com.yfy.beans.Record;
import com.yfy.final_tag.MusicSearcher;
import com.yfy.final_tag.Logger;

/**
 * @author yfy1
 * @Date 2015年12月28日
 * @version 1.0
 * @Desprition
 */
public class RecordListTask implements MediaScannerConnectionClient {
	private final static String TAG = RecordListTask.class.getSimpleName();

	private String path;
	private Context context;
	private MediaScannerConnection scanner;

	private OnFileListListener onFileListListener;

	private Handler mHandler = new Handler();

	public RecordListTask(String path, Context context) {
		this.path = path;
		this.context = context;
		scanner = new MediaScannerConnection(context.getApplicationContext(),
				this);
	}

	public void startScan() {
		scanner.connect();
	}

	@Override
	public void onMediaScannerConnected() {
		File[] files = new File(path).listFiles();

		if (files != null) {
			for (File file : files) {
				Logger.e(TAG, file.getPath());
				scanner.scanFile(file.getPath(), null);
			}
		}
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		scanner.disconnect();
		final List<Record> records = MusicSearcher.getInstance(context)
				.getRecord(path);

		for (Record record : records) {
			record.repair();
		}

		mHandler.post(new Runnable() {

			@Override
			public void run() {
				if (onFileListListener != null) {
					onFileListListener.OnFind(records);
				}
			}
		});

	}

	public static interface OnFileListListener {
		public void OnFind(List<Record> records);
	}

	public void setOnFileFindListener(OnFileListListener onFileListListener) {
		this.onFileListListener = onFileListListener;
	}
}
