package com.yfy.app.process.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.app.notice.bean.ItemDataClickListener;
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.app.process.ProcessSelectUserAllActivity;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/11.
 */

public class ProcessSelectUserAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ProcessSelectUserAllActivity mContext;
    private List<ChildBean> showData;//界面显示数据
    private List<ChildBean> allData=new ArrayList<>();//原始数据
    private OnScrollToListener onScrollToListener;

    private String max_num;

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public ProcessSelectUserAllAdapter(ProcessSelectUserAllActivity context) {
        mContext = context;
        showData = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return showData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return showData.get(position).getType();
    }
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TagFinal.TYPE_PARENT){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_parent, parent, false);
            return new ParentViewHolder(view);
        }
        if (viewType==TagFinal.TYPE_CHILD){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_child, parent, false);
            return new ChildViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder){
            ParentViewHolder parent = (ParentViewHolder) holder;
            parent.bean=showData.get(position-1);
            parent.index=position;
            parent.name.setText(parent.bean.getDepname());
            parent.count.setText(parent.bean.getSelectNum()+"/"+parent.bean.getAllNum());
            if (parent.bean.isExpand()){
                parent.arrow.setImageResource(R.drawable.notice_contacts_switch_open);
            }else{
                parent.arrow.setImageResource(R.drawable.notice_contacts_switch_close);
            }
            if (parent.bean.isGroupChick()){
                parent.checkBox.setImageResource(R.drawable.ic_stat_name);
            }else{
                parent.checkBox.setImageResource(R.drawable.ic_stat);
            }
        }
        if (holder instanceof ChildViewHolder){
            ChildViewHolder child = (ChildViewHolder) holder;
            child.bean=showData.get(position-1);
            child.child_index=position;
            child.name.setText(child.bean.getUsername());
            GlideTools.chanCircle(mContext,child.bean.getHeadPic(),child.user_head,R.drawable.head_user);
            if (child.bean.isChick()){
                child.checkBox.setImageResource(R.drawable.ic_stat_name);
            }else{
                child.checkBox.setImageResource(R.drawable.ic_stat);
            }
        }

    }

    private ItemDataClickListener imageClickListener = new ItemDataClickListener() {

        @Override
        public void onExpandChildren(ChildBean bean) {
            int position = getCurrentPosition(bean.getDepid());
            List<ChildBean> adddata =getChildrenBy(bean.getDepid(),position + 2);
            if (StringJudge.isEmpty(adddata)) {
                return;
            }
            addAll(adddata, position + 1);// 插入到点击点的下方
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position + 1);
            }
        }

        @Override
        public void onHideChildren(ChildBean bean) {
            int position = getCurrentPosition(bean.getDepid());
            removeAll(position + 1,bean.getAllNum());
            if (onScrollToListener != null) {
                onScrollToListener.scrollTo(position+1);
            }
        }
    };


    //
    public void addAll(List<ChildBean> list, int position) {
        showData.addAll(position, list);
        notifyItemRangeInserted(position+1, list.size());
    }

    private List<ChildBean> getChildrenBy(String depid,int position) {
        List<ChildBean> adddata=new ArrayList<>();

        for (ChildBean bean :allData){
            if (bean.getType()==TagFinal.TYPE_PARENT)continue;
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
        notifyItemRangeRemoved(position+1, itemCount);//top有个view
    }

    protected int getCurrentPosition(String Depid) {
        for (int i = 0; i < showData.size(); i++) {
            if (Depid.equalsIgnoreCase(showData.get(i).getDepid())) {
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

    class ParentViewHolder extends RecyclerView.ViewHolder {

        public ImageView arrow;
        public TextView name;
        public TextView count;
        public AppCompatImageView checkBox;
        public RelativeLayout relativeLayout;
        public ChildBean bean;
        private int index;

        public ParentViewHolder(View itemView) {
            super(itemView);
            arrow =  itemView.findViewById(R.id.contacts_arrow);
            name =  itemView.findViewById(R.id.contacts_group_name);
            count =  itemView.findViewById(R.id.contacts_count);
            checkBox = itemView.findViewById(R.id.contacts_group_check_box);
            relativeLayout =  itemView.findViewById(R.id.contacts_group_layout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageClickListener != null) {
                        if (bean.isExpand()) {
                            bean.setExpand(false);
                            arrow.setImageResource(R.drawable.notice_contacts_switch_close);
                            imageClickListener.onHideChildren(bean);
                        } else {
                            bean.setExpand(true);
                            imageClickListener.onExpandChildren(bean);
                            arrow.setImageResource(R.drawable.notice_contacts_switch_open);

                        }
                    }
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.isGroupChick()){
                        bean.setGroupChick(false);
                        bean.setSelectNum(0);
                        notifyItemChanged(index,bean);
                        setSelectGroup(bean ,false);

                    }else{
                        bean.setGroupChick(true);
                        bean.setSelectNum(bean.getAllNum());
                        notifyItemChanged(index,bean);
                        setSelectGroup(bean ,true);

                    }

                }
            });
        }
    }

    public void setSelectGroup(ChildBean parent, boolean is) {
        if (parent.getAllNum()==0)return ;
        for (int i=0;i<allData.size();i++){
            ChildBean child =allData.get(i);
            if (child.getType()==TagFinal.TYPE_PARENT){

            }else{
                List<String> tags=child.getGroup_tag();
                if (StringJudge.isEmpty(tags))continue;
                for (String s:tags){
                    if (s.equals(parent.getDepid())){
                        child.setChick(is);
                        refreshParent(tags,parent.getDepid(),is);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }




    private void refreshParent(List<String> tags,String dep_id,boolean is) {
        for (String i:tags){
            if (i.equals(dep_id))continue;
            for (ChildBean bean:allData){
                if (bean.getType()==TagFinal.TYPE_CHILD)continue;
                if (bean.getDepid().equals(i)){
                    int num=bean.getSelectNum();
                    bean.setSelectNum(is?num+1:num-1);
                    if (bean.getAllNum()<=bean.getSelectNum()){
                        bean.setGroupChick(true);
                        bean.setSelectNum(bean.getAllNum());
                    }else if (bean.getSelectNum()<=0){
                        bean.setGroupChick(false);
                        bean.setSelectNum(0);
                    }else{
                        bean.setGroupChick(false);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }


    class ChildViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView user_head;
        public AppCompatImageView checkBox;
        public RelativeLayout relativeLayout;
        private ChildBean bean;

        private int child_index;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.contacts_name);
            user_head =  itemView.findViewById(R.id.contacts_head);
            checkBox =  itemView.findViewById(R.id.contacts_check_box);
            relativeLayout =  itemView.findViewById(R.id.contacts_child_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (max_num){
                        case "0":
                            break;
                    }
                }
            });
        }
    }


    private void refreshParent(List<String> tags,boolean is) {
        for (String i:tags){
            for (ChildBean bean:allData){
                if (bean.getType()==TagFinal.TYPE_CHILD)continue;
                if (bean.getDepid().equals(i)){
                    int num=bean.getSelectNum();

                    bean.setSelectNum(is?num+1:num-1);
                    if (bean.getAllNum()<=bean.getSelectNum()){
                        bean.setGroupChick(true);
                        bean.setSelectNum(bean.getAllNum());
                    }else if (bean.getSelectNum()<=0){
                        bean.setGroupChick(false);
                        bean.setSelectNum(0);
                    }else{

                        bean.setGroupChick(false);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
