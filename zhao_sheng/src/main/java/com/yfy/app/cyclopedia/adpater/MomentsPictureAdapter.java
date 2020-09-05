package com.yfy.app.cyclopedia.adpater;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.Images;
import com.yfy.base.adapter.AbstractAdapter;
import com.yfy.final_tag.ViewUtils;

import java.util.List;


public class MomentsPictureAdapter extends AbstractAdapter<Images> {

	// private final static String TAG = MomentsPictureAdapter.class
	// .getSimpleName();

	private final static int ONE_ITEM = 150;
	private final static int TWO_FOUR_ITEM = 120;

	private GridView gridView;
	private int threeWidth;
	private int itemWidth = 0;
	private int columnNum = 3;
	private Context context;

	public MomentsPictureAdapter(Context context, List<Images> list,
								 GridView gridView) {
		super(context, list);
		this.gridView = gridView;
		this.context=context;

		Resources resource = context.getResources();
		int width = resource.getDisplayMetrics().widthPixels;
		threeWidth = (width - gridView.getPaddingLeft()
				- gridView.getPaddingRight() - ViewUtils
					.getHorizontalSpacing(gridView)) / columnNum;
		initColumnNum(list);
	}

	@Override
	public void renderData(int position, DataViewHolder holder, List<Images> list) {
		ImageView picture = holder.getView(ImageView.class, R.id.picture);
		Glide.with(context).load(list.get(position)).into(picture);
		LayoutParams params = (LayoutParams) picture.getLayoutParams();
		if (params.width != itemWidth) {
			params.width = itemWidth;
			params.height = itemWidth;
			picture.requestLayout();
		}
	}

	@Override
	public ResInfo getResInfo() {
		ResInfo resInfo = new ResInfo();
		resInfo.layout = R.layout.graded_moments_pictrue_adp_item;
		resInfo.initIds = new int[] { R.id.picture };
		return resInfo;
	}

	@Override
	public void notifyDataSetChanged(List<Images> list) {
		initColumnNum(list);
		super.notifyDataSetChanged(list);
	}

	private void initColumnNum(List<Images> list) {
		float density = context.getResources().getDisplayMetrics().density;
		if (list.size() == 1) {
			itemWidth = (int) (ONE_ITEM * density);
			gridView.setNumColumns(1);
			resetGridView();
		} else if (list.size() == 2) {
			itemWidth = (int) (TWO_FOUR_ITEM * density);
			gridView.setNumColumns(2);
			LayoutParams params = gridView.getLayoutParams();
			params.width = gridView.getPaddingLeft()
					+ gridView.getPaddingRight() + 2 * itemWidth
					+ ViewUtils.getHorizontalSpacing(gridView);
			gridView.requestLayout();
		} else {
			itemWidth = threeWidth;
			gridView.setNumColumns(columnNum);
			resetGridView();
		}
	}

	private void resetGridView() {
		LayoutParams params = gridView.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		gridView.requestLayout();
	}
}
