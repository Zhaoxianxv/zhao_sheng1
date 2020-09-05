package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.DelayServiceSetActivity;
import com.yfy.app.delay_service.bean.DelayEventBean;
import com.yfy.final_tag.Base;
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

public class DelayMasterAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private DateBean dateBean;

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    private List<DelayEventBean> dataList;
    private Activity mContext;
    public void setDataList(List<DelayEventBean> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DelayMasterAdminAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_master_admin_item, parent, false);
            return new RecyclerViewHolder(view);

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
            reHolder.name.setText(reHolder.bean.getAdminname());
            reHolder.service_reason.setText(StringUtils.getTextJoint("课后服务:%1$s",reHolder.bean.getElectivedetail()));
            reHolder.end_reason.setText(StringUtils.getTextJoint("组织放学:%1$s",reHolder.bean.getLeavedetail()));

//            reHolder.end_reason.setText(reHolder.bean.getLeavedetail());
            GlideTools.chanCircle(mContext,reHolder.bean.getAdminheadpic() , reHolder.user_head,R.drawable.order_user_name);
            if (reHolder.bean.getIsaddbyme().equalsIgnoreCase(TagFinal.TRUE)){
                reHolder.tag.setColorFilter(ColorRgbUtil.getForestGreen());

            }else{
                reHolder.tag.setColorFilter(ColorRgbUtil.getGray());
            }


            List<String> add_list= new ArrayList<>();
            List<String> end_list= new ArrayList<>();
            reHolder.add_multi.clearItem();
            reHolder.end_multi.clearItem();
            if (StringJudge.isEmpty(reHolder.bean.getElectivedetailpic())){
                reHolder.add_multi.setVisibility(View.GONE);
                reHolder.add_multi.setList(add_list);
            }else{
                add_list=StringUtils.listToStringSplitCharacters(reHolder.bean.getElectivedetailpic(),",");
                reHolder.add_multi.setVisibility(View.VISIBLE);
                reHolder.add_multi.setList(add_list);
            }
            if (StringJudge.isEmpty(reHolder.bean.getLeavedetailpic())){
                reHolder.end_multi.setVisibility(View.GONE);
                reHolder.end_multi.setList(end_list);
            }else{
                end_list=StringUtils.listToStringSplitCharacters(reHolder.bean.getLeavedetailpic(),",");
                reHolder.end_multi.setVisibility(View.VISIBLE);
                reHolder.end_multi.setList(end_list);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView service_reason;
        TextView end_reason;
        AppCompatImageView tag;
        ImageView user_head;
        MultiPictureView add_multi;
        MultiPictureView end_multi;

        RelativeLayout layout;
        int index;
        DelayEventBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            add_multi=  itemView.findViewById(R.id.delay_master_set_service_multi);
            end_multi=  itemView.findViewById(R.id.delay_master_set_end_multi);
            name=  itemView.findViewById(R.id.delay_master_set_name);
            service_reason=  itemView.findViewById(R.id.delay_master_set_service_reason);
            end_reason=  itemView.findViewById(R.id.delay_master_set_end_reason);
            user_head=  itemView.findViewById(R.id.delay_master_set_head);
            tag=  itemView.findViewById(R.id.delay_master_set_tag);
            layout=  itemView.findViewById(R.id.delay_master_set_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getIsaddbyme().equalsIgnoreCase(TagFinal.TRUE)){
                        Intent intent=new Intent(mContext,DelayServiceSetActivity.class);
                        intent.putExtra(Base.type, false);
                        intent.putExtra(Base.data, bean);
                        intent.putExtra(Base.date, dateBean);
                        mContext.startActivityForResult(intent, TagFinal.UI_ADD);
                    }
                }
            });

            add_multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
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
            end_multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
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
