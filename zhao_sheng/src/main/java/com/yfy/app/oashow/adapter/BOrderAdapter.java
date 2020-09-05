package com.yfy.app.oashow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.oashow.OrderDetailActivity;
import com.yfy.app.oashow.bean.Admin;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class BOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Admin> dataList;
    private Activity  mContext;

    public void setDataList(List<Admin> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;

    public BOrderAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_ord_uer_detail, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }



    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setLongClickable(true);

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);

            reHolder.room_name.setText(reHolder.bean.getRoom());
            if (reHolder.bean.getStatus().equals("申请中")){
                reHolder.state.setTextColor(ColorRgbUtil.getCrimson());
            }
            if (reHolder.bean.getStatus().equals("已拒绝")){
                reHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
            }
            if (reHolder.bean.getStatus().equals("已通过")){
                reHolder.state.setTextColor(ColorRgbUtil.getPink());
            }
            if (reHolder.bean.getStatus().equals("后勤已安排")){
                reHolder.state.setTextColor(ColorRgbUtil.getGreen());
            }

            String time="",applydate="";
            if (reHolder.bean!=null){
                if (reHolder.bean.getDate().length()>5){
                    time =reHolder.bean.getDate().substring(5);
                }
                if (reHolder.bean.getApplydate().length()>10){
                    applydate =reHolder.bean.getApplydate().substring(5);
                }
            }
            reHolder.time.setText("("+time.replaceAll("/","月")+"·"+reHolder.bean.getTime()+")");
            reHolder.apply_time.setText(applydate);
            reHolder.state.setText(reHolder.bean.getStatus());
//            holder.itemView.setTag(new ContextMenuRecyclerView.RecyclerItemMarker(position, info));

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    footViewHolder.allEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {



        TextView room_name;
        TextView state;
        TextView time;
        TextView apply_time;
        RelativeLayout layout;
        ImageView headimage;
        Admin bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);


            state=  itemView.findViewById(R.id.order_type_tv);
            time=  itemView.findViewById(R.id.order_time_tv);
            apply_time=  itemView.findViewById(R.id.order_apply_time_tv);
            room_name=  itemView.findViewById(R.id.order_room_tv);
            headimage=  itemView.findViewById(R.id.order_type_iv);
            layout=  itemView.findViewById(R.id.order_main_item_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, OrderDetailActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ID_TAG,bean.getId());
                    intent.putExtras(b);
                    mContext.startActivity(intent);

                }
            });
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;
        RelativeLayout allEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading =  itemView.findViewById(R.id.pb_loading);
            tvLoading =  itemView.findViewById(R.id.tv_loading);
            llEnd =  itemView.findViewById(R.id.ll_end);
            allEnd =  itemView.findViewById(R.id.recycler_bottom);

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
