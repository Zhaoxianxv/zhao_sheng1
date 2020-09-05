package com.yfy.app.seal.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.seal.SealDetailNewActivity;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class SealAdminAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<SealMainBean> dataList;
    private Activity mContext;

    public void setDataList(List<SealMainBean> dataList) {
        this.dataList = dataList;
    }

    public SealAdminAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seal_main_item, parent, false);
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
            reHolder.name.setText(StringUtils.getTextJoint("%1$s(%2$s-%3$s)",
                    reHolder.bean.getProposername(),
                    reHolder.bean.getSignetname(),
                    reHolder.bean.getTypename()));
            reHolder.state.setText(reHolder.bean.getStatus());
            reHolder.time.setVisibility(View.GONE);
            String reason;

            if (reHolder.bean.getTypename().equals("借章")){
                reason = StringUtils.getTextJoint("%1$s~%2$s",
                        reHolder.bean.getStartdate(), reHolder.bean.getEnddate());
            }else{
                reason = StringUtils.getTextJoint("%1$s", reHolder.bean.getStartdate());
            }
            reHolder.reason.setText(reHolder.bean.getTitle());
            reHolder.date.setText(reason);
            reHolder.date.setTextColor(ColorRgbUtil.getGrayText());
            GlideTools.chanCircle(mContext, reHolder.bean.getProposerheadpic(), reHolder.head,R.drawable.head_user );
            switch (reHolder.bean.getStatus()) {
                case "待审核":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                    break;
                case "待盖章":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "待取章":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "已取章":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "未通过":
                    reHolder.state.setTextColor(ColorRgbUtil.getMaroon());
                    break;
                case "已还章":
                    reHolder.state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
                case "已盖章":
                    reHolder.state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView name;
        TextView state;
        TextView date;
        TextView reason;
        TextView time;
        RelativeLayout layout;
        SealMainBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            head=  itemView.findViewById(R.id.seal_main_item_head);
            name=  itemView.findViewById(R.id.seal_main_item_name);
            time=  itemView.findViewById(R.id.seal_main_item_time);
            state=  itemView.findViewById(R.id.seal_main_item_state);
            date=  itemView.findViewById(R.id.seal_main_item_date);
            reason=  itemView.findViewById(R.id.seal_main_item_reason);
            layout=  itemView.findViewById(R.id.maintain_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getStatus()){
                        case "未通过":
                            do_tpe=TagFinal.FALSE;
                        case "已还章":
                            do_tpe=TagFinal.FALSE;
                            break;
                        case "已盖章":
                            do_tpe=TagFinal.FALSE;
                            break;
                    }

                    //单/多选
                    Intent data=new Intent(mContext,SealDetailNewActivity.class);
                    data.putExtra(TagFinal.OBJECT_TAG,bean.getId());
                    data.putExtra(TagFinal.TYPE_TAG,"admin");
                    mContext.startActivityForResult(data,TagFinal.UI_REFRESH );
                }
            });

        }
    }

    private String do_tpe;

    public void setDo_tpe(String do_tpe) {
        this.do_tpe = do_tpe;
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        notifyDataSetChanged();
    }









}
