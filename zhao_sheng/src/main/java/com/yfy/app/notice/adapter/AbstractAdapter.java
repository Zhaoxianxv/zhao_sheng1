/**
 * 
 */
package com.yfy.app.notice.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public abstract class AbstractAdapter<TItem> extends BaseAdapter {

	protected Context context;
	protected List<TItem> list;
	protected LayoutInflater inflater;

	private ResInfo resInfo;

	public AbstractAdapter(Context context, List<TItem> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		init();
	}

	public void setDataList(List<TItem> list) {
		this.list = list;
	}

	private void init() {
		resInfo = getResInfo();
		if (resInfo != null) {
			if (resInfo.initIds == null)
				resInfo.initIds = new int[] {};
			if (resInfo.listnnerIds == null)
				resInfo.listnnerIds = new int[] {};
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int position2 = position;
		final DataViewHolder holder;
		if (convertView == null) {
			holder = new DataViewHolder();
			convertView = inflater.inflate(resInfo.layout, null);
			for (int id : resInfo.initIds) {
				holder.setView(id, convertView.findViewById(id)); //
			}
			convertView.setTag(holder);
			onceInit(position, holder, list);
		} else {
			holder = (DataViewHolder) convertView.getTag();
		}

		for (int id : resInfo.listnnerIds) {
			holder.getView(View.class, id).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							onInnerClick(v, position2, list, holder);
						}
					});
		}
		renderData(position, holder, list);
		return convertView;
	}

	private OnAdapterListenner<TItem> listenner = null;

	public void setOnAdapterListenner(OnAdapterListenner<TItem> listenner) {
		this.listenner = listenner;
	}

	public static interface OnAdapterListenner<TItem> {
		public void onAdapterClick(View v, int position, List<TItem> list,
								   AbstractAdapter<TItem> adapter,
								   AbstractAdapter<TItem>.DataViewHolder holder);
	}

	public void onInnerClick(View v, int position, List<TItem> list,
			DataViewHolder holder) {
		if (listenner != null) {
			listenner.onAdapterClick(v, position, list, this, holder);
		}
	}

	protected void onceInit(int position, DataViewHolder holder,
			List<TItem> list) {

	}

	public abstract void renderData(int position, DataViewHolder holder,
			List<TItem> list);

	public abstract com.yfy.app.notice.adapter.AbstractAdapter.ResInfo getResInfo();

	public static class ResInfo {
		public int[] initIds;
		public int[] listnnerIds;
		public int layout;
	}

	public class DataViewHolder {

		SparseArray<Object> objectMaps = new SparseArray<Object>();

		public void setView(int key, View v) {
			setObject(key, v);
		}

		public void setObject(int key, Object value) {
			objectMaps.put(key, value);
		}

		@SuppressWarnings("unchecked")
		public <T> T getView(Class<T> clazz, int key) {
			return (T) this.objectMaps.get(key);
		}

		public boolean setText(int resId, String text) {
			View view = getView(View.class, resId);
			if (view != null && view instanceof TextView) {
				((TextView) view).setText(text);
				return true;
			}
			return false;
		}

		public boolean setVisible(int resId, int visiblity) {
			View view = getView(View.class, resId);
			if (view instanceof View) {
				if (visiblity != view.getVisibility()) {
					view.setVisibility(visiblity);
				}
				return true;
			}
			return false;
		}
	}

	public void notifyDataSetChanged(List<TItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}
