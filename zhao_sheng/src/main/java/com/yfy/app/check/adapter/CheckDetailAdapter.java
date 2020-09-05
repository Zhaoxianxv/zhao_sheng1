package com.yfy.app.check.adapter;

import android.app.Activity;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckChildBean;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<CheckChild> dataList;
    private Activity mContext;

    public void setDataList(List<CheckChild> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckDetailAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }
    private CheckStu checkStu;

    public void setCheckStu(CheckStu checkStu) {
        this.checkStu = checkStu;
    }


    private ClasslistBean classlistBean;

    public void setBean(ClasslistBean classlistBean) {
        this.classlistBean = classlistBean;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_byid_detail, parent, false);
            return new ParentHolder(view);

        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_byid_detail_child, parent, false);
            return new ChildHolder(view);

        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt_center, parent, false);
            return new BaseHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ParentHolder) {
            ParentHolder parent = (ParentHolder) holder;
            parent.bean=dataList.get(position);
            parent.name.setText(parent.bean.getUsername());
            parent.time.setText(parent.bean.getAdddate());
            parent.state.setText(parent.bean.getState());
            switch (parent.bean.getState()){
                case "缺勤中":
                    parent.state.setTextColor(ColorRgbUtil.getMaroon());
                    break;
                    default:
                        parent.state.setTextColor(ColorRgbUtil.getForestGreen());
            }
            parent.tell.setText(parent.bean.getUsermobile());
            GlideTools.chanCircle(mContext,parent.bean.getUserheadpic() ,parent.user_head ,R.drawable.head_user );
//            parent.one.setText(StringUtils.getTextJoint("上传人:%1$s",parent.bean.getAdduser()));
            parent.one.setText(HtmlTools.getHtmlString("上传人:", parent.bean.getAdduser()));
//            parent.two.setText(StringUtils.getTextJoint("检查类型:%1$s",parent.bean.getIllchecktype()));
            parent.two.setText(HtmlTools.getHtmlString("检查类型:", parent.bean.getIllchecktype()));
//            parent.three.setText(StringUtils.getTextJoint("上报时间:%1$s",parent.bean.getAdddate()));
            parent.three.setText(HtmlTools.getHtmlString("上报时间:", parent.bean.getAdddate()));
//            parent.four.setText(StringUtils.getTextJoint("缺勤类型:%1$s",parent.bean.getIlltype()));
            parent.four.setText(HtmlTools.getHtmlString("缺勤类型:", parent.bean.getIlltype()));
        }
        if (holder instanceof ChildHolder) {
            ChildHolder child = (ChildHolder) holder;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getAdddate());
            child.del.setVisibility(View.GONE);

            child.setChild(child.bean.getIllstatedetail(), child.flowLayout,true );

        }
        if (holder instanceof BaseHolder) {
            BaseHolder child = (BaseHolder) holder;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getUsername());
            child.name.setTextColor(ColorRgbUtil.getForestGreen());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ParentHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        TextView tell;
        TextView time;
        TextView one;
        TextView two;
        TextView three;
        TextView four;
        ImageView user_head;
        CheckChild bean;

        ParentHolder(View itemView) {
            super(itemView);
            user_head =  itemView.findViewById(R.id.check_user_head);
            tell =  itemView.findViewById(R.id.check_user_tell);
            state =  itemView.findViewById(R.id.check_user_state);
            name =  itemView.findViewById(R.id.check_user_name);
            time =  itemView.findViewById(R.id.check_user_time);
            one=  itemView.findViewById(R.id.check_detail_parent_one);
            two=  itemView.findViewById(R.id.check_detail_parent_two);
            three=  itemView.findViewById(R.id.check_detail_parent_three);
            four=  itemView.findViewById(R.id.check_detail_parent_four);


        }
    }


    private class ChildHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView del;
        FlowLayout flowLayout;
        CheckChild bean;
        ChildHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_class_name);
            del=  itemView.findViewById(R.id.check_class_num_del);
            flowLayout=  itemView.findViewById(R.id.check_class_num_flow);


        }

        public void setChild(List<CheckChildBean> states, FlowLayout flow, Boolean is){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            for (final CheckChildBean state:states){
//                RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.check_flowlayout,flow, false);
//                TextView tv=layout.findViewById(R.id.check_name);
                TextView tv= (TextView) mInflater.inflate(R.layout.check_flowlayout,flow, false);
                tv.setText(HtmlTools.getHtmlString(state.getTablename()+": ",
                        StringUtils.getTextJoint("%1$s%2$s",state.getTablevalue(),state.getTableunit()) ));
//                flow.addView(layout);
                flow.addView(tv);
            }
        }
    }


    private class BaseHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckChild bean;

        BaseHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.public_top_text);
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
