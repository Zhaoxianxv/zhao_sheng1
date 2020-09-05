/**
 *
 */
package com.yfy.adapter.base;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author yfy1
 * @Date 2015年12月21日
 * @version 1.0
 * @Desprition
 */
public abstract class AbstractAdapter3<TItem> extends BaseAdapter {

	protected Context context;
	protected List<TItem> list;
	protected LayoutInflater inflater;

	private ResInfo resInfo;

	public AbstractAdapter3(Context context, List<TItem> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		init();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int position2 = position;
		final DataViewHolder holder;
		if (convertView == null) {
			holder = new DataViewHolder();
			convertView = inflater.inflate(resInfo.layout, null);
			for (int id : resInfo.initIds) {
				holder.setView(id, convertView.findViewById(id)); // 资源id作为key,缓存界面中的组件
			}
			convertView.setTag(holder);
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
								   AbstractAdapter3<TItem> adapter, DataViewHolder holder);
	}

	public void onInnerClick(View v, int position, List<TItem> list,
							 DataViewHolder holder) {
		if (listenner != null) {
			listenner.onAdapterClick(v, position, list, this, holder);
		}
	}

	public abstract void renderData(int position, DataViewHolder holder,
									List<TItem> list);

	public abstract ResInfo getResInfo();

	public static class ResInfo {
		public int[] initIds;
		public int[] listnnerIds;
		public int layout;
	}

	@SuppressLint("UseSparseArrays")
	public static class DataViewHolder {

		HashMap<Integer, View> mapView = new HashMap<Integer, View>();

		public void setView(int key, View v) {
			this.mapView.put(key, v);
		}

		@SuppressWarnings("unchecked")
		public <T> T getView(int key) {
			return (T) this.mapView.get(key);
		}

		@SuppressWarnings("unchecked")
		public <T> T getView(Class<T> clazz, int key) {
			return (T) this.mapView.get(key);
		}

	}

	public void notifyDataSetChanged(List<TItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}
