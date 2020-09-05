package com.yfy.app.delay_service.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.DelayServiceStuDetailActivity;
import com.yfy.app.delay_service.DelayTeaToStuSingeActivity;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/12/27.
 */

public class ChoiceStuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<EventClass> dataList;

    private Activity mContext;
    private DateBean dateBean;
    private String tea_id;
    private String canedit;

    public void setCanedit(String canedit) {
        this.canedit = canedit;
    }

    public void setTea_id(String tea_id) {
        this.tea_id = tea_id;
    }

    public void setDate(DateBean dateBean) {
        this.dateBean = dateBean;
    }

    public void setDataList(List<EventClass> dataList) {

        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    public List<EventClass> getDataList() {
        return dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceStuAdapter(Activity mContext) {
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
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_stu_item, parent, false);
            return new RecyclerViewHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_tea_item, parent, false);
            return new ItemHolder(view);
        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delay_service_top_item, parent, false);
            return new TopHolder(view);
        }
        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stu_gridview_item, parent, false);
            return new StuHolder(view);
        }
        return null;
    }


    private int heigh;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;

            reHolder.bean = dataList.get(position);
            reHolder.index=position;
            reHolder.name.setText(StringUtils.getTextJoint("%1$s(%2$s)",reHolder.bean.getStuname(),reHolder.bean.getClassname()));
            reHolder.tag.setText(reHolder.bean.getIsabsent());
            reHolder.content.setText(reHolder.bean.getPhonenumber());
            reHolder.content.setTextColor(ColorRgbUtil.getMediumBlue());
            switch (reHolder.bean.getIsabsent()){
                case "未考评":
                    reHolder.tag.setTextColor(ColorRgbUtil.getGrayText());
                    reHolder.check.setImageResource(R.drawable.ic_chevron_right_calendar_24dp);
                    reHolder.check.setColorFilter(ColorRgbUtil.getBaseColor());
                    reHolder.check.setClickable(false);
                    break;
                case "正常":
                    reHolder.check.setClickable(false);
                    reHolder.tag.setTextColor(ColorRgbUtil.getDrakGreen());
                    reHolder.check.setImageResource(R.drawable.ic_chevron_right_calendar_24dp);
                    reHolder.check.setColorFilter(ColorRgbUtil.getBaseColor());
                    break;
//                case "巡查":
//                    break;
                    default:
                        reHolder.check.setClickable(true);
                        reHolder.check.setImageResource(R.drawable.ic_clear_black_24dp);
                        reHolder.check.setColorFilter(ColorRgbUtil.getOrangeRed());
                        reHolder.tag.setTextColor(ColorRgbUtil.getBaseColor());
                        break;
            }
            GlideTools.chanCircle(mContext, reHolder.bean.getStuheadpic(), reHolder.head,R.drawable.order_user_name);

        }
        if (holder instanceof TopHolder){
            TopHolder topH = (TopHolder) holder;
            topH.bean = dataList.get(position);
            topH.left_text.setText(StringUtils.getTextJoint("考勤人数：%1$s",topH.bean.getElectiveid()));
            topH.right_text.setText(StringUtils.getTextJoint("缺席人数：%1$s",topH.bean.getElectivename()));
        }
        if (holder instanceof StuHolder){
            StuHolder child = (StuHolder) holder;
            child.index=position;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getStuname());
            if (child.bean.getStuid().equals("$")){
                child.head.setVisibility(View.GONE);
                child.select.setVisibility(View.GONE);
            }else{
                child.head.setVisibility(View.VISIBLE);
                GlideTools.chanCircle(mContext, child.bean.getStuheadpic(), child.head, R.drawable.head_user);
                if (child.bean.isCheck()){
                    child.select.setVisibility(View.VISIBLE);
                }else{
                    child.select.setVisibility(View.GONE);
                }
            }


        }
        if (holder instanceof ItemHolder){
            ItemHolder itemH = (ItemHolder) holder;
            itemH.bean = dataList.get(position);
            List<String> photos= new ArrayList<>();
            photos.addAll(itemH.bean.getImage()==null?photos:itemH.bean.getImage());
            itemH.multi.clearItem();
            if (StringJudge.isEmpty(photos)){
                itemH.multi.setVisibility(View.GONE);
                itemH.multi.addItem(photos);
            }else{
                itemH.multi.setVisibility(View.VISIBLE);
                itemH.multi.addItem(photos);
            }
            String type="";
            if (itemH.bean.getChecktype().equals("tea")){
                type="考勤教师:%1$s";
                itemH.three.setVisibility(View.GONE);
                itemH.name.setTextColor(ColorRgbUtil.getBaseText());
            }else{
                type="巡查人员:%1$s";
                itemH.name.setTextColor(ColorRgbUtil.getBaseColor());
                itemH.three.setVisibility(View.VISIBLE);
                itemH.three.setText(StringUtils.getTextJoint("巡查内容:%1$s",itemH.bean.getType()));
            }
            itemH.two.setText(StringUtils.getTextJoint("代课老师:%1$s",
                    StringJudge.isEmpty(itemH.bean.getSubstituteteacher())?"无":itemH.bean.getSubstituteteacher()));
            itemH.name.setText(StringUtils.getTextJoint(type,itemH.bean.getTeachername()));

            itemH.content.setText(StringUtils.getTextJoint("考勤人数:%1$s",itemH.bean.getStuarrive()));
            itemH.one.setText(StringUtils.getTextJoint("考勤备注:%1$s",itemH.bean.getContent()));

            itemH.tag.setText(itemH.bean.getPhonenumber());
//            itemH.two.setVisibility(View.GONE);
            GlideTools.chanCircle(mContext,itemH.bean.getTeacherheadpic() ,itemH.head, R.drawable.head_user );;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView content;
        ImageView head;
        AppCompatImageView check;
        AppCompatTextView tag;
        RelativeLayout layout;
        int index;
        EventClass bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.event_stu_layout);
            name=  itemView.findViewById(R.id.event_stu_name);
            content=  itemView.findViewById(R.id.event_stu_content);
            tag=  itemView.findViewById(R.id.event_stu_tag);
            head=  itemView.findViewById(R.id.event_head_icon);
            check=  itemView.findViewById(R.id.event_head_check);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(StringJudge.isEmpty(bean.getDetail())){
                        if (canedit.equals(TagFinal.FALSE)){
                            Toast.makeText(mContext, "当前日期无法考勤",Toast.LENGTH_SHORT ).show();
                            return;
                        }

                        Intent intent=new Intent(mContext,DelayTeaToStuSingeActivity.class);
                        Bundle b=new Bundle();
                        b.putParcelable(Base.date,dateBean);//date
                        b.putParcelable(Base.data,bean);//date
                        b.putString(Base.id,tea_id);//date
                        intent.putExtras(b);
                        mContext.startActivityForResult(intent,TagFinal.UI_ADD);
                    }else{
                        Intent intent=new Intent(mContext,DelayServiceStuDetailActivity.class);
                        Bundle b=new Bundle();
                        b.putParcelable(TagFinal.OBJECT_TAG,bean);
                        b.putString(TagFinal.CONTENT_TAG,canedit);
                        intent.putExtras(b);
                        mContext.startActivityForResult(intent,TagFinal.UI_ADD);
                    }
                }
            });
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tell!=null){
                        tell.tell(bean.getPhonenumber());
                    }
                }
            });

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getIsabsent()){
                        case "未考评":
                            break;
                        case "正常":
                            break;
                        default:
                            if (tell!=null){
                                tell.clear(bean,index);
                            }
                            break;
                    }
                }
            });
        }
    }
    private class TopHolder extends RecyclerView.ViewHolder {

        TextView left_text;
        TextView right_text;
        EventClass bean;
        TopHolder(View itemView) {
            super(itemView);
            left_text=itemView.findViewById(R.id.event_top_one);
            right_text=itemView.findViewById(R.id.event_top_two);
        }
    }
    private class StuHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public AppCompatImageView select;
        private RelativeLayout layout;
        private ImageView head;
        private int index;
        EventClass bean;
        public StuHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.stu_grid_name);
            head =  itemView.findViewById(R.id.stu_grid_head);
            select =  itemView.findViewById(R.id.stu_grid_select);
            layout=itemView.findViewById(R.id.stu_grid_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setCheck(!bean.isCheck());
                    if (bean.getStuid().equals("$")){

                        if (bean.isCheck()){
                            bean.setStuname("取消全选");
                        }else{
                            bean.setStuname("全选");
                        }
                        for (EventClass s:dataList){
                            s.setCheck(bean.isCheck());
                        }
                        notifyDataSetChanged();
                    }else{
                        notifyItemChanged(index,bean );
                    }

                }
            });
        }
    }
    private class ItemHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView content;
        TextView tag;
        TextView one;
        TextView two;
        TextView three;
        ImageView head;
        MultiPictureView multi;
        EventClass bean;
        ItemHolder(View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.event_head_icon);
            name=itemView.findViewById(R.id.event_stu_name);
            tag=itemView.findViewById(R.id.event_stu_tag);
            content=itemView.findViewById(R.id.event_stu_content);
            one=itemView.findViewById(R.id.event_one);
            two=itemView.findViewById(R.id.event_two);
            three=itemView.findViewById(R.id.event_three);
            multi=itemView.findViewById(R.id.shape_main_item_mult);
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




    private Tell tell;

    public void setTell(Tell tell) {
        this.tell = tell;
    }

    public interface Tell{
        void tell(String phone);
        void clear(EventClass bean, int index);
    }


}
