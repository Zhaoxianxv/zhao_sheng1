package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.base.adapter.AbstractAdapter;
import com.yfy.final_tag.ViewUtils;

import java.util.List;


public class AwardPictureAdapter extends AbstractAdapter<String> {



	private GridView gridView;
	private int gridViewWidth;
	private int numColnum=3;
	private int itemWidth;
	private Context context;

	public AwardPictureAdapter(Context context, List<String> list,
							   GridView gridView,int width) {
		super(context, list);
		this.gridView = gridView;
		this.context=context;

		gridViewWidth=context.getResources().getDisplayMetrics().widthPixels-300;;
//		numColnum=gridView.getNumColumns();
		itemWidth= (gridViewWidth - gridView.getPaddingLeft() - gridView.getPaddingRight() - ViewUtils.getHorizontalSpacing(gridView)) / numColnum;
	}

	@Override
	public void renderData(int position, DataViewHolder holder, List<String> list) {
		ImageView picture = holder.getView(ImageView.class, R.id.picture);
		Glide.with(context).load(list.get(position)).into(picture);
		LayoutParams params=picture.getLayoutParams();
		params.width=itemWidth;
		params.height=itemWidth;
		picture.setLayoutParams(params);


	}

	@Override
	public ResInfo getResInfo() {
		ResInfo resInfo = new ResInfo();
		resInfo.layout = R.layout.graded_moments_pictrue_adp_item;
		resInfo.initIds = new int[] { R.id.picture };
		return resInfo;
	}

	@Override
	public void notifyDataSetChanged(List<String> list) {
		super.notifyDataSetChanged(list);
	}


}
