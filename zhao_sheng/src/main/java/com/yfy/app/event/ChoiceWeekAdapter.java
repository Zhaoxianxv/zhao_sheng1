package com.yfy.app.event;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.duty.bean.Week;
import com.yfy.app.duty.bean.WeekBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class ChoiceWeekAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Week> dataList;
    private Activity mContext;
    private String type="yyyy年MM月dd";

    public void setDataList(List<Week> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }



    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public ChoiceWeekAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return TagFinal.TWO_INT;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.TWO_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_singe_item_layout, parent, false);
            return new ChildViewHolder(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder cHolder = (ChildViewHolder) holder;
            cHolder.bean=dataList.get(position);
            cHolder.name.setText(cHolder.bean.getWeektitle()+" : "+cHolder.bean.getStartdate()+" —— " +cHolder.bean.getEnddate());

            if (cHolder.bean.getIscurrent().equals(TagFinal.TRUE)){

            }else{

            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }

    public class ChildViewHolder  extends RecyclerView.ViewHolder {
        TextView name;
        Week bean;
        ChildViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.selected_item_name);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    Intent intent =new Intent();
                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
                    mContext.setResult(Activity.RESULT_OK,intent);
                    mContext.finish();

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
