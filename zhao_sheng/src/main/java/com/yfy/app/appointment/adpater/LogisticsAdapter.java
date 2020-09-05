package com.yfy.app.appointment.adpater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.OrderAdminDoActivity;
import com.yfy.app.appointment.bean.OrderBean;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/5/27.
 */

public class LogisticsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {





    private List<OrderBean> dataList;

    private Activity mContext;

    public void setDataList(List<OrderBean> dataList) {
        this.dataList = dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    public LogisticsAdapter(Activity mContext) {
        this.mContext = mContext;
        this.dataList = new ArrayList<>();

    }
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TagFinal.TYPE_FOOTER;
        } else {
            return TagFinal.TYPE_ITEM;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_logistics_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean = dataList.get(position);
            reHolder.name.setText(reHolder.bean.getRoom());
            String applydate="";
            if (reHolder.bean !=null){

                if (reHolder.bean.getApplydate().length()>10){
                    applydate =reHolder.bean.getApplydate().substring(5);
                }
            }
            reHolder.apply_time.setText(applydate.replaceAll("/","月").replace(" ","日 ")+"·"+reHolder.bean.getTime());
            reHolder.e_time.setText(reHolder.bean.getDate());
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case TagFinal.LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case TagFinal.LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case TagFinal.LOADING_END: // 加载到底
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
        TextView name;
        TextView apply_time;
        TextView e_time;
        RelativeLayout layout;
        OrderBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.logistics_name);
            layout=itemView.findViewById(R.id.logistics_layout);
            apply_time = (TextView) itemView.findViewById(R.id.logistics_apply_time);
            e_time = (TextView) itemView.findViewById(R.id.logistics_employ_time);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext, OrderAdminDoActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ID_TAG,bean.getId());
                    if (bean.getStatus().equals("申请中")){

                    }else{
                        b.putBoolean(TagFinal.DELETE_TAG,false);
                        b.putBoolean("is_maintain",true);
                        intent.putExtras(b);
                        mContext.startActivityForResult(intent,TagFinal.UI_CONTENT);
                    }

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
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
            allEnd = (RelativeLayout) itemView.findViewById(R.id.recycler_bottom);

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
