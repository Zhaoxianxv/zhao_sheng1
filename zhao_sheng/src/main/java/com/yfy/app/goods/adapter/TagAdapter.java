package com.yfy.app.goods.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.zhao_sheng.R;
import com.yfy.app.goods.GoodsTagActivity;
import com.yfy.app.goods.bean.GoodsType;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean is_show;

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }

    private List<GoodsType> dataList;
    private GoodsTagActivity mContext;

    public void setDataList(List<GoodsType> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;





    public TagAdapter(GoodsTagActivity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
//        GoodsType type=new GoodsType();
//        type.setId("0"); +
//        type.setName("未找到所需物品");
//        type.setCanreply(TagFinal.TYPE_TAG);
//        dataList.add(type);
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
     {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_tag_layout, parent, false);
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
            reHolder.name.setText(StringUtils.getTextJoint("%1$s",reHolder.bean.getName(),reHolder.bean.getSurpluscount()));
            reHolder.num.setText(StringUtils.getTextJoint("库存:%1$s %2$s",reHolder.bean.getSurpluscount(),reHolder.bean.getUnit()));
            if (is_show){
                reHolder.num.setVisibility(View.GONE);
            }else{
                reHolder.num.setVisibility(View.VISIBLE);
            }

            reHolder.type.setText(reHolder.bean.getTag());
            if (reHolder.bean.getId().equals("0")){
                reHolder.name.setTextColor(ColorRgbUtil.getOrange());
            }else{
                reHolder.name.setTextColor(ColorRgbUtil.getBaseText());
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
        TextView num;
        RelativeLayout layout;
        GoodsType bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.selected_item_name);
            num=  itemView.findViewById(R.id.selected_item_num);
            type= itemView.findViewById(R.id.selected_item_type);
            layout= itemView.findViewById(R.id.goods_tag_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent data=new Intent();
                    data.putExtra(TagFinal.OBJECT_TAG,bean);
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
