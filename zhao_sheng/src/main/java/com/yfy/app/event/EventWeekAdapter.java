package com.yfy.app.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.duty.bean.WeekBean;
import com.yfy.app.event.bean.EventBean;
import com.yfy.final_tag.ColorRgbUtil;
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
public class EventWeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String user="负责人:";
    private String addlss="地点:";
    private String conten="内容:";
    private String add_time="时间:";
    private String base_bg="#F4A460", text_color="#444444";
    private List<EventBean> dataList;
    private Activity mContext;
    private String type="yyyy年MM月dd";

    public void setDataList(List<EventBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public EventWeekAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getView_type();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TWO_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt, parent, false);
            return new ParentViewHolder(view);

        }
        if (viewType == TagFinal.ONE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_week_item, parent, false);
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
            if (!StringJudge.isEmpty(cHolder.bean.getImage())){
                photos.addAll(StringUtils.listToStringSplitCharacters(cHolder.bean.getImage(),"|"));
            }
            cHolder.mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                cHolder.mult.setVisibility(View.GONE);
                cHolder.mult.addItem(photos);
            }else{
                cHolder.mult.setVisibility(View.VISIBLE);
                cHolder.mult.addItem(photos);
            }
            cHolder.name.setText(HtmlTools.getHtmlString(base_bg, user, text_color, cHolder.bean.getLiableuser()));
            cHolder.content.setText(HtmlTools.getHtmlString(base_bg, conten, text_color, cHolder.bean.getContent()));
            cHolder.site.setText(HtmlTools.getHtmlString(base_bg, addlss, text_color, cHolder.bean.getDate()+DateUtils.getWeek(cHolder.bean.getDate())));
            cHolder.time.setText(HtmlTools.getHtmlString(base_bg, add_time, text_color, cHolder.bean.getAddress()));
            cHolder.five.setVisibility(View.GONE);
        }
        if (holder instanceof ParentViewHolder){
            ParentViewHolder topHolder = (ParentViewHolder) holder;
            topHolder.bean=dataList.get(position);
            topHolder.name.setBackground(mContext.getResources().getDrawable(R.drawable.ic_line_weight_black_24dp));
            topHolder.name.setTextSize(14f);
            topHolder.name.setTextColor(ColorRgbUtil.getBaseColor());
            topHolder.name.setText(topHolder.bean.getDep_name());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    public class ChildViewHolder  extends RecyclerView.ViewHolder {
        TextView name;
        TextView content;
        TextView site;
        TextView time;
        TextView five;

        MultiPictureView mult;
        public EventBean bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.event_add_user_name);
            time= (TextView) itemView.findViewById(R.id.event_fuz_user_name);
            site= (TextView) itemView.findViewById(R.id.event_item_time);
            content= (TextView) itemView.findViewById(R.id.event_item_site);
            five= (TextView) itemView.findViewById(R.id.event_item_content);
            mult=  itemView.findViewById(R.id.event_week_mult);
            mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, MultPicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                    b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public EventBean bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.public_center_txt_add);

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
