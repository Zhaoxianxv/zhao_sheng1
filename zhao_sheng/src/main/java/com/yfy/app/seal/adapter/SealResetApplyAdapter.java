package com.yfy.app.seal.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.seal.bean.SealApply;
import com.yfy.app.seal.bean.SealType;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class SealResetApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SealApply sealApplytop;

    private List<SealType> types;

    public void setTypes(List<SealType> types) {
        this.types = types;
        if (StringJudge.isEmpty(types)){
            return;
        }
        if (types.size()==1){

        }else{

        }
    }

    public void setSealApplytop(SealApply sealApplytop) {
        this.sealApplytop = sealApplytop;
    }

    private List<SealApply> dataList;
    private BaseActivity mContext;

    public void setDataList(List<SealApply> dataList) {
        this.dataList = dataList;
    }

    public List<SealApply> getDataList() {
        return dataList;
    }

    public SealResetApplyAdapter(BaseActivity mContext){
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
        if (viewType == TagFinal.TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seal_apply_top, parent, false);
            return new RecyclerViewHolder(view);
        }
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceHolder(view);
        }
        if (viewType == TagFinal.TYPE_REFRECH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_long_txt_edit, parent, false);
            return new LongTxtEditHolder(view);
        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_txt_edit, parent, false);
            return new EditTxtHolder(view);
        }
        if (viewType == TagFinal.TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_item_show_multi, parent, false);
            return new MultiHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        holder.itemView.setLongClickable(true);
        if (holder instanceof RecyclerViewHolder) {
            RecyclerViewHolder reHolder = (RecyclerViewHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.apply_user.setText(reHolder.bean.getTop_value().get(0).getKey());
            reHolder.apply_time.setText(reHolder.bean.getTop_value().get(1).getKey());
            reHolder.master_user.setText(reHolder.bean.getTop_value().get(2).getKey());
//            reHolder.seal_can.setText("");
            if (StringJudge.isEmpty(reHolder.bean.getTop_value().get(2).getValue())){
                reHolder.master_user.setTextColor(ColorRgbUtil.getGrayText());
            }else{
                reHolder.master_user.setTextColor(ColorRgbUtil.getBaseText());
            }
            reHolder.seal_type.setText(reHolder.bean.getTop_value().get(3).getKey());
            if (StringJudge.isEmpty(reHolder.bean.getTop_value().get(3).getValue())){
                reHolder.seal_type.setTextColor(ColorRgbUtil.getGrayText());
            }else{
                reHolder.seal_type.setTextColor(ColorRgbUtil.getBaseText());
            }
        }
        if (holder instanceof ChoiceHolder) {
            ChoiceHolder choice = (ChoiceHolder) holder;
            choice.initDateDialog();
            choice.index_position=position;
            choice.bean=dataList.get(position);
            choice.apply_name.setText(choice.bean.getDo_seal_time().getName());
            choice.apply_value.setText(choice.bean.getDo_seal_time().getValue());
        }
        if (holder instanceof LongTxtEditHolder) {
            LongTxtEditHolder edit = (LongTxtEditHolder) holder;
            edit.index=position;
            edit.bean=dataList.get(position);
            edit.apply_name.setText(edit.bean.getDo_seal_time().getName());
            edit.apply_edit.setFocusable(false);
            edit.apply_edit.setText(edit.bean.getDo_seal_time().getValue());
        }
        if (holder instanceof EditTxtHolder) {
            EditTxtHolder edittxt = (EditTxtHolder) holder;
            edittxt.index=position;
            edittxt.bean=dataList.get(position);
            edittxt.apply_name.setText(edittxt.bean.getDo_seal_time().getName());
            edittxt.apply_edit.setFocusable(false);
            edittxt.apply_edit.setText(edittxt.bean.getDo_seal_time().getValue());
        }
        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);
            if (StringJudge.isEmpty(multiHolder.bean.getDo_seal_time().getListValue())){
                multiHolder.multi_name.setText(multiHolder.bean.getDo_seal_time().getName()+"(无)");
                multiHolder.multi.setVisibility(View.GONE);
            }else{
                multiHolder.multi_name.setText(multiHolder.bean.getDo_seal_time().getName());
                multiHolder.multi.setVisibility(View.VISIBLE);
                multiHolder.multi.setList(multiHolder.bean.getDo_seal_time().getListValue());
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView apply_user;
        TextView apply_time;
        AppCompatTextView master_user;
        AppCompatTextView seal_type;
        AppCompatTextView seal_can;
        RelativeLayout layout;
        SealApply bean;
        RecyclerViewHolder(View itemView) {
            super(itemView);
            apply_user=  itemView.findViewById(R.id.seal_apply_user_name);
            apply_time=  itemView.findViewById(R.id.seal_apply_apply_time);
            master_user=  itemView.findViewById(R.id.seal_apply_choice_master_user);
            seal_can=  itemView.findViewById(R.id.seal_can);
            seal_type=  itemView.findViewById(R.id.seal_apply_seal_type);
            layout=  itemView.findViewById(R.id.maintain_item_layout);
            master_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    if (sealChoice!=null){
                        sealChoice.choiceUser(bean);
                    }
                }
            });
            seal_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
                    if (sealChoice!=null){
                        sealChoice.choiceSealType();
                    }
                }
            });


        }
    }

    private class ChoiceHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        SealApply bean;
        int index_position;
        ChoiceHolder(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getType()){
                        case "time":
                            mContext.closeKeyWord();
                            date_dialog.showAtBottom();
                            break;

                    }

                }
            });


        }

        private ConfirmDateAndTimeWindow date_dialog;
        private void initDateDialog(){
            date_dialog = new ConfirmDateAndTimeWindow(mContext);
            date_dialog.setOnPopClickListenner(new ConfirmDateAndTimeWindow.OnPopClickListenner() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.set:
                            KeyValue value=bean.getDo_seal_time();
                            value.setKey(date_dialog.getTimeName());
                            value.setValue(date_dialog.getTimeValue());
                            notifyItemChanged(index_position,bean);
                            date_dialog.dismiss();
                            break;
                        case R.id.cancel:
                            date_dialog.dismiss();
                            break;
                    }

                }
            });
        }



    }

    private class LongTxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        SealApply bean;
        int index;

        LongTxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_long_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_long_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    bean.getDo_seal_time().setValue(s.toString().trim());
                }
            });



        }
    }
    private class EditTxtHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        SealApply bean;
        int index;

        EditTxtHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    bean.getDo_seal_time().setValue(s.toString().trim());
                }
            });



        }
    }



    private class MultiHolder extends RecyclerView.ViewHolder {
        TextView multi_name;
        MultiPictureView multi;
        SealApply bean;
        int position_index;

        MultiHolder(View itemView) {
            super(itemView);
            multi_name = itemView.findViewById(R.id.public_show_multi_name);
            multi = itemView.findViewById(R.id.public_show_multi);
            initAbsListView();

        }


        private void initAbsListView() {

            multi.setClickable(false);
            multi.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
                @Override
                public void onDeleted(@NotNull View view, int index) {
                    multi.removeItem(index,true);
                    bean.getDo_seal_time().getListValue().remove(index);
                    notifyItemChanged(position_index, bean);
                }
            });
            multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mContext, SingePicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                    intent.putExtras(b);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        notifyDataSetChanged();
    }





    public interface SealChoice{
        void choiceUser(SealApply bean);
        void choiceSealType();
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }
}
