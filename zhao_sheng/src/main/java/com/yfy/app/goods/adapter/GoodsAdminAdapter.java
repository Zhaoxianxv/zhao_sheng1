package com.yfy.app.goods.adapter;

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
import com.yfy.app.goods.GoodsAdminDetailActivity;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class GoodsAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private String eq_tag="没找到这个东西";
    private String title_tag="申请申购";
    private List<GoodsBean> dataList;
    private Activity mContext;

    private String is_master;

    public void setIs_master(String is_master) {
        this.is_master = is_master;
    }

    public void setDataList(List<GoodsBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;



    public GoodsAdminAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_main_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.itemView.setLongClickable(true);

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getUsername());
            reHolder.time.setText(reHolder.bean.getSubmit_time());
            reHolder.tag.setText(reHolder.bean.getItem_name());
            reHolder.state.setText(reHolder.bean.getStatus());

            if (reHolder.bean.getItem_id().equals("0")){
                reHolder.tag.setText(title_tag);
                reHolder.tag.setTextColor(ColorRgbUtil.getBlue());
            }else{
                reHolder.tag.setText(reHolder.bean.getItem_name());
                reHolder.tag.setTextColor(ColorRgbUtil.getGrayText());
            }
            switch (reHolder.bean.getStatus()){
                case "驳回申请":
                    reHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
                    break;
                case "已提交":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                    break;
                case "准备中":
                    switch (is_master){
                        case TagFinal.TRUE:
                            reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                            break;
                        case TagFinal.FALSE:
                            reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                            break;
                        case TagFinal.TYPE_TAG:
                            reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                            break;
                    }

                    break;
                case "已准备":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                    break;
                case "同意采购":
                    switch (is_master){
                        case TagFinal.TRUE:
                            reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                            break;
                        case TagFinal.FALSE:
                            reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                            break;
                        case TagFinal.TYPE_TAG:
                            reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                            break;
                    }
                    break;
                default:
                    reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                    break;
            }
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
                    footViewHolder.allEnd.setVisibility(View.GONE);
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
    private String yes_state,no_state;

    public void setYes_state(String yes_state) {
        this.yes_state = yes_state;
    }

    public void setNo_state(String no_state) {
        this.no_state = no_state;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tag;
        TextView time;
        TextView state;
        RelativeLayout layout;
        GoodsBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.goods_item_title);
            tag=  itemView.findViewById(R.id.goods_item_tag);
            time=  itemView.findViewById(R.id.goods_item_time);
            state=  itemView.findViewById(R.id.goods_item_state);
            layout=itemView.findViewById(R.id.goods_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //单/多选
                    Intent data=new Intent(mContext,GoodsAdminDetailActivity.class);
                    data.putExtra(TagFinal.ID_TAG,bean.getId());
                    data.putExtra(TagFinal.TYPE_TAG,is_master);
                    data.putExtra("no_state",no_state);
                    data.putExtra("yes_state",yes_state);
                    mContext.startActivityForResult(data,TagFinal.UI_TAG);
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
