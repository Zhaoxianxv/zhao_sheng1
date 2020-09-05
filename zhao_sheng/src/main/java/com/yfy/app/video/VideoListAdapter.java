package com.yfy.app.video;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.video.beans.Videobean;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/31.
 */

public class VideoListAdapter extends XlistAdapter<Videobean> {


    public VideoListAdapter(Context context, List<Videobean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<Videobean> list) {
        Videobean bean=list.get(position);
        holder.getView(TextView.class,R.id.video_name).setText(bean.getUser_name());
        holder.getView(TextView.class,R.id.video_title).setText(bean.getTitle());
        holder.getView(TextView.class,R.id.video_time).setText(bean.getTime());
        ImageView icon=holder.getView(ImageView.class,R.id.video_image);


        GlideTools.chanCircle(context,bean.getImage(),icon,R.drawable.head_user);


    }

    @Override
    public ResInfo getResInfo() {
        ResInfo info=new ResInfo();
        info.layout= R.layout.video_list_item;
        info.initIds=new int[]{
                R.id.video_image,
                R.id.video_title,
                R.id.video_name,
                R.id.video_time,
        };
        return info;
    }
}
