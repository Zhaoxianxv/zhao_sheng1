package com.yfy.app.notice.adapter;

import android.app.Activity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.final_tag.glide.GlideTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class NoticeSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<ChildBean> dataList;
    private Activity mContext;

    public void setDataList(List<ChildBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    public List<ChildBean> getDataList() {
        return dataList;
    }

    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;





    public NoticeSearchAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_child, parent, false);
            return new ChildViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder reHolder = (ChildViewHolder) holder;
            reHolder.bean=dataList.get(position);

            reHolder.index=position;
            reHolder.name.setText(reHolder.bean.getUsername());
            GlideTools.chanCircle(mContext,reHolder.bean.getHeadPic(),reHolder.user_head,R.drawable.head_user);

            if (reHolder.bean.isChick()){
                reHolder.checkBox.setImageResource(R.drawable.ic_check_selected);
            }else{
                reHolder.checkBox.setImageResource(R.drawable.ic_check_unselect);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView user_head;
        public AppCompatImageView checkBox;
        public RelativeLayout relativeLayout;
        ChildBean bean;
        int index;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.contacts_name);
            user_head = (AppCompatImageView) itemView.findViewById(R.id.contacts_head);
            checkBox = itemView.findViewById(R.id.contacts_check_box);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.contacts_child_layout);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bean.isChick()) {
                        bean.setChick(false);
                    } else {
                        bean.setChick(true);
                    }
                    notifyItemChanged(index);
                }
            });
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
