package com.yfy.app.seal.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.seal.SealApplyActivity;
import com.yfy.app.seal.bean.SealApply;
import com.yfy.app.seal.bean.SealType;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class SealApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    private SealApplyActivity mContext;

    public void setDataList(List<SealApply> dataList) {
        this.dataList = dataList;
    }

    public List<SealApply> getDataList() {
        return dataList;
    }

    public SealApplyAdapter(SealApplyActivity mContext){
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
            return new TxtEditHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_multi_add, parent, false);
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
            choice.initTeaDialog();
            choice.index_position=position;
            choice.bean=dataList.get(position);
            choice.apply_name.setText(choice.bean.getDo_seal_time().getName());
            choice.apply_value.setText(choice.bean.getDo_seal_time().getKey());
            if (StringJudge.isEmpty(choice.bean.getDo_seal_time().getValue())){
                choice.apply_value.setTextColor(ColorRgbUtil.getGrayText());
            }else {
                choice.apply_value.setTextColor(ColorRgbUtil.getBaseText());
            }
        }
        if (holder instanceof LongTxtEditHolder) {
            LongTxtEditHolder edit = (LongTxtEditHolder) holder;
            edit.index=position;
            edit.bean=dataList.get(position);
            edit.apply_name.setText(edit.bean.getDo_seal_time().getName());
            if (StringJudge.isEmpty(edit.bean.getDo_seal_time().getValue())){
                edit.apply_edit.setHint(edit.bean.getDo_seal_time().getKey());
            }else{
                edit.apply_edit.setText(edit.bean.getDo_seal_time().getValue());
            }
        }
        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);
            multiHolder.multi_name.setText(multiHolder.bean.getDo_seal_time().getKey());
            multiHolder.multi.setVisibility(View.VISIBLE);
            multiHolder.multi.setList(multiHolder.bean.getDo_seal_time().getListValue());
        }
        if (holder instanceof TxtEditHolder) {
            TxtEditHolder edittxt = (TxtEditHolder) holder;
            edittxt.index=position;
            edittxt.bean=dataList.get(position);
            edittxt.apply_name.setText(edittxt.bean.getDo_seal_time().getName());
            if (StringJudge.isEmpty(edittxt.bean.getDo_seal_time().getValue())){
                edittxt.apply_edit.setHint(edittxt.bean.getDo_seal_time().getKey());
            }else{
                edittxt.apply_edit.setText(edittxt.bean.getDo_seal_time().getValue());
            }
            List<String> listOne=StringUtils.listToStringSplitCharacters(edittxt.bean.getType(), "_");
            switch (listOne.get(0)){
                case "txt":
                    edittxt.apply_edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case "float":
                    if (listOne.size()!=1){
                        edittxt.apply_edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
                    }else{
                        edittxt.apply_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    break;
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
                        case "seal_type":
                            setSealTypeData();
                            mContext.closeKeyWord();
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

        private CPWListView cpwListView;
        private List<String> dialog_name_list=new ArrayList<>();
        private void initTeaDialog(){
            cpwListView = new CPWListView(mContext);
            cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
                @Override
                public void onClick(int index, String type) {
                    switch (type){
                        case "type":
                            SealType state=types.get(index);
                            bean.setDo_seal_time(new KeyValue(
                                    state.getTypename(),
                                    state.getTypeid(),
                                    StringUtils.getTextJoint("%1$s:",state.getTypename())));
                            notifyItemChanged(index_position,bean);
                            if (dataList.size()>2){
                                dataList.clear();
                                dataList.add(sealApplytop);
                                dataList.add(bean);
                            }
                            initCreateApply(state.getTables(),state.getTypename());
                            cpwListView.dismiss();
                            break;
                    }
                }
            });
        }
        private void setSealTypeData(){
            cpwListView.setType("type");
            if (StringJudge.isEmpty(types)){
//                mContext.toast(R.string.success_not_details);
                return;
            }else{
                dialog_name_list.clear();
                for(SealType s:types){
                    dialog_name_list.add(s.getTypename());
                }
            }
            mContext.closeKeyWord();
            cpwListView.setDatas(dialog_name_list);
            cpwListView.showAtCenter();
        }


    }



    private void initCreateApply(List<SealApply> list,String name){

        SealApply sealApply_time=new SealApply(TagFinal.TYPE_ITEM);
        SealApply start_time=new SealApply(TagFinal.TYPE_ITEM);
        SealApply end_time=new SealApply(TagFinal.TYPE_ITEM);
        sealApply_time.setType("time");
        start_time.setType("time");
        end_time.setType("time");
        sealApply_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","用章时间"),
                "",
                StringUtils.getTextJoint("%1$s:","用章时间"),"1"));
        start_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","借章时间"),
                "",
                StringUtils.getTextJoint("%1$s:","借章时间"),"1"));
        end_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","还章时间"),
                "",
                StringUtils.getTextJoint("%1$s:","还章时间"),"2"));


        if(name.equals("盖章")){
            dataList.add(sealApply_time);
        }else{
            dataList.add(start_time);
            dataList.add(end_time);
        }
        for (SealApply apply:list){
            apply.setType(apply.getValuetype());
            apply.setList(true);
            List<String> listOne=StringUtils.listToStringSplitCharacters(apply.getType(), "_");
            KeyValue keyValue=new KeyValue();
            keyValue.setType(apply.getValuetype());
            switch (listOne.get(0)){
                case "time":
                    apply.setView_type(TagFinal.TYPE_ITEM);
                    keyValue.setKey(StringUtils.getTextJoint("请选择%1$s",apply.getTablename()));
                    keyValue.setValue(apply.getDefaultvalue());
                    keyValue.setName(StringUtils.getTextJoint("%1$s:",apply.getTablename()));
                    apply.setDo_seal_time(keyValue);
                    break;
                case "txt":
                    apply.setView_type(TagFinal.TYPE_PARENT);

                    keyValue.setKey(StringUtils.getTextJoint("请填写%1$s",apply.getTablename()));
                    keyValue.setValue(apply.getDefaultvalue());
                    keyValue.setName(StringUtils.getTextJoint("%1$s:",apply.getTablename()));
                    apply.setDo_seal_time(keyValue);
                    break;
                case "float":
                    apply.setView_type(TagFinal.TYPE_PARENT);
                    keyValue.setKey(StringUtils.getTextJoint("请填写%1$s",apply.getTablename()));
                    keyValue.setValue(apply.getDefaultvalue());
                    keyValue.setName(StringUtils.getTextJoint("%1$s:",apply.getTablename()));
                    apply.setDo_seal_time(keyValue);
                    break;
                case "longtxt":
                    apply.setView_type(TagFinal.TYPE_REFRECH);
                    keyValue.setKey(StringUtils.getTextJoint("请填写%1$s",apply.getTablename()));
                    keyValue.setValue(apply.getDefaultvalue());
                    keyValue.setName(StringUtils.getTextJoint("%1$s:",apply.getTablename()));
                    apply.setDo_seal_time(keyValue);
                    break;
                case Base.DATA_TYPE_IMG:
                    apply.setView_type(TagFinal.TYPE_CHILD);
                    keyValue.setKey(StringUtils.getTextJoint(" %1$s",apply.getTablename()));
                    keyValue.setValue(apply.getTablename());

                    apply.setDo_seal_time(keyValue);
                    break;
                default:
                    apply.setView_type(TagFinal.TYPE_PARENT);


                    keyValue.setKey(StringUtils.getTextJoint("请填写%1$s",apply.getTablename()));
                    keyValue.setValue(apply.getDefaultvalue());
                    keyValue.setName(StringUtils.getTextJoint("%1$s:",apply.getTablename()));
                    apply.setDo_seal_time(keyValue);
                    break;
            }



            dataList.add(apply);

        }

        //-------------------------

//        SealApply short_obj=new SealApply(TagFinal.TYPE_CHILD);
//        KeyValue zip=new KeyValue("图片");
//        zip.setType("zip");
//        zip.setListValue(new ArrayList<String>());
//        short_obj.setDo_seal_time(zip);
//        dataList.add(short_obj);

        notifyDataSetChanged();
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
    private class TxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        SealApply bean;
        int index;

        TxtEditHolder(View itemView) {
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
            multi_name = itemView.findViewById(R.id.public_add_multi_name);
            multi = itemView.findViewById(R.id.public_add_multi);
            initAbsListView();
            initDialog();

        }

        ConfirmAlbumWindow album_select;
        private void initDialog() {
            album_select = new ConfirmAlbumWindow(mContext);
            album_select.setTwo_select(mContext.getResources().getString(R.string.album));
            album_select.setOne_select(mContext.getResources().getString(R.string.take_photo));
            album_select.setName(mContext.getResources().getString(R.string.upload_type));
            album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
                @Override
                public void onClick(View view) {
                    if (sealChoice!=null){
                        sealChoice.refresh(bean, position_index);
                    }

                    switch (view.getId()) {
                        case R.id.popu_select_one:
                            PermissionTools.tryCameraPerm(mContext);
                            break;
                        case R.id.popu_select_two:
                            PermissionTools.tryWRPerm(mContext);
                            break;
                    }
                }
            });
        }
        private void initAbsListView() {
            multi.setAddClickCallback(new MultiPictureView.AddClickCallback() {
                @Override
                public void onAddClick(View view) {
                    mContext.closeKeyWord();
                    album_select.showAtBottom();
                }
            });
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
//				Logger.e(TAG, "onItemClicked: "+index );
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
        void refresh(SealApply bean,int position_index);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }
}
