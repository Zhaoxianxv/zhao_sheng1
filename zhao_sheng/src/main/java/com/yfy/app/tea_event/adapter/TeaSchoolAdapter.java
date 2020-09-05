package com.yfy.app.tea_event.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.tea_event.bean.TeaDe;
import com.yfy.app.tea_event.bean.TeaDeInfo;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.MySpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class TeaSchoolAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<TeaDeInfo> dataList;
    private Fragment mContext;

    public void setDataList(List<TeaDeInfo> dataList) {
        this.dataList = dataList;
    }


    public List<TeaDeInfo> getDataList() {
        return dataList;
    }


    public TeaSchoolAdapter(Fragment mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView

        int  mothed= TagFinal.TYPE_ITEM;
        switch (dataList.get(position).getType()){
            case "单选":
                mothed= TagFinal.TYPE_ITEM;
                break;
            case "文本":
                mothed= TagFinal.TYPE_FOOTER;
                break;

        }
       return mothed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_school, parent, false);
            return new RecyclerViewHolder(view);

        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tea_event_text_editor, parent, false);
            return new EditorHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.index=position;
            reHolder.name.setText(reHolder.bean.getEvaluatetitle());
            reHolder.spinner.setAdapter(new MyAdapter(mContext.getActivity(),reHolder.bean.getEvaluateselect()));
        }
        if (holder instanceof EditorHolder){
            EditorHolder editor= (EditorHolder) holder;
            editor.bean=dataList.get(position);
            editor.name.setText(editor.bean.getEvaluatetitle());
            editor.editText.setText(editor.bean.getContent() );
        }
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        MySpinner spinner;
        int index;
        TeaDeInfo bean;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.tea_school_name);
            spinner=  itemView.findViewById(R.id.tea_school_state);
            spinner.setItemClick(new MySpinner.ItemClick() {
                @Override
                public void onClick(int position) {
                    Logger.e("  position "+position);//选择项下标
                    List<TeaDe> list= bean.getEvaluateselect();
                    for (TeaDe de:list){
                        de.setIscheck(TagFinal.FALSE);
                    }
                    list.get(position).setIscheck(TagFinal.TRUE);
                    notifyItemChanged(index, bean);
                }
            });
//              recyclerView +spinner spinner选择后recyclerView会刷新不能直接用
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    TextView tv= (TextView) view;//当前显示选项
//                    String name=tv.getText().toString();
//                    String item = parent.getItemAtPosition(position).toString();
//                    Logger.e("  getEvaluatetitle "+bean.getEvaluatetitle());
//                    Logger.e("  position "+position);//选择项下标
//                    Logger.e("  name "+name);
//                    Logger.e("  item "+item);
//
//                    List<TeaDe> list= bean.getEvaluateselect();
//
//                    if(position==0){
//                        return;
//                    }
//                    if (list.get(position).getTitle().equals(name)){//
//                        return;
//                    }
////                    boolean first=true;
////                    if (first&&position==0){
////                        first=false;
////                        return;
////                    }
//                    for (TeaDe de:list){
//                        de.setIscheck(TagFinal.FALSE);
//                    }
//                    list.get(position).setIscheck(TagFinal.TRUE);
//                    notifyItemChanged(index, bean);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
        }
    }


    private class EditorHolder extends RecyclerView.ViewHolder{

        TextView name;
        EditText editText;
        TeaDeInfo bean;
        public EditorHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tea_edit_name);
            editText=itemView.findViewById(R.id.tea_edit_edit);
            editText.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    int maxNum=ConvertObjtect.getInstance().getInt(bean.getMaxword());
                    if (s.length()>maxNum){
                        Toast.makeText(mContext.getActivity(), "字数超出限制！", Toast.LENGTH_LONG).show();
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
