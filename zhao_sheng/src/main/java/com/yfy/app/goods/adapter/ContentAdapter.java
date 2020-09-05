package com.yfy.app.goods.adapter;

import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.goods.ContentActivity;
import com.yfy.app.goods.bean.Content;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.TagFinal;

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Content> dataList;
    private ContentActivity mContext;
    private boolean isedit=false;
    private Gson gson;

    public void setIsedit(boolean isedit) {
        this.isedit = isedit;
        notifyDataSetChanged();
    }

    public List<Content> getDataList() {
        return dataList;
    }

    public void setDataList(List<Content> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;





    public ContentAdapter(ContentActivity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
        gson=new Gson();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
     {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_content_item, parent, false);
            return new RecyclerViewHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.itemView.setLongClickable(true);

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.bean.setPosition(position);
            reHolder.name.setText(reHolder.bean.getContent());
            if (isedit){
                reHolder.delete_icon.setVisibility(View.VISIBLE);
            }else{
                reHolder.delete_icon.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        AppCompatImageView delete_icon;
        Content bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.selected_item_name);
            delete_icon= itemView.findViewById(R.id.goods_delete_content);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent data=new Intent();
                    data.putExtra(TagFinal.OBJECT_TAG,bean.getContent());
                    mContext.setResult(RESULT_OK,data);
                    mContext.finish();
                }
            });
            delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataList.remove(bean.getPosition());
                    notifyItemRangeRemoved(bean.getPosition(), 1);

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
