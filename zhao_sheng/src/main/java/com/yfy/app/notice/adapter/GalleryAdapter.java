package com.yfy.app.notice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;

import java.util.ArrayList;
import java.util.List;


public class GalleryAdapter extends BaseAdapter {
	private Context context;
	private List<String> urls=new ArrayList<String>();
	private LayoutInflater minflater;

	public void setUrls(List<String> urls){
		this.urls=urls;
		notifyDataSetChanged();
	}
	public GalleryAdapter(Context context, List<String> urls){
		this.context=context;
		this.urls=urls;
		minflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		View view=null;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.notice_gallery_adp_item, null);
			viewholder = new ViewHolder();
		
			viewholder.icon = (ImageView) convertView.findViewById(R.id.galley_itme);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		
		Glide.with(context).load(urls.get(position)).into(viewholder.icon);

		return convertView;
	}
	
	
	class ViewHolder {
		public ImageView icon;
	}

}
