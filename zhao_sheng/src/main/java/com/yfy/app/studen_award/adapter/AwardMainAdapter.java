package com.yfy.app.studen_award.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.duty.adpater.DutyDateAdapter;
import com.yfy.app.duty.bean.WeekBean;
import com.yfy.app.studen_award.bean.AwardB;
import com.yfy.app.studen_award.bean.AwardBean;
import com.yfy.base.App;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.example.zhao_sheng.R.drawable.radio_red_cry;

/**
 * Created by yfy1 on 2017/4/5.
 */

public class AwardMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<AwardB> dataList;
    private Activity mContext;
    private String tab="得分次数：";

    public void setDataList(List<AwardB> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    private Resources resources;
    public AwardMainAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();

        resources = mContext.getResources();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getType_view();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TWO_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_main_top_item, parent, false);
            return new ParentViewHolder(view);

        }
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.award_main_xlist_item, parent, false);
            return new ChildViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder cHolder = (ChildViewHolder) holder;
            cHolder.bean=dataList.get(position);

            List<String> photos= new ArrayList<>();
            photos.addAll(cHolder.bean.getImages()==null?photos:cHolder.bean.getImages());
            cHolder.mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                cHolder.mult.setVisibility(View.GONE);
                cHolder.mult.addItem(photos);
            }else{
                cHolder.mult.setVisibility(View.VISIBLE);
                cHolder.mult.addItem(photos);
            }
            cHolder.content.setText(cHolder.bean.getContent());
            cHolder.title.setText(cHolder.bean.getType());
            cHolder.user.setText(cHolder.bean.getTeacher());
            if (cHolder.bean.getScore().equals("1")){
                cHolder.user.setCompoundDrawablesWithIntrinsicBounds(null,null,resources.getDrawable(R.drawable.radio_orange_sweet),null);
            }else{
                cHolder.user.setCompoundDrawablesWithIntrinsicBounds(null,null,resources.getDrawable(R.drawable.radio_red_cry),null);
            }
            String time=cHolder.bean.getDate();
            cHolder.time_yyyy.setText(time.substring(0,4));
            cHolder.time_mm.setText(time.substring(5).replace("/","-"));
        }
        if (holder instanceof ParentViewHolder){
            ParentViewHolder topHolder = (ParentViewHolder) holder;
            topHolder.bean=dataList.get(position);
            topHolder.title.setText(topHolder.bean.getTermname());
            topHolder.scroe.setText(tab+topHolder.bean.getCount());
            topHolder.yes_num.setText(topHolder.bean.getLaughcount());
            topHolder.no_num.setText(topHolder.bean.getCrycount());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    public class ChildViewHolder  extends RecyclerView.ViewHolder {
        TextView time_yyyy;
        TextView time_mm;
        TextView title;
        TextView content;
        AppCompatTextView user;
        MultiPictureView mult;
        public AwardB bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            time_yyyy=  itemView.findViewById(R.id.award_list_time_txt);
            time_mm=  itemView.findViewById(R.id.award_list_time_txt_moth);
            title=  itemView.findViewById(R.id.award_childs_title);
            content=  itemView.findViewById(R.id.award_child_content);
            user=  itemView.findViewById(R.id.award_childs_admin_user_icon);
            mult=  itemView.findViewById(R.id.award_child_mult);
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

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView scroe;
        AppCompatTextView yes_num;
        AppCompatTextView no_num;
        public AwardB bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.award_parent_title);
            scroe= itemView.findViewById(R.id.award_parent_score);
            yes_num= itemView.findViewById(R.id.award_parent_yes_num);
            no_num= itemView.findViewById(R.id.award_parent_no_num);
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
