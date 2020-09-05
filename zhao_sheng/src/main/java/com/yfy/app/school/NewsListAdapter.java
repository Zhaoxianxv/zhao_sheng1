/**
 * 
 */
package com.yfy.app.school;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.school.bean.SchoolNews;
import com.yfy.base.adapter.AbstractAdapter2;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class NewsListAdapter extends AbstractAdapter2<SchoolNews> {

	// private final static String TAG = NewsListAdapter.class.getSimpleName();



	public NewsListAdapter(Context context, List<SchoolNews> list) {
		super(context, list);

	}

	@Override
	public int[] getFindViewByIDs() {
		return new int[] { R.id.news_pic, R.id.news_title, R.id.news_content,
				R.id.news_date };
	}

	@Override
	public int getLayoutId() {
		return R.layout.school_item_news_listview;
	}

	@Override
	public void renderData(int position,
			AbstractAdapter2<SchoolNews>.DataViewHolder holder,
			List<SchoolNews> list) {
		SchoolNews schoolNews = list.get(position);

		ImageView news_pic = holder.getView(ImageView.class, R.id.news_pic);
		TextView news_title = holder.getView(TextView.class, R.id.news_title);
		TextView news_content = holder.getView(TextView.class, R.id.news_content);
		TextView news_date = holder.getView(TextView.class, R.id.news_date);


		GlideTools.chanMult(context, schoolNews.getNewslist_image(), news_pic,R.drawable.icon_gra_error);
		news_title.setText(schoolNews.getNewslist_head());
		news_content.setText(schoolNews.getNewslist_point());
		news_date.setText(schoolNews.getNewslist_time());
	}



}
