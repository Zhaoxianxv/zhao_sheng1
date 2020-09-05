package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.delay_service.EventDetailActivity;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class ChoiceAbsentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EventClass> dataList;

    private Activity mContext;

    public void setDataList(List<EventClass> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceAbsentAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getView_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item_lefttxt_back, parent, false);
            return new ParentHolder(view);

        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_stu_item, parent, false);
            return new ChildHolder(view);

        }
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_item_tea_detail, parent, false);
            return new TeaHolder(view);

        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TeaHolder) {
            TeaHolder reHolder = (TeaHolder) holder;

            reHolder.bean = dataList.get(position);
            if (StringJudge.isEmpty(reHolder.bean.getImage())){
                reHolder.multi.setVisibility(View.GONE);
            }else{
                reHolder.multi.setVisibility(View.VISIBLE);
                reHolder.multi.setList(reHolder.bean.getImage());
            }
            reHolder.del.setVisibility(View.GONE);
            reHolder.phone.setText(StringUtils.getTextJoint("考勤人数:%1$s",reHolder.bean.getStuarrive()));

            GlideTools.chanCircle(mContext,reHolder.bean.getTeacherheadpic(),reHolder.head, R.drawable.order_user_name);
            if (reHolder.bean.getChecktype().equals("tea")){
                reHolder.type.setVisibility(View.GONE);
                reHolder.name.setTextColor(ColorRgbUtil.getGrayText());
                reHolder.name.setText(StringUtils.getTextJoint("考勤教师:%1$s",reHolder.bean.getTeachername()));
            }else{
                reHolder.name.setTextColor(ColorRgbUtil.getBaseColor());
                reHolder.type.setVisibility(View.VISIBLE);
                reHolder.name.setText(StringUtils.getTextJoint("巡查人员:%1$s",reHolder.bean.getTeachername()));
            }
            reHolder.phone.setTextColor(ColorRgbUtil.getGrayText());
            reHolder.content.setText(StringUtils.getTextJoint("考勤备注:%1$s",StringJudge.isEmpty(reHolder.bean.getContent())?"未填写":reHolder.bean.getContent()));
            reHolder.type.setText(StringUtils.getTextJoint("巡查内容:%1$s",reHolder.bean.getType()));
            reHolder.one.setText(StringUtils.getTextJoint("代课老师:%1$s",reHolder.bean.getSubstituteteacher()));

            if (reHolder.bean.getSubstituteteacher().equals("无")){
                reHolder.one.setTextColor(ColorRgbUtil.getGrayText());
            }else{
                reHolder.one.setTextColor(ColorRgbUtil.getBaseColor());
            }
        }
        if (holder instanceof ParentHolder) {
            ParentHolder reHolder = (ParentHolder) holder;
            reHolder.bean = dataList.get(position);
            reHolder.name.setText(reHolder.bean.getElectivename());
            reHolder.name.setTextColor(ColorRgbUtil.getBaseText());
        }
        if (holder instanceof ChildHolder) {
            ChildHolder child = (ChildHolder) holder;
            child.bean = dataList.get(position);
            child.name.setText(child.bean.getStuname());
            GlideTools.chanCircle(mContext, child.bean.getStuheadpic(), child.head, R.drawable.order_user_name);
            child.tag.setText(child.bean.getType());
            child.content.setText(child.bean.getPhonenumber());
            child.content.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class ParentHolder extends RecyclerView.ViewHolder {


        TextView name;
        EventClass bean;
        ParentHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.public_xlist_left_txt);


        }
    }

    private class TeaHolder extends RecyclerView.ViewHolder {


        TextView one;
        TextView name;
        TextView del;
        TextView phone;
        TextView type;
        TextView content;
        MultiPictureView multi;
        RelativeLayout layout;
        EventClass bean;
        ImageView head;
        TeaHolder(View itemView) {
            super(itemView);
            one=  itemView.findViewById(R.id.event_add_one);
            head=  itemView.findViewById(R.id.event_detail_head);
            layout=  itemView.findViewById(R.id.event_detail_item_layout);
            name=  itemView.findViewById(R.id.event_add_user);
            del=  itemView.findViewById(R.id.event_del_item);
            phone=  itemView.findViewById(R.id.event_add_user_phone);
            type=  itemView.findViewById(R.id.event_add_date);
            content=  itemView.findViewById(R.id.event_add_content);
            multi=  itemView.findViewById(R.id.event_detail_show_multi);
            multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });


        }
    }
    private class ChildHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView content;
        ImageView head;
        AppCompatTextView tag;
        RelativeLayout layout;
        EventClass bean;
        ChildHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.event_stu_layout);
            name=  itemView.findViewById(R.id.event_stu_name);
            content=  itemView.findViewById(R.id.event_stu_content);
            tag=  itemView.findViewById(R.id.event_stu_tag);
            head=  itemView.findViewById(R.id.event_head_icon);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,EventDetailActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.OBJECT_TAG,bean.getId());
                    intent.putExtras(b);
                    mContext.startActivity(intent);

                }
            });

        }

    }


    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

}
