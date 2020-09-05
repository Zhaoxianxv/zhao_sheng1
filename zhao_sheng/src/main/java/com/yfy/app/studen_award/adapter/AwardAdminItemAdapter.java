package com.yfy.app.studen_award.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.bean.AwardBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfyandr on 2017/4/28.
 */

public class AwardAdminItemAdapter extends XlistAdapter<AwardBean> {



    public AwardAdminItemAdapter(Context context, List<AwardBean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<AwardBean> list) {

        AwardBean bean=list.get(position);
        holder.getView(TextView.class,R.id.award_admin_list_item_type).setText(bean.getType());
        holder.getView(TextView.class,R.id.award_admin_list_item_time).setText(bean.getDate().replace("/","-"));
        holder.getView(TextView.class,R.id.award_admin_list_item_name).setText(bean.getTeacher());

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo res=new ResInfo();
        res.layout=R.layout.award_admin_item;
        res.initIds=new int[]{
            R.id.award_admin_list_item_name,
            R.id.award_admin_list_item_type,
            R.id.award_admin_list_item_time,
        };

        return res;
    }
}
