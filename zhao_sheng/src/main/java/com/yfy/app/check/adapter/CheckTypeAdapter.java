package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.CheckStuActivity;
import com.yfy.app.check.CheckStuAddParentActivity;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.app.check.bean.IllType;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ScreenTools;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private CheckStu checkStu;
    private DateBean dateBean;
    private ClasslistBean classlistBean;
    private CheckState checkState;

    public void setCheckStu(CheckStu checkStu) {
        this.checkStu = checkStu;
    }

    public void
    setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    public void setClasslistBean(ClasslistBean classlistBean) {
        this.classlistBean = classlistBean;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    private List<IllType> dataList;
    private Activity mContext;

    public void setDataList(List<IllType> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckTypeAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getView_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_parent_l, parent, false);
            return new ParentHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item_lefttxt_back, parent, false);
            return new ChildHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ParentHolder) {
            ParentHolder reHolder = (ParentHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getIlltypegroupname());
        }
        if (holder instanceof ChildHolder) {
            ChildHolder child = (ChildHolder) holder;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getIlltypename());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class ParentHolder extends RecyclerView.ViewHolder {
        TextView name;
        IllType bean;
        ParentHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_parent_name);
        }
    }

    private class ChildHolder extends RecyclerView.ViewHolder {
        TextView name;
        IllType bean;
        ChildHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.public_xlist_left_txt);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent intent=new Intent(mContext,CheckStuAddParentActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG, checkStu);
                    intent.putExtra(TagFinal.TYPE_TAG, dateBean);
                    intent.putExtra(TagFinal.CLASS_BEAN, classlistBean);
                    intent.putExtra(TagFinal.type, checkState);
                    intent.putExtra(TagFinal.ID_TAG, bean);
                    mContext.startActivityForResult(intent,TagFinal.UI_ADD);
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
