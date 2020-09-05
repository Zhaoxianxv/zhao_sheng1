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
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbstractAdapter2<TItem> extends BaseAdapter {

	protected Context context;
	protected List<TItem> list;
	protected LayoutInflater inflater;

	private int[] idAry;

	public AbstractAdapter2(Context context, List<TItem> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);

		idAry = this.getFindViewByIDs();
		if (idAry == null)
			idAry = new int[] {};

	}

	public abstract int[] getFindViewByIDs();

	public abstract int getLayoutId();

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
	public View getView(int position, View view, ViewGroup container) {
		DataViewHolder holder = null;
		if (view == null) {
			holder = new DataViewHolder();
			view = inflater.inflate(getLayoutId(), null);
			for (int id : idAry) {
				holder.setView(id, view.findViewById(id)); // 资源id作为key,缓存界面中的组件
			}
			view.setTag(holder);
		} else {
			holder = (DataViewHolder) view.getTag();
		}
		renderData(position, holder, list);
		return view;
	}

	public void notifyDataSetChanged(List<TItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public List<TItem> getListData() {
		return list;
	}

	public abstract void renderData(int position, DataViewHolder holder,
									List<TItem> list);

	@SuppressLint("UseSparseArrays")
	public class DataViewHolder {

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
}
