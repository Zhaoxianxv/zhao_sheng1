package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.CheckGetStuToIllActivity;
import com.yfy.app.check.bean.IllChild;
import com.yfy.app.check.bean.IllGroup;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckTjParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<IllGroup> dataList;
    private Activity mContext;

    public void setDataList(List<IllGroup> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckTjParentAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_tj_parent_layout, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.user_class.setText(reHolder.bean.getStatisticsname());
            reHolder.setChild(reHolder.bean.getStatisticslist(),reHolder.user_name  );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        FlowLayout user_name;
        TextView user_class;
        RelativeLayout layout;
        IllGroup bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            user_name=  itemView.findViewById(R.id.check_tj_ill_flow );
            user_class=  itemView.findViewById(R.id.check_tj_main_name);


        }

        public void setChild(List<IllChild> states,FlowLayout flowLayout){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flowLayout.getChildCount()!=0){
                flowLayout.removeAllViews();
            }
            for (final IllChild state:states){
                final TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flowLayout, false);
                tv.setText(StringUtils.getTextJoint("%1$s:%2$s",state.getStatisticstype(),state.getStatisticscount()));
                switch (state.getStatisticstype()){
                    case "实到人数":
                        if (state.getStatisticscount().equals(states.get(0).getStatisticscount())){
                            tv.setTextColor(ColorRgbUtil.getBaseText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getOrange());
                        }
                        break;
                    case "应到人数":
                        tv.setTextColor(ColorRgbUtil.getBaseText());
                        break;
                    case "缺勤人数":
                        if (state.getStatisticscount().equals("0")){
                            tv.setTextColor(ColorRgbUtil.getBaseText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getOrange());
                        }
                        break;
                    default:
                        if (state.getStatisticscount().equals("0")){
                            tv.setTextColor(ColorRgbUtil.getGrayText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getMaroon());
                        }
                        break;
                }

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (state.getStatisticstypeid()==null)return;

                        Intent intent=new Intent(mContext,CheckGetStuToIllActivity.class);
                        intent.putExtra(Base.type, state.getStatisticstypeid());
                        intent.putExtra(Base.id, bean.getStatisticsid());
                        intent.putExtra(Base.date, dateBean);
                        intent.putExtra(Base.title, bean.getStatisticsname());
                        mContext.startActivity(intent);

                    }
                });
                flowLayout.addView(tv);
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


    private DateBean dateBean;

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }
}
