package com.yfy.app.duty.adpater;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.bean.PlaneInfo;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class ChoiceTimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public ArrayList<PlaneInfo> getDataList() {

        ArrayList<PlaneInfo> list=new ArrayList<>();
        for (PlaneInfo info:dataList){
            if (info.isIs_Select()){
                list.add(info);
            }
        }
        return list;
    }
    private List<PlaneInfo> dataList;
    private Activity mContext;

    public void setDataList(List<PlaneInfo> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceTimeAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position==0){
            return TagFinal.TWO_INT;
        }else{
            return TagFinal.ONE_INT;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_item_layout, parent, false);
            return new ParentViewHolder(view);

        }
        if (viewType == TagFinal.TWO_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt, parent, false);
            return new TopViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder reHolder = (ParentViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getDate());
            reHolder.type.setVisibility(View.VISIBLE);

            reHolder.index=position;
            if (reHolder.bean.isIs_Select()){
                reHolder.name.setTextColor(ColorRgbUtil.getBaseColor());
            }else{
                reHolder.name.setTextColor(ColorRgbUtil.getBaseText());
            }
            if (reHolder.bean.getIsedit().equals(TagFinal.TRUE)){
                reHolder.type.setText("可编辑");
                reHolder.type.setTextColor(ColorRgbUtil.getOrange());
            }else{
                reHolder.name.setTextColor(ColorRgbUtil.getGrayText());
                reHolder.type.setText("不可编辑");
                reHolder.type.setTextColor(ColorRgbUtil.getGrayText());
            }
        }
        if (holder instanceof TopViewHolder){
            TopViewHolder topHolder = (TopViewHolder) holder;
            topHolder.bean=dataList.get(position);
            topHolder.name.setTextColor(ColorRgbUtil.getGrayText());
            topHolder.name.setBackgroundColor(ColorRgbUtil.getWhite());
            topHolder.name.setText(topHolder.bean.getDate());
            topHolder.name.setTextSize(16f);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        PlaneInfo bean;
        int index;
        ParentViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            type= (TextView) itemView.findViewById(R.id.selected_item_type);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    if (bean.getIsedit().equals(TagFinal.TRUE)){
                        bean.setIs_Select(!bean.isIs_Select());
                        notifyItemChanged(index,bean);
                    }

                }
            });
        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        PlaneInfo bean;
        TopViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.public_center_txt_add);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (timeChioce!=null){
                        timeChioce.chioceTime();
                    }
                }
            });
        }
    }


    private TimeChioce timeChioce;

    public void setTimeChioce(TimeChioce timeChioce) {
        this.timeChioce = timeChioce;
    }

    public interface TimeChioce{
        void chioceTime();
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
