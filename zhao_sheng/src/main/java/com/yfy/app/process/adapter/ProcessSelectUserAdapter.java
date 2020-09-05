package com.yfy.app.process.adapter;

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
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.app.process.ProcessSelectUserActivity;
import com.yfy.app.process.bean.ItemDataClickListener;
import com.yfy.app.process.bean.ProBeanChild;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/11.
 */

public class ProcessSelectUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ProcessSelectUserActivity mContext;
    private List<ProBeanChild> showData;//界面显示数据
    private List<ProBeanChild> allData=new ArrayList<>();//原始数据
    private OnScrollToListener onScrollToListener;

    private String max_num;
    private int index_position;

    public void setIndex_position(int index_position) {
        this.index_position = index_position;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public ProcessSelectUserAdapter(ProcessSelectUserActivity context) {
        mContext = context;
        showData = new ArrayList<>();
    }




    public void setAllData(List<ProBeanChild> allData) {
        this.allData.clear();
        showData.clear();
        this.allData = allData;

        for (ProBeanChild bean:allData){
            if (bean.getView_type()==TagFinal.TYPE_PARENT||bean.getView_type()==TagFinal.TYPE_CHECK){
                showData.add(bean);
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return showData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return showData.get(position).getView_type();
    }
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TagFinal.TYPE_CHECK){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_group, parent, false);
            return new GroupH(view);
        }
        if (viewType==TagFinal.TYPE_PARENT){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_parent_singe, parent, false);
            return new ParentViewHolder(view);
        }
        if (viewType==TagFinal.TYPE_CHILD){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_child_singe, parent, false);
            return new ChildViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder){
            ParentViewHolder parent = (ParentViewHolder) holder;
            parent.bean=showData.get(position);
            parent.index=position;
            parent.name.setText(parent.bean.getDepartname());
            if (parent.bean.isExpand()){
                parent.arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            }else{
                parent.arrow.setImageResource(R.drawable.ic_arrow_drop_right_black_24dp);
            }

        }
        if (holder instanceof ChildViewHolder){
            ChildViewHolder child = (ChildViewHolder) holder;
            child.bean=showData.get(position);
            child.child_index=position;
            child.name.setText(child.bean.getUsername());

            GlideTools.chanCircle(mContext,child.bean.getUserHeadPic(),child.user_head,R.drawable.head_user);
        }
      if (holder instanceof GroupH){
          GroupH groupH = (GroupH) holder;
          groupH.bean=showData.get(position);
          groupH.key.setText(groupH.bean.getDepartname());

        }

    }

    private ItemDataClickListener imageClickListener = new ItemDataClickListener() {

        @Override
        public void onExpandChildren(ProBeanChild bean) {
            int position = getCurrentPosition(bean.getDepartid());
            List<ProBeanChild> adddata =getChildrenBy(bean.getDepartid());
            if (StringJudge.isEmpty(adddata)) {
                return;
            }
            addAll(adddata, position + 1);// 插入到点击点的下方
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position + 1);
            }
        }

        @Override
        public void onHideChildren(ProBeanChild bean) {
            int position = getCurrentPosition(bean.getDepartid());
            removeAll(position + 1,bean.getAllNum());
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position+1);
            }
        }
    };


    //
    public void addAll(List<ProBeanChild> list, int position) {
        showData.addAll(position, list);
        notifyItemRangeInserted(position+1, list.size());
    }

    private List<ProBeanChild> getChildrenBy(String depid) {
        List<ProBeanChild> adddata=new ArrayList<>();

        for (ProBeanChild bean :allData){
            if (bean.getView_type()==TagFinal.TYPE_PARENT)continue;
            List<String> tags=bean.getGroup_tag();
            if (StringJudge.isEmpty(tags))continue;
            boolean is=false;
            for (String s:tags){
                if (s.equals(depid)){
                    is=true;
                }
            }
            if (is) {
                adddata.add(bean);
            }
        }
        return adddata;
    }

//

    /**
     * 从position开始删除，删除
     *
     * @param position
     * @param itemCount
     *            删除的数目
     */
    protected void removeAll(int position, int itemCount) {
        for (int i = 0; i < itemCount; i++) {
            showData.remove(position);
        }
        notifyItemRangeRemoved(position, itemCount);//top有个view
    }

    protected int getCurrentPosition(String Depid) {
        for (int i = 0; i < showData.size(); i++) {
            if (Depid.equalsIgnoreCase(showData.get(i).getDepartid())) {
                return i;
            }
        }
        return -1;
    }




    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    private int loadState = 2;
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }
    class GroupH extends RecyclerView.ViewHolder {
        TextView key;
        RelativeLayout layout;
        public ProBeanChild bean;
        private int index;
        public GroupH(View itemView) {
            super(itemView);
            key =  itemView.findViewById(R.id.contacts_group_name);
            layout =  itemView.findViewById(R.id.public_type_txt_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (max_num.equalsIgnoreCase("1")){
                        Intent intent=new Intent();
                        intent.putExtra(Base.type,max_num);
                        intent.putExtra(Base.data,bean);
                        intent.putExtra(Base.index,index_position);
                        mContext.setResult(Activity.RESULT_OK,intent);
                        mContext.finish();
                    }
                }
            });
        }
    }
    class ParentViewHolder extends RecyclerView.ViewHolder {

        public ImageView arrow;
        public TextView name;
        public RelativeLayout relativeLayout;
        public ProBeanChild bean;
        private int index;

        public ParentViewHolder(View itemView) {
            super(itemView);
            arrow =  itemView.findViewById(R.id.contacts_arrow);
            name =  itemView.findViewById(R.id.contacts_group_name);
            relativeLayout =  itemView.findViewById(R.id.contacts_group_layout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageClickListener != null) {
                        if (bean.isExpand()) {
                            bean.setExpand(false);
                            arrow.setImageResource(R.drawable.ic_arrow_drop_right_black_24dp);
                            imageClickListener.onHideChildren(bean);
                        } else {
                            bean.setExpand(true);
                            imageClickListener.onExpandChildren(bean);
                            arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        }
                    }
                }
            });

        }
    }




    class ChildViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView user_head;
        public RelativeLayout relativeLayout;
        private ProBeanChild bean;

        private int child_index;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.contacts_name);
            user_head =  itemView.findViewById(R.id.contacts_head);
            relativeLayout =  itemView.findViewById(R.id.contacts_child_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   if (max_num.equalsIgnoreCase("1")){
                       Intent intent=new Intent();
                       intent.putExtra(Base.type,max_num);
                       intent.putExtra(Base.data,bean);
                       intent.putExtra(Base.index,index_position);
                       mContext.setResult(Activity.RESULT_OK,intent);
                       mContext.finish();
                   }
                }
            });
        }
    }


}
