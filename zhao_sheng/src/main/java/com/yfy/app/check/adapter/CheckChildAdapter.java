package com.yfy.app.check.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.check.CheckDetailActivity;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.IllAllBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<IllAllBean> dataList;
    private Activity mContext;
    private boolean isdel=false;

    public void setIsdel(boolean isdel) {
        this.isdel = isdel;
    }

    public void setDataList(List<IllAllBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    private CheckStu checkStu;

    public void setCheckStu(CheckStu checkStu) {
        this.checkStu = checkStu;
    }

    public CheckChildAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_user_ill, parent, false);
            return new ChildHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ChildHolder) {
            ChildHolder reHolder = (ChildHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getIlltype());
            reHolder.state.setText(reHolder.bean.getIllstate());
            switch (reHolder.bean.getIllstate()){
                case "未返校":
                    reHolder.state.setTextColor(ColorRgbUtil.getOrange());
                    break;
                case "已返校":
                    reHolder.state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
                    default:
                        reHolder.state.setTextColor(ColorRgbUtil.getForestGreen());
                        break;
            }
            reHolder.start.setText(StringUtils.getTextJoint("开始日期:%1$s",reHolder.bean.getIlldate()));
            reHolder.end.setText(StringUtils.getTextJoint("返校日期:%1$s",reHolder.bean.getReturndate()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ChildHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        TextView start;
        TextView end;
        RelativeLayout layout;
        IllAllBean bean;
        ChildHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_all_name);
            state=  itemView.findViewById(R.id.check_all_state);
            start=  itemView.findViewById(R.id.check_all_start_time);
            end=  itemView.findViewById(R.id.check_all_end_time);
            layout=  itemView.findViewById(R.id.check_all_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent =new Intent(mContext,CheckDetailActivity.class);
                    intent.putExtra(TagFinal.OBJECT_TAG,checkStu);
                    intent.putExtra(TagFinal.id,bean);
                    intent.putExtra(TagFinal.TYPE_TAG,isdel);
                    mContext.startActivityForResult(intent,TagFinal.UI_REFRESH);
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
