package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.bean.OperType;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class OperTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OperType> dataList;

    private Activity mContext;


    public void setDataList(List<OperType> dataList) {
        this.dataList = dataList;

    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public OperTagAdapter(Activity mContext) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_item_layout, parent, false);
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
            reHolder.name.setText(reHolder.bean.getOpearname());
            reHolder.type.setVisibility(View.GONE);
            reHolder.name.setTextColor(ColorRgbUtil.getBaseText());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        int index;
        OperType bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            type= (TextView) itemView.findViewById(R.id.selected_item_type);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent data=new Intent();
                    data.putExtra(TagFinal.TYPE_TAG, true);
                    data.putExtra(Base.data,bean);
                    mContext.setResult(RESULT_OK,data);
                    mContext.finish();


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
