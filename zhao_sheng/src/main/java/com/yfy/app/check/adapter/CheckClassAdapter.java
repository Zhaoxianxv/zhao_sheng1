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
import com.yfy.app.check.bean.CheckState;
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
public class CheckClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<ClasslistBean> dataList;
    private DateBean dateBean;
    private Activity mContext;

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    public void setDataList(List<ClasslistBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckClassAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_class_item, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getGroupname());
//            reHolder.type.setText(StringUtils.getTextJoint("异常:%1$s",reHolder.bean.getIllcount()));
            reHolder.setChild(reHolder.bean.getIlllist(),reHolder.flow  );
            reHolder.setChild(reHolder.bean.getStates(),reHolder.flow_one,true  );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        FlowLayout flow;
        FlowLayout flow_one;
        ClasslistBean bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_class_name);
            type=  itemView.findViewById(R.id.check_class_num);
            flow=  itemView.findViewById(R.id.check_class_num_flow);
            flow_one=  itemView.findViewById(R.id.check_class_one);
//            name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //单/多选

//                }
//            });

        }

        public void setChild(List<IllType> states, FlowLayout flow){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            for (final IllType state:states){
                final TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flow, false);
                tv.setText(StringUtils.getTextJoint("%1$s:%2$s",state.getIlltypename(),state.getIlltypecount()));
                if (state.getIlltypecount().equals("0")){
                    tv.setTextColor(ColorRgbUtil.getGrayText());
                }else{
                    tv.setTextColor(ColorRgbUtil.getOrangeRed());
                }
                flow.addView(tv);
            }
        }

        public void setChild(List<CheckState> states, FlowLayout flow, Boolean is){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            int width= ScreenTools.instance(mContext).getScreenWidth();

            for (final CheckState state:states){
                Button tv = (Button) mInflater.inflate(R.layout.check_flowlayout_button,flow, false);
                tv.setText(state.getStatetitle());
                tv.setWidth(width/2-30);
//                ScreenTools.instance(mContext).getScreenHeight()
                int tint;
                if (state.getState().equals("未检测")){
                    tint=ColorRgbUtil.getOrange();
                    tv.setTextColor(ColorRgbUtil.getWhite());
                }else{
                    tint=ColorRgbUtil.getForestGreen();
                    tv.setTextColor(ColorRgbUtil.getWhite());
                }
                tv .getBackground().setColorFilter(tint, PorterDuff.Mode.DARKEN);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext,CheckStuActivity.class);
                        intent.putExtra(TagFinal.OBJECT_TAG,bean);
                        intent.putExtra(TagFinal.ID_TAG,state);
                        intent.putExtra(TagFinal.CLASS_BEAN,dateBean);
                        mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
                    }
                });
                flow.addView(tv);
            }
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
