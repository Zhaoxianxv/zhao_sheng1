/**
 *
 */
package com.yfy.app.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.pro.adapter.PropertyClassAdapter;
import com.yfy.base.activity.WcfActivity;
import com.yfy.beans.SchoolClass;
import com.yfy.beans.SchoolGrade;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yfy1
 * @Date 2016年1月12日
 * @version 1.0
 * @Desprition
 */
public class PropertyActivity extends WcfActivity implements OnClickListener {
	private final static String TAG = PropertyActivity.class.getSimpleName();

	private TextView head_title;
	private LinearLayout float_view;
	private TextView group_tv;

	private ExpandableListView expandListView;
	private PropertyClassAdapter adapter;
	private List<SchoolGrade> schoolGradeList = new ArrayList<SchoolGrade>();

	private final String no = "0";
	private final String method = "getbj";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property);
		initAll();
	}

	private void initAll() {
		initViews();
		renderData();
	}

	private void initViews() {
		head_title = (TextView) findViewById(R.id.head_title);
		float_view = (LinearLayout) findViewById(R.id.float_view);
		group_tv = (TextView) findViewById(R.id.grade_name);

		head_title.setVisibility(View.VISIBLE);
		head_title.setText("校产");
		setOnClickListener(mActivity, R.id.left_rela);
		setOnClickListener(mActivity, float_view);

		expandListView = (ExpandableListView) findViewById(R.id.expandListView);
		expandListView.setOnScrollListener(onScrollListener);
		expandListView.setOnChildClickListener(onChildClickListener);
		expandListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
										int groupPosition, long id) {
				return true;
			}
		});

		adapter = new PropertyClassAdapter(this, schoolGradeList);
		expandListView.setAdapter(adapter);
	}

	private void renderData() {
		if (StringJudge.isEmpty(schoolGradeList)) {
			getDataFromNet();
		} else {

			adapter.notifyDataSetChanged(schoolGradeList);
			expandAll(expandListView, adapter);
		}
	}

	private void getDataFromNet() {
		Object[] params = new Object[] { no };
		ParamsTask dataTask = new ParamsTask(params, method);
		execute(dataTask);
	}

	private OnChildClickListener onChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
									int groupPosition, int childPosition, long id) {

			SchoolGrade schoolGrade = schoolGradeList.get(groupPosition);
			SchoolClass schoolClass = schoolGrade.getSchoolClassList().get(
					childPosition);

			Intent intent = new Intent(PropertyActivity.this, ClassPropertyActivity.class);
			Bundle b = new Bundle();
			b.putString("classId", schoolClass.getClassid());
			b.putString("title",
					schoolGrade.getGradename() + schoolClass.getClassname());
			intent.putExtras(b);
			startActivity(intent);
			return false;
		}
	};

	private int indicatorGroupHeight;
	private int indicatorGroupId = -1;
	private OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
							 int visibleItemCount, int totalItemCount) {

			final ExpandableListView listView = (ExpandableListView) view;

			int npos = view.pointToPosition(0, 0);
			if (npos == AdapterView.INVALID_POSITION)
				return;

			long pos = listView.getExpandableListPosition(npos);
			int childPos = ExpandableListView.getPackedPositionChild(pos);
			int groupPos = ExpandableListView.getPackedPositionGroup(pos);

			if (childPos == AdapterView.INVALID_POSITION) {
				Logger.e(TAG, "childPos == AdapterView.INVALID_POSITION");
				View groupView = listView.getChildAt(npos
						- listView.getFirstVisiblePosition());
				indicatorGroupHeight = groupView.getHeight();
				float_view.setVisibility(View.GONE);
			} else {
				float_view.setVisibility(View.VISIBLE);
			}

			if (groupPos != AdapterView.INVALID_POSITION) {
				float_view.setVisibility(View.VISIBLE);
			}

			if (indicatorGroupHeight == 0) {
				return;
			}
			if (groupPos != indicatorGroupId) {
				group_tv.setText(schoolGradeList.get(groupPos).getGradename());
				indicatorGroupId = groupPos;
			}
			if (indicatorGroupId == -1)
				return;

			/**
			 * calculate point (0,indicatorGroupHeight) 下面是形成往上推出的效果
			 */
			int showHeight = indicatorGroupHeight;
			int nEndPos = listView.pointToPosition(0, indicatorGroupHeight);
			if (nEndPos == AdapterView.INVALID_POSITION)
				return;
			long pos2 = listView.getExpandableListPosition(nEndPos);
			int groupPos2 = ExpandableListView.getPackedPositionGroup(pos2);
			if (groupPos2 != indicatorGroupId) {
				View viewNext = listView.getChildAt(nEndPos
						- listView.getFirstVisiblePosition());
				showHeight = viewNext.getTop();
			}

			// update group position
			MarginLayoutParams layoutParams = (MarginLayoutParams) float_view
					.getLayoutParams();
			layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
			float_view.setLayoutParams(layoutParams);
		}
	};

	private void expandAll(ExpandableListView listView,
						   BaseExpandableListAdapter adpater) {
		if (adpater == null) {
			return;
		}
		int groupCount = adapter.getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			if (!listView.isGroupExpanded(i)) {
				listView.expandGroup(i);
			}
		}
	}

	@Override
	public boolean onSuccess(String result, WcfTask wcfTask) {
		Logger.e(TAG, result);
		schoolGradeList = JsonParser.getSchoolGradeList(result);

		adapter.notifyDataSetChanged(schoolGradeList);
		expandAll(expandListView, adapter);
		return false;
	}

	@Override
	public void onError(WcfTask wcfTask) {
		toastShow("网络异常,获取班级列表失败");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_rela:
				finish();
				break;
		}
	}
}
