package com.yfy.app.goods.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.goods.bean.BeanItem;
import com.yfy.app.goods.bean.Flowbean;
import com.yfy.app.goods.bean.GoodsBean;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.multi.MultiPictureView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2018/1/2.
 */

public class GoodsDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Flowbean> dataList;
    private Activity mContext;
    private String base_bg="#b3b3b3", text_color="#444444";

    public void setDataList(BeanItem bean) {

        this.bean= bean;
        this.dataList = bean.getInfo();
//        getRandomHeights(dataList);
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    private final int TYPE_TOP = 3;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    private String type="新物品提交";
    private BeanItem bean;
    private boolean end=true;

    public GoodsDetailAdapter(Activity mContext) {
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position  == 0) {
            return TYPE_TOP;
        }else{
            return TYPE_ITEM;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_detail_item, parent, false);
            return new RecyclerViewHolder(view);
        }else if (viewType == TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item_top, parent, false);
            return new TopHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position-1);
            Glide.with(mContext)
                    .load(reHolder.bean.getUser_avatar())
                    .apply(new RequestOptions().circleCrop())
                    .into(reHolder.head);
            reHolder.name.setText(reHolder.bean.getName());
            reHolder.type.setText(reHolder.bean.getStatus());
            reHolder.content.setText(reHolder.bean.getRemark());
            reHolder.time.setText(reHolder.bean.getTime());
            if (StringJudge.isEmpty(reHolder.bean.getImages())){
                reHolder.mult.setVisibility(View.GONE);
            }else{
                reHolder.mult.setVisibility(View.VISIBLE);
                reHolder.mult.setList(reHolder.bean.getImages());
            }



        } else if (holder instanceof TopHolder) {
            TopHolder topHolder = (TopHolder) holder;
            if (bean==null)return;
            Glide.with(mContext)
                    .load(bean.getUser_avatar())
                    .apply(new RequestOptions().circleCrop())
                    .into(topHolder.top_head);
            topHolder.top_name.setText(bean.getUsername());
            topHolder.top_time.setText(bean.getSubmit_time());
            topHolder.top_one.setText(HtmlTools.getHtmlString(base_bg,"物品名称：",text_color,bean.getItem_name()));
            topHolder.top_two.setText(HtmlTools.getHtmlString(base_bg,"物品数量：",text_color,bean.getCount()+bean.getUnit()));
            topHolder.top_three.setText(HtmlTools.getHtmlString(base_bg,"申请备注：",text_color,bean.getRemark()));
            List<String> photos= new ArrayList<>();
            photos.addAll(bean.getImages()==null?photos:bean.getImages());
            topHolder.multi.clearItem();
            if (StringJudge.isEmpty(photos)){
                topHolder.multi.setVisibility(View.GONE);
                topHolder.multi.addItem(photos);
            }else{
                topHolder.multi.setVisibility(View.VISIBLE);
                topHolder.multi.addItem(photos);
            }
            if (bean.getType().equals(type)){
                topHolder.top_one.setVisibility(View.GONE);
                topHolder.top_two.setVisibility(View.GONE);
            }else{
                topHolder.top_one.setVisibility(View.VISIBLE);
                topHolder.top_two.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView time;
        TextView content;
        TextView type;
        ImageView head;
        Flowbean bean;
        MultiPictureView mult;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.detail_item_time);
            name= (TextView) itemView.findViewById(R.id.detail_item_name);
            type= (TextView) itemView.findViewById(R.id.detail_item_type);
            content= (TextView) itemView.findViewById(R.id.detail_item_content);
            head= (ImageView) itemView.findViewById(R.id.detail_item_head);
            mult= (MultiPictureView) itemView.findViewById(R.id.maintain_main_item_mult);

            mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });

        }
    }



    private class TopHolder extends RecyclerView.ViewHolder {


        ImageView top_head;
        TextView top_name;
        TextView top_time;
        TextView top_one;
        TextView top_two;
        TextView top_three;

        MultiPictureView multi;


        TopHolder(View itemView) {
            super(itemView);
            top_head = (ImageView) itemView.findViewById(R.id.goods_detail_head_icon);
            top_name= (TextView) itemView.findViewById(R.id.goods_detail_name);
            top_time= (TextView) itemView.findViewById(R.id.goods_detail_time);
            top_one= (TextView) itemView.findViewById(R.id.goods_detail_one);
            top_two= (TextView) itemView.findViewById(R.id.goods_detail_two);
            top_three= (TextView) itemView.findViewById(R.id.goods_detail_three);
            multi= (MultiPictureView) itemView.findViewById(R.id.goods_detail_mult);
            multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
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

//
//    public DoAdmin doAdmin;
//
//    public void setDoAdmin(DoAdmin doAdmin) {
//        this.doAdmin = doAdmin;
//    }
//
//    public interface DoAdmin{
//        void onClickDo(String id, String tag);
//    }


}
