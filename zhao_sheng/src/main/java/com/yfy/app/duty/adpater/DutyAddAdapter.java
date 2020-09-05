package com.yfy.app.duty.adpater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.duty.bean.AddBean;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yfy1 on 2016/10/17.
 */
public class DutyAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<AddBean> dataList;
    private Activity mContext;

    public void setDataList(List<AddBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }

    public List<AddBean> getDataList() {
        return dataList;
    }

    // 当前加载状态，默认为加载完成
    private int loadState = 2;

    public DutyAddAdapter(Activity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView

        if (dataList.get(position).isType_top()){
            return TagFinal.TYPE_FOOTER;
        }else{
            if (dataList.get(position).getId().equals("0")){
                return TagFinal.THREE_INT;
            }else{
                return TagFinal.TYPE_ITEM;
            }
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_add_item_one, parent, false);
            return new RecyclerViewHolder(view);

        }
        if (viewType == TagFinal.THREE_INT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_long_txt_edit_card, parent, false);
            return new EditorViewHolder(view);

        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.duty_add_item_three, parent, false);
            return new TopViewHolder(view);
//
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.index=position;

            reHolder.name.setText(reHolder.bean.getTitle());
            setMultList(reHolder.bean.getAddImg(), reHolder.add_mult);
            List<String> photos= new ArrayList<>();
            List<String> images=StringUtils.listToStringSplitCharacters(reHolder.bean.getImage(),"|");
            photos.addAll(reHolder.bean.getImage()==null?photos:images);
            reHolder.show_mult.clearItem();
            if (StringJudge.isEmpty(photos)){
                reHolder.show_mult.setVisibility(View.GONE);
                reHolder.show_mult.addItem(photos);
            }else{
                reHolder.show_mult.setVisibility(View.VISIBLE);
                reHolder.show_mult.addItem(photos);
            }
            //edit

            if (reHolder.bean.getIsnormal().equals(TagFinal.FALSE)){
                reHolder.type_yes.setChecked(false);
                reHolder.type_no.setChecked(true);
                reHolder.edit.setVisibility(View.VISIBLE);
                reHolder.edit.setText(reHolder.bean.getContent());
            }else if (reHolder.bean.getIsnormal().equals(TagFinal.TRUE)){
                reHolder.type_yes.setChecked(true);
                reHolder.type_no.setChecked(false);
                reHolder.edit.setVisibility(View.GONE);
            }else{
                reHolder.type_yes.setChecked(false);
                reHolder.type_no.setChecked(false);
                reHolder.edit.setVisibility(View.GONE);
            }

        }else if (holder instanceof EditorViewHolder){
            EditorViewHolder editor= (EditorViewHolder) holder;
            editor.bean=dataList.get(position);
            if (StringJudge.isEmpty(editor.bean.getContent())){

            }else{
                editor.editor.setText(editor.bean.getContent());
            }

        }else if(holder instanceof TopViewHolder){
            TopViewHolder topHolder = (TopViewHolder) holder;
            topHolder.addBean=dataList.get(0);
            topHolder.add_time.setText(topHolder.addBean.getTime_name());
            topHolder.add_type.setText(topHolder.addBean.getType_name());
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size() ;
    }





    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView submit;
        RadioGroup group;
        RadioButton type_no;
        RadioButton type_yes;
        MultiPictureView add_mult;
        MultiPictureView show_mult;
        EditText edit;
        int index;
        AddBean bean;
        private void initView(){
            notifyItemChanged(index,bean);
        }
        RecyclerViewHolder(View itemView) {
            super(itemView);
            edit=  itemView.findViewById(R.id.duty_add_no_remark);
            name=  itemView.findViewById(R.id.duty_add_name);
            submit=  itemView.findViewById(R.id.duty_add_item_submit);
            group=  itemView.findViewById(R.id.duty_add_radio_group);
            type_no=  itemView.findViewById(R.id.duty_add_no);
            type_yes=  itemView.findViewById(R.id.duty_add_yes);
            add_mult=  itemView.findViewById(R.id.duty_add_mult);
            show_mult=  itemView.findViewById(R.id.duty_show_mult);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.duty_add_no:
                            bean.setIsnormal(TagFinal.FALSE);
                            type_yes.setTextColor(ColorRgbUtil.getBaseColor());
                            type_no.setTextColor(ColorRgbUtil.getWhite());
                            type_no.setBackgroundResource(R.drawable.radius_rigth_bottom4_top4);
                            type_yes.setBackgroundColor(Color.TRANSPARENT);
                            edit.setVisibility(View.VISIBLE);
                            break;
                        case R.id.duty_add_yes:
                            bean.setIsnormal(TagFinal.TRUE);
                            bean.setContent("");
                            type_yes.setTextColor(ColorRgbUtil.getWhite());
                            type_no.setTextColor(ColorRgbUtil.getBaseColor());
                            type_yes.setBackgroundResource(R.drawable.radius_left_bottom4_top4);
                            type_no.setBackgroundColor(Color.TRANSPARENT);
                            edit.setVisibility(View.GONE);
                            break;
                    }
                }
            });



            add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
                @Override
                public void onAddClick(View view) {
//                    typeDialog.showAtBottom();
                    if (intermult!=null){
                        intermult.addIcon(bean.getId(),index );
                    }
                }
            });
            add_mult.setClickable(false);
            add_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
                @Override
                public void onDeleted(@NotNull View view, int inde) {
                    add_mult.removeItem(inde,true);
                    bean.getAddImg().remove(inde);
                    notifyItemChanged(index,bean);
                }
            });

            add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
            show_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
            show_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
                @Override
                public void onDeleted(@NotNull View view, int inde) {
                    if (intermult!=null){
                        intermult.delImage(bean,index, inde);
                    }
                }
            });
            edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String content=s.toString();
                    bean.setContent(content);
                }
            });
        }
    }


    public void addMult(String uri,MultiPictureView add_mult){
        if (uri==null) return;
        add_mult.addItem(uri);
    }
    public void setMultList(List<String> list,MultiPictureView add_mult){
        add_mult.clearItem();
        if (StringJudge.isEmpty(list))return;
        for (String photo:list){
            if (photo==null) continue;
            addMult(photo,add_mult);
        }
    }



    public Addmult intermult;

    public void setAddmult(Addmult intermult) {
        this.intermult = intermult;
    }

    public interface Addmult{
        void addIcon(String id,int index);
        void delImage(AddBean bean,int postion,int index);
    }




    private class EditorViewHolder extends RecyclerView.ViewHolder {

        EditText editor;
        TextView submit;
        AddBean bean;
        EditorViewHolder(View itemView) {
            super(itemView);
            editor=  itemView.findViewById(R.id.duty_add_edit);
            submit=  itemView.findViewById(R.id.duty_add_item_submit);
            editor.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String content=s.toString();
                    bean.setContent(content);
                }
            });

        }
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {

        TextView add_time;
        TextView add_type;
        RelativeLayout layout;
        AddBean addBean;
        TopViewHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.duty_add_type_layout);
            add_time=  itemView.findViewById(R.id.duty_add_time);
            add_type=  itemView.findViewById(R.id.duty_add_duty_type);

        }
    }




    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }


}
