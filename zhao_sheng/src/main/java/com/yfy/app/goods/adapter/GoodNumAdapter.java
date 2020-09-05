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
import com.yfy.app.Numbean;
import com.yfy.app.goods.GoodNumByidActivity;
import com.yfy.app.goods.GoodNumDoActivity;
import com.yfy.app.goods.GoodsDetailActivity;
import com.yfy.app.goods.bean.GoodNumBean;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class GoodNumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<GoodNumBean> dataList;
    private Activity mContext;

    public void setDataList(List<GoodNumBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public void setIs_edit(boolean is_edit) {
        this.is_edit = is_edit;
    }

    private boolean is_edit=false;
    public GoodNumAdapter(Activity mContext){
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

            reHolder.time.setText(reHolder.bean.getAdddate());
            reHolder.state.setText(reHolder.bean.getState());
            reHolder.tag.setText(StringUtils.getTextJoint("%1$s(新增：%2$s)",
                    reHolder.bean.getItem_name(),reHolder.bean.getNew_count()));
            reHolder.tag_one.setText(StringUtils.getTextJoint("%1$s(现有：%2$s)",
                    reHolder.bean.getItem_name(),reHolder.bean.getNow_count()));
            reHolder.name.setText(reHolder.bean.getAddluser());
            switch (reHolder.bean.getState()){
                case "驳回申请":
                    reHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
                    break;
                case "待审核":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());

                    break;
                case "已准备":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());
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

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tag;
        TextView tag_one;
        TextView time;
        TextView state;
        RelativeLayout layout;
        GoodNumBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.goods_item_title);
            tag_one=  itemView.findViewById(R.id.goods_item_one);
            tag=  itemView.findViewById(R.id.goods_item_tag);
            time=  itemView.findViewById(R.id.goods_item_time);
            state=  itemView.findViewById(R.id.goods_item_state);
            layout= itemView.findViewById(R.id.goods_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent intent=new Intent(mContext,GoodNumDoActivity.class);
                    intent.putExtra(TagFinal.ID_TAG,bean.getId());
                    mContext.startActivityForResult(intent,TagFinal.UI_TAG);
//                    Intent data=new Intent(mContext,GoodNumByidActivity.class);
//                    data.putExtra(TagFinal.OBJECT_TAG,bean);
//                    data.putExtra(TagFinal.TYPE_TAG,is_edit);
//                    mContext.startActivityForResult(data,TagFinal.UI_TAG);
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
