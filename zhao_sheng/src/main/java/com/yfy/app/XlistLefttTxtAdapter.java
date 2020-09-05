package com.yfy.app;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.base.XlistAdapter;
import com.yfy.db.UserPreferences;

import java.util.List;

/**
 * Created by yfyandr on 2017/5/9.
 */

public class XlistLefttTxtAdapter extends XlistAdapter<String> {

    public XlistLefttTxtAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<String> list) {
        TextView txt=holder.getView(TextView.class,R.id.public_xlist_left_txt);
        txt.setText(list.get(position));
        if (list.get(position).equals(UserPreferences.getInstance().getTermName())){
            txt.setTextColor(Color.rgb(153,36,41));
        }else{
            txt.setTextColor(Color.rgb(98,97,97));
        }

    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.selected_item_lefttxt_back;
        resInfo.initIds=new int[]{
                R.id.public_xlist_left_txt,
        };
        return resInfo;
    }
}
