package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.bean.AwardType;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/4/5.
 */

public class AwardDialogTypeAdapter extends XlistAdapter<AwardType> {

    public AwardDialogTypeAdapter(Context context, List<AwardType> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<AwardType> list) {
        AwardType type=list.get(position);
        holder.getView(TextView.class,R.id.award_grid_item_text).setText(type.getType_name());

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
