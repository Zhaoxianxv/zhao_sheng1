package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.bean.OperType;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OperType> dataList;

    private Activity mContext;
    private boolean isselectType;

    public void setIsselectType(boolean isselectType) {
        this.isselectType = isselectType;
    }

    public void setDataList(List<OperType> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    public ArrayList<OperType> getDataList() {
        ArrayList<OperType> list=new ArrayList<>();
        for (OperType type:dataList){
            if (type.isIselect())
                list.add(type);
        }
        return list;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public TagAdapter(Activity mContext) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_check, parent, false);
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
            reHolder.type.setChecked(reHolder.bean.isIselect());
            if (reHolder.bean.isIselect()){
                reHolder.name.setTextColor(ColorRgbUtil.getMaroon());
            }else{
                reHolder.name.setTextColor(ColorRgbUtil.getGrayText());
            }


        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        RadioButton type;
        RelativeLayout layout;
        int index;
        OperType bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            type=  itemView.findViewById(R.id.selected_item_check);
            layout=  itemView.findViewById(R.id.selected_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    if (isselectType){
                        Intent data=new Intent();
                        data.putExtra(TagFinal.TYPE_TAG, true);
                        data.putExtra(TagFinal.OBJECT_TAG,bean);
                        mContext.setResult(RESULT_OK,data);
                        mContext.finish();
                    }else{
                        bean.setIselect(!bean.isIselect());
                        notifyItemChanged(index,bean );
                    }

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
