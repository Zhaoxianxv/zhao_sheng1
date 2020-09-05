package com.yfy.app;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private List<KeyValue> dataList;
    private int loadState = 2;

    public MainAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    public void setDataList(List<KeyValue> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getView_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.oa_gridview_item, parent, false);
            return new ItemHolder(view);
        }
        if (viewType == TagFinal.TYPE_TOP) {
            View tagView = LayoutInflater.from(parent.getContext()).inflate(R.layout.oa_gridview_top, parent, false);
            return new TagHolder(tagView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder){
            ItemHolder iHolder = (ItemHolder) holder;
            iHolder.bean = dataList.get(position);
            iHolder.index=position;
            iHolder.name.setText(iHolder.bean.getName());
            iHolder.icon.setImageResource(iHolder.bean.getRes_image());
            if (StringJudge.isEmpty(iHolder.bean.getKey())){
                GlideTools.loadImage(mContext,iHolder.bean.getRes_image(),iHolder.icon);
            }else{
                GlideTools.loadImage(mContext,Base.RETROFIT_URI+iHolder.bean.getKey(),iHolder.icon);
            }
            iHolder.icon.setColorFilter(iHolder.bean.getRes_color());

            iHolder.count.setVisibility(View.VISIBLE);
            if (iHolder.bean.getMax().equalsIgnoreCase("0")) iHolder.count.setVisibility(View.GONE);
            if (StringJudge.isEmpty(iHolder.bean.getMax())) iHolder.count.setVisibility(View.GONE);
//            GradientDrawable myGrad = (GradientDrawable)itemHolder.icon.getBackground();
//            myGrad.setColor(Color.parseColor(itemHolder.bean.getKey()));
        }
        if (holder instanceof TagHolder){
            TagHolder tagHolder = (TagHolder) holder;
            tagHolder.bean = dataList.get(position);
            tagHolder.tag.setText(tagHolder.bean.getName());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }


    private class ItemHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView count;
        public AppCompatImageView icon;
        private RelativeLayout layout;
        private int index;
        KeyValue bean;
        public ItemHolder(View itemView) {
            super(itemView);
            count =  itemView.findViewById(R.id.oa_item_admin_count);
            name =  itemView.findViewById(R.id.oa_item_name);
            icon =  itemView.findViewById(R.id.oa_item_icon_image);
            layout = itemView.findViewById(R.id.oa_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemOnc!=null){
                        itemOnc.onc(bean);
                    }
                }
            });
        }
    }

    private class TagHolder extends RecyclerView.ViewHolder {

        KeyValue bean;
        AppCompatTextView tag;
        public TagHolder(View itemView) {
            super(itemView);
            tag=itemView.findViewById(R.id.oa_grid_view);
            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tag.getText().equals("学生综合评价")){

                    }else{
                        if (itemOnc!=null){
                            itemOnc.onLoad();
                        }
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


    public interface ItemOnc{
        void onc(KeyValue bean);
        void onLoad();
    }

    public ItemOnc itemOnc;

    public void setItemOnc(ItemOnc itemOnc) {
        this.itemOnc = itemOnc;
    }
}
