package com.yfy.app.vote.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import com.yfy.app.vote.VoteDetailActicity;
import com.yfy.app.vote.bean.Vote;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class VoteMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Vote> dataList;
    private Activity mContext;

    public void setDataList(List<Vote> dataList) {
        this.dataList = dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;



    public VoteMainAdapter(Activity mContext){
        this.mContext=mContext;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vote_main_item_layout, parent, false);
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
            reHolder.bean=dataList.get(position);
            reHolder.index=position;
            reHolder.time.setText(reHolder.bean.getData());
            reHolder.title.setText(reHolder.bean.getTitle());
            if (reHolder.bean.getIsend().equals("false")) {
                reHolder.isSendTv.setText("未投票");
                reHolder.isSendTv.setTextColor(Color.RED);
                reHolder.title.setTextColor(ColorRgbUtil.getBaseText());
                reHolder.time.setTextColor(ColorRgbUtil.getBaseText());
            } else {
                reHolder.isSendTv.setText("已投票");
                reHolder.isSendTv.setTextColor(ColorRgbUtil.getGrayText());
                reHolder.title.setTextColor(ColorRgbUtil.getGrayText());
                reHolder.time.setTextColor(ColorRgbUtil.getGrayText());
            }
        }
        if (holder instanceof FootViewHolder) {
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
        return dataList.size()+1;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView time;
        TextView isSendTv;
        LinearLayout layout;
        int index;
        Vote bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.title);
            time =  itemView.findViewById(R.id.date);
            isSendTv =  itemView.findViewById(R.id.issend);
            layout =  itemView.findViewById(R.id.vote_main_item_layout);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle b = new Bundle();
                    b.putString("voteId", bean.getId());
                    b.putBoolean("boo", true);
                    b.putInt("position", index);
                    if (bean.getIsend().equals("true")) {
                        b.putBoolean("isVoted", true);
                    } else {
                        b.putBoolean("isVoted", false);
                    }
                    Intent intent = new Intent(mContext,VoteDetailActicity.class);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent, TagFinal.UI_ADD);
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
