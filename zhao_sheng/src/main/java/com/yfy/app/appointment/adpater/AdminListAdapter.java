package com.yfy.app.appointment.adpater;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.AdminOrderActivity;
import com.yfy.app.appointment.OrderAdminDoActivity;
import com.yfy.app.appointment.bean.OrderBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.List;

/**
 * Created by yfy1 on 2017/2/10.
 */

public class AdminListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private String ty="等级：";
    private String st="已通过";
    private List<OrderBean> dataList;
    private AdminOrderActivity mContext;

    public void setDataList(List<OrderBean> dataList) {

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

    public AdminListAdapter(AdminOrderActivity mContext, List<OrderBean> dataList) {
        this.mContext=mContext;
        this.dataList = dataList;

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_admin_xlist_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
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
            reHolder.room_name.setText(reHolder.bean.getRoom());

            String time="";
            if (reHolder.bean!=null){
                if (reHolder.bean.getDate().length()>5){
                    time =reHolder.bean.getDate().substring(5);
                }

            }

            reHolder.time.setText("("+time.replaceAll("/","月")+"·"+reHolder.bean.getTime()+")");
            reHolder.apply_time.setText(reHolder.bean.getApplydate());
            reHolder.state.setText(reHolder.bean.getStatus());
            reHolder.version.setText(ty+reHolder.bean.getTypename());


            switch (reHolder.bean.getStatus().trim()){
                case "已通过":
                    reHolder.state.setTextColor(ColorRgbUtil.getGreen());
                    break;
                case "已拒绝":
                    reHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
                    break;
                case "申请中":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                default:
                    if (reHolder.bean.getCanedit().equals(TagFinal.TRUE)){
                        reHolder.state.setTextColor(ColorRgbUtil.getOrangeRed());
                    }else{
                        reHolder.state.setTextColor(Color.GRAY );
                    }
                    break;
            }

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    footViewHolder.allEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END: // 加载到底
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




        TextView room_name;
        TextView state;
        TextView apply_time;
        TextView time;
        TextView version;
        RelativeLayout  layout;
        OrderBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);


            state= (TextView) itemView.findViewById(R.id.order_admin_type_tv);
            room_name= (TextView) itemView.findViewById(R.id.order_room_name);
            time= (TextView) itemView.findViewById(R.id.order_admin_time_tv);
            apply_time= (TextView) itemView.findViewById(R.id.order_admin_apply_time_tv);
            version= (TextView) itemView.findViewById(R.id.order_admin_version_tv);
            layout= (RelativeLayout) itemView.findViewById(R.id.order_admin_item_layout);




            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext, OrderAdminDoActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ID_TAG,bean.getId());
                    if(bean.getCanedit().equals(TagFinal.TRUE)){
                        b.putBoolean(TagFinal.DELETE_TAG,true);
                    }else{
                        b.putBoolean(TagFinal.DELETE_TAG,false);
                    }
                    b.putBoolean("is_maintain",false);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent,TagFinal.UI_CONTENT);
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
