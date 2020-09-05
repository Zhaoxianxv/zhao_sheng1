package com.yfy.app.cyclopedia.adpater;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.beans.Note.NoteBean;
import com.yfy.base.XlistAdapter;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/13.
 */

public class CycSpoorAdapter extends XlistAdapter<NoteBean> {



    private Context context;


    public CycSpoorAdapter(Context context, List<NoteBean> list) {
        super(context, list);
        this.context=context;
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<NoteBean> list) {
        NoteBean bean=list.get(position);
        final int i=position;
        holder.getView(TextView.class,R.id.cyclopedia_type_spoor_tv).setText(bean.getAuthor());
        holder.getView(TextView.class,R.id.cyclopedia_type_grade_tv).setText(bean.getTag_title());
        holder.getView(TextView.class,R.id.cyc_spoor_praise_num_tv).setText(bean.getPraise());
        holder.getView(TextView.class,R.id.cyc_commet_it).setText(bean.getContent());
        ImageView keyword=holder.getView(ImageView.class,R.id.cyclopedia_type_two_iv);
        if (bean.getKeyword().equals("自然")){
            Glide.with(context)
                    .load(R.drawable.cyc_tree)
                    .into(keyword);
        }else{
            Glide.with(context)
                    .load(R.drawable.cyc_cultural)
                    .into(keyword);
        }
        GridView gridView=holder.getView(GridView.class,R.id.spoor_gridview);

        MomentsPictureAdapter adapter;
        if (gridView.getAdapter() == null) {
            adapter = new MomentsPictureAdapter(context, bean.getImages(), gridView);
            gridView.setAdapter(adapter);
        } else {
            adapter = (MomentsPictureAdapter) gridView.getAdapter();
            adapter.notifyDataSetChanged(bean.getImages());
        }

        ImageView praise_iv = holder.getView(ImageView.class, R.id.cyc_spoor_praise_iv);

        if (bean.getIspraise().equals("false")){
            Glide.with(context)
                    .load(R.drawable.cyc_praise_ic)
                    .into(praise_iv);
        }else{
            Glide.with(context)
                    .load(R.drawable.cyc_praise_ic_down)
                    .into(praise_iv);
        }
        praise_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenner.onitem(i);
            }
        });

        if (bean.getImages()==null){
            gridView.setVisibility(View.GONE);
        }else{
            gridView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo =new ResInfo();
        resInfo.layout= R.layout.cyc_spoor_item_layout;
        resInfo.initIds=new int[]{
                R.id.cyc_spoor_user_name,
                R.id.cyclopedia_type_spoor_tv,
                R.id.cyclopedia_type_two_iv,
                R.id.cyclopedia_type_grade_tv,
                R.id.cyc_spoor_praise_num_tv,
                R.id.cyc_spoor_praise_iv,
                R.id.cyc_commet_it,
                R.id.spoor_gridview
        };
        return resInfo;
    }


    public PraiseListnner listenner;


    public void setListenner(PraiseListnner listenner) {
        this.listenner = listenner;
    }

    public static interface PraiseListnner {
        public void  onitem(int id);
    }




}
