/**
 * 
 */
package com.yfy.app.pro.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.adapter.base.AbstractExpandableListAdapter;
import com.yfy.beans.SchoolGrade;

import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class PropertyClassAdapter extends
		AbstractExpandableListAdapter<SchoolGrade> {

	public PropertyClassAdapter(Context context, List<SchoolGrade> list) {
		super(context, list);
	}

	@Override
	public ResInfo getResInfo() {
		ResInfo resInfo = new ResInfo();
		resInfo.groupLayout = R.layout.property_item_group;
		resInfo.childLayout = R.layout.property_item_child;
		resInfo.groupIds = new int[] { R.id.grade_name };
		resInfo.childIds = new int[] { R.id.class_name };
		return resInfo;
	}

	@Override
	public void renderChildData(
			int groupPosition,
			int childPosition,
			DataViewHolder holder,
			List<SchoolGrade> list) {
		SchoolGrade schoolGrade = list.get(groupPosition);
		holder.getView(TextView.class, R.id.class_name).setText(
				schoolGrade.getSchoolClassList().get(childPosition)
						.getClassname());
	}

	@Override
	public void renderGroupData(
			int groupPosition,
			DataViewHolder holder,
			List<SchoolGrade> list, boolean isExpand) {
		SchoolGrade schoolGrade = list.get(groupPosition);
		holder.getView(TextView.class, R.id.grade_name).setText(schoolGrade.getGradename());
	}
}
