package com.yfy.app.oashow.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.maintainnew.bean.FlowBean;
import com.yfy.app.maintainnew.bean.MainBean;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2018/1/2.
 */

public class BDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<FlowBean> dataList;
    private Activity mContext;
    private String base_bg="#b3b3b3", text_color="#444444";

    public void setDataList(List<FlowBean> dataList, MainBean bean) {
        this.bean = bean;
        this.dataList = dataList;
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
    private String type="完成维修";
    private MainBean bean;
    private boolean end=true;

    public BDetailAdapter(Activity mContext, List<FlowBean> dataList, MainBean bean, boolean end) {
        this.mContext=mContext;
        this.dataList = dataList;
        this.bean = bean;
        this.end = end;


    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if (position  == 0) {
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
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_item_footer, parent, false);
            return new FootViewHolder(view);
        }else if (viewType == TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_item_top, parent, false);
            return new TopHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position-1);

            GlideTools.chanCircle(mContext,reHolder.bean.getAvatar(),reHolder.head,R.drawable.head_user);
            reHolder.name.setText(reHolder.bean.getName());
            reHolder.type.setText(reHolder.bean.getState());
            reHolder.content.setText(reHolder.bean.getContent());
            reHolder.time.setText(reHolder.bean.getTime());
            if (StringJudge.isEmpty(reHolder.bean.getImage())){
                reHolder.mult.setVisibility(View.GONE);
            }else{
                reHolder.mult.setVisibility(View.VISIBLE);
                reHolder.mult.setList(reHolder.bean.getImage());
            }

        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footHolder = (FootViewHolder) holder;
            if (StringJudge.isEmpty(bean.getMaintain_info())){
                footHolder.layout.setVisibility(View.GONE);
            }else{
                footHolder.end_text.setText("");
            }

        } else if (holder instanceof TopHolder) {
            TopHolder topHolder = (TopHolder) holder;


            GlideTools.chanCircle(mContext,bean.getUser_avatar(),topHolder.top_head,R.drawable.head_user);
            topHolder.top_name.setText(bean.getUser_name());
            topHolder.top_time.setText(bean.getSubmit_time());
            topHolder.top_type.setText(HtmlTools.getHtmlString(base_bg,"报修部门：",text_color,bean.getDepartment_name()));
            topHolder.top_place.setText(HtmlTools.getHtmlString(base_bg,"报修地点：",text_color,bean.getAddress()));
            topHolder.top_time_one.setText(HtmlTools.getHtmlString(base_bg,"报修时间：",text_color,bean.getSubmit_time()));



            topHolder.top_content.setText(HtmlTools.getHtmlString(base_bg,"报修详情：",text_color,bean.getContent()));
            List<String> photos= new ArrayList<>();
            photos.addAll(bean.getImage()==null?photos:bean.getImage());
            topHolder.multi.clearItem();
            if (StringJudge.isEmpty(photos)){
                topHolder.multi.setVisibility(View.GONE);
                topHolder.multi.addItem(photos);
            }else{
                topHolder.multi.setVisibility(View.VISIBLE);
                topHolder.multi.addItem(photos);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 2;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        TextView time;
        TextView content;
        TextView type;
        ImageView head;
        FlowBean bean;
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

    private class FootViewHolder extends RecyclerView.ViewHolder {


        TextView end_text;
        RelativeLayout layout;

        FootViewHolder(View itemView) {
            super(itemView);

            end_text = (TextView) itemView.findViewById(R.id.maintain_detail_end);
            layout = (RelativeLayout) itemView.findViewById(R.id.detail_item_do_layout);




        }
    }

    private class TopHolder extends RecyclerView.ViewHolder {


        ImageView top_head;
        TextView top_name;
        TextView top_time;
        TextView top_type;
        TextView top_time_one;
        TextView top_place;
        TextView top_content;
        MultiPictureView multi;


        TopHolder(View itemView) {
            super(itemView);
            top_head = (ImageView) itemView.findViewById(R.id.maintain_detail_head_icon);
            top_name= (TextView) itemView.findViewById(R.id.maintain_detail_name);
            top_time= (TextView) itemView.findViewById(R.id.maintain_detail_time);
            top_type= (TextView) itemView.findViewById(R.id.maintain_detail_type);
            top_time_one=  itemView.findViewById(R.id.maintain_detail_time_one);
            top_place= (TextView) itemView.findViewById(R.id.maintain_detail_place);
            top_content= (TextView) itemView.findViewById(R.id.maintain_detail_content);
            multi= (MultiPictureView) itemView.findViewById(R.id.shape_main_item_mult);
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


    public DoAdmin doAdmin;

    public void setDoAdmin(DoAdmin doAdmin) {
        this.doAdmin = doAdmin;
    }

    public interface DoAdmin{
        void onClickDo(String id, String tag);
    }


}
