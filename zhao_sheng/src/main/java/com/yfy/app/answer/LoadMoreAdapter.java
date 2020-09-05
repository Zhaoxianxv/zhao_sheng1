package com.yfy.app.answer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;

import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.answer.bean.AnswerListBean;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/11/7.
 */

public class LoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AnswerListBean> dataList;
    private Context mContext;
    private  String user_name;
    public void setDataList(List<AnswerListBean> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
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

    public LoadMoreAdapter(Context mContext, List<AnswerListBean> dataList,String name) {
        this.mContext=mContext;
        this.dataList = dataList;
        this.user_name=name;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    List<Integer> lists;
    private void getRandomHeights(List<AnswerListBean> datas) {
        lists = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            lists.add((int) (200 + Math.random() * 400));
        }
    }

    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
//            ViewGroup.LayoutParams params = recyclerViewHolder.tvItem.getLayoutParams();
//            params.height = heigh;//把随机的高度赋予item布局
//            holder.itemView.setLayoutParams(params);
            recyclerViewHolder.pic=dataList.get(position);

            Glide.with(mContext)
                    .load(recyclerViewHolder.pic.getUser_avatar())
                    .apply(new RequestOptions().circleCrop())
                    .into(recyclerViewHolder.head);

            if ( recyclerViewHolder.pic.getIsanswer().equals("false")){
                recyclerViewHolder.tag.setVisibility(View.GONE);
            }else{
                recyclerViewHolder.tag.setVisibility(View.VISIBLE);
            }
            //点赞true可点
            if (recyclerViewHolder.pic.getIs_praise().equals("true")){
                recyclerViewHolder.parise.setImageResource(R.mipmap.praise_red);
            }else{
                recyclerViewHolder.parise.setImageResource(R.mipmap.praise_gray);
            }

            if (StringJudge.isEmpty(recyclerViewHolder.pic.getImage())){
                recyclerViewHolder.image.setVisibility(View.GONE);
            }else{
                final String url=recyclerViewHolder.pic.getImage();
                recyclerViewHolder.image.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(url)
                        .apply(new RequestOptions().error(R.drawable.oval_gray))
                        .into(recyclerViewHolder.image);

            }
            if (Base.user.getName().equals(user_name)){
                recyclerViewHolder.adopt.setVisibility(View.VISIBLE);
            }else{
                recyclerViewHolder.adopt.setVisibility(View.GONE);
            }

            recyclerViewHolder.time.setText(recyclerViewHolder.pic.getTime());
            recyclerViewHolder.name.setText(recyclerViewHolder.pic.getUser_name());
            recyclerViewHolder.num.setText(recyclerViewHolder.pic.getPraise_count());
            recyclerViewHolder.content.setText(recyclerViewHolder.pic.getContent());

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    footViewHolder.allEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView head;
        TextView tag;
        TextView time;
        TextView name;
        TextView num;
        TextView content;
        ImageView parise;
        ImageView image;
        ImageView adopt;
        View layout;
        AnswerListBean pic;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.answer_detail_item_head);
            tag = (TextView) itemView.findViewById(R.id.answer_detail_item_tag);
            time = (TextView) itemView.findViewById(R.id.answer_detail_item_time);
            name = (TextView) itemView.findViewById(R.id.answer_detail_item_user_name);
            num = (TextView) itemView.findViewById(R.id.answer_detail_item_parise_num);
            content = (TextView) itemView.findViewById(R.id.answer_detail_item_content);
            parise = (ImageView) itemView.findViewById(R.id.answer_detail_item_parise);
            image = (ImageView) itemView.findViewById(R.id.answer_detail_item_image);
            adopt = (ImageView) itemView.findViewById(R.id.answer_detail_item_adopt);
            parise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int priase_tag=0;
                    if (pic.getIs_praise().equals("true")){
                        priase_tag=0;
                    }else{
                        priase_tag=1;
                    }
                    if (viewOnClick!=null){
                         viewOnClick.isparise(priase_tag,pic.getId());
                    }

                }
            });
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,pic.getImage());
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
            adopt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewOnClick!=null){
                        viewOnClick.adopt(adopt,pic.getId(),pic.getCandel(),pic.getIsanswer());
                    }
                }
            });
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pbLoading;
        TextView tvLoading;
        LinearLayout llEnd;
        RelativeLayout allEnd;

        FootViewHolder(View itemView) {
            super(itemView);
            pbLoading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
            tvLoading = (TextView) itemView.findViewById(R.id.tv_loading);
            llEnd = (LinearLayout) itemView.findViewById(R.id.ll_end);
            allEnd = (RelativeLayout) itemView.findViewById(R.id.recycler_bottom);

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


    public ViewOnClick viewOnClick;

    public void setViewOnClick(ViewOnClick viewOnClick) {
        this.viewOnClick = viewOnClick;
    }

    public interface ViewOnClick{
        void isparise(int is,String id);
        void adopt(ImageView adopt,String id,String del,String is);

    }



}