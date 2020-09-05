package com.yfy.app.seal.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.maintainnew.bean.DepTag;
import com.yfy.app.seal.bean.SealTag;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class SealTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<SealTag> dataList;
    private Activity mContext;

    public void setDataList(List<SealTag> dataList) {
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


    public SealTagAdapter(Activity mContext) {
        this.mContext = mContext;
        this.dataList = new ArrayList<>();
    }

    public SealTagAdapter(Activity mContext, List<SealTag> dataList){
        this.mContext=mContext;
        this.dataList = dataList;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seal_tag_layout, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.itemView.setLongClickable(true);

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.type.setVisibility(View.VISIBLE);
            if (StringJudge.isEmpty(reHolder.bean.getBorrowedate())){
                reHolder.name.setText(StringUtils.getTextJoint("申请人:%1$s\n类型:%2$s\n状态:%3$s",
                        reHolder.bean.getBorrowuser(),"盖章",reHolder.bean.getStatus()));
                reHolder.type.setText(StringUtils.getTextJoint("盖章时间:%1$s",reHolder.bean.getBorrowsdate()));

            }else {
                reHolder.name.setText(StringUtils.getTextJoint("申请人:%1$s\n类型:%2$s\n状态:%3$s",
                        reHolder.bean.getBorrowuser(),"借章",reHolder.bean.getStatus()));
                reHolder.type.setText(StringUtils.getTextJoint("开始时间:%1$s\n结束时间:%2$s",reHolder.bean.getBorrowsdate(),
                        reHolder.bean.getBorrowedate()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        SealTag bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            type= (TextView) itemView.findViewById(R.id.selected_item_type);

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
