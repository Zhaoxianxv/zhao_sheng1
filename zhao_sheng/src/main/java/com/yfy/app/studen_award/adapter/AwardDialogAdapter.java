package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.bean.GradeBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/4/5.
 */

public class AwardDialogAdapter extends XlistAdapter<GradeBean> {

    public AwardDialogAdapter(Context context, List<GradeBean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<GradeBean> list) {
        GradeBean type=list.get(position);
        holder.getView(TextView.class,R.id.award_grid_item_text).setText(type.getClass_name());
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo rexlist=new ResInfo();
        rexlist.layout= R.layout.award_dialog_grid_item;
        rexlist.initIds=new int[]{
                R.id.award_grid_item_text
        };
        return rexlist;
    }
}
