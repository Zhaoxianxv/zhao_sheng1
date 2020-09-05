package com.yfy.app.tea_event.adapter;

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
import com.yfy.app.attennew.bean.Subject;
import com.yfy.app.tea_event.TeaDActivity;
import com.yfy.app.tea_event.TeaDetailsActivity;
import com.yfy.app.tea_event.bean.TeaClass;
import com.yfy.app.tea_event.bean.Teacher;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TeaClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<TeaClass> dataList;
    private Activity mContext;

    private String term_id;

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public void setDataList(List<TeaClass> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public TeaClassAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_class, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getCoursename());
            reHolder.state.setText(reHolder.bean.getClassname());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        RelativeLayout layout;
        TeaClass bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tea_event_name);
            state=  itemView.findViewById(R.id.tea_event_state);
            layout=  itemView.findViewById(R.id.tea_event_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent intent=new Intent(mContext,TeaDActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
                    intent.putExtra(TagFinal.ID_TAG, term_id);
                    mContext.startActivityForResult(intent,TagFinal.UI_TAG);
                }
            });//李瑞华

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
