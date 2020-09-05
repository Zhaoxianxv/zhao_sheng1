package com.yfy.app;

import android.app.Activity;
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
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/11.
 */

public class ContactSingeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
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

    public ContactSingeAdapter(Activity context) {
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
            parent.name.setText(parent.bean.getDepname());
            parent.count.setVisibility(View.GONE);
            if (parent.bean.isExpand()){
                parent.arrow.setImageResource(R.drawable.notice_contacts_switch_open);
            }else{
                parent.arrow.setImageResource(R.drawable.notice_contacts_switch_close);
            }
            parent.checkBox.setVisibility(View.GONE);

        }
        if (holder instanceof ChildViewHolder){
            ChildViewHolder child = (ChildViewHolder) holder;
            child.bean=showData.get(position-1);
            child.name.setText(child.bean.getUsername());
            Glide.with(mContext).load(child.bean.getHeadPic()).apply(new RequestOptions().circleCrop()).into(child.user_head);
            child.checkBox.setVisibility(View.GONE);
        }
        if (holder instanceof TopHolder){

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
        notifyItemRangeRemoved(position+1, itemCount);
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

        public ParentViewHolder(View itemView) {
            super(itemView);
            arrow = (ImageView) itemView.findViewById(R.id.contacts_arrow);
            name = (TextView) itemView.findViewById(R.id.contacts_group_name);
            count = (TextView) itemView.findViewById(R.id.contacts_count);
            checkBox = itemView.findViewById(R.id.contacts_group_check_box);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.contacts_group_layout);
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

        }



    }



    class ChildViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView user_head;
        public AppCompatImageView checkBox;
        public RelativeLayout relativeLayout;
        private ChildBean bean;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.contacts_name);
            user_head = (AppCompatImageView) itemView.findViewById(R.id.contacts_head);
            checkBox =  itemView.findViewById(R.id.contacts_check_box);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.contacts_child_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent();
                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
                    mContext.setResult(Activity.RESULT_OK,intent);
                    mContext.finish();
                }
            });
        }
    }



    class TopHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView search_tv;

        public TopHolder(View itemView) {
            super(itemView);
            search_tv =  itemView.findViewById(R.id.search_tv);
            search_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ContactSearchActivity.class);
                    Bundle b = new Bundle();
                    b.putParcelableArrayList(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) allData);
                    intent.putExtras(b);
                    mContext.startActivityForResult(intent,TagFinal.UI_TAG);
                }
            });
        }
    }


}
