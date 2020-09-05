package com.yfy.app.duty.adpater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.duty.bean.MainB;
import com.yfy.final_tag.ColorRgbUtil;
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
public class DutyMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<MainB> dataList;
    private Activity mContext;


    public void setDataList(List<MainB> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DutyMainAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.THREE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_main_item_one, parent, false);
            return new ChildViewHolder(view);
        }
        if (viewType == TagFinal.TWO_INT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_main_item, parent, false);
            return new ParentViewHolder(view);
        }
        if (viewType == TagFinal.ONE_INT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_singe_top_txt_center, parent, false);
            return new TopViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder pHolder = (ParentViewHolder) holder;
            pHolder.bean=dataList.get(position);
            pHolder.user.setVisibility(View.VISIBLE);
            pHolder.user.setText(pHolder.bean.getType());
        }
        if (holder instanceof TopViewHolder) {
            TopViewHolder pHolder = (TopViewHolder) holder;
            pHolder.bean=dataList.get(position);
            pHolder.time.setText(pHolder.bean.getDate());
        }
        if (holder instanceof ChildViewHolder){
            ChildViewHolder cHolder = (ChildViewHolder) holder;
            cHolder.bean=dataList.get(position);
            switch (cHolder.bean.getChild_bg()){//bg
                case TagFinal.ONE_INT:
                    cHolder.line.setVisibility(View.GONE);
                    cHolder.layout.setBackgroundResource(R.drawable.radius_top_left5_rigth5);
                    break;
                case TagFinal.TWO_INT:
                    cHolder.line.setVisibility(View.VISIBLE);
                    cHolder.layout.setBackgroundResource(R.drawable.album_default_loading_pic);
                    break;
                case TagFinal.THREE_INT:
                    cHolder.line.setVisibility(View.VISIBLE);
                    cHolder.layout.setBackgroundResource(R.drawable.radius_bottom_left5_rigth5);
                    break;
            }
            if (!cHolder.bean.getTitle().equals("小结")){

                cHolder.mult.setVisibility(View.VISIBLE);
                cHolder.state.setVisibility(View.VISIBLE);
                List<String> photos= new ArrayList<>();
                if (StringJudge.isEmpty(cHolder.bean.getImage())){
                    photos.clear();
                }else{
                    photos=StringUtils.listToStringSplitCharacters(cHolder.bean.getImage(),"|");
                }

                cHolder.mult.clearItem();
                if (StringJudge.isEmpty(photos)){
                    cHolder.mult.setVisibility(View.GONE);
                    cHolder.mult.addItem(photos);
                }else{
                    cHolder.mult.setVisibility(View.VISIBLE);
                    cHolder.mult.addItem(photos);
                }
                cHolder.title.setText(StringUtils.getTextJoint("%1$s(%2$s)",cHolder.bean.getTitle(),cHolder.bean.getRealname()));
                if (cHolder.bean.getIsnormal().equals(TagFinal.TRUE)){
                    cHolder.edit.setVisibility(View.GONE);
                    cHolder.state.setText("正常");
                    cHolder.state.setTextColor(ColorRgbUtil.getGreen());
                }else if (cHolder.bean.getIsnormal().equals(TagFinal.FALSE)){
                    cHolder.edit.setVisibility(View.VISIBLE);
                    cHolder.edit.setText(cHolder.bean.getContent());
                    cHolder.state.setText("异常");
                    cHolder.state.setTextColor(ColorRgbUtil.getBaseColor());
                }else{
                    cHolder.edit.setVisibility(View.GONE);
                    cHolder.state.setText("未检查");
                    cHolder.state.setTextColor(ColorRgbUtil.getGrayText());
                }
            }else{
                cHolder.edit.setVisibility(View.VISIBLE);
                cHolder.state.setVisibility(View.GONE);
                cHolder.mult.setVisibility(View.GONE);
                cHolder.title.setText(StringUtils.getTextJoint("%1$s(%2$s)",cHolder.bean.getTitle(),cHolder.bean.getRealname()));
                cHolder.edit.setText(cHolder.bean.getContent());
            }

        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder {
        TextView user;
        public MainB bean;
        ParentViewHolder(View itemView) {
            super(itemView);
            user= (TextView) itemView.findViewById(R.id.duty_main_item_user);

        }
    }

    public class TopViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        public MainB bean;
        TopViewHolder(View itemView) {
            super(itemView);
            time= (TextView) itemView.findViewById(R.id.public_top_text);
        }
    }
    public class ChildViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView state;
        TextView edit;
        MultiPictureView mult;

        View line;
        RelativeLayout layout;
        public MainB bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            line=  itemView.findViewById(R.id.duty_main_item_line);
            layout=  itemView.findViewById(R.id.duty_item_layout);
            title=  itemView.findViewById(R.id.duty_main_item_name);
            state=  itemView.findViewById(R.id.duty_main_item_state);
            edit=  itemView.findViewById(R.id.duty_main_item_edit);
            mult=  itemView.findViewById(R.id.duty_main_item_mult);
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
