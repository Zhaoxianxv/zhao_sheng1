package com.yfy.app.delay_service.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.ChoiceClassActivity;
import com.yfy.app.delay_service.EventStuActivity;
import com.yfy.app.delay_service.bean.AbsentInfo;
import com.yfy.app.delay_service.bean.EventTea;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.textView.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class ChoiceClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AbsentInfo> dataList;

    private ChoiceClassActivity mContext;

    public void setDataList(List<AbsentInfo> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceClassAdapter(ChoiceClassActivity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
//        return dataList.get(position).getView_type();
        return TagFinal.TYPE_CHILD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_item_class, parent, false);
            return new RecyclerViewHolder(view);
        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_item_layout, parent, false);
            return new ParentHolder(view);
        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;

            reHolder.bean = dataList.get(position);
            reHolder.name.setText(reHolder.bean.getElectivename());
            setChild(reHolder.bean.getTeacher_list(),reHolder.flow ,reHolder.bean);
            reHolder.adr.setText(StringUtils.getTextJoint("上课地点:%1$s",reHolder.bean.getElectiveaddr()));

        }
        if (holder instanceof ParentHolder) {
            ParentHolder parent = (ParentHolder) holder;

            parent.bean = dataList.get(position);
            parent.name.setText(parent.bean.getGradename());
            parent.tag.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        FlowLayout flow;

        RelativeLayout layout;
        TextView name;
        TextView adr;
        AbsentInfo bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.event_class_layout);
            flow=  itemView.findViewById(R.id.event_class_item_flow);
            name=  itemView.findViewById(R.id.tag_item_txt);
            adr=  itemView.findViewById(R.id.tag_item_adr);
        }
    }
    private class ParentHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView tag;
        AbsentInfo bean;
        ParentHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.selected_item_name);
            tag=itemView.findViewById(R.id.selected_item_type);
        }
    }

    public  Dotype dotype;

    public void setDotype(Dotype dotype) {
        this.dotype = dotype;
    }

    public interface Dotype{
        void tellPhone(String phone);
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



    public void setChild(final List<EventTea> list, FlowLayout flow,final AbsentInfo bean){
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (int i=0;i<list.size();i++){
            View view =  mInflater.inflate(R.layout.delay_service_tea_flow,flow, false);
            RelativeLayout layout= view.findViewById(R.id.event_tea_name_layout);

            TextView name= view.findViewById(R.id.event_tea_name);
            TextView one= view.findViewById(R.id.event_tea_one);
            TextView two= view.findViewById(R.id.event_tea_two);
            TextView three= view.findViewById(R.id.event_tea_three);
            final TextView phone= view.findViewById(R.id.event_tea_phone);
            ImageView head= view.findViewById(R.id.event_tea_head);


            GlideTools.chanCircle(mContext,list.get(i).getHeadpic()  ,head ,R.drawable.head_user);
            name.setText(list.get(i).getTeachername());
            one.setText(StringUtils.getTextJoint("应到人数:%1$s",list.get(i).getElectivestucount()));
            two.setText(StringUtils.getTextJoint("实到人数:%1$s",list.get(i).getElectivestuarrivebytea()));
            three.setVisibility(View.GONE);
//            three.setText(StringUtils.getTextJoint("巡查:%1$s",list.get(i).getElectivestuarrive()));
            phone.setText(StringJudge.isEmpty(list.get(i).getPhonenumber())?"未录入":list.get(i).getPhonenumber());
            phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tell=phone.getText().toString();
                    Logger.e(tell);
                    if (tell.equals("未录入"))return;
                    if (dotype!=null){
                        dotype.tellPhone(tell);
                    }
                }
            });

            phone.setTextColor(ColorRgbUtil.getMediumBlue());
            if (list.get(i).getElectivestucount().equals(list.get(i).getElectivestuarrivebytea())){
                two.setTextColor(ColorRgbUtil.getDrakGreen());
            }else{
                two.setTextColor(ColorRgbUtil.getOrangeRed());
            }
            if (list.get(i).getElectivestucount().equals(list.get(i).getElectivestuarrive())){
                three.setTextColor(ColorRgbUtil.getDrakGreen());
            }else{
                three.setTextColor(ColorRgbUtil.getOrangeRed());
            }
            if (list.get(i).getElectivestuarrive().equals("0")){
                three.setTextColor(ColorRgbUtil.getGrayText());
            }
            if (list.get(i).getElectivestuarrivebytea().equals("0")){
                two.setTextColor(ColorRgbUtil.getGrayText());
            }

            final EventTea tea=list.get(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,EventStuActivity.class);
                    Bundle b=new Bundle();
                    b.putInt("index_position", index_position);
                    b.putParcelable(Base.data,bean);
                    b.putString(Base.id,tea.getTeacherid());
                    b.putParcelable(Base.date, dateBean);
                    b.putString(Base.can_edit, canedit);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
                }
            });
            flow.addView(view);
        }
    }


    private DateBean dateBean;
    private String canedit;
    private int index_position=0;


    public void setIndex_position(int index_position) {
        this.index_position = index_position;
    }

    public void setCanedit(String canedit) {
        this.canedit = canedit;
    }

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
    }
}
