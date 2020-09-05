package com.yfy.app.integral.subjcet;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;

import java.util.List;

/**
 * Created by yfyandr on 2018/1/23.
 */

public class DoAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<StuAwrad> dataList;
    private Activity mContext;
    private String user_name;
    public void setDataList(List<StuAwrad> dataList) {

        this.dataList = dataList;

//        getRandomHeights(dataList);
    }
    public List<StuAwrad> getDataList(){
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

    public DoAdapter(Activity mContext, List<StuAwrad> dataList) {
        this.mContext=mContext;
        this.dataList = dataList;

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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subjcet_stu_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

//    List<Integer> lists;
//    private void getRandomHeights(List<AnswerListBean> datas) {
//        lists = new ArrayList<>();
//        for (int i = 0; i < datas.size(); i++) {
//            lists.add((int) (200 + Math.random() * 400));
//        }
//    }

    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
//            ViewGroup.LayoutParams params = recyclerViewHolder.tvItem.getLayoutParams();
//            params.height = heigh;//把随机的高度赋予item布局
//            holder.itemView.setLayoutParams(params);
            reHolder.bean=dataList.get(position);

            reHolder.name.setText(reHolder.bean.getStuname());


            if (reHolder.bean.isSelecter()){
                reHolder.tilk.setVisibility(View.VISIBLE);
            }else{
                reHolder.tilk.setVisibility(View.GONE);
            }
            if (reHolder.bean.getIsaward().equals("true")){
                reHolder.tag.setVisibility(View.VISIBLE);
            }else{
                reHolder.tag.setVisibility(View.GONE);
            }


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
        TextView tag;
        TextView name;
        ImageView tilk;
        RelativeLayout layout;
        StuAwrad bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.subjcet_stu_name);
            tag = (TextView) itemView.findViewById(R.id.subjcet_stu_tag);
            tilk = (ImageView) itemView.findViewById(R.id.subjcet_stu_selecter);
            layout = (RelativeLayout) itemView.findViewById(R.id.subjcet_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bean.setSelecter(!bean.isSelecter());
                    notifyDataSetChanged();
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






}
