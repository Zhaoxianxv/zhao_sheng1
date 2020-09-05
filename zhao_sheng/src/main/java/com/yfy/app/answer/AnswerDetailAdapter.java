package com.yfy.app.answer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.answer.bean.AnswerListBean;
import com.yfy.final_tag.Base;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/9.
 */

public class AnswerDetailAdapter extends XlistAdapter<AnswerListBean> {

    private  String user_name;
    public AnswerDetailAdapter(Context context, List<AnswerListBean> list,String name) {
        super(context, list);
        this.user_name=name;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<AnswerListBean> list) {
        AnswerListBean bean=list.get(position);
        final boolean is;
        final int i=position;
        final String id=bean.getId();
        final String name=bean.getUser_name();
        TextView tag=holder.getView(TextView.class,R.id.answer_detail_item_tag);
        ImageView head=holder.getView(ImageView.class,R.id.answer_detail_item_head);
        ImageView parise=holder.getView(ImageView.class,R.id.answer_detail_item_parise);
        ImageView image=holder.getView(ImageView.class,R.id.answer_detail_item_image);
        ImageView adopt=holder.getView(ImageView.class,R.id.answer_detail_item_adopt);

        holder.getView(TextView.class,R.id.answer_detail_item_time).setText(bean.getTime());
        holder.getView(TextView.class,R.id.answer_detail_item_user_name).setText(bean.getUser_name());
        holder.getView(TextView.class,R.id.answer_detail_item_parise_num).setText(bean.getPraise_count());
        holder.getView(TextView.class,R.id.answer_detail_item_content).setText(bean.getContent());


        GlideTools.chanCircle(context,bean.getUser_avatar(),head, R.drawable.head_user);

        if (StringJudge.isEmpty(bean.getImage())){
            image.setVisibility(View.GONE);
        }else{
            final String url=bean.getImage();
            image.setVisibility(View.VISIBLE);
            GlideTools.loadImage(context,url,image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewOnClick!=null){
                        viewOnClick.bigImage(url);
                    }
                }
            });
        }

        final String is_answer=bean.getIsanswer();
        if (is_answer.equals("false")){
            tag.setVisibility(View.GONE);
        }else{
            tag.setVisibility(View.VISIBLE);
        }
        //点赞true可点
        if (bean.getIs_praise().equals("true")){
            parise.setImageResource(R.mipmap.praise_red);
            parise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewOnClick!=null){
                        viewOnClick.isparise(0,id);
                    }
                }
            });

        }else{
            parise.setImageResource(R.mipmap.praise_gray);
            parise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewOnClick!=null){
                        viewOnClick.isparise(1,id);
                    }
                }
            });
        }

        if (Base.user.getName().equals(user_name)){

            adopt.setVisibility(View.VISIBLE);
        }else{
            adopt.setVisibility(View.GONE);
        }


        final ImageView icon=adopt;
        final String del=bean.getCandel();
        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewOnClick!=null){
                    viewOnClick.adopt(icon,id,del,is_answer);
                }
            }
        });


    }

    @Override
    public ResInfo getResInfo() {

        ResInfo info=new ResInfo();
        info.layout= R.layout.answer_detail_item;
        info.initIds=new int[]{
                R.id.answer_detail_item_tag,
                R.id.answer_detail_item_head,
                R.id.answer_detail_item_user_name,
                R.id.answer_detail_item_time,
                R.id.answer_detail_item_adopt,
                R.id.answer_detail_item_parise,
                R.id.answer_detail_item_parise_num,
                R.id.answer_detail_item_content,
                R.id.answer_detail_item_image,
        };
        return info;
    }

    public ViewOnClick viewOnClick;

    public void setViewOnClick(ViewOnClick viewOnClick) {
        this.viewOnClick = viewOnClick;
    }

    public interface ViewOnClick{
        void isparise(int is,String id);
        void bigImage(String url);
        void adopt(ImageView adopt,String id,String del,String is);

    }



}
