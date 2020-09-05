package com.yfy.app.info_submit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;

public abstract class AbstractAdapter<TItem> extends BaseAdapter {

	protected Context context;
	protected List<TItem> list;
	protected LayoutInflater inflater;

	private int[] idAry;
	private int[] listnnerId;

	public AbstractAdapter(Context context, List<TItem> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);

		idAry = this.getFindViewByIDs();
		if (idAry == null)
			idAry = new int[] {};

		listnnerId = this.setListennerId();
		if (listnnerId == null)
			listnnerId = new int[] {};
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

	@SuppressWarnings({ "unchecked" })
	@Override
	public View getView(int position, View view, ViewGroup container) {
		final int position2 = position;
		DataViewHolder holder = null;
		if (view == null) {
			holder = new DataViewHolder();
			view = inflater.inflate(getLayoutId(), null);
			for (int id : idAry) {
				holder.setView(id, view.findViewById(id));
			}
			view.setTag(holder);
		} else {
			holder = (DataViewHolder) view.getTag();
		}

		for (int id : listnnerId) {
			holder.getView(View.class, id).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							onViewclick(v, position2, list);
						}
					});
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

	public abstract void onViewclick(View v, int position, List<TItem> list);

	public abstract int[] setListennerId();

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
