package com.yfy.app.tea_evaluate.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.tea_evaluate.bean.TeaDetail;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private String base_bg="#b3b3b3", text_color="#444444";
    private List<TeaDetail> dataList;
    private Context mContext;

    public void setDataList(List<TeaDetail> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    private final int TYPE_ICON = 3;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;






    public DetailAdapter(Context mContext, List<TeaDetail> dataList){

        this.mContext=mContext;
        this.dataList = dataList;
    }











    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (dataList.get(position).getType().equals("head")) {
            return TYPE_ITEM ;
        } else if(dataList.get(position).getType().equals("text")) {
            return TYPE_FOOTER;
        }else{
            return TYPE_ICON;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_detail_header, parent, false);
            return new RecyclerTopHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_chart_detail_item, parent, false);
            return new ItemHolder(view);
        }else if (viewType == TYPE_ICON) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_chart_detail_icon, parent, false);
            return new IconHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerTopHolder) {
            RecyclerTopHolder reHolder = (RecyclerTopHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getTitle());
            reHolder.submit.setText(reHolder.bean.getState());
            switch (reHolder.bean.getState()){
                case "已通过":
                    reHolder.submit.setTextColor(ColorRgbUtil.getTeaTwo());
                    break;
                case "未通过":
                    reHolder.submit.setTextColor(ColorRgbUtil.getTeaOne());
                    break;
                default:
                    reHolder.submit.setTextColor(ColorRgbUtil.getOrange());
                    break;

            }



            GlideTools.chanCircle(mContext,reHolder.bean.getHead_image(),reHolder.head, R.drawable.head_user);

        } else if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.bean=dataList.get(position);
            if (StringJudge.isEmpty(itemHolder.bean.getTitle())){
                itemHolder.item.setText("");
                itemHolder.item.setBackgroundColor(ColorRgbUtil.getGrayBackground());
            }else{
                itemHolder.item.setText(HtmlTools.getHtmlString(base_bg,
                        itemHolder.bean.getTitle()+"：",
                        text_color,
                        itemHolder.bean.getContent()
                ));
                itemHolder.item.setBackgroundColor(ColorRgbUtil.getWhite());
            }

        } else if (holder instanceof IconHolder) {
            IconHolder iconHolder = (IconHolder) holder;
            iconHolder.bean=dataList.get(position);
            iconHolder.icon.setText("印证材料:"+iconHolder.bean.getIcon());

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerTopHolder extends RecyclerView.ViewHolder {

        ImageView head;
        TextView name;
        TextView submit;
        TeaDetail bean;
        RecyclerTopHolder(View itemView) {
            super(itemView);
            head=itemView.findViewById(R.id.tea_detail_head);
            name= (TextView) itemView.findViewById(R.id.tea_detail_name);
            submit= (TextView) itemView.findViewById(R.id.tea_detail_submit);

        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView item;
        TeaDetail bean;
        ItemHolder(View itemView) {
            super(itemView);
            item =  itemView.findViewById(R.id.tea_detail_item);
        }
    }
    private class IconHolder extends RecyclerView.ViewHolder {
        TextView icon;
        TeaDetail bean;
        IconHolder(View itemView) {
            super(itemView);
            icon =  itemView.findViewById(R.id.tea_detail_icon);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String icon=bean.getContent();
                    String[] name=icon.split(Pattern.quote("."));
                    String nam=name[name.length-1];
                    if (nam.equals("JPG")||nam.equals("jpg")){
                        Intent intent=new Intent(mContext, SingePicShowActivity.class);
                        Bundle b=new Bundle();
                        b.putString(TagFinal.ALBUM_SINGE_URI,bean.getContent());
                        Logger.e("zxx", "onClick: "+ bean.getContent());
                        intent.putExtras(b);
                        mContext.startActivity(intent);
                    }else{
                        Toast.makeText(mContext,"无法打开文件！",Toast.LENGTH_SHORT).show();
                    }
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
