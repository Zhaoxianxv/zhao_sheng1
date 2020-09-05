package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckChildBean;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{



    private List<CheckChild> dataList;
    private Activity mContext;

    public void setDataList(List<CheckChild> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckParentAdapter(Activity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_byid_detail_parent, parent, false);
            return new ParentHolder(view);

        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_byid_detail_child, parent, false);
            view.setOnClickListener(this);
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
            if (parent.bean.isIs_show()){
                parent.tag.setVisibility(View.VISIBLE);
            }else{
                parent.tag.setVisibility(View.GONE);
            }
        }
        if (holder instanceof ChildHolder) {
            ChildHolder child = (ChildHolder) holder;
//            child.itemView.setOnClickListener(this);
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getAdddate());
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
        TextView del;
        TextView tag;
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
            tag=  itemView.findViewById(R.id.ill_parent_tag);
            del=  itemView.findViewById(R.id.check_detail_parent_del);

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delFace!=null){
                        delFace.del(bean);
                    }
                }
            });

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


    public DelFace delFace;

    public void setDelFace(DelFace delFace) {
        this.delFace = delFace;
    }

    public interface DelFace{
        void del(CheckChild bean);
        void del_child(CheckChild bean);
    }
    private class ChildHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView child_del;
        FlowLayout flowLayout;
        CheckChild bean;
        ChildHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_class_name);
            child_del=  itemView.findViewById(R.id.check_class_num_del);
            flowLayout=  itemView.findViewById(R.id.check_class_num_flow);
            child_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delFace!=null){
                        delFace.del_child(bean);
                    }
                }
            });

        }

        public void setChild(List<CheckChildBean> states, FlowLayout flow, Boolean is){
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flow.getChildCount()!=0){
                flow.removeAllViews();
            }
            for (final CheckChildBean state:states){
                RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.check_multi_flowlayout,flow, false);
                TextView tv=layout.findViewById(R.id.check_name);
                MultiPictureView multi=layout.findViewById(R.id.check_flow_layout_multi);
//                TextView tv= (TextView) mInflater.inflate(R.layout.check_flowlayout,flow, false);

                String type=state.getTablevaluetype().split("_")[0];
                switch (type){
                    case Base.DATA_TYPE_IMG:
                        tv.setVisibility(View.GONE);

                        List<String> photos=StringUtils.listToStringSplitCharacters(state.getTablevalue(),",");
                        multi.clearItem();
                        if (StringJudge.isEmpty(state.getTablevalue())){
                            multi.setVisibility(View.GONE);
                        }else{
                            multi.setVisibility(View.VISIBLE);
                            multi.setList(photos);
                            multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                                @Override
                                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                                    Intent intent=new Intent(mContext, MultPicShowActivity.class);
                                    Bundle b=new Bundle();
                                    b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                                    b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                                    intent.putExtras(b);
                                    mContext.startActivity(intent);
                                }
                            });
                        }
                        break;
                        default:
                            multi.setVisibility(View.GONE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText(HtmlTools.getHtmlString(state.getTablename()+": ",
                                    StringUtils.getTextJoint("%1$s%2$s",state.getTablevalue(),state.getTableunit()) ));
                            break;
                }

                flow.addView(layout);
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



    @Override
    public void onClick(View v) {
    }

}
