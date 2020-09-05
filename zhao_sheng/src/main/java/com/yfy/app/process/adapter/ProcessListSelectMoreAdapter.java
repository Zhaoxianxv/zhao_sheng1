package com.yfy.app.process.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;


public class ProcessListSelectMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<KeyValue> dataList;
    private Activity mContext;

    public List<KeyValue> getDataList() {
        return dataList;
    }

//    public void addItemData(int position,List<ProLayerTwoData> two){
//        dataList.addAll(position, two);
//        Logger.e(two.size()+"");
//        notifyItemRangeInserted(position+1, two.size());
//    }
//    public void removeItemData(String id) {
//        int n=0;
//        for (ProLayerTwoData proLayerTwoData:dataList){
//            if (id.equalsIgnoreCase(String.valueOf((long) proLayerTwoData.getId()))){
//                Logger.e(id);
//                dataList.remove(n);
//                notifyItemRangeRemoved(n, 1);
//            }
//            n++;
//        }
//    }
    public void setDataList(List<KeyValue> dataList) {
        this.dataList = dataList;
    }

    public ProcessListSelectMoreAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getView_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TagFinal.TYPE_SELECT_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_txt_check_layout, parent, false);
            return new ProSelectSingleH(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProSelectSingleH) {
            ProSelectSingleH selectSingleH = (ProSelectSingleH) holder;
            selectSingleH.index_position=position;
            selectSingleH.bean= dataList.get(position);
            selectSingleH.apply_name.setText(StringUtils.getTextJoint("%1$s\t",selectSingleH.bean.getName()));
            if (selectSingleH.bean.getType().equalsIgnoreCase(TagFinal.TRUE)){
                selectSingleH.apply_value.setColorFilter(ColorRgbUtil.getBaseColor());
                GlideTools.loadImage(mContext,R.drawable.ic_radio_button_checked,selectSingleH.apply_value);
            }else {
                selectSingleH.apply_value.setColorFilter(ColorRgbUtil.getGray());
                GlideTools.loadImage(mContext,R.drawable.ic_radio_button_unchecked,selectSingleH.apply_value);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ProSelectSingleH extends RecyclerView.ViewHolder {
        AppCompatTextView apply_name;
        AppCompatImageView apply_value;
        RelativeLayout layout;
        KeyValue bean;
        int index_position;
        ProSelectSingleH(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_txt_check_left_title);
            apply_value=  itemView.findViewById(R.id.public_txt_check_right_value);
            layout=  itemView.findViewById(R.id.public_txt_check_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.getType().equalsIgnoreCase(TagFinal.TRUE)){
                        bean.setType(TagFinal.FALSE);
                    }else{
                        bean.setType(TagFinal.TRUE);
                    }
                    notifyItemChanged(index_position,bean);
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
        notifyDataSetChanged();
    }





}
