package com.yfy.app.allclass.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.allclass.beans.ReplyaBean;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2017/3/17.
 */

public class ShapeSingeDetailAdapter extends XlistAdapter<ReplyaBean> {
    Context context;
    private List<ReplyaBean> taskList=new ArrayList<>();

    private String gray="#808080";
    private String DoderBlue="#1E90FF";
    private String huifu="回复";




    public void setTaskList(List<ReplyaBean> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public ShapeSingeDetailAdapter(Context context,List<ReplyaBean> taskList) {
        super(context,taskList);
        this.context=context;
        this.taskList=taskList;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<ReplyaBean> list) {
        ReplyaBean shapeMainList=list.get(position);
        holder.getView(TextView.class,R.id.detail_reply_name).setText(shapeMainList.getAuth_name());
        holder.getView(TextView.class,R.id.detail_reply_time).setText(shapeMainList.getTime().replace("/","-"));
        holder.getView(TextView.class,R.id.detail_reply_content).setText(shapeMainList.getContent());
        TextView name_d=holder.getView(TextView.class,R.id.detail_reply_name_d);
        ImageView headPic=holder.getView(ImageView.class,R.id.detail_reply_headPic);
        Glide.with(context)
                .load(shapeMainList.getHead_photo())
                .apply(new RequestOptions().circleCrop())
                .into(headPic);
        if (StringJudge.isEmpty(shapeMainList.getReply_name())){
            name_d.setText(shapeMainList.getReply_name());
        }else{
            String html=":@"+shapeMainList.getReply_name();
            String msg= "<font color="+gray+" >"+huifu+"</font>"+"<font color="+DoderBlue+">"+html+"</font>";
            name_d.setText(Html.fromHtml(msg));
        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo reinfo=new ResInfo();
        reinfo.layout=R.layout.shape_detail_reply;
        reinfo.initIds=new int[]{
                R.id.detail_reply_headPic,
                R.id.detail_reply_name,
                R.id.detail_reply_time,
                R.id.detail_reply_content,
                R.id.detail_reply_content_num,
                R.id.detail_reply_name_d,
        };

        return reinfo;
    }







}
