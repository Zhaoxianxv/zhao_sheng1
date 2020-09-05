package com.yfy.app.process.adapter;

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
import com.yfy.app.process.ProcessDetailActivity;
import com.yfy.app.process.ProcessRoomDetailActivity;
import com.yfy.app.process.bean.ProcessMainBean;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;


public class ProcessMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProcessMainBean> dataList;
    private Activity mContext;
    public void
    setDataList(List<ProcessMainBean> dataList) {

        this.dataList = dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public ProcessMainAdapter(Activity mContext, List<ProcessMainBean> dataList) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.process_main_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.title.setText(reHolder.bean.getRealname());
            reHolder.title_sub.setText(reHolder.bean.getAdddate());
            reHolder.content.setText(reHolder.bean.getTitle());
            reHolder.right_state.setText(reHolder.bean.getState());
            String color_s=StringJudge.isEmpty(reHolder.bean.getStatecolor())?"696969":reHolder.bean.getStatecolor();
            reHolder.right_state.setTextColor(Color.parseColor(StringUtils.getTextJoint("#%1$s", color_s)));
            GlideTools.chanCircle(mContext,reHolder.bean.getHeadPic(),reHolder.head, R.drawable.head_user);
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
        ImageView head;
        TextView title;
        TextView title_sub;
        TextView right_state;
        TextView content;
        ProcessMainBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.process_item_layout);
            right_state =  itemView.findViewById(R.id.process_right_state);
            head =  itemView.findViewById(R.id.process_head_icon);
            title_sub =  itemView.findViewById(R.id.process_title_sub);
            title =  itemView.findViewById(R.id.process_title);
            content =  itemView.findViewById(R.id.process_content);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (Base.process_type){
                        case "functionrooms":
                            intent=new Intent(mContext, ProcessRoomDetailActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putParcelable(Base.data,bean);
                            bundle.putBoolean(Base.type,false);
                            intent.putExtras(bundle);
                            mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
                            break;
                            default:
                                intent=new Intent(mContext, ProcessDetailActivity.class);
                                Bundle b=new Bundle();
                                b.putParcelable(Base.data,bean);
                                b.putBoolean(Base.type,false);
                                intent.putExtras(b);
                                mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
                                break;
                    }

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
            pbLoading =  itemView.findViewById(R.id.pb_loading);
            tvLoading =  itemView.findViewById(R.id.tv_loading);
            llEnd =  itemView.findViewById(R.id.ll_end);
            allEnd =  itemView.findViewById(R.id.recycler_bottom);

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
