package com.yfy.app.process.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.process.ProcessAddActivity;
import com.yfy.app.process.ProcessRoomGroupActivity;
import com.yfy.app.process.ProcessSelectUserActivity;
import com.yfy.app.process.ProcessSelectUserAllActivity;
import com.yfy.app.process.bean.ProLayerTwoData;
import com.yfy.app.process.bean.ProLayerOneData;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class ProcessAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProLayerOneData> dataList;
    private ProcessAddActivity mContext;

    public void setDataList(List<ProLayerOneData> dataList) {
        this.dataList = dataList;
    }

    public List<ProLayerOneData> getDataList() {
        return dataList;
    }

    public ProcessAddAdapter(ProcessAddActivity mContext){
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

        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceHolder(view);
        }
        if (viewType == TagFinal.TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceDateHolder(view);
        }
        if (viewType == TagFinal.TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new SelectUserHolder(view);
        }
        if (viewType == TagFinal.TYPE_REFRECH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_long_txt_edit, parent, false);
            return new LongTxtEditHolder(view);
        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_txt_edit, parent, false);
            return new TxtEditHolder(view);
        }
        if (viewType == TagFinal.TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_multi_add, parent, false);
            return new MultiHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHECK) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_check_two, parent, false);
            return new CheckTwoHolder(view);
        }
        if (viewType == TagFinal.TYPE_GROUP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_group, parent, false);
            return new GroupHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CheckTwoHolder) {
            CheckTwoHolder checkTwoH = (CheckTwoHolder) holder;
            checkTwoH.index_position=position;
            checkTwoH.bean=dataList.get(position);
            checkTwoH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",checkTwoH.bean.getTitle()));
            switch (checkTwoH.bean.getKeyValue().getValue()){
                case "off":
                    checkTwoH.check_flow.setVisibility(View.GONE);
                    checkTwoH.radioTwo.setTextColor(ColorRgbUtil.getBaseColor());
                    checkTwoH.radioOne.setTextColor(ColorRgbUtil.getWhite());
                    checkTwoH.radioOne.setBackgroundResource(R.drawable.radius_rigth_bottom4_top4);
                    checkTwoH.radioTwo.setBackgroundColor(Color.TRANSPARENT);
                    break;
                case "no":
                    checkTwoH.check_flow.setVisibility(View.VISIBLE);
                    checkTwoH.setcheckData(checkTwoH.bean);
                    checkTwoH.radioTwo.setTextColor(ColorRgbUtil.getWhite());
                    checkTwoH.radioOne.setTextColor(ColorRgbUtil.getBaseColor());
                    checkTwoH.radioTwo.setBackgroundResource(R.drawable.radius_left_bottom4_top4);
                    checkTwoH.radioOne.setBackgroundColor(Color.TRANSPARENT);
                    break;
                    default:
                        checkTwoH.radioOne.setBackgroundColor(Color.TRANSPARENT);
                        checkTwoH.radioTwo.setBackgroundColor(Color.TRANSPARENT);
                        checkTwoH.radioTwo.setTextColor(ColorRgbUtil.getBaseColor());
                        checkTwoH.radioOne.setTextColor(ColorRgbUtil.getBaseColor());
                        checkTwoH.check_flow.setVisibility(View.GONE);
                        break;
            }
        }
        if (holder instanceof SelectUserHolder) {
            SelectUserHolder selectUserH = (SelectUserHolder) holder;
            selectUserH.index_position=position;
            selectUserH.bean=dataList.get(position);
            selectUserH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",selectUserH.bean.getTitle()));
            if (selectUserH.bean.isIs_select()){
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectUserH.apply_value.setText(selectUserH.bean.getKeyValue().getName());
            }else {
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectUserH.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ChoiceHolder) {
            ChoiceHolder choice = (ChoiceHolder) holder;
            choice.initTeaDialog();
            choice.index_position=position;
            choice.bean=dataList.get(position);
            choice.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",choice.bean.getTitle()));
            if (choice.bean.isIs_select()){
                choice.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                choice.apply_value.setText(choice.bean.getKeyValue().getName());
            }else {
                choice.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                choice.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ChoiceDateHolder) {
            ChoiceDateHolder choice = (ChoiceDateHolder) holder;
            choice.index_position=position;
            choice.initDateDialog();
            choice.bean=dataList.get(position);
            choice.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",choice.bean.getTitle()));
            if (choice.bean.isIs_select()){
                choice.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                choice.apply_value.setText(choice.bean.getKeyValue().getName());
            }else {
                choice.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                choice.apply_value.setText("请选择");
            }
        }

        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);
            multiHolder.multi.setVisibility(View.VISIBLE);
            if (!StringJudge.isEmpty(multiHolder.bean.getMax())){
                multiHolder.multi.setMax(ConvertObjtect.getInstance().getInt(multiHolder.bean.getMax()));
            }
            multiHolder.multi.setList(multiHolder.bean.getKeyValue().getListValue());
            multiHolder.multi_name.setText(StringUtils.getTextJoint("%1$s\t",multiHolder.bean.getTitle()));
        }
        if (holder instanceof LongTxtEditHolder) {
            LongTxtEditHolder edit = (LongTxtEditHolder) holder;
            edit.index=position;
            edit.bean=dataList.get(position);

            edit.apply_name.setText(StringUtils.getTextJoint("%1$s\t",edit.bean.getTitle()));
            if (StringJudge.isEmpty(edit.bean.getKeyValue().getValue())){
                edit.apply_edit.setHint(edit.bean.getKeyValue().getKey());
            }else{
                edit.apply_edit.setText(edit.bean.getKeyValue().getValue());
            }
        }
        if (holder instanceof TxtEditHolder) {
            TxtEditHolder edittxt = (TxtEditHolder) holder;
            edittxt.index = position;
            edittxt.bean = dataList.get(position);
            edittxt.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",edittxt.bean.getTitle()));
            if (StringJudge.isEmpty(edittxt.bean.getKeyValue().getValue())){
                edittxt.apply_edit.setHint(edittxt.bean.getKeyValue().getKey());
            }else{
                edittxt.apply_edit.setText(edittxt.bean.getKeyValue().getValue());
            }
            List<String> listOne=StringUtils.listToStringSplitCharacters(edittxt.bean.getValuetype(), "_");
            switch (listOne.get(0)){
                case "txt":
                    edittxt.apply_edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case "int":
                    edittxt.apply_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;
                case "decimal":
                    if (listOne.size()!=1){
                        edittxt.apply_edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
                    }else{
                        edittxt.apply_edit.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                    break;
            }
        }
        if (holder instanceof GroupHolder) {
            GroupHolder groupH = (GroupHolder) holder;
            groupH.index_position=position;
            groupH.bean=dataList.get(position);
            groupH.apply_name.setText(StringUtils.getTextJoint("%1$s\t",groupH.bean.getTitle()));

            groupH.setdata(groupH.bean);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class ChoiceHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerOneData bean;
        int index_position;
        ChoiceHolder(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getValuetype()){
                        case "select":
                            mContext.closeKeyWord();
                            if (bean.getMax().equalsIgnoreCase("1")){
                                setProcessSelect(StringUtils.listToStringSplitCharacters(bean.getSelects(),","));
                            }else{

                            }
                            break;
                        case "list":
                            mContext.closeKeyWord();
                            break;
                    }

                }
            });
        }

        private void setProcessList(List<String> types){
            cpwListView.setType("type");
            if (StringJudge.isEmpty(types)){
                return;
            }else{
                dialog_name_list.clear();
                for (String s:types){
                    CPWBean cpwBean=new CPWBean();
                    cpwBean.setValue(s);
                    cpwBean.setName(s);
                    dialog_name_list.add(cpwBean);
                }
                cpwListView.setDatas(dialog_name_list);
                cpwListView.showAtCenter();
            }
        }
        private void setProcessSelect(List<String> types){
            cpwListView.setType("type");
            if (StringJudge.isEmpty(types)){
                return;
            }else{
                dialog_name_list.clear();
                for (String s:types){
                    CPWBean cpwBean=new CPWBean();
                    cpwBean.setValue(s);
                    cpwBean.setName(s);
                    dialog_name_list.add(cpwBean);
                }
                cpwListView.setDatas(dialog_name_list);
                cpwListView.showAtCenter();
            }
        }
        private CPWListBeanView cpwListView;
        private List<CPWBean> dialog_name_list=new ArrayList<>();
        private void initTeaDialog(){
            cpwListView = new CPWListBeanView(mContext);
            cpwListView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
                @Override
                public void onClick(CPWBean cpwBean, String type) {
                    switch (type){
                        case "type":
                            bean.setIs_select(true);
                            KeyValue keyValue = bean.getKeyValue();
                            keyValue.setName(cpwBean.getName());
                            keyValue.setValue(cpwBean.getName());
                            notifyItemChanged(index_position,bean);
                            cpwListView.dismiss();
                            break;
                    }
                }
            });
        }


    }
    private class ChoiceDateHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerOneData bean;
        int index_position;

        ChoiceDateHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_choice_key);
            apply_value = itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getValuetype()){
                        case "datetime":
                            mContext.closeKeyWord();
                            dateAndTimeDialog.showAtBottom();
                            break;
                    }

                }
            });
        }

        private ConfirmDateAndTimeWindow dateAndTimeDialog;
        private void initDateDialog() {
            dateAndTimeDialog = new ConfirmDateAndTimeWindow(mContext);
            dateAndTimeDialog.setOnPopClickListenner(new ConfirmDateAndTimeWindow.OnPopClickListenner() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.set:
                            bean.setIs_select(true);
                            KeyValue value=bean.getKeyValue();
                            value.setName(dateAndTimeDialog.getTimeName());
                            value.setValue(dateAndTimeDialog.getTimeValue());
                            notifyItemChanged(index_position,bean);
                            dateAndTimeDialog.dismiss();
                            break;
                        case R.id.cancel:
                            dateAndTimeDialog.dismiss();
                            break;
                    }
                }
            });
        }
    }
    private class SelectUserHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerOneData bean;
        int index_position;

        SelectUserHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_choice_key);
            apply_value = itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getMax().equalsIgnoreCase("1")){
                        Intent intent=new Intent(mContext,ProcessSelectUserActivity.class);
                        intent.putExtra(Base.type,bean.getSelects());
                        intent.putExtra(Base.title,bean.getTitle());
                        intent.putExtra(Base.num,bean.getMax());
                        intent.putExtra(Base.id,"0");
                        intent.putExtra(Base.index,index_position);
                        mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_SINGLE);
                    }else{
                        Intent intent=new Intent(mContext,ProcessSelectUserAllActivity.class);
                        intent.putExtra(Base.type,bean.getSelects());
                        intent.putExtra(Base.title,bean.getTitle());
                        intent.putExtra(Base.index,index_position);
                        intent.putExtra(Base.id,"0");
                        intent.putExtra(Base.num,bean.getMax());
                        mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_MORE);
                    }
                }
            });
        }



    }
    private class CheckTwoHolder extends RecyclerView.ViewHolder
    {
        AppCompatTextView apply_name;
        RadioGroup radioGroup;
        RadioButton radioOne;
        RadioButton radioTwo;
        FlowLayout check_flow;
        ProLayerOneData bean;
        int index_position;

        CheckTwoHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_check_title);
            radioGroup = itemView.findViewById(R.id.public_type_check_radio_group);
            radioTwo = itemView.findViewById(R.id.public_type_check_two);
            radioOne = itemView.findViewById(R.id.public_type_check_one);
            check_flow = itemView.findViewById(R.id.public_type_check_flow_layout);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.public_type_check_one:
                            bean.getKeyValue().setValue("off");
                            break;
                        case R.id.public_type_check_two:
                            bean.getKeyValue().setValue("no");
                            break;
                    }
                    notifyItemChanged(index_position,bean);
                }
            });
        }
        private void setcheckData(ProLayerOneData res){
            List<KeyValue> top_jz=new ArrayList<>();
            for (ProLayerTwoData linkBean:res.getRecordlinkvalue()){
                top_jz.add(linkBean.getKeyValue());
            }
            setCheckFlowLayoutTop(top_jz,check_flow);
        }


    }
    private class LongTxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        ProLayerOneData bean;
        int index;

        LongTxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_long_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_long_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    bean.getKeyValue().setValue(s.toString().trim());
                }
            });
        }
    }
    private class TxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        ProLayerOneData bean;
        int index;
        TxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    List<String> listOne=StringUtils.listToStringSplitCharacters(bean.getValuetype(), "_");
                    switch (listOne.get(0)){
                        case "decimal":
                            if (listOne.size()>1){
                                iedit(s,ConvertObjtect.getInstance().getInt(listOne.get(1)));
                            }else{
                                bean.getKeyValue().setValue(s.toString().trim());
                            }
                            break;
                            default:
                                bean.getKeyValue().setValue(s.toString().trim());
                                break;
                    }
                }
            });
        }

        //限制num位小数.
        public void iedit(Editable s,int num){
            String edit_String=s.toString();
            int posDot=edit_String.indexOf(".");
            if (posDot<0){
                return;
            }
            if (posDot==0){
                s.delete(0,edit_String.length());
                return;
            }
            if (edit_String.length()-posDot-1>num)
                s.delete(posDot+num+1,edit_String.length());
        }
    }
    private class MultiHolder extends RecyclerView.ViewHolder {
        TextView multi_name;
        MultiPictureView multi;
        ProLayerOneData bean;
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


    private class GroupHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        FlowLayout flowLayout;
        RelativeLayout layout;
        ProLayerOneData bean;
        int index_position;

        GroupHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_group_title);
            flowLayout = itemView.findViewById(R.id.public_type_group_flow_layout);
            layout = itemView.findViewById(R.id.public_type_group_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(mContext,ProcessRoomGroupActivity.class);
                    intent.putExtra(Base.data, bean);
                    mContext.startActivityForResult(intent,TagFinal.UI_ADD);
                }

            });
        }

        private void setdata(ProLayerOneData res){
            List<KeyValue> top_jz=new ArrayList<>();
            for (ProLayerTwoData linkBean:res.getRecordlinkvalue()){
                top_jz.add(linkBean.getKeyValue());
            }
            setFlowLayoutTop(top_jz);
        }
        private void setFlowLayoutTop(List<KeyValue> top_jz){

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            if (flowLayout.getChildCount()!=0){
                flowLayout.removeAllViews();
            }
            for (KeyValue bean:top_jz){
                RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.public_detail_top_item,flowLayout, false);
                TextView key=layout.findViewById(R.id.seal_detail_key);
                TextView value=layout.findViewById(R.id.seal_detail_value);
                RatingBar ratingBar=layout.findViewById(R.id.seal_detail_value_star);
                LinearLayout linearLayout=layout.findViewById(R.id.public_detail_txt_layout);
                MultiPictureView multi=layout.findViewById(R.id.public_detail_layout_multi);

                key.setTextColor(ColorRgbUtil.getGrayText());
                value.setTextColor(ColorRgbUtil.getBaseText());

                key.setText(bean.getKey());
                linearLayout.setVisibility(View.VISIBLE);
                multi.setVisibility(View.GONE);
                value.setText(bean.getValue());
                ratingBar.setVisibility(View.GONE);
                value.setVisibility(View.VISIBLE);

                flowLayout.addView(layout);
            }
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





    private void setCheckFlowLayoutTop(List<KeyValue> top_jz,FlowLayout check_flow){

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        if (check_flow.getChildCount()!=0){
            check_flow.removeAllViews();
        }
        for (final KeyValue bean:top_jz){
            Logger.e(bean.getType());
            switch (bean.getType()){
                case "text":
                case "longtext":
                    RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.public_type_long_txt_edit,check_flow, false);
                    TextView title=layout.findViewById(R.id.public_type_long_txt_edit_key);
                    EditText longEdit=layout.findViewById(R.id.public_type_long_txt_edit_value);
                    title.setText(bean.getName());
                    if (StringJudge.isEmpty(bean.getValue())){
                        longEdit.setHint(bean.getKey());
                    }else{
                        longEdit.setText(bean.getValue());
                    }
                    longEdit.addTextChangedListener(new MyWatcher() {
                        @Override
                        public void afterTextChanged(Editable editable) {
                            bean.setValue(editable.toString().trim());
                        }
                    });
                    check_flow.addView(layout);
                    break;
                default:
                    break;
            }

        }
    }

    public interface SealChoice{


        void refresh(ProLayerOneData bean, int position_index);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }
}
