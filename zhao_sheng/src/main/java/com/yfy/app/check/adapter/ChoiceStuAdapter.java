package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.CheckStuParentDetailActivity;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class ChoiceStuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CheckStu> dataList;
    private Activity mContext;
    public void setDataList(List<CheckStu> dataList) {
        this.dataList = dataList;
    }

    public List<CheckStu> getDataList() {
        return dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceStuAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }
    private CheckState checkState;
    private DateBean dateBean;
    private ClasslistBean classlistBean;

    public void setBean(ClasslistBean classlistBean) {
        this.classlistBean = classlistBean;
    }

    public void setState(CheckState state) {
        this.checkState = state;
    }

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.TYPE_TOP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_gridview_item, parent, false);
            return new StuHolder(view);
        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StuHolder){
            StuHolder child = (StuHolder) holder;
            child.index=position;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getUsername());
            child.state.setText(child.bean.getIllstate());
//            if (child.bean.getStuid().equals("$")){
//                child.head.setVisibility(View.GONE);
//                child.select.setVisibility(View.GONE);
//            }else{
            child.head.setVisibility(View.VISIBLE);
            GlideTools.chanCircle(mContext, Base.RETROFIT_URI+child.bean.getHeadpic(), child.head, R.drawable.head_user);
            if (child.bean.getIllid().equals("0")){
                child.state.setTextColor(ColorRgbUtil.getForestGreen());
            }else{
                child.state.setTextColor(ColorRgbUtil.getOrange());
            }
//            }


        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class StuHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView state;
        public AppCompatImageView select;
        private RelativeLayout layout;
        private ImageView head;
        private int index;
        private CheckStu bean;
        public StuHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.stu_grid_name);
            state =  itemView.findViewById(R.id.stu_grid_state);
            head =  itemView.findViewById(R.id.stu_grid_head);
            select =  itemView.findViewById(R.id.stu_grid_select);
            layout=itemView.findViewById(R.id.stu_grid_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                    Intent intent=new Intent(mContext,CheckStuParentDetailActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG, bean);
                    intent.putExtra(TagFinal.TYPE_TAG, dateBean);
                    intent.putExtra(TagFinal.type, checkState);
                    intent.putExtra(Base.state, true);

                    intent.putExtra(TagFinal.CLASS_BEAN, classlistBean);
                    mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);

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
