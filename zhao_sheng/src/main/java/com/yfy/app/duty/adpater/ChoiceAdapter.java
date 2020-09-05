package com.yfy.app.duty.adpater;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.DutyAddActivity;
import com.yfy.app.duty.DutyNotAddActivity;
import com.yfy.app.duty.bean.PlaneA;
import com.yfy.app.duty.bean.PlaneInfo;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class ChoiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<PlaneA> dataList;
    private Activity mContext;
    private String type="yyyy年MM月dd";
    public void setDataList(List<PlaneA> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_time_one, parent, false);
            return new ParentViewHolder(view);

        }
        if (viewType == TagFinal.TWO_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_time_two, parent, false);
            return new ChildViewHolder(view);

        }
        if (viewType == TagFinal.THREE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt, parent, false);
            return new TopViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder reHolder = (ParentViewHolder) holder;
            reHolder.bean=dataList.get(position);

            reHolder.name.setText(reHolder.bean.getTermname()
                    +" "+reHolder.bean.getWeekname()
                    +" "+DateUtils.getWeek(reHolder.bean.getDate())
                    +" "+DateUtils.changeDate(reHolder.bean.getDate(), type));
            if (reHolder.bean.getIsedit().equals(TagFinal.TRUE)){
                reHolder.type.setText("可编辑");
                reHolder.type.setTextColor(ColorRgbUtil.getOrange());
            }else{
                reHolder.type.setText("不可编辑");
                reHolder.type.setTextColor(ColorRgbUtil.getGrayText());
            }
        }
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childHolder = (ChildViewHolder) holder;
            childHolder.bean=dataList.get(position);
            childHolder.name.setText(childHolder.bean.getDutyreporttype());
            childHolder.state.setText("已检查:"+childHolder.bean.getCheckcount()+"/"+childHolder.bean.getOptioncount());
            if (childHolder.bean.getCheckcount().equals(childHolder.bean.getOptioncount())){
                childHolder.state.setTextColor(ColorRgbUtil.getGreen());
            }else{
                childHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
            }

            if (childHolder.bean.getIssummarize().equals(TagFinal.TRUE)){
                childHolder.content.setText("已填写");
                childHolder.content.setTextColor(ColorRgbUtil.getGreen());
            }else{
                childHolder.content.setText("未填写");
                childHolder.content.setTextColor(ColorRgbUtil.getBaseColor());
            }


            if (childHolder.bean.getIsedit().equals(TagFinal.TRUE)){
                childHolder.name.setTextColor(ColorRgbUtil.getBaseColor());
            }else{
                childHolder.name.setTextColor(ColorRgbUtil.getGrayText());
            }
        }
        if (holder instanceof TopViewHolder){
            TopViewHolder topHolder = (TopViewHolder) holder;
            topHolder.bean=dataList.get(position);
            topHolder.name.setTextColor(ColorRgbUtil.getGrayText());
            topHolder.name.setBackgroundColor(ColorRgbUtil.getWhite());
            topHolder.name.setText(topHolder.bean.getDate());
            topHolder.name.setTextSize(16f);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView type;
        PlaneA bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.duty_time_name);
            type= (TextView) itemView.findViewById(R.id.duty_time_type);
        }
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        TextView content;
        RelativeLayout layout;
        PlaneA bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.duty_time_name);
            state=  itemView.findViewById(R.id.duty_time_state);
            content=  itemView.findViewById(R.id.duty_time_content);
            layout=  itemView.findViewById(R.id.duty_time_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if (bean.getIsedit().equals(TagFinal.TRUE)){
                        intent=new Intent(mContext,DutyAddActivity.class);
                        intent.putExtra(TagFinal.OBJECT_TAG,bean);
                        mContext.startActivityForResult(intent,TagFinal.UI_ADD);
                    }else{
                        intent=new Intent(mContext,DutyNotAddActivity.class);
                        intent.putExtra(TagFinal.OBJECT_TAG,bean);
                        mContext.startActivityForResult(intent,TagFinal.UI_ADD);
                    }

                }
            });
        }
    }
    private class TopViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        PlaneA bean;
        TopViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.public_center_txt_add);

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (timeChioce!=null){
                        timeChioce.chioceTime();
                    }
                }
            });
        }
    }


    private  TimeChioce timeChioce;

    public void setTimeChioce(TimeChioce timeChioce) {
        this.timeChioce = timeChioce;
    }

    public interface TimeChioce{
        void chioceTime();
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
