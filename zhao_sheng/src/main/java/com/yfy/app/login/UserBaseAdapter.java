package com.yfy.app.login;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.login.bean.UserBaseData;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.TagFinal;

import java.util.List;

/**
 * Created by yfyandr on 2017/9/21.
 */

public class UserBaseAdapter extends XlistAdapter<UserBaseData> {

    public UserBaseAdapter(Context context, List<UserBaseData> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<UserBaseData> list) {
        UserBaseData bean=list.get(position);
        holder.getView(TextView.class,R.id.user_base_left_txt).setText(bean.getTagname());
        TextView rigth=holder.getView(TextView.class,R.id.user_base_rigth_txt);

        final String is=bean.getResult(), inputtype=bean.getInputtype();
        rigth.setText(is);
        final int num=position;
        if (bean.getIsedit().equals(TagFinal.TRUE)){
            rigth.setTextColor(Color.rgb(11,11,11));
            rigth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemOnc!=null){
                        itemOnc.itemOn(is,inputtype, num);
                    }
                }
            });
        }else{
            rigth.setTextColor(Color.GRAY);
            rigth.setOnClickListener(null);
        }
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo res=new ResInfo();
        res.layout= R.layout.user_base_item;
        res.initIds=new int[]{
            R.id.user_base_left_txt,
            R.id.user_base_rigth_txt,
        };
        return res;
    }

    public ItemOnc itemOnc;

    public ItemOnc getItemOnc() {

        return itemOnc;
    }

    public void setItemOnc(ItemOnc itemOnc) {
        this.itemOnc = itemOnc;
    }

    public interface ItemOnc{
        void itemOn(String is,String inputtype,int num);
    }
}
