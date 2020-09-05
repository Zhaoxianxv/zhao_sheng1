package com.yfy.app.answer.fragment;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.answer.bean.AnswerListBean;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/2.
 */

public class Tab1Adapter extends XlistAdapter<AnswerListBean> {

    private final String S="条回答 | ";
    private Context context;

    public Tab1Adapter(Context context, List<AnswerListBean> list) {
        super(context, list);
        this.context=context;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<AnswerListBean> list) {
        AnswerListBean bean=list.get(position);
        holder.getView(TextView.class,R.id.answer_tab1_item_theme).setText(bean.getContent());
        holder.getView(TextView.class,R.id.answer_tab1_item_name).setText(bean.getUser_name());
        String ps=bean.getAnswer_count()+S+bean.getTime();
        holder.getView(TextView.class,R.id.answer_tab1_item_ps).setText(ps);


        ImageView head=holder.getView(ImageView.class,R.id.answer_tab1_item_head);

        GlideTools.chanCircle(context,bean.getUser_avatar(),head,R.drawable.head_user);

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo res=new ResInfo();

        res.layout= R.layout.answer_tab1_itme;
        res.initIds=new int[]{
                R.id.answer_tab1_item_head,
                R.id.answer_tab1_item_theme,
                R.id.answer_tab1_item_ps,
                R.id.answer_tab1_item_name,

        };
        return res;
    }
}
