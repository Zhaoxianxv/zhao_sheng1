package com.yfy.app.maintainnew.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.maintainnew.MaintainNewDetailAdminActivity;
import com.yfy.app.maintainnew.bean.MainBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class MaintainAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainBean> dataList;
    private Activity mContext;
    private String user_name="状态：";
    private String type="完成维修";
    public void
    setDataList(List<MainBean> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public MaintainAdminAdapter(Activity mContext, List<MainBean> dataList) {
        this.mContext=mContext;
        this.dataList = dataList;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_item_admin, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

//    List<Integer> lists;
//    private void getRandomHeights(List<AnswerListBean> datas) {
//        lists = new ArrayList<>();
//        for (int i = 0; i < datas.size(); i++) {
//            lists.add((int) (200 + Math.random() * 400));
//        }
//    }

    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.time.setText(reHolder.bean.getSubmit_time().split(" ")[0]);
            reHolder.replce.setText(reHolder.bean.getAddress());
            reHolder.user.setText(reHolder.bean.getUser_name());
            reHolder.state.setText(reHolder.bean.getDealstate());
            switch (reHolder.bean.getDealstate()){
                case "完成维修":
                    reHolder.state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
                case "拒绝维修":
                    reHolder.state.setTextColor(mContext.getResources().getColor(R.color.DarkRed));
                    break;
                default:
                    reHolder.state.setTextColor(Color.rgb(255,102,51));
                    break;
            }
            GlideTools.chanCircle(mContext, reHolder.bean.getUser_avatar(), reHolder.head_icon, R.drawable.radius_oval_gray);


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

        RelativeLayout layout;

        ImageView head_icon;
        TextView user;
        TextView time;
        TextView state;
        TextView replce;
        MainBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            layout= (RelativeLayout) itemView.findViewById(R.id.maintain_item_layout);
            time= (TextView) itemView.findViewById(R.id.maintain_item_time);
            head_icon= (ImageView) itemView.findViewById(R.id.main_head_icon);
            replce= (TextView) itemView.findViewById(R.id.maintain_content);
            user= (TextView) itemView.findViewById(R.id.maintain_user);
            state= (TextView) itemView.findViewById(R.id.maintain_new_state);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, MaintainNewDetailAdminActivity.class);
                    Bundle b=new Bundle();
                    b.putParcelable(TagFinal.OBJECT_TAG,bean);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
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
