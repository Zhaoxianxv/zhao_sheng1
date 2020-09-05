package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.check.CheckTjStuActivity;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.IllChild;
import com.yfy.app.check.bean.IllGroup;
import com.yfy.app.check.bean.MasterUser;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckTjAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<IllGroup> dataList;
    private Activity mContext;

    public void setDataList(List<IllGroup> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckTjAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_tj_layout, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.user_class.setText(reHolder.bean.getGroupname());
//            reHolder.type.setText(StringUtils.getTextJoint("异常:%1$s",reHolder.bean.getIllcount()));
            reHolder.setChild(reHolder.bean.getIllstatistics(),reHolder.flow_type  );
            reHolder.setChild(reHolder.bean.getStates(),reHolder.flow_state,true  );
            reHolder.setChild(reHolder.bean.getGroupmaster(),reHolder.user_name,""  );
//            GlideTools.chanCircle(mContext, reHolder.bean.get, , );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        FlowLayout user_name;
        TextView user_class;
        FlowLayout flow_type;
        FlowLayout flow_state;
        RelativeLayout layout;
        IllGroup bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            user_name=  itemView.findViewById(R.id.check_tj_user_name);
            user_class=  itemView.findViewById(R.id.check_tj_user_class);
            flow_state=  itemView.findViewById(R.id.check_tj_state_flow);
            flow_type=  itemView.findViewById(R.id.check_tj_ill_flow);
            layout=  itemView.findViewById(R.id.check_tj_group);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选

                    Intent intent=new Intent(mContext, CheckTjStuActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
                    mContext.startActivity(intent);

                }
            });

        }

        public void setChild(List<IllChild> states, FlowLayout flow){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            for (IllChild state:states){
                final TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flow, false);
                tv.setText(StringUtils.getTextJoint("%1$s:%2$s",state.getIlltypename(),state.getIllcount()));
                tv.setTextColor(ColorRgbUtil.getGrayText());
                switch (state.getIlltypename()){
                    case "实到人数":
                        if (state.getIllcount().equals(states.get(0).getIllcount())){
                            tv.setTextColor(ColorRgbUtil.getBaseText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getOrange());
                        }
                        break;
                    case "应到人数":
                        tv.setTextColor(ColorRgbUtil.getBaseText());

                        break;
                    case "缺勤人数":
                        if (state.getIllcount().equals("0")){
                            tv.setTextColor(ColorRgbUtil.getBaseText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getOrange());
                        }
                        break;
                    default:
                        if (state.getIllcount().equals("0")){
                            tv.setTextColor(ColorRgbUtil.getGrayText());
                        }else{
                            tv.setTextColor(ColorRgbUtil.getMaroon());
                        }
                        break;
                }
                flow.addView(tv);
            }
        }
        public void setChild(List<MasterUser> states, FlowLayout flow,String s){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            for (MasterUser state:states){
                final TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flow, false);
                tv.setText(StringUtils.getTextJoint("%1$s",state.getMastername()));
//                if (state.getIllcount().equals("0")){
//                    tv.setTextColor(ColorRgbUtil.getGrayText());
//                }else{
//                    tv.setTextColor(ColorRgbUtil.getOrangeRed());
//                }
                flow.addView(tv);
            }
        }

        public void setChild(List<CheckState> states, FlowLayout flow, Boolean is){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
//            int width= ScreenTools.instance(mContext).getScreenWidth();

            for (final CheckState state:states){
                TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flow, false);
                tv.setText(state.getStatetitle());

                tv.setBackground(mContext.getDrawable(R.drawable.radius4_graybg_grayline));
                int tint;
                if (state.getState().equals("未检测")){
                    tint=ColorRgbUtil.getGainsboro();
                    tv.setTextColor(ColorRgbUtil.getWhite());
                    tv.setText(state.getStatetitle()+":未检测");
                }else{
                    tint=ColorRgbUtil.getForestGreen();
                    tv.setTextColor(ColorRgbUtil.getWhite());
                    tv.setText(state.getStatetitle()+":已检测");
                }
                tv .getBackground().setColorFilter(tint, PorterDuff.Mode.DARKEN);
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
