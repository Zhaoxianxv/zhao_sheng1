package com.yfy.app.cyclopedia.adpater;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/20.
 */

public class GridAdapter extends XlistAdapter<String> {



    public GridAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<String> list) {
        String name=list.get(position);
        holder.getView(TextView.class,R.id.cyc_hot_item_tv).setText(name);

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.cyc_grid_item;
        resInfo.initIds=new int[]{
                R.id.cyc_hot_item_tv
        };
        return resInfo;
    }
}
