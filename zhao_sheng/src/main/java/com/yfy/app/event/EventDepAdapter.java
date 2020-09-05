package com.yfy.app.event;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.event.bean.Dep;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class EventDepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Dep> dataList;
    private Activity mContext;

    public void setDataList(List<Dep> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public EventDepAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.ONE_INT;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_item_layout, parent, false);
            return new ParentViewHolder(view);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder reHolder = (ParentViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getDepartname());
            reHolder.type.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        Dep bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            type= (TextView) itemView.findViewById(R.id.selected_item_type);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选

                    Intent intent=new Intent();
                    intent.putExtra(TagFinal.OBJECT_TAG,bean );
                    mContext.setResult(Activity.RESULT_OK,intent);
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
