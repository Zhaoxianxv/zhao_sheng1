package com.yfy.app.ebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Logger;
import com.yfy.view.swipe.SimpleSwipeListener;
import com.yfy.view.swipe.SwipeLayout;
import com.yfy.view.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfyandr on 2017/9/6.
 */

public class EbookAdminAdpater extends BaseSwipeAdapter {

    private String gray="#a3a3a3";
    private String blue="#087FD1";
    private SwipeLayout swipeLayout;



    private NewsAdapterDeleteonClick viewDeleteonClick;

    public interface NewsAdapterDeleteonClick {
        void delete(EbookBean bean);
    }

    public void setNewsAdapterDeleteonClick(NewsAdapterDeleteonClick viewDeleteonClick) {
        this.viewDeleteonClick = viewDeleteonClick;
    }

    private List<EbookBean> adminNewsList = new ArrayList<EbookBean>();
    private Context context;
    private LayoutInflater minflater;

    public void setAdminNewsList(List<EbookBean> adminNewsList) {

        this.adminNewsList = adminNewsList;
        Logger.e("zxx", "setAdminNewsList----:" + adminNewsList.size());
        notifyDataSetChanged();
    }

    public EbookAdminAdpater(Context context) {
        this.context = context;
        minflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return adminNewsList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        // TODO Auto-generated method stub
        return R.id.swipe;
    }


    @Override
    public View generateView(int position, ViewGroup parent) {

        View convertView = minflater.inflate(R.layout.ebook_file_admin_item, parent, false);
        final int num = position;
        swipeLayout= (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));
        // 当隐藏的删除menu被打开的时候的回调函数
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//				Toast.makeText(context, "Open", Toast.LENGTH_SHORT).show();
            }
        });
        // 双击的回调函数
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//				Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @SuppressWarnings("ResourceAsColor")
    @Override
    public void fillValues(final int position, View convertView) {

        String title;
        title = adminNewsList.get(position).getTitle();
        final String filePath=adminNewsList.get(position).getFilePath();
        final String url=adminNewsList.get(position).getFileurl();
        String pass=url.substring(url.lastIndexOf("."),url.length());
        final EbookBean bean=adminNewsList.get(position);
        ViewHolder viewholder = new ViewHolder();
        viewholder.title = (TextView) convertView.findViewById(R.id.item_title);
        viewholder.yes = (TextView) convertView.findViewById(R.id.yes);
        convertView.setTag(viewholder);
        viewholder.title.setText(title+pass);

        // 添加删除布局的点击事件
        viewholder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (viewDeleteonClick != null) {
                    viewDeleteonClick.delete(bean);
                }
            }
        });
    }

    class ViewHolder {
        public TextView title;
        public TextView yes;

    }
}
