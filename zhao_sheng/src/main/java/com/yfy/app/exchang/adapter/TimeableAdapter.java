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

public class TimeableAdapter extends XlistAdapter<Map<String,CourseInfor>> {


    private boolean is_5,is_4,is_3,is_2,is_1;
    public TimeableAdapter(Context context, List<Map<String,CourseInfor>> list) {
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

            String name=bean.getCoursename()+"\n\n"+bean.getClassname();
            itme_2.setText(name);
            itme_2.setBackgroundResource(R.color.exchange_able_2);

            final String class_id=bean.getClassid(),
                    scheduleid=bean.getScheduleid(),
                    no=index+"("+bean.getCoursename()+")",
                    class_name=bean.getClassname();
            if (StringJudge.isEmpty(bean.getIschage())){
                itme_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.onCourse(class_id,scheduleid,class_name,no,"1");
                        }
                    }
                });
            }else{

                if (bean.getIschage().equals(R.string.course_exchanged)){
                    itme_2.setBackgroundResource(R.color.red);
                    is_1=true;
                }else{
                    is_1=false;
                    itme_2.setBackgroundResource(R.color.Orange);
                }
                itme_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.did(is_1);
                        }
                    }
                });
            }
        }
        if (map.get("2")==null){//星期2
            itme_3.setText("");
            itme_3.setOnClickListener(null);
            itme_3.setBackgroundResource(R.color.exchange_able_3_nu);
        }else{
            CourseInfor bean=map.get("2");
            String name=bean.getCoursename()+"\n\n"+bean.getClassname();
            itme_3.setText(name);
            itme_3.setBackgroundResource(R.color.exchange_able_3);
            final String class_id=bean.getClassid(),
                    scheduleid=bean.getScheduleid(),
                    no=index+"("+bean.getCoursename()+")",
                    class_name=bean.getClassname();
            if (StringJudge.isEmpty(bean.getIschage())){
                itme_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.onCourse(class_id,scheduleid,class_name,no,"2");
                        }
                    }
                });
            }else{

                if (bean.getIschage().equals(R.string.course_exchanged)){
                    is_2=true;
                    itme_3.setBackgroundResource(R.color.red);
                }else{
                    is_2=false;
                    itme_3.setBackgroundResource(R.color.Orange);
                }
                itme_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.did(is_2);
                        }
                    }
                });
            }

        }
        if (map.get("3")==null){//星期3
            itme_4.setText("");
            itme_4.setOnClickListener(null);
            itme_4.setBackgroundResource(R.color.exchange_able_4_nu);
        }else{
            CourseInfor bean=map.get("3");
            String name=bean.getCoursename()+"\n\n"+bean.getClassname();
            itme_4.setText(name);
            itme_4.setBackgroundResource(R.color.exchange_able_4);
            final String class_id=bean.getClassid(),
                    scheduleid=bean.getScheduleid(),
                    no=index+"("+bean.getCoursename()+")",
                    class_name=bean.getClassname();
            if (StringJudge.isEmpty(bean.getIschage())){
                itme_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.onCourse(class_id,scheduleid,class_name,no,"3");
                        }
                    }
                });
            }else{

                if (bean.getIschage().equals(R.string.course_exchanged)){
                    is_3=true;
                    itme_4.setBackgroundResource(R.color.red);
                }else{
                    is_3=false;
                    itme_4.setBackgroundResource(R.color.Orange);
                }
                itme_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.did(is_3);
                        }
                    }
                });
            }
        }
        if (map.get("4")==null){//星期4
            itme_5.setText("");
            itme_5.setOnClickListener(null);
            itme_5.setBackgroundResource(R.color.exchange_able_5_nu);
        }else{
            CourseInfor bean=map.get("4");
            String name=bean.getCoursename()+"\n\n"+bean.getClassname();
            itme_5.setText(name);
            itme_5.setBackgroundResource(R.color.exchange_able_5);
            final String class_id=bean.getClassid(),
                    scheduleid=bean.getScheduleid(),
                    no=index+"("+bean.getCoursename()+")",
                    class_name=bean.getClassname();
            if (StringJudge.isEmpty(bean.getIschage())){
                itme_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.onCourse(class_id,scheduleid,class_name,no,"4");
                        }
                    }
                });
            }else{

                if (bean.getIschage().equals(R.string.course_exchanged)){
                    itme_5.setBackgroundResource(R.color.red);
                    is_4=true;
                }else{
                    is_4=false;
                    itme_5.setBackgroundResource(R.color.Orange);
                }
                itme_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.did(is_4);
                        }
                    }
                });
            }
        }
        if (map.get("5")==null){//星期5
            itme_6.setText("");
            itme_6.setOnClickListener(null);
            itme_6.setBackgroundResource(R.color.exchange_able_6_nu);
        }else{
            CourseInfor bean=map.get("5");
            String name=bean.getCoursename()+"\n\n"+bean.getClassname();
            itme_6.setText(name);
            itme_6.setBackgroundResource(R.color.exchange_able_6);
            final String class_id=bean.getClassid(),
                    scheduleid=bean.getScheduleid(),
                    no=index+"("+bean.getCoursename()+")",
                    class_name=bean.getClassname();
            if (StringJudge.isEmpty(bean.getIschage())){
                itme_6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.onCourse(class_id,scheduleid,class_name,no,"5");
                        }
                    }
                });
            }else{

                if (bean.getIschage().equals(R.string.course_exchanged)){
                    itme_6.setBackgroundResource(R.color.red);
                    is_5=true;
                }else{
                    itme_6.setBackgroundResource(R.color.Orange);
                    is_5=false;
                }
                itme_6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onCourse!=null){
                            onCourse.did(is_5);
                        }
                    }
                });
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



    public CourseTbale onCourse;

    public void setOnCourse(CourseTbale onCourse) {
        this.onCourse = onCourse;
    }

    public interface CourseTbale {
        void onCourse(String class_id,String scheduleid,String name,String no,String time);
        void did(boolean is);
    }

}
