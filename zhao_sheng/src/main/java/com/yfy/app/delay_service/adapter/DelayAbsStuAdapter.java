package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.delay_service.bean.AbsStu;
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

public class DelayAbsStuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AbsStu> dataList;
    private Activity mContext;
    public void setDataList(List<AbsStu> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DelayAbsStuAdapter(Activity mContext) {
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
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_master_tea_stu_icon, parent, false);
            return new RecyclerViewHolder(view);

        }
        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_master_tea_set, parent, false);
            return new TeaHolder(view);

        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_master_tea_tag, parent, false);
            return new TagHolder(view);

        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean = dataList.get(position);
            reHolder.index=position;
            reHolder.name.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf"));
            reHolder.name.setText(StringUtils.getTextJoint("%2$s:%1$s",reHolder.bean.getStuname(),reHolder.bean.getClassname()));
            reHolder.state.setText(reHolder.bean.getType());
            reHolder.tell_txt.setText(reHolder.bean.getPhonenumber());
            reHolder.date_txt.setText(reHolder.bean.getAdddate());
            reHolder.reason_txt.setText(reHolder.bean.getContent());
            if (StringJudge.isEmpty(reHolder.bean.getContent())){
                reHolder.reason_txt.setVisibility(View.GONE);
            }else {
                reHolder.reason_txt.setVisibility(View.VISIBLE);
            }
            GlideTools.chanCircle(mContext,reHolder.bean.getStuheadpic() , reHolder.user_head,R.drawable.order_user_name);

            List<String> photos= new ArrayList<>();
            photos.addAll(reHolder.bean.getImage()==null?photos:reHolder.bean.getImage());

            reHolder.multi.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.multi.setVisibility(View.GONE);
                reHolder.multi.setList(photos);
            }else{
                reHolder.multi.setVisibility(View.VISIBLE);
                reHolder.multi.setList(photos);
            }

        }
        if (holder instanceof TeaHolder) {
            TeaHolder reHolder = (TeaHolder) holder;
            reHolder.bean = dataList.get(position);
            reHolder.index=position;
            reHolder.name.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf"));
            reHolder.name.setText(reHolder.bean.getTeachername());
            reHolder.service_reason.setText(StringUtils.getTextJoint("实到人数:%1$s",reHolder.bean.getStuarrive()));

            reHolder.end_reason.setText(reHolder.bean.getContent());
            GlideTools.chanCircle(mContext,reHolder.bean.getTeacherheadpic() , reHolder.user_head,R.drawable.order_user_name);
            reHolder.tag.setVisibility(View.GONE);
            List<String> photos= new ArrayList<>();
            photos.addAll(reHolder.bean.getImage()==null?photos:reHolder.bean.getImage());

            reHolder.multi.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.multi.setVisibility(View.GONE);
                reHolder.multi.setList(photos);
            }else{
                reHolder.multi.setVisibility(View.VISIBLE);
                reHolder.multi.setList(photos);
            }
        }
        if (holder instanceof TagHolder) {
            TagHolder reHolder = (TagHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tell_txt;
        TextView date_txt;
        TextView reason_txt;
        AppCompatTextView state;
        ImageView user_head;
        RelativeLayout layout;
        MultiPictureView multi;
        int index;
        AbsStu bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            multi=  itemView.findViewById(R.id.delay_master_set_end_multi);
            reason_txt=  itemView.findViewById(R.id.delay_master_set_reason);
            name=  itemView.findViewById(R.id.delay_master_set_name);
            date_txt=  itemView.findViewById(R.id.delay_master_set_date);
            tell_txt=  itemView.findViewById(R.id.delay_master_set_tell);
            user_head=  itemView.findViewById(R.id.delay_master_set_head);
            state=  itemView.findViewById(R.id.delay_master_show_state);
            layout=  itemView.findViewById(R.id.delay_master_set_layout);

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
            tell_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tell=bean.getPhonenumber();
                    if (tell.equals("未录入"))return;
                    if (dotype!=null){
                        dotype.tellPhone(tell);
                    }
                }
            });

        }
    }
    private class TeaHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView service_reason;
        TextView end_reason;
        AppCompatImageView tag;
        ImageView user_head;
        RelativeLayout layout;
        MultiPictureView multi;
        int index;
        AbsStu bean;
        TeaHolder(View itemView) {
            super(itemView);
            multi=  itemView.findViewById(R.id.delay_master_set_end_multi);
            name=  itemView.findViewById(R.id.delay_master_set_name);
            service_reason=  itemView.findViewById(R.id.delay_master_set_service_reason);
            end_reason=  itemView.findViewById(R.id.delay_master_set_end_reason);
            user_head=  itemView.findViewById(R.id.delay_master_set_head);
            tag=  itemView.findViewById(R.id.delay_master_set_tag);
            layout=  itemView.findViewById(R.id.delay_master_set_layout);

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

        }
    }

    private class TagHolder extends RecyclerView.ViewHolder {
        TagHolder(View itemView) {
            super(itemView);

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



    public Dotype dotype;

    public void setDotype(Dotype dotype) {
        this.dotype = dotype;
    }

    public interface Dotype{
        void tellPhone(String phone);
    }


}
