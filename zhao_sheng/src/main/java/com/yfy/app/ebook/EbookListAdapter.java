package com.yfy.app.ebook;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/31.
 */

public class EbookListAdapter extends XlistAdapter<EbookBean> {


    public EbookListAdapter(Context context, List<EbookBean> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<EbookBean> list) {
        EbookBean bean=list.get(position);
        holder.getView(TextView.class,R.id.ebook_name).setText(bean.getUser_name());
        holder.getView(TextView.class,R.id.ebook_title).setText(bean.getTitle());
        TextView button= holder.getView(TextView.class,R.id.ebook_button);
        ImageView icon=holder.getView(ImageView.class,R.id.ebook_image);

        GlideTools.loadImage(context,bean.getImage(),icon);

//
        final TextView but=button;
        final String url=bean.getFileurl();
        final String title=bean.getTitle();
        final String filePath=bean.getFilePath();

        if (bean.isdown()){
            button.setText("打开");
        }else{
            button.setText("下载");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doButton!=null){
                    doButton.button(but,url,title,filePath);
                }
            }
        });

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo info=new ResInfo();
        info.layout= R.layout.ebook_list_item;
        info.initIds=new int[]{
                R.id.ebook_image,
                R.id.ebook_button,
                R.id.ebook_title,
                R.id.ebook_name,

        };
        return info;
    }

    private DoButton doButton;

    public DoButton getDoButton() {
        return doButton;
    }

    public void setDoButton(DoButton doButton) {
        this.doButton = doButton;
    }

    interface DoButton{
        void button(TextView t,String is,String title,String file);
    }
}
