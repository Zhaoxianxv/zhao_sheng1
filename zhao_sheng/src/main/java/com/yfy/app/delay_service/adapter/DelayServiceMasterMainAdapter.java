package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.delay_service.DelayAbsClassDetailActivity;
import com.yfy.app.delay_service.bean.AbsStu;
import com.yfy.app.delay_service.bean.DelayAbsenteeismClass;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class DelayServiceMasterMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<DelayAbsenteeismClass> dataList;
    private Activity mContext;

    public void setDataList(List<DelayAbsenteeismClass> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DelayServiceMasterMainAdapter(Activity mContext){
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

        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_master_class, parent, false);
            return new ChildViewHolder(view);
        }

        if (viewType == TagFinal.ONE_INT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt_center, parent, false);
            return new TopViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder){
            ChildViewHolder cHolder = (ChildViewHolder) holder;
            cHolder.bean=dataList.get(position);
            cHolder.delay_name.setText(cHolder.bean.getElectivename());
            cHolder.class_name.setText(StringUtils.getTextJoint("上课地点:%1$s",cHolder.bean.getElectiveaddr()));
            cHolder.all_num.setText(StringUtils.getTextJoint("应到人数:%1$s", cHolder.bean.getStucount()));
            cHolder.in_num.setText(StringUtils.getTextJoint("缺勤人数:%1$d", cHolder.bean.getElective_stuetail().size()));
            if (cHolder.bean.getIscheck().equalsIgnoreCase(TagFinal.TRUE)){
                cHolder.type.setText("已考勤");
                cHolder.type.setTextColor(ColorRgbUtil.getForestGreen());
            }else{
                cHolder.type.setText("未考勤");
                cHolder.type.setTextColor(ColorRgbUtil.getGray());
            }
            if (cHolder.bean.getSituation_count().equalsIgnoreCase("0")){
                cHolder.in_num.setTextColor(ColorRgbUtil.getBaseText());
                cHolder.all_num.setTextColor(ColorRgbUtil.getForestGreen());
            }else{
                cHolder.in_num.setTextColor(ColorRgbUtil.getMaroon());
                cHolder.all_num.setTextColor(ColorRgbUtil.getGray());
            }
            List<KeyValue> keyValues=new ArrayList<>();

            for (AbsStu tea:cHolder.bean.getTeacher_list()){
                keyValues.add(new KeyValue(tea.getTeachername(), tea.getPhonenumber()));
            }
            if (StringJudge.isEmpty(keyValues)){
                cHolder.tea_flow.setVisibility(View.GONE);
            }else{
                cHolder.tea_flow.setVisibility(View.VISIBLE);
                cHolder.setTopFlow(keyValues);
            }

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        public DelayAbsenteeismClass bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            user= (TextView) itemView.findViewById(R.id.duty_main_item_user);

        }
    }

    public class TopViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        public DelayAbsenteeismClass bean;
        TopViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.public_top_text);
        }
    }
    public class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView delay_name;
        TextView class_name;
        TextView all_num;
        TextView in_num;
        TextView type;
        FlowLayout tea_flow;
        RelativeLayout layout;
        public DelayAbsenteeismClass bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            tea_flow=  itemView.findViewById(R.id.master_class_tea_list);
            layout=  itemView.findViewById(R.id.delay_master_class_layout);
            delay_name = itemView.findViewById(R.id.delay_master_class_name);
            class_name=  itemView.findViewById(R.id.master_class_name);
            all_num=  itemView.findViewById(R.id.delay_all_num);
            in_num=  itemView.findViewById(R.id.delay_abs_num);
            type=  itemView.findViewById(R.id.delay_abs_tag);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DelayAbsClassDetailActivity.class);

                    ArrayList<AbsStu> stu_list=new ArrayList<>();
                    ArrayList<AbsStu> tea_list=new ArrayList<>();

                    for (AbsStu abs:bean.getElective_stuetail()){
                        abs.setView_type(TagFinal.TYPE_ITEM);
                        stu_list.add(abs);
                    }
                    for (AbsStu abs:bean.getElective_classdetail()){
                        abs.setView_type(TagFinal.TYPE_TOP);
                        tea_list.add(abs);
                    }

                    intent.putParcelableArrayListExtra(Base.data, stu_list);
                    intent.putParcelableArrayListExtra(Base.reason, tea_list);
                    intent.putExtra(Base.date, dateBean);
                    intent.putExtra(Base.title,bean);
                    mContext.startActivity(intent);
                }
            });

        }

        private void setTopFlow(List<KeyValue> top_jz){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (tea_flow.getChildCount()!=0){
                tea_flow.removeAllViews();
            }
            for (KeyValue bean:top_jz){
                RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.public_detail_top_item,tea_flow, false);
//            RelativeLayout parent_layout=layout.findViewById(R.id.seal_detail_txt_layout);
                TextView key=layout.findViewById(R.id.seal_detail_key);
                final TextView value=layout.findViewById(R.id.seal_detail_value);
                RatingBar ratingBar=layout.findViewById(R.id.seal_detail_value_star);

                key.setTextColor(ColorRgbUtil.getBaseText());
                value.setTextColor(ColorRgbUtil.getBlue());
                key.setText(StringUtils.getTextJoint("%1$s : ",bean.getKey()));
                switch (bean.getType()){
                    case "rating":
                        if (bean.getValue().equals("")){
                            ratingBar.setRating(0f);
                        }else{
                            ratingBar.setRating(ConvertObjtect.getInstance().getFloat(bean.getValue()));
                        }
                        ratingBar.setVisibility(View.VISIBLE);
                        value.setVisibility(View.GONE);
                        break;
                    default:
                        value.setText(bean.getValue());
                        ratingBar.setVisibility(View.GONE);
                        value.setVisibility(View.VISIBLE);
                        break;
                }


                value.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tell=value.getText().toString();
                        Logger.e(tell);
                        if (tell.equals("未录入"))return;
                        if (dotype!=null){
                            dotype.tellPhone(tell);
                        }
                    }
                });
                tea_flow.addView(layout);
            }
        }


    }


    public Dotype dotype;

    public void setDotype(Dotype dotype) {
        this.dotype = dotype;
    }

    public interface Dotype{
        void tellPhone(String phone);
    }

    private DateBean dateBean;

    public void setDateBean(DateBean dateBean) {
        this.dateBean = dateBean;
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
