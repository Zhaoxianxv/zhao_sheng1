package com.yfy.app.duty.adpater;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.bean.AddBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.multi.MultiPictureView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class DutyNotAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<AddBean> dataList;
    private Activity mContext;

    public void setDataList(List<AddBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    public List<AddBean> getDataList() {
        return dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DutyNotAddAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView

        if (dataList.get(position).isType_top()){
            return TagFinal.TYPE_FOOTER;
        }else{
            if (dataList.get(position).getId().equals("0")){
                return TagFinal.THREE_INT;
            }else{
                return TagFinal.TYPE_ITEM;
            }
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_not_item_one, parent, false);
            return new ItemViewHolder(view);

        }
        if (viewType == TagFinal.THREE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_not_item_two, parent, false);
            return new EditorViewHolder(view);

        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_add_item_three, parent, false);
            return new TopViewHolder(view);
//
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder reHolder = (ItemViewHolder) holder;
            reHolder.bean=dataList.get(position);

            if (reHolder.bean.getIsnormal().equals(TagFinal.FALSE)){
                reHolder.remark.setText(reHolder.bean.getContent());
                reHolder.remark.setVisibility(View.VISIBLE);
               reHolder.state.setText("异常");
               reHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
            }else if (reHolder.bean.getIsnormal().equals(TagFinal.TRUE)){
                reHolder.state.setText("正常");
                reHolder.remark.setVisibility(View.GONE);
                reHolder.state.setTextColor(ColorRgbUtil.getGreen());
            }else{
                reHolder.state.setText("未检测");
                reHolder.remark.setVisibility(View.GONE);
                reHolder.state.setTextColor(ColorRgbUtil.getGrayBackground());
            }
            reHolder.title.setText(reHolder.bean.getTitle());
            setMultList(reHolder.bean.getAddImg(), reHolder.show_mult);
            List<String> photos= new ArrayList<>();
            List<String> images=StringUtils.listToStringSplitCharacters(reHolder.bean.getImage(),"|");
            photos.addAll(reHolder.bean.getImage()==null?photos:images);
            reHolder.show_mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.show_mult.setVisibility(View.GONE);
                reHolder.show_mult.addItem(photos);
            }else{
                reHolder.show_mult.setVisibility(View.VISIBLE);
                reHolder.show_mult.addItem(photos);
            }


        }else if (holder instanceof EditorViewHolder){
            EditorViewHolder editor= (EditorViewHolder) holder;
            editor.bean=dataList.get(position);
            if (StringJudge.isEmpty(editor.bean.getContent())){
            }else{
                editor.editor.setText(editor.bean.getContent());
            }
            editor.editor.setText(editor.bean.getTitle());

        }else if(holder instanceof TopViewHolder){
            TopViewHolder topHolder = (TopViewHolder) holder;
            topHolder.addBean=dataList.get(0);
            topHolder.add_time.setText(topHolder.addBean.getTime_name());
            topHolder.add_type.setText(topHolder.addBean.getType_name());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }



    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView state;
        TextView remark;
        MultiPictureView show_mult;
        AddBean bean;
        ItemViewHolder(View itemView) {
            super(itemView);
            title=  itemView.findViewById(R.id.duty_not_name);
            state=  itemView.findViewById(R.id.duty_not_state);
            remark=  itemView.findViewById(R.id.duty_not_remark);
            show_mult=  itemView.findViewById(R.id.duty_not_mult);
        }
    }


    public void addMult(String uri,MultiPictureView add_mult){
        if (uri==null) return;
        add_mult.addItem(uri);
    }
    public void setMultList(List<String> list,MultiPictureView add_mult){
        add_mult.clearItem();
        if (StringJudge.isEmpty(list))return;
        for (String photo:list){
            if (photo==null) continue;
            addMult(photo,add_mult);
        }
    }


    private class EditorViewHolder extends RecyclerView.ViewHolder {

        TextView editor;
        TextView title;
        AddBean bean;
        EditorViewHolder(View itemView) {
            super(itemView);
            editor=  itemView.findViewById(R.id.duty_not_item_edit);
            title=  itemView.findViewById(R.id.duty_not_title);
        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {
        TextView add_time;
        TextView add_type;
        RelativeLayout layout;
        AddBean addBean;
        TopViewHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.duty_add_type_layout);
            add_time=  itemView.findViewById(R.id.duty_add_time);
            add_type=  itemView.findViewById(R.id.duty_add_duty_type);

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
