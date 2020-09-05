/**
 *
 */
package com.yfy.asycnTask;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.yfy.beans.Record;

/**
 * @author yfy1
 * @Date 2015年12月28日
 * @version 1.0
 * @Desprition
 */
public class RecordFindTask extends AsyncTask<Object, Object, Object> {

	private OnFileFindListener onFileFindListener;

	@Override
	protected Object doInBackground(Object... params) {
		String parentPath = (String) params[0];
		List<Record> records = new ArrayList<Record>();
		Record record=null;

		File file = new File(parentPath);
		File[] files = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String filename = pathname.getPath();
				if (pathname.isDirectory())
					return true;
				if (filename.endsWith(".aac"))
					return true;
				else
					return false;
			}
		});
		for (File file2 : files) {
			String path=file2.getPath();
//			Media
//			
//			record=new Record(Utils.getFileName2(path), path, Utils.getLastDate(path), Utils.getFileSize(path), duration)
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {

		if (onFileFindListener != null) {
			onFileFindListener.OnFind((List<Record>) result);
		}
	}

	public static interface OnFileFindListener {
		public void OnFind(List<Record> records);
	}

	public void setOnFileFindListener(OnFileFindListener onFileFindListener) {
		this.onFileFindListener = onFileFindListener;
	}
}
