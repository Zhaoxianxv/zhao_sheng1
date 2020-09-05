package com.yfy.app.tea_event.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.bean.Stu;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TeaStuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Stu> dataList;
    private Activity mContext;

    public void setDataList(List<Stu> dataList) {
        this.dataList = dataList;
    }



    public TeaStuAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_item, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getStuname());
            reHolder.content.setVisibility(View.GONE);
            reHolder.state.setVisibility(View.GONE);
            reHolder.tag.setVisibility(View.GONE);
            GlideTools.chanCircle(mContext, reHolder.bean.getHeadpic(), reHolder.header,R.drawable.oval_gray);
//            if (reHolder.bean.getIsevaluated().equals(TagFinal.FALSE)){
//                reHolder.state.setText("未评测");
//                reHolder.state.setTextColor(ColorRgbUtil.getOrange());
//            }else{
//                reHolder.state.setText("已评测");
//                reHolder.state.setTextColor(ColorRgbUtil.getGreen());
//            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        ImageView header;
        TextView content;
        AppCompatImageView tag;
        RelativeLayout layout;
        Stu bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tea_event_name);
            content= (TextView) itemView.findViewById(R.id.tea_event_content);
            state=  itemView.findViewById(R.id.tea_event_state);
            tag=  itemView.findViewById(R.id.tea_event_tag);
            header=  itemView.findViewById(R.id.tea_event_header);
            layout=  itemView.findViewById(R.id.tea_event_layout);


        }
    }


    /**
     * 设置上拉加载状态
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */

    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }









}
