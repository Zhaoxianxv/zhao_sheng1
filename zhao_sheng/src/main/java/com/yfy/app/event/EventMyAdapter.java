package com.yfy.app.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.event.bean.EventBean;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class EventMyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private String user="负责人:";
    private String addlss="地点:";
    private String conten="内容:";
    private String add_time="添加时间:";
    private List<EventBean> dataList;
    private Activity mContext;

    public void setDataList(List<EventBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public EventMyAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.ONE_INT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_main_item, parent, false);
            return new ParentViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder reHolder = (ParentViewHolder) holder;
            reHolder.bean=dataList.get(position);
            if (reHolder.bean.isIs()){
                String time= reHolder.bean.getDate();
                reHolder.top.setVisibility(View.VISIBLE);
                reHolder.top.setText(
                        reHolder.bean.getTerm_name()
                        +"  "+DateUtils.changeDate(time)
                        +"  "+reHolder.bean.getWeek_name()
                        +"  "+DateUtils.getWeek(time)
                );
            }else{
                reHolder.top.setVisibility(View.GONE);
            }

            reHolder.add_name.setText(reHolder.bean.getDepartmentname());
            reHolder.fz_name.setText(HtmlTools.getHtmlString(user,reHolder.bean.getLiableuser()));
            reHolder.time.setText(HtmlTools.getHtmlString(addlss,reHolder.bean.getAddress()));
            reHolder.content.setText(HtmlTools.getHtmlString(conten,reHolder.bean.getContent()));
            reHolder.dep.setVisibility(View.VISIBLE);
            reHolder.dep.setText(HtmlTools.getHtmlString(add_time, reHolder.bean.getAdddate()));
            List<String> photos= new ArrayList<>();
            if (!StringJudge.isEmpty(reHolder.bean.getImage())){
                photos.addAll(StringUtils.listToStringSplitCharacters(reHolder.bean.getImage(),"|"));
            }
            reHolder.mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.mult.setVisibility(View.GONE);
                reHolder.mult.addItem(photos);
            }else{
                reHolder.mult.setVisibility(View.VISIBLE);
                reHolder.mult.addItem(photos);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView top;
        TextView add_name;
        TextView fz_name;
        TextView time;
        TextView dep;
        TextView content;
        CardView card;
        MultiPictureView mult;
        EventBean  bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            card=  itemView.findViewById(R.id.event_my_card);
            top=  itemView.findViewById(R.id.event_main_top);
            add_name=  itemView.findViewById(R.id.event_add_user_name);
            fz_name=  itemView.findViewById(R.id.event_fuz_user_name);
            time=  itemView.findViewById(R.id.event_item_time);
            dep=  itemView.findViewById(R.id.event_item_site);
            content=  itemView.findViewById(R.id.event_item_content);
            mult=  itemView.findViewById(R.id.event_main_mult);
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
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(mContext,EventAlterActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
                    mContext.startActivityForResult(intent,TagFinal.UI_ADMIN);
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
