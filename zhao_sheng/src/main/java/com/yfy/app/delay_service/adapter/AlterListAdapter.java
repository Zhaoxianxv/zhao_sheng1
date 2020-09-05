package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.delay_service.bean.AbsentInfo;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class AlterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AbsentInfo> dataList;

    private Activity mContext;
    private boolean isselectType;

    public void setIsselectType(boolean isselectType) {
        this.isselectType = isselectType;
    }

    public void setDataList(List<AbsentInfo> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public AlterListAdapter(Activity mContext) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_alter_item, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean = dataList.get(position);
            reHolder.index=position;
            reHolder.name.setText(reHolder.bean.getElectivename());

            reHolder.type.setText(StringUtils.getTextJoint(
                    "考勤地点:%1$s——考勤人数:%2$s",
                    reHolder.bean.getElectiveaddr(),
                    reHolder.bean.getArrive_count()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }
    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        TextView button;
        RelativeLayout layout;
        int index;
        AbsentInfo bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.tag_item_txt);
            type=  itemView.findViewById(R.id.tag_item_adr);
            button=  itemView.findViewById(R.id.tag_item_button);
            layout=  itemView.findViewById(R.id.event_alter_layout);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tell!=null){
                        tell.tell(bean);
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


    private Tell tell;

    public void setTell(Tell tell) {
        this.tell = tell;
    }

    public interface Tell{
        void tell(AbsentInfo bean);
    }

}
