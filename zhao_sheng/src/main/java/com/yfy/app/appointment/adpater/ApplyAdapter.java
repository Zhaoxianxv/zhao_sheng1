package com.yfy.app.appointment.adpater;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.MyFunRooom;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2017/2/10.
 */

public class ApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private List<MyFunRooom> dataList;
    private Activity mContext;

    public void setDataList(List<MyFunRooom> dataList) {

        this.dataList = dataList;
    }
    public  ArrayList<MyFunRooom> getDataList() {
        ArrayList<MyFunRooom> list=new ArrayList<>();
        for (MyFunRooom room:dataList){
            if (room.isSelctor())list.add(room);
        }
        return list;
    }


    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public ApplyAdapter(Activity mContext, List<MyFunRooom> dataList) {
        this.mContext=mContext;
        this.dataList = dataList;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_apply_time_item, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.index=position;
            if (reHolder.bean.isSelctor()){
                reHolder.room_type.setTextColor(Color.RED);
                reHolder.room_type.setText("√");
            }else{
                if (reHolder.bean.getStatus().equals("已安排")){
                    reHolder.room_type.setBackgroundResource(R.color.gray);
                    reHolder.room_type.setTextColor(ColorRgbUtil.getGrayText());
                }
                if (reHolder.bean.getStatus().equals("未申请")){
                    reHolder.room_type.setBackgroundResource(R.color.white);
                    reHolder.room_type.setTextColor(ColorRgbUtil.getForestGreen());
                }
                if (reHolder.bean.getStatus().equals("他人申请中")){
                    reHolder.room_type.setBackgroundResource(R.color.white);
                    reHolder.room_type.setTextColor(ColorRgbUtil.getBlue());
                }
                if (reHolder.bean.getStatus().equals("申请中")){
                    reHolder.room_type.setBackgroundResource(R.color.white);
                    reHolder.room_type.setTextColor(ColorRgbUtil.getOrange());
                }
                reHolder.room_type.setText(reHolder.bean.getStatus());
            }

            reHolder.name.setText(reHolder.bean.getTime_name()+" ("+reHolder.bean.getUser_name()+")");

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView room_type;
        int index;
        MyFunRooom bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.order_apply_title);
            room_type= (TextView) itemView.findViewById(R.id.order_apply_state);
            room_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getStatus().equals("已安排")){
                        Toast.makeText(mContext,"已安排",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        if (bean.isSelctor()){
                            bean.setSelctor(false);
                        }else{
                            bean.setSelctor(true);
                        }
                        notifyItemChanged(index,bean);
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
