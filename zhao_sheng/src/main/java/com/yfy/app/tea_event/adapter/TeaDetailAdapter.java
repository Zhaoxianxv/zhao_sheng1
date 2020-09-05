package com.yfy.app.tea_event.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.app.tea_event.bean.TeaDeBean;
import com.yfy.app.tea_event.bean.TeaDeInfo;
import com.yfy.app.tea_event.bean.TeaEventRes;
import com.yfy.app.tea_event.bean.Teacher;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TeaDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private TeaEventRes data;
    public void initData(TeaEventRes re){
        if (StringJudge.isEmpty(re.getEvaluatelist()))return;
        this.data=re;
        dataList.clear();
        for (TeaDeInfo info:data.getEvaluatelist()){
            switch (info.getType()){
                case "单选":
                    TeaDeBean bean=new TeaDeBean();
                    bean.setView_type(TagFinal.TYPE_PARENT);
                    bean.setType(info.getType());
                    bean.setEvaluatetitle(info.getEvaluatetitle());
                    bean.setEvaluateid(info.getEvaluateid());
                    dataList.add(bean);
                    for (TeaDe teade:info.getEvaluateselect()){
                        TeaDeBean be=new TeaDeBean();
                        be.setView_type(TagFinal.TYPE_REFRECH);
                        be.setTitle(teade.getTitle());
                        be.setId(teade.getId());
                        be.setIscheck(teade.getIscheck());
                        dataList.add(be);
                        for (TeaDeBean bea:teade.getEvaluatelast()){
                            bea.setId(be.getId());
                            bea.setIsShow(teade.getIscheck());
                            dataList.add(bea);
                        }
                    }
                    break;
                case "文本":
                    TeaDeBean te=new TeaDeBean();
                    te.setView_type(TagFinal.TYPE_FOOTER);
                    te.setType(info.getType());
                    te.setEvaluatetitle(info.getEvaluatetitle());
                    te.setContent(info.getContent());
                    te.setEvaluateid(info.getEvaluateid());
                    te.setMaxword(info.getMaxword());
                    dataList.add(te);
                    break;
            }
        }
        notifyDataSetChanged();
    }


    private List<TeaDeBean> dataList;
    private Teacher teacher;
    private Activity mContext;
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public TeaEventRes getRes() {

        if (StringJudge.isNull(data))
            return null;
        for (TeaDeInfo info:data.getEvaluatelist()){
            switch (info.getType()){
                case "单选":
                    for (TeaDe teade:info.getEvaluateselect()){
                        teade.setIscheck(initBean(TagFinal.TYPE_REFRECH, teade.getId()).getIscheck());
                        for (TeaDeBean bea:teade.getEvaluatelast()){
                            bea.setSelectischeck(initBean(TagFinal.TYPE_CHILD, bea.getSelectid()).getSelectischeck());
                        }
                    }
                    break;
                case "文本":
                    info.setContent(initBean(TagFinal.TYPE_FOOTER, info.getEvaluateid()).getContent());
                    break;
            }
        }
        return data;
    }

    private TeaDeBean initBean(int tag,String id){
        for (TeaDeBean bean:dataList) {
            switch (tag){
                case TagFinal.TYPE_FOOTER:
                    if (bean.getView_type()!=tag)continue;
                    if (id.equals(bean.getEvaluateid()))return bean;
                    break;
                case TagFinal.TYPE_REFRECH:
                    if (bean.getView_type()!=tag)continue;
                    if (id.equals(bean.getId()))return bean;
                    break;
                case TagFinal.TYPE_CHILD:
                    if (bean.getView_type()!=tag)continue;
                    if (id.equals(bean.getSelectid()))return bean;
                    break;


            }
        }
        return null;
    }
    public TeaDetailAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (position==0){
            return TagFinal.TYPE_TOP;
        }else{
            return dataList.get(position-1).getView_type();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_item, parent, false);
            return new TopHolder(view);

        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text, parent, false);
            return new TextHolder(view);

        }
        if (viewType == TagFinal.TYPE_REFRECH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text, parent, false);
            return new TextHolder(view);

        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text_child, parent, false);
            return new ChildHolder(view);

        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text_editor, parent, false);
            return new EditorHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TopHolder) {
            TopHolder reHolder = (TopHolder) holder;
            reHolder.bean=teacher;
            reHolder.tag.setVisibility(View.GONE);
            reHolder.name.setText(reHolder.bean.getTeachername());
            reHolder.content.setText(reHolder.bean.getCoursename());
            GlideTools.chanCircle(mContext, reHolder.bean.getHeadpic(), reHolder.header,R.drawable.oval_gray);
            if (reHolder.bean.getIsevaluated().equals(TagFinal.FALSE)){
                reHolder.state.setText("未评测");
                reHolder.state.setTextColor(ColorRgbUtil.getOrange());
            }else{
                reHolder.state.setText("已评测");
                reHolder.state.setTextColor(ColorRgbUtil.getGreen());
            }
        }
        if (holder instanceof TextHolder){
            TextHolder text=(TextHolder)holder;
            text.bean=dataList.get(position-1);
            text.index=position;
            if (text.bean.getView_type()==TagFinal.TYPE_REFRECH){
                text.name.setText(text.bean.getTitle());
                text.name.setTextSize(16f);
                if (text.bean.getIscheck().equals(TagFinal.TRUE)){
                    text.name.setTextColor(ColorRgbUtil.getGreen());
                }else{
                    text.name.setTextColor(ColorRgbUtil.getGrayText());
                }
            }else{
                text.name.setText(text.bean.getEvaluatetitle());
                text.name.setTextSize(18f);

                text.name.setTextColor(ColorRgbUtil.getBaseText());
            }
        }
        if (holder instanceof ChildHolder){
            ChildHolder child= (ChildHolder) holder;
            child.bean=dataList.get(position-1);
            child.index=position;
            child.name.setText(child.bean.getSelecttitle());
            if (child.bean.getSelectischeck().equals(TagFinal.TRUE)){
                child.name.setTextColor(ColorRgbUtil.getBaseText());
                child.tag.setVisibility(View.VISIBLE);
            }else{
                child.name.setTextColor(ColorRgbUtil.getGrayText());
                child.tag.setVisibility(View.GONE);
            }
            if (child.bean.getIsShow().equals(TagFinal.TRUE)){
                child.layout.setVisibility(View.VISIBLE);
                child.name.setVisibility(View.VISIBLE);

            }else{
                child.name.setVisibility(View.GONE);
                child.tag.setVisibility(View.GONE);
                child.layout.setVisibility(View.GONE);
            }
        }
        if (holder instanceof EditorHolder){
            EditorHolder editor= (EditorHolder) holder;
            editor.bean=dataList.get(position-1);
            editor.name.setText(editor.bean.getEvaluatetitle());
            editor.editText.setText(editor.bean.getContent() );
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size()+1;
    }

    private class TopHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView state;
        ImageView header;
        TextView content;
        AppCompatImageView tag;
        RelativeLayout layout;
        Teacher bean;
        TopHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tea_event_name);
            tag=  itemView.findViewById(R.id.tea_event_tag);
            content= (TextView) itemView.findViewById(R.id.tea_event_content);
            state=  itemView.findViewById(R.id.tea_event_state);
            header=  itemView.findViewById(R.id.tea_event_header);
            layout=  itemView.findViewById(R.id.tea_event_layout);


        }
    }
    private class TextHolder extends RecyclerView.ViewHolder{
        TextView name;
        int index;
        TeaDeBean bean;
        public TextHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.tea_event_text);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getView_type()==TagFinal.TYPE_PARENT)return;
                    initData(index,bean.getId());
                    bean.setIscheck(TagFinal.TRUE);
                    notifyItemChanged(index,bean);
                }
            });
        }
    }

    private void initData(int index,String id){
        int i=0;
        for (TeaDeBean tea:dataList){
            i++;
            if (tea.getView_type()==TagFinal.TYPE_REFRECH){
                if (tea.getIscheck().equals(TagFinal.TRUE)){
                    if (index==i)continue;
                    tea.setIscheck(TagFinal.FALSE);
                    notifyItemChanged(i,tea );
                }
            }
            if (tea.getView_type()==TagFinal.TYPE_CHILD){
                if (tea.getId().equals(id)){
                    tea.setIsShow(TagFinal.TRUE);
                    notifyItemChanged(i,tea );
                }else{
                    tea.setIsShow(TagFinal.FALSE);
                    tea.setSelectischeck(TagFinal.FALSE);
                    notifyItemChanged(i,tea );
                }
            }
        }
    }
    private class ChildHolder extends RecyclerView.ViewHolder{

        TextView name;
        AppCompatImageView tag;
        RelativeLayout layout;
        TeaDeBean bean;
        int index;
        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tea_event_text);
            tag=itemView.findViewById(R.id.tea_event_text_tag);
            layout=itemView.findViewById(R.id.tea_event_text_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bean.setSelectischeck(bean.getSelectischeck().equals(TagFinal.TRUE)?TagFinal.FALSE:TagFinal.TRUE);
                    notifyItemChanged(index,bean);
                }
            });
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
            editText.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    int maxNum=ConvertObjtect.getInstance().getInt(bean.getMaxword());
                    if (s.length()>maxNum){
                        Toast.makeText(mContext, "字数超出限制！", Toast.LENGTH_LONG).show();
                        s.delete(maxNum,s.length() );
                    }
                    String content=s.toString().trim();
                    bean.setContent(content);
                }
            });
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
