package com.yfy.app.notice.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.app.notice.bean.ItemDataClickListener;
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.app.notice.cyc.ContactSearchActivity;
import com.yfy.app.notice.cyc.ContactsSelectActivity;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/11.
 */

public class SelectContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ContactsSelectActivity mContext;
    private List<ChildBean> showData;//界面显示数据
    private List<ChildBean> allData=new ArrayList<>();//原始数据
    private OnScrollToListener onScrollToListener;

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    // 脚布局
    private final int TYPE_GROUP = 2;


    public void setAllData(List<ChildBean> allData) {
        this.allData.clear();
        showData.clear();
        this.allData = allData;

        for (ChildBean bean:allData){
            if (bean.getType()==TagFinal.TYPE_PARENT){
                showData.add(bean);
            }
        }
        notifyDataSetChanged();
    }


    public List<ChildBean> getAllData() {
        return allData;
    }

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public SelectContactsAdapter(ContactsSelectActivity context) {
        mContext = context;
        showData = new ArrayList<ChildBean>();
    }

    @Override
    public int getItemCount() {
        return showData.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TagFinal.TYPE_TOP;
        }else{
            return showData.get(position-1).getType();
        }

    }
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==TagFinal.TYPE_PARENT){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_parent, parent, false);
            return new ParentViewHolder(view);
        }else if (viewType==TagFinal.TYPE_CHILD){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.contacts_child, parent, false);
            return new ChildViewHolder(view);
        }else{
            View view  = LayoutInflater.from(mContext).inflate(R.layout.notice_include_seach, parent, false);
            return new TopHolder(view);
        }

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
                parent.checkBox.setImageResource(R.drawable.ic_check_selected);
            }else{
                parent.checkBox.setImageResource(R.drawable.ic_check_unselect);
            }
        }
        if (holder instanceof ChildViewHolder){
            ChildViewHolder child = (ChildViewHolder) holder;
            child.bean=showData.get(position-1);
            child.child_index=position;
            child.name.setText(child.bean.getUsername());
            Glide.with(mContext).load(child.bean.getHeadPic()).apply(new RequestOptions().circleCrop()).into(child.user_head);
            if (child.bean.isChick()){
                child.checkBox.setImageResource(R.drawable.ic_check_selected);
            }else{
                child.checkBox.setImageResource(R.drawable.ic_check_unselect);
            }
        }
        if (holder instanceof TopHolder){
            TopHolder top = (TopHolder) holder;
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
                    if (bean.isChick()){
                        bean.setChick(false);
                    }else{
                        bean.setChick(true);
                    }
                    refreshParent(bean.getGroup_tag(),bean.isChick());//刷新parent（多个）   //刷新child（多个）
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



    class TopHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView search_tv;
        TextView allSelcet;
        public TopHolder(View itemView) {
            super(itemView);
            search_tv =  itemView.findViewById(R.id.search_tv);
            allSelcet =  itemView.findViewById(R.id.group_type);
            search_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ContactSearchActivity.class);
                    Bundle b = new Bundle();
                    b.putString(TagFinal.TYPE_TAG, type);
                    b.putParcelableArrayList(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) allData);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent,TagFinal.UI_TAG);
                }
            });
            allSelcet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allSelcet.getText().equals("-- 点击全选 --")){

                        allSelcet.setText("-- 取消全选 --");
                        initSelcetAll(true);
                    }else{
                        allSelcet.setText("-- 点击全选 --");
                        initSelcetAll(false);
                    }
                }
            });
        }
    }

    public void initSelcetAll(boolean is){
        for (ChildBean bean:allData){
            if (bean.getType()==TagFinal.TYPE_PARENT){
                bean.setSelectNum(is?bean.getAllNum():0);
                bean.setGroupChick(is);
            }else{
                bean.setChick(is);
            }
        }
        notifyDataSetChanged();
    }

}
