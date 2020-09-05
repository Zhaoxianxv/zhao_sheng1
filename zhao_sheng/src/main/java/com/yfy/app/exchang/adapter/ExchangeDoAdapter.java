package com.yfy.app.exchang.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.exchang.bean.CourseInfor;
import com.yfy.base.XlistAdapter;
import com.yfy.final_tag.StringJudge;

import java.util.List;
import java.util.Map;

/**
 * Created by yfyandr on 2017/8/22.
 */

public class ExchangeDoAdapter extends XlistAdapter<Map<String,CourseInfor>> {


    public ExchangeDoAdapter(Context context, List<Map<String,CourseInfor>> list) {
        super(context, list);
    }

    @Override
    public void renderData(int position, ViewHolder holder, List<Map<String,CourseInfor>> list) {
        Map<String,CourseInfor> map=list.get(position);
        TextView itme_1=holder.getView(TextView.class,R.id.exchange_table_item_1);
        TextView itme_2=holder.getView(TextView.class,R.id.exchange_table_item_2);
        TextView itme_3=holder.getView(TextView.class,R.id.exchange_table_item_3);
        TextView itme_4=holder.getView(TextView.class,R.id.exchange_table_item_4);
        TextView itme_5=holder.getView(TextView.class,R.id.exchange_table_item_5);
        TextView itme_6=holder.getView(TextView.class,R.id.exchange_table_item_6);
        String index="第"+(position+1)+"节";
        itme_1.setText(index);

        if (map.get("1")==null){//星期一
            itme_2.setText("");
            itme_2.setBackgroundResource(R.color.exchange_able_2_nu);
            itme_2.setOnClickListener(null);
        }else{
            CourseInfor bean=map.get("1");
            String name=bean.getCoursename()+"\n("+bean.getTeachername()+")";
            itme_2.setText(name);
            final String class_id=bean.getClassid(),
                    no=index+"("+bean.getCoursename()+")",
                    scheduleid=bean.getScheduleid();
            if (StringJudge.isEmpty(bean.getCanchange())){
                itme_2.setOnClickListener(null);
                itme_2.setBackgroundResource(R.color.exchange_able_2);
            }else{
                if (bean.getCanchange().equals("1")){
                    itme_2.setBackgroundResource(R.color.ForestGreen);
                    itme_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onCourse!=null){
                                onCourse.onCourse("",scheduleid,no,"1");
                            }
                        }
                    });
                }else{
                    itme_2.setOnClickListener(null);
                    itme_2.setBackgroundResource(R.color.DarkGray);
                }
            }
        }
        if (map.get("2")==null){//星期2
            itme_3.setText("");
            itme_3.setOnClickListener(null);
            itme_3.setBackgroundResource(R.color.exchange_able_3_nu);
        }else{
            CourseInfor bean=map.get("2");
            String name=bean.getCoursename()+"\n("+bean.getTeachername()+")";
            itme_3.setText(name);
            final String class_id=bean.getClassid(),
                    no=index+"("+bean.getCoursename()+")",
                    scheduleid=bean.getScheduleid();
            if (StringJudge.isEmpty(bean.getCanchange())){
                itme_3.setOnClickListener(null);
                itme_3.setBackgroundResource(R.color.exchange_able_3);
            }else{
                if (bean.getCanchange().equals("1")){
                    itme_3.setBackgroundResource(R.color.ForestGreen);
                    itme_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onCourse!=null){
                                onCourse.onCourse("",scheduleid,no,"2");
                            }
                        }
                    });
                }else{
                    itme_3.setOnClickListener(null);
                    itme_3.setBackgroundResource(R.color.DarkGray);
                }
            }

        }
        if (map.get("3")==null){//星期3
            itme_4.setText("");
            itme_4.setOnClickListener(null);
            itme_4.setBackgroundResource(R.color.exchange_able_4_nu);
        }else{
            CourseInfor bean=map.get("3");
            String name=bean.getCoursename()+"\n("+bean.getTeachername()+")";
            itme_4.setText(name);

            final String class_id=bean.getClassid(),
                    no=index+"("+bean.getCoursename()+")",
                    scheduleid=bean.getScheduleid();
            if (StringJudge.isEmpty(bean.getCanchange())){
                itme_4.setOnClickListener(null);
                itme_4.setBackgroundResource(R.color.exchange_able_4);
            }else{
                if (bean.getCanchange().equals("1")){
                    itme_4.setBackgroundResource(R.color.ForestGreen);
                    itme_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onCourse!=null){
                                onCourse.onCourse("",scheduleid,no,"3");
                            }
                        }
                    });
                }else{
                    itme_4.setOnClickListener(null);
                    itme_4.setBackgroundResource(R.color.DarkGray);
                }
            }
        }
        if (map.get("4")==null){//星期4
            itme_5.setText("");
            itme_5.setOnClickListener(null);
            itme_5.setBackgroundResource(R.color.exchange_able_5_nu);
        }else{
            CourseInfor bean=map.get("4");
            String name=bean.getCoursename()+"\n("+bean.getTeachername()+")";
            itme_5.setText(name);

            final String class_id=bean.getClassid(),
                    no=index+"("+bean.getCoursename()+")",
                    scheduleid=bean.getScheduleid();
            if (StringJudge.isEmpty(bean.getCanchange())){
                itme_5.setOnClickListener(null);
                itme_5.setBackgroundResource(R.color.exchange_able_5);
            }else{
                if (bean.getCanchange().equals("1")){
                    itme_5.setBackgroundResource(R.color.ForestGreen);
                    itme_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onCourse!=null){
                                onCourse.onCourse("",scheduleid,no,"4");
                            }
                        }
                    });
                }else{
                    itme_5.setOnClickListener(null);
                    itme_5.setBackgroundResource(R.color.DarkGray);
                }
            }
        }
        if (map.get("5")==null){//星期5
            itme_6.setText("");
            itme_6.setOnClickListener(null);
            itme_6.setBackgroundResource(R.color.exchange_able_6_nu);
        }else{
            CourseInfor bean=map.get("5");
            String name=bean.getCoursename()+"\n("+bean.getTeachername()+")";
            itme_6.setText(name);

            final String class_id=bean.getClassid(),
                    no=index+"("+bean.getCoursename()+")",
                    scheduleid=bean.getScheduleid();
            if (StringJudge.isEmpty(bean.getCanchange())){
                itme_6.setOnClickListener(null);
                itme_6.setBackgroundResource(R.color.exchange_able_6);
            }else{
                if (bean.getCanchange().equals("1")){
                    itme_6.setBackgroundResource(R.color.ForestGreen);
                    itme_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onCourse!=null){
                                onCourse.onCourse("",scheduleid,no,"5");
                            }
                        }
                    });
                }else{
                    itme_6.setOnClickListener(null);
                    itme_6.setBackgroundResource(R.color.DarkGray);
                }
            }
        }
    }

    @Override
    public ResInfo getResInfo() {
        ResInfo resInfo=new ResInfo();
        resInfo.layout= R.layout.exchange_timeable_item;
        resInfo.initIds=new int[]{
                R.id.exchange_table_item_1,
                R.id.exchange_table_item_2,
                R.id.exchange_table_item_3,
                R.id.exchange_table_item_4,
                R.id.exchange_table_item_5,
                R.id.exchange_table_item_6,
        };
        return resInfo;
    }



    public ExchangeDoTbale onCourse;

    public void setOnCourse(ExchangeDoTbale onCourse) {
        this.onCourse = onCourse;
    }

    public interface ExchangeDoTbale {
        void onCourse(String class_id, String scheduleid,String no1,String time);
    }

}
