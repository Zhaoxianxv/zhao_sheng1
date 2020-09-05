package com.yfy.app.slide;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckSlideRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {



    private List<Inventory> dataList;
    private Activity mContext;

    public void setDataList(List<Inventory> dataList) {
        this.dataList = dataList;
    }

    public CheckSlideRecyclerAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return position%2;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_byid_detail_child, parent, false);
            view.setOnClickListener(this);
            return new ParentHolder(view);
        }
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_itme_layout, parent, false);
            return new ChildHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentHolder) {
            ParentHolder reHolder = (ParentHolder) holder;
            reHolder.bean=dataList.get(position);
        }
        if (holder instanceof ChildHolder) {
            ChildHolder child = (ChildHolder) holder;
            child.bean=dataList.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ParentHolder extends RecyclerView.ViewHolder {
        TextView name;
        RelativeLayout layout;
        Inventory bean;
        ParentHolder(View itemView) {
            super(itemView);

            layout=  itemView.findViewById(R.id.check_detail_child_tag);
            name=  itemView.findViewById(R.id.check_class_num_del);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Logger.e("点击删除");
                }
            });
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.e("点击ParentHolder");
                }
            });

        }
    }


    private class ChildHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        Inventory bean;
        ChildHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.slide_item_tag);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Logger.e("点击ChildHolder");
                }
            });

        }
    }


    @Override
    public void onClick(View v) {
    }


    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        notifyDataSetChanged();
    }









}
