package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.delay_service.bean.DetailBean;
import com.yfy.app.delay_service.bean.EventClass;
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

public class TeaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DetailBean> dataList;

    private Activity mContext;
    private EventClass topbean;

    public void setBean(EventClass bean) {
        this.topbean = bean;
    }

    public void setDataList(List<DetailBean> dataList) {
        this.dataList = dataList;
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public TeaDetailAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position==0){
            return TagFinal.TYPE_PARENT;
        }else{
            return TagFinal.TYPE_CHILD;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_item_tea_detail, parent, false);
            return new RecyclerViewHolder(view);

        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_item_tea_parent, parent, false);
            return new TopHolder(view);

        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;

            reHolder.bean = dataList.get(position-1);
            if (StringJudge.isEmpty(reHolder.bean.getImage())){
                reHolder.multi.setVisibility(View.GONE);
            }else{
                reHolder.multi.setVisibility(View.VISIBLE);
                reHolder.multi.setList(reHolder.bean.getImage());
            }
            GlideTools.chanCircle(mContext,reHolder.bean.getTeacherheadpic(),reHolder.head, R.drawable.order_user_name);
            reHolder.name.setText(StringUtils.getTextJoint("考勤教师:%1$s",reHolder.bean.getTeachername()));
            reHolder.phone.setText(StringUtils.getTextJoint("联系电话:%1$s",StringJudge.isEmpty(reHolder.bean.getPhonenumber())?"未录入":reHolder.bean.getPhonenumber()));
            reHolder.content.setText(StringUtils.getTextJoint("考勤备注:%1$s",StringJudge.isEmpty(reHolder.bean.getContent())?"未填写":reHolder.bean.getContent()));
            reHolder.type.setText(StringUtils.getTextJoint("考勤结果:%1$s",reHolder.bean.getType()));
            reHolder.one.setVisibility(View.GONE);
//            reHolder.layout.setTop(10);
        }
        if (holder instanceof TopHolder) {
            TopHolder top = (TopHolder) holder;
            top.bean = topbean;
            GlideTools.chanCircle(mContext,topbean.getStuheadpic(),top.head, R.drawable.order_user_name);
            top.stu_name.setText(topbean.getStuname());
            top.type_tag.setText(topbean.getIsabsent());
            top.stu_phone.setText(topbean.getPhonenumber());
            top.class_name.setText(StringUtils.getTextJoint("班级：%1$s",topbean.getClassname()));
            top.two_name.setText(StringUtils.getTextJoint("托管班级：%1$s",topbean.getElectivename()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size()+1 ;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView one;
        TextView del;
        TextView phone;
        TextView type;
        TextView content;
        MultiPictureView multi;
        RelativeLayout layout;
        DetailBean bean;
        ImageView head;
        RecyclerViewHolder(View itemView) {
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
                    Intent intent=new Intent(mContext, MultPicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                    b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tell!=null){
                        tell.tell(bean.getPhonenumber());
                    }
                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tell!=null){
                        tell.del(bean.getId());
                    }
                }
            });
        }
    }

    private class TopHolder extends RecyclerView.ViewHolder {

        ImageView head;
        TextView stu_name;
        TextView stu_phone;
        TextView type_tag;

        TextView class_name;
        TextView two_name;
        EventClass bean;
        TopHolder(View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.event_detail_head);
            stu_name=  itemView.findViewById(R.id.event_detail_stu_name);
            stu_phone=  itemView.findViewById(R.id.event_detail_stu_phone);
            type_tag=  itemView.findViewById(R.id.event_detail_type);
            class_name=  itemView.findViewById(R.id.event_class_name);
            two_name=  itemView.findViewById(R.id.event_class_two);
            stu_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tell!=null){
                        tell.tell(topbean.getPhonenumber());
                    }
                }
            });


        }
    }


    private String canedit=TagFinal.FALSE;



    public void setCanedit(String canedit) {
        this.canedit = canedit;
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


    private Tell tell;

    public void setTell(Tell tell) {
        this.tell = tell;
    }

    public interface Tell{
        void tell(String phone);
        void del(String id);
    }

}
