package com.yfy.app.tea_event.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.bean.TeaDeBean;
import com.yfy.app.tea_event.bean.Teacher;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.TagFinal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TeaDAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<TeaDeBean> dataList;

    private Teacher teacher;
    private Activity mContext;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setDataList(List<TeaDeBean> dataList) {
        this.dataList = dataList;
    }



    public TeaDAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView

        return dataList.get(position).getView_type();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_p, parent, false);
            return new PHolder(view);

        }
        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text, parent, false);
            return new TextHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_child, parent, false);
            return new ChildHolder(view);
        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_tag, parent, false);
            return new TagHolder(view);
        }

        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text_editor, parent, false);
            return new EditorHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TextHolder){
            TextHolder text=(TextHolder)holder;
            text.bean=dataList.get(position);
            text.index=position;
            text.name.setText(text.bean.getEvaluatetitle());
            text.name.setTextSize(16f);
            text.name.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        if (holder instanceof PHolder){
            PHolder pHolder=(PHolder)holder;
            pHolder.bean=dataList.get(position);
            pHolder.index=position;
            pHolder.name.setText(pHolder.bean.getTitle());
            pHolder.name.setTextSize(14f);
            pHolder.one.setText("比率:"+pHolder.bean.getPercentage());
            pHolder.two.setText("已投/总投:"+pHolder.bean.getCheckcount()+"/"+pHolder.bean.getMaxcount());
            switch (pHolder.bean.getTitle().trim()){
                case "满意率":
                    pHolder.name.setTextColor(ColorRgbUtil.getGreen());
                    break;
                case "不满意率":
                    pHolder.name.setTextColor(ColorRgbUtil.getBaseColor());
                    break;
            }

        }
        if (holder instanceof EditorHolder){
            EditorHolder editor= (EditorHolder) holder;
            editor.bean=dataList.get(position);
            editor.name.setText(editor.bean.getEvaluatetitle());
            editor.editText.setText(editor.bean.getContent());
            editor.editText.setFocusable(false);
        }
        if (holder instanceof TagHolder){
            TagHolder tag= (TagHolder) holder;
            tag.bean=dataList.get(position);
            tag.name.setText(tag.bean.getTitle());
        }
        if (holder instanceof ChildHolder){
            ChildHolder child= (ChildHolder) holder;
            child.bean=dataList.get(position);
            child.name.setText(child.bean.getSelecttitle());
            child.time.setText("比率:"+child.bean.getPercentage());
            child.one.setText("该项已投人数/不满意人数:"+child.bean.getCheckcount()+"/"+child.bean.getMaxcount());
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class TextHolder extends RecyclerView.ViewHolder{
        TextView name;
        int index;
        TeaDeBean bean;
        public TextHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.tea_event_text);

        }
    }
    private class PHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView one;
        TextView two;
        int index;
        TeaDeBean bean;
        public PHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.tea_event_text);
            one =itemView.findViewById(R.id.tea_event_one);
            two =itemView.findViewById(R.id.tea_event_two);

        }
    }


    private class EditorHolder extends RecyclerView.ViewHolder{

        TextView name;
        EditText editText;
        TeaDeBean bean;
        public EditorHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tea_edit_name);
            editText=itemView.findViewById(R.id.tea_edit_edit);

        }
    }

    private class TagHolder extends RecyclerView.ViewHolder{

        TextView name;
        TeaDeBean bean;
        public TagHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tea_event_tag);

        }
    }
    private class ChildHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView time;
        TextView one;
        TeaDeBean bean;
        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tea_event_child_name);
            time=itemView.findViewById(R.id.tea_event_child_time);
            one=itemView.findViewById(R.id.tea_event_child_one);

        }
    }


    /**
     * 设置上拉加载状态
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */

    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }









}
