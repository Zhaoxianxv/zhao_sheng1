/**
 *
 */
package com.yfy.adapter.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.yfy.base.ChildCount;

/**
 * @author yfy1
 * @Date 2015年12月16日
 * @version 1.0
 * @Desprition
 */
public abstract class AbstractExpandableListAdapter<TItem extends ChildCount>
		extends BaseExpandableListAdapter {
	private final static String TAG = AbstractExpandableListAdapter.class
			.getSimpleName();

	protected Context context;
	protected List<TItem> list;
	protected LayoutInflater inflater;

	private ResInfo resInfo;

	private Map<View, Integer> containerMap = new HashMap<View, Integer>();

	public AbstractExpandableListAdapter(Context context, List<TItem> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		resInfo = getResInfo();
		if (resInfo != null) {
			if (resInfo.groupIds == null)
				resInfo.groupIds = new int[] {};
			if (resInfo.childIds == null)
				resInfo.childIds = new int[] {};
			if (resInfo.groupListnnerIds == null)
				resInfo.groupListnnerIds = new int[] {};
			if (resInfo.childListnnerIds == null)
				resInfo.childListnnerIds = new int[] {};
		}
	}

	public abstract ResInfo getResInfo();

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return list.get(groupPosition).getChildCount();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).getChild(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		final int position2 = groupPosition;
		final DataViewHolder holder;
		if (convertView == null) {
			holder = new DataViewHolder();
			convertView = inflater.inflate(resInfo.groupLayout, null);
			for (int id : resInfo.groupIds) {
				holder.setView(id, convertView.findViewById(id)); // 资源id作为key,缓存界面中的组件
			}
			convertView.setTag(holder);
		} else {
			holder = (DataViewHolder) convertView.getTag();
		}
		containerMap.put(convertView, groupPosition);

		for (int id : resInfo.groupListnnerIds) {
			holder.getView(View.class, id).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							onInnerGroupClick(v, position2, list, holder);
						}
					});
		}
		renderGroupData(groupPosition, holder, list, isExpanded);
		return convertView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		final int position2 = groupPosition;
		final int position3 = childPosition;
		final DataViewHolder holder;
		if (convertView == null) {
			holder = new DataViewHolder();
			convertView = inflater.inflate(resInfo.childLayout, null);
			for (int id : resInfo.childIds) {
				holder.setView(id, convertView.findViewById(id)); // 资源id作为key,缓存界面中的组件
			}
			convertView.setTag(holder);
		} else {
			holder = (DataViewHolder) convertView.getTag();
		}

		for (int id : resInfo.childListnnerIds) {
			holder.getView(View.class, id).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							onInnerChildClick(v, position2, position3, list,
									holder);
						}
					});
		}
		renderChildData(groupPosition, childPosition, holder, list);
		return convertView;
	}

	public abstract void renderChildData(int groupPosition, int childPosition,
										 DataViewHolder holder, List<TItem> list);

	public abstract void renderGroupData(int groupPosition,
										 DataViewHolder holder, List<TItem> list, boolean isExpand);

	public void onInnerGroupClick(View v, int groupPosition, List<TItem> list,
								  DataViewHolder holder) {
		if (listenner != null) {
			listenner.onGroupClick(v, groupPosition, list, this, holder);
		}
	}

	public void onInnerChildClick(View v, int groupPosition, int childPosition,
								  List<TItem> list, DataViewHolder holder) {
		if (listenner != null) {
			listenner.OnChildClick(v, groupPosition, childPosition, list, this,
					holder);
		}
	}

	private OnAdapterListenner<TItem> listenner = null;

	public void setOnAdapterListenner(OnAdapterListenner<TItem> listenner) {
		this.listenner = listenner;
	}

	public static interface OnAdapterListenner<TItem extends ChildCount> {
		public void onGroupClick(View v, int groupPosition, List<TItem> list,
								 AbstractExpandableListAdapter<TItem> adapter,
								 DataViewHolder holder);

		public void OnChildClick(View v, int groupPosition, int childPosition,
								 List<TItem> list, AbstractExpandableListAdapter<TItem> adapter,
								 DataViewHolder holder);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public View getContainerView(int groupPosition) {
		View view = null;
		for (Entry<View, Integer> entry : containerMap.entrySet()) {
			if (entry.getValue() == groupPosition) {
				return entry.getKey();
			}
		}
		return view;
	}

	public List<TItem> getList() {
		return list;
	}

	public void notifyDataSetChanged(List<TItem> list) {
		this.list = list;
		super.notifyDataSetChanged();
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

	public static class ResInfo {
		public int[] groupIds;
		public int[] childIds;
		public int[] groupListnnerIds;
		public int[] childListnnerIds;
		public int groupLayout;
		public int childLayout;
	}

}
