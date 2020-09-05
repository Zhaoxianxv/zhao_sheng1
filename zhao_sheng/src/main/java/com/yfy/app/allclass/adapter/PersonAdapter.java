package com.yfy.app.allclass.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.allclass.ShapeDetailActivity;
import com.yfy.app.allclass.beans.ShapeMainList;
import com.yfy.app.allclass.beans.TopBean;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2018/3/12.
 */

public class PersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShapeMainList> dataList;
    private List<TopBean> list=new ArrayList<>();
    private Activity mContext;
    private String user_name;
    public void setDataList(List<ShapeMainList> dataList) {
        this.dataList = dataList;
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    private final int TYPE_TOP= 3;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;


    public PersonAdapter(Activity mContext, List<ShapeMainList> dataList) {
        this.mContext=mContext;
        this.dataList = dataList;

    }

    public void setList(List<TopBean> list) {
        this.list = list;
        notifyDataSetChanged();
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
        if (viewType == TYPE_TOP){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_person_top, parent, false);
            return new TopViewHolder(view);
        }else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shape_main_xlist_item, parent, false);
            return new RecyclerViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_refresh_footer, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopViewHolder){
            TopViewHolder topViewHolder=(TopViewHolder) holder;
            ItemAdapter adapter=new ItemAdapter(list,mContext);
            topViewHolder.listView.setAdapter(adapter);

        }else if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position-1);
            reHolder.tag.setText(reHolder.bean.getTags());
            reHolder.who.setText(reHolder.bean.getPraise());
            reHolder.content.setText(reHolder.bean.getContent());
            reHolder.content.setAutoLinkMask(Linkify.ALL);
            reHolder.content.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
            reHolder.time.setText(reHolder.bean.getTime());
            reHolder.name.setText(reHolder.bean.getName());
            reHolder.layout_bottom.setVisibility(View.GONE);

            GlideTools.chanCircle(mContext,reHolder.bean.getHead_photo(),reHolder.head,R.drawable.head_user);
            List<String> photos= new ArrayList<>();
            photos.addAll(reHolder.bean.getImages()==null?photos:reHolder.bean.getImages());
            reHolder.mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.mult.setVisibility(View.GONE);
                reHolder.mult.addItem(photos);
            }else{
                reHolder.mult.setVisibility(View.VISIBLE);
                reHolder.mult.addItem(photos);
            }
            if (StringJudge.isEmpty(reHolder.bean.getReply_count())){
                reHolder.comment.setText("");
            }else{
                reHolder.comment.setText(reHolder.bean.getReply_count());
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
        return dataList.size() + 2;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView head;
        TextView tag;
        TextView time;
        TextView name;
        TextView comment;
        TextView who;
        TextView content;
        ImageView parise_do;
        MultiPictureView mult;
        LinearLayout layout;
        LinearLayout layout_bottom;
        ShapeMainList bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.shape_item_head);
            tag = (TextView) itemView.findViewById(R.id.shape_item_type);
            time = (TextView) itemView.findViewById(R.id.shape_item_time);
            name = (TextView) itemView.findViewById(R.id.shape_item_name);
            layout= (LinearLayout) itemView.findViewById(R.id.shape_item_group_layout);
            layout_bottom= (LinearLayout) itemView.findViewById(R.id.layout_bottom);
            comment = (TextView) itemView.findViewById(R.id.shape_itemt_comment);
            who = (TextView) itemView.findViewById(R.id.shape_itemt_who_praise);
            content = (TextView) itemView.findViewById(R.id.shape_item_content);
            mult= (MultiPictureView) itemView.findViewById(R.id.shape_main_item_mult);

            parise_do = (ImageView) itemView.findViewById(R.id.shape_item_praise);
            parise_do.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewOnClick != null) {
                        viewOnClick.praise(bean.getId(),bean.getIsPraise().equals("否")?1:0);;
                    }

                }
            });


            layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (viewOnClick != null) {
                        viewOnClick.delete(bean);
                    }
                    return true;
                }
            });

//            head.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(mContext,ShapeUserDetailActivity.class);
//                    intent.putExtra("user_id",bean.getUser_id());
//                    intent.putExtra("user_type",bean.getUser_type());
//                    intent.putExtra("bean",bean);
//
//                    mContext.startActivity(intent);
//                }
//            });

            content.setOnClickListener(new View.OnClickListener() {
                Boolean flag = true;
                @Override
                public void onClick(View view) {
                    if(flag){
                        flag = false;
                        content.setEllipsize(null); // 展开
                        content.setSingleLine(flag);
                    }else{
                        flag = true;
                        content.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                        content.setSingleLine(flag);
                    }
                }
            });
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
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,ShapeDetailActivity.class);
                    intent.putExtra("num",bean.getReply_count());
                    intent.putExtra("bean",bean);
                    mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
                }
            });


        }
    }

    private class TopViewHolder extends  RecyclerView.ViewHolder{

        ListView listView;
        public TopViewHolder(View itemView) {
            super(itemView);
            listView= (ListView) itemView.findViewById(R.id.shape_top_list);
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
        public void delete(ShapeMainList num);
        public void praise(String dynamicId, int position);
    }
}
