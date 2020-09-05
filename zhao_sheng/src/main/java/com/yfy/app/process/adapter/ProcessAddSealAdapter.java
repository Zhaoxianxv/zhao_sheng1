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
import com.yfy.app.notice.bean.OnScrollToListener;
import com.yfy.app.process.ProcessAddSealActivity;
import com.yfy.app.process.ProcessSelectUserActivity;
import com.yfy.app.process.ProcessSelectUserAllActivity;
import com.yfy.app.process.bean.ProLinkListBean;
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
public class ProcessAddSealAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<ProLayerTwoData> showDataList;
    public List<ProLayerOneData> allDataList;
    public ProcessAddSealActivity mContext;
    private OnScrollToListener onScrollToListener;

    public void setOnScrollToListener(OnScrollToListener onScrollToListener) {
        this.onScrollToListener = onScrollToListener;
    }

    public List<ProLayerTwoData> getShowDataList() {
        return showDataList;
    }

    public List<ProLayerOneData> getAllDataList() {
        return allDataList;
    }

    public void setAllDataList(List<ProLayerOneData> allDataList) {
        this.allDataList = allDataList;
        initViews();

    }


    private void  initViews(){
        showDataList.clear();
        for (ProLayerOneData oneData:allDataList){
            ProLayerTwoData twoData=new ProLayerTwoData();
            twoData.setId(oneData.getId());
            twoData.setStep(oneData.getStep());
            twoData.setHeadPic(oneData.getHeadPic());
            twoData.setContent(oneData.getContent());
            twoData.setDisplaycontent(oneData.getDisplaycontent());
            twoData.setValuetype(oneData.getValuetype());
            twoData.setAdddate(oneData.getAdddate());
            twoData.setTitle(oneData.getTitle());
            twoData.setCannull(oneData.getCannull());
            twoData.setUnit(oneData.getUnit());
            twoData.setMin(oneData.getMin());
            twoData.setMax(oneData.getMax());
            twoData.setSelects(oneData.getSelects());
            twoData.setIsshow(oneData.getIsshow());
            twoData.setRecordvaluearray(oneData.getRecordvaluearray());
            twoData.setRecordlinkvalues(oneData.getRecordlinkvalue());


            KeyValue keyNew=new KeyValue(String.valueOf(oneData.getId()));
            keyNew.setType(twoData.getValuetype());
            if(StringJudge.isEmpty(oneData.getContent())){
                keyNew.setKeyValueLeftRight("","","","");
            }else{
                twoData.setIs_select(true);
                keyNew.setKeyValueLeftRight(oneData.getTitle(),"",oneData.getDisplaycontent(),oneData.getContent());
            }



            List<String> names=StringUtils.listToStringSplitCharacters(oneData.getValuetype(), "_");
            switch (names.get(0)){
                case "datetime":
                    twoData.setView_type(TagFinal.TYPE_DATE);
                    break;
                case "select":
                    if (StringJudge.isEmpty(twoData.getRecordlinkvalues())){
                        twoData.setView_type(TagFinal.TYPE_SELECT_SINGLE);
                    }else{
                        twoData.setView_type(TagFinal.TYPE_SELECT_GROUP);
                        if (!StringJudge.isEmpty(oneData.getContent())){

                        }
                    }
                    break;
                case "list":
                    if (StringJudge.isEmpty(twoData.getRecordlinkvalues())){
                        twoData.setView_type(TagFinal.TYPE_LIST_SINGLE);
                    }else{
                        twoData.setView_type(TagFinal.TYPE_LIST_GROUP);
                        if (!StringJudge.isEmpty(oneData.getContent())){

                        }
                    }
                    break;
                case "selectuser":
                    twoData.setView_type(TagFinal.TYPE_SELECT_USER);
                    break;
                case "longtext":
                    keyNew.setRight_key(StringUtils.getTextJoint("请输入%1$s",twoData.getTitle()));
                    twoData.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);

                    break;
                case "image":
                    if(!StringJudge.isEmpty(twoData.getContent())){
                        keyNew.setListValue(StringUtils.listToStringSplitCharacters(twoData.getContent(),","));
                    }
                    twoData.setView_type(TagFinal.TYPE_IMAGE);
                    break;
                case "check":
                    twoData.setView_type(TagFinal.TYPE_CHECK);
                    break;
                case "group":
                    twoData.setView_type(TagFinal.TYPE_GROUP);
                    break;
                case "decimal":
                case "text":
                case "int":
                    twoData.setView_type(TagFinal.TYPE_TXT_EDIT);
                    keyNew.setRight_key(StringUtils.getTextJoint("请输入%1$s",twoData.getTitle()));

                    break;
                default:
                    twoData.setView_type(TagFinal.TYPE_DATE);
                    break;
            }
            twoData.setKeyValue(keyNew);
            showDataList.add(twoData);
        }
        notifyDataSetChanged();

        initOneData();
//        initOneData();
    }


    private void initOneData(){
        for (int i=0;i<showDataList.size();i++){
            ProLayerTwoData twoData=showDataList.get(i);
            List<String> names=StringUtils.listToStringSplitCharacters(twoData.getValuetype(), "_");
            switch (names.get(0)){
                case "datetime":
                    break;
                case "select":
                    if (!StringJudge.isEmpty(twoData.getRecordlinkvalues())){
                        if (!StringJudge.isEmpty(twoData.getContent())){
                            linkSelectTwoBean(twoData,twoData.getContent(),i);
                        }
                    }
                    break;
                case "list":
                    if (!StringJudge.isEmpty(twoData.getRecordlinkvalues())){
                        if (!StringJudge.isEmpty(twoData.getContent())){
                            linkListOneBean(twoData,twoData.getContent(),i);
                        }
                    }
                    break;
                case "selectuser":
                    break;
                case "longtext":
                    break;
                case "image":
                    break;
                case "check":
                    break;
                case "group":
                    break;
                case "decimal":
                case "text":
                case "int":
                    break;
                default:
                    break;
            }
        }
    }


    public ProcessAddSealAdapter(ProcessAddSealActivity mContext){
        this.mContext=mContext;
        this.showDataList = new ArrayList<>();
        this.allDataList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return showDataList.get(position).getView_type();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TagFinal.TYPE_SELECT_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProSelectSingleH(view);
        }
        if (viewType == TagFinal.TYPE_SELECT_GROUP||viewType == TagFinal.TYPE_SELECT_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProSelectGroupH(view);
        }
        if (viewType == TagFinal.TYPE_LIST_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProListSingleH(view);
        }
        if (viewType == TagFinal.TYPE_LIST_GROUP||viewType == TagFinal.TYPE_LIST_ONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProListGroupH(view);
        }
        if (viewType == TagFinal.TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceDateHolder(view);
        }
        if (viewType == TagFinal.TYPE_SELECT_USER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new SelectUserHolder(view);
        }
        if (viewType == TagFinal.TYPE_LONG_TXT_EDIT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_long_txt_edit, parent, false);
            return new LongTxtEditHolder(view);
        }
        if (viewType == TagFinal.TYPE_TXT_EDIT) {
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
            checkTwoH.bean= showDataList.get(position);
            checkTwoH.check_flow.setVisibility(View.GONE);
            checkTwoH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",checkTwoH.bean.getTitle()));
            switch (checkTwoH.bean.getKeyValue().getRight_value()){
                case TagFinal.FALSE:
                    checkTwoH.radioTwo.setTextColor(ColorRgbUtil.getBaseColor());
                    checkTwoH.radioOne.setTextColor(ColorRgbUtil.getWhite());
                    checkTwoH.radioOne.setBackgroundResource(R.drawable.radius_rigth_bottom4_top4);
                    checkTwoH.radioTwo.setBackgroundColor(Color.TRANSPARENT);
                    break;
                case TagFinal.TRUE:
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
                        break;
            }
        }
        if (holder instanceof SelectUserHolder) {
            SelectUserHolder selectUserH = (SelectUserHolder) holder;
            selectUserH.index_position=position;
            selectUserH.bean= showDataList.get(position);
            selectUserH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",selectUserH.bean.getTitle()));
            if (selectUserH.bean.isIs_select()){
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectUserH.apply_value.setText(selectUserH.bean.getKeyValue().getRight_name());
            }else {
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectUserH.apply_value.setText("请选择");
            }
        }

        if (holder instanceof ChoiceDateHolder) {
            ChoiceDateHolder choice = (ChoiceDateHolder) holder;
            choice.index_position=position;
            choice.initDateDialog();
            choice.bean= showDataList.get(position);
            choice.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",choice.bean.getTitle()));
            if (choice.bean.isIs_select()){
                choice.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                choice.apply_value.setText(choice.bean.getKeyValue().getRight_name());
            }else {
                choice.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                choice.apply_value.setText("请选择");
            }
        }

        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean= showDataList.get(position);
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
            edit.bean= showDataList.get(position);

            edit.apply_name.setText(StringUtils.getTextJoint("%1$s\t",edit.bean.getTitle()));
            if (StringJudge.isEmpty(edit.bean.getKeyValue().getRight_value())){
                edit.apply_edit.setHint(edit.bean.getKeyValue().getRight_key());
            }else{
                edit.apply_edit.setText(edit.bean.getKeyValue().getRight_value());
            }
        }
        if (holder instanceof TxtEditHolder) {
            TxtEditHolder edittxt = (TxtEditHolder) holder;
            edittxt.index = position;
            edittxt.bean = showDataList.get(position);
            edittxt.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",edittxt.bean.getTitle()));
            if (StringJudge.isEmpty(edittxt.bean.getKeyValue().getRight_value())){
                edittxt.apply_edit.setHint(edittxt.bean.getKeyValue().getRight_key());
            }else{
                edittxt.apply_edit.setText(edittxt.bean.getKeyValue().getRight_value());
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
            groupH.bean= showDataList.get(position);
            groupH.apply_name.setText(StringUtils.getTextJoint("%1$s\t",groupH.bean.getTitle()));

            groupH.setdata(groupH.bean);
        }
        if (holder instanceof ProListGroupH) {
            ProListGroupH listGroupH = (ProListGroupH) holder;
            listGroupH.initTeaDialog();
            listGroupH.index_position=position;
            listGroupH.bean= showDataList.get(position);
            listGroupH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",listGroupH.bean.getTitle()));
            if (listGroupH.bean.isIs_select()){
                listGroupH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                listGroupH.apply_value.setText(listGroupH.bean.getKeyValue().getRight_name());
            }else {
                listGroupH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                listGroupH.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ProListSingleH) {
            ProListSingleH listSingleH = (ProListSingleH) holder;
            listSingleH.initTeaDialog();
            listSingleH.index_position=position;
            listSingleH.bean= showDataList.get(position);
            listSingleH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",listSingleH.bean.getTitle()));
            if (listSingleH.bean.isIs_select()){
                listSingleH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                listSingleH.apply_value.setText(listSingleH.bean.getKeyValue().getRight_name());
            }else {
                listSingleH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                listSingleH.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ProSelectSingleH) {
            ProSelectSingleH selectSingleH = (ProSelectSingleH) holder;
            selectSingleH.initTeaDialog();
            selectSingleH.index_position=position;
            selectSingleH.bean= showDataList.get(position);
            selectSingleH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",selectSingleH.bean.getTitle()));
            if (selectSingleH.bean.isIs_select()){
                selectSingleH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectSingleH.apply_value.setText(selectSingleH.bean.getKeyValue().getRight_name());
            }else {
                selectSingleH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectSingleH.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ProSelectGroupH) {
            ProSelectGroupH selectGroupH = (ProSelectGroupH) holder;
            selectGroupH.initTeaDialog();
            selectGroupH.index_position=position;
            selectGroupH.bean= showDataList.get(position);
            selectGroupH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",selectGroupH.bean.getTitle()));
            if (selectGroupH.bean.isIs_select()){
                selectGroupH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectGroupH.apply_value.setText(selectGroupH.bean.getKeyValue().getRight_name());
            }else {
                selectGroupH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectGroupH.apply_value.setText("请选择");
            }
        }
    }

    @Override
    public int getItemCount() {
        return showDataList.size();
    }


    private class ProSelectSingleH extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerTwoData bean;
        int index_position;
        ProSelectSingleH(View itemView) {
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
                                setProcessTypeData(StringUtils.listToStringSplitCharacters(bean.getSelects(),","));
                            }
                            break;
                    }

                }
            });
        }
        private void setProcessTypeData(List<String> types){
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
                            keyValue.setRight_name(cpwBean.getName());
                            keyValue.setRight_value(cpwBean.getValue());
                            notifyItemChanged(index_position,bean);
                            cpwListView.dismiss();
                            break;
                    }
                }
            });
        }
    }
    private class ProSelectGroupH extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerTwoData bean;
        int index_position;
        ProSelectGroupH(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getValuetype()){
                        case "select":
                            mContext.closeKeyWord();
                            setProcessTypeData(StringUtils.listToStringSplitCharacters(bean.getSelects(),","));
                            break;
                    }

                }
            });
        }
        private void setProcessTypeData(List<String> types){
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
                            if (StringJudge.isEmpty(bean.getKeyValue().getRight_value())){

                            }else{

                                if (bean.getKeyValue().getRight_value().equalsIgnoreCase(cpwBean.getValue())){
                                    cpwListView.dismiss();
                                    return;
                                }
                            }
                            KeyValue keyValue = bean.getKeyValue();
                            keyValue.setRight_name(cpwBean.getName());
                            keyValue.setRight_value(cpwBean.getValue());
                            notifyItemChanged(index_position,bean);
                            linkSelectTwoBean(bean,cpwBean.getValue(),index_position);
                            cpwListView.dismiss();
                            break;
                    }
                }
            });
        }
    }
    private class ProListSingleH extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerTwoData bean;
        int index_position;
        ProListSingleH(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.closeKeyWord();
                    setProcessSelect(bean.getRecordvaluearray());
                }
            });
        }
        private void setProcessSelect(List<ProLinkListBean> types){
            cpwListView.setType("type");
            if (StringJudge.isEmpty(types)){
                return;
            }else{
                dialog_name_list.clear();
                for (ProLinkListBean s:types){
                    CPWBean cpwBean=new CPWBean();

                    cpwBean.setValue(String.valueOf(s.getId()));
                    cpwBean.setName(s.getName());
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
                            keyValue.setRight_name(cpwBean.getName());
                            keyValue.setRight_value(cpwBean.getValue());
                            notifyItemChanged(index_position,bean);
                            cpwListView.dismiss();

                            break;
                    }
                }
            });
        }
    }
    private class ProListGroupH extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerTwoData bean;
        int index_position;
        ProListGroupH(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.closeKeyWord();
                    setProcessSelect(bean.getRecordvaluearray());

                }
            });
        }
        private void setProcessSelect(List<ProLinkListBean> types){
            cpwListView.setType("type");
            if (StringJudge.isEmpty(types)){
                return;
            }else{
                dialog_name_list.clear();
                for (ProLinkListBean s:types){
                    CPWBean cpwBean=new CPWBean();
                    cpwBean.setValue(String.valueOf((long) s.getId()));
                    cpwBean.setName(s.getName());
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

                            if (StringJudge.isEmpty(bean.getKeyValue().getRight_value())){

                            }else{

                                if (bean.getKeyValue().getRight_value().equalsIgnoreCase(cpwBean.getValue())){
                                    cpwListView.dismiss();
                                    return;
                                }
                            }
                            bean.setIs_select(true);
                            KeyValue keyValue = bean.getKeyValue();
                            keyValue.setRight_name(cpwBean.getName());
                            keyValue.setRight_value(cpwBean.getValue());
                            notifyItemChanged(index_position,bean);
                            cpwListView.dismiss();

                            linkListOneBean(bean,cpwBean.getValue(),index_position);

                            break;
                    }
                }
            });
        }
    }
    private class ChoiceDateHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        ProLayerTwoData bean;
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
                            value.setRight_name(dateAndTimeDialog.getTimeName());
                            value.setRight_value(dateAndTimeDialog.getTimeValue());
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
        ProLayerTwoData bean;
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
                        intent.putExtra(Base.id,"0");
                        intent.putExtra(Base.num,bean.getMax());
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
    private class CheckTwoHolder extends RecyclerView.ViewHolder {
        AppCompatTextView apply_name;
        RadioGroup radioGroup;
        RadioButton radioOne;
        RadioButton radioTwo;
        FlowLayout check_flow;
        ProLayerTwoData bean;
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
                            bean.getKeyValue().setRight_value(TagFinal.FALSE);

                            break;
                        case R.id.public_type_check_two:
                            bean.getKeyValue().setRight_value(TagFinal.TRUE);
                            break;
                    }
                    notifyItemChanged(index_position,bean);
                    linkListOneBean(bean,bean.getKeyValue().getRight_value(),index_position);
                }
            });
        }



    }
    private class LongTxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        ProLayerTwoData bean;
        int index;

        LongTxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_long_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_long_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    bean.getKeyValue().setRight_value(s.toString().trim());
                }
            });
        }
    }
    private class TxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        ProLayerTwoData bean;
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
                                bean.getKeyValue().setRight_value(s.toString().trim());
                            }else{
                                bean.getKeyValue().setRight_value(s.toString().trim());
                            }
                            break;
                            default:
                                bean.getKeyValue().setRight_value(s.toString().trim());
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
        ProLayerTwoData bean;
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
                    bean.getKeyValue().getListValue().remove(index);
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
        ProLayerTwoData bean;
        int index_position;

        GroupHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_group_title);
            flowLayout = itemView.findViewById(R.id.public_type_group_flow_layout);
            layout = itemView.findViewById(R.id.public_type_group_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }

            });


        }

        private void setdata(ProLayerTwoData res){
            List<KeyValue> top_jz=new ArrayList<>();
            for (ProLayerTwoData linkBean:res.getRecordlinkvalues()){
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




    public interface SealChoice{


        void refresh(ProLayerTwoData bean, int position_index);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }


    private void setKeyValueData(KeyValue data,String left_title,String right_key,String right_name,String right_value){
        data.setLeft_title(left_title);
        data.setRight_key(right_key);
        data.setRight_name(right_name);
        data.setRight_value(right_value);

    }

    private void linkListOneBean(ProLayerTwoData layerTwoData, String state, int adapter_position){
        List<ProLayerTwoData> proLayerAdd=new ArrayList<>();
        List<ProLayerTwoData> proLayerDel=new ArrayList<>();

        for (ProLayerTwoData allTwoData: layerTwoData.getRecordlinkvalues()){
            if (allTwoData.getLinkvalue().equalsIgnoreCase(state)) {
                KeyValue keyNew=new KeyValue(String.valueOf(allTwoData.getId()));
                if(StringJudge.isEmpty(allTwoData.getContent())){
                    setKeyValueData(keyNew,allTwoData.getTitle(),"未完成","","");
                }else{
                    allTwoData.setIs_select(true);
                    setKeyValueData(keyNew,allTwoData.getTitle(),"",allTwoData.getDisplaycontent(),allTwoData.getContent());
                }

                keyNew.setType(allTwoData.getValuetype());
                List<String> names=StringUtils.listToStringSplitCharacters(allTwoData.getValuetype(), "_");
                switch (names.get(0)){
                    case "datetime":
                        allTwoData.setView_type(TagFinal.TYPE_DATE);
                        break;
                    case "select":
                        if (StringJudge.isEmpty(allTwoData.getRecordlinkvalues())){
                            allTwoData.setView_type(TagFinal.TYPE_SELECT_SINGLE);
                        }else{
                            allTwoData.setView_type(TagFinal.TYPE_SELECT_ONE);
                        }
                        break;
                    case "list":
                        if (StringJudge.isEmpty(allTwoData.getRecordlinkvalues())){
                            allTwoData.setView_type(TagFinal.TYPE_LIST_SINGLE);
                        }else{
                            allTwoData.setView_type(TagFinal.TYPE_LIST_ONE);
                        }
                        break;
                    case "selectuser":
                        allTwoData.setView_type(TagFinal.TYPE_SELECT_USER);
                        break;

                    case "image":
                        if(!StringJudge.isEmpty(allTwoData.getContent())){
                            keyNew.setListValue(StringUtils.listToStringSplitCharacters(allTwoData.getContent(),","));
                        }
                        allTwoData.setView_type(TagFinal.TYPE_IMAGE);
                        break;
                    case "check":
                        allTwoData.setView_type(TagFinal.TYPE_CHECK);
                        break;
                    case "group":
                        allTwoData.setView_type(TagFinal.TYPE_GROUP);
                        break;
                    case "longtext":

                        keyNew.setKey(StringUtils.getTextJoint("请输入%1$s",allTwoData.getTitle()));
                        allTwoData.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                        break;
                    case "decimal":
                    case "text":
                    case "int":
                        allTwoData.setView_type(TagFinal.TYPE_TXT_EDIT);
                        keyNew.setKey(StringUtils.getTextJoint("请输入%1$s",allTwoData.getTitle()));
                        break;
                    default:
                        allTwoData.setView_type(TagFinal.TYPE_DATE);
                        break;
                }
                allTwoData.setKeyValue(keyNew);
                proLayerAdd.add(allTwoData);
            }else{
                proLayerDel.add(allTwoData);
            }
        }
        for (ProLayerTwoData delData:proLayerDel){

            if (StringJudge.isEmpty(delData.getRecordlinkvalues())){
                for (int i = 0; i < showDataList.size(); i++) {
                    ProLayerTwoData showTwoData=showDataList.get(i);
                    if (delData.getId()==showTwoData.getId()){
                        showTwoData.getKeyValue().setRight_value("");
                        showTwoData.getKeyValue().setListValue(new ArrayList<String>());
                        showDataList.remove(i);
                        notifyItemRangeRemoved(i, 1);//top有个view
                        continue;
                    }
                }
            }else{
                for (ProLayerTwoData delThreeData:delData.getRecordlinkvalues()){
                    for (int i = 0; i < showDataList.size(); i++) {
                        ProLayerTwoData showDelTwoData=showDataList.get(i);
                        if (delThreeData.getId()==showDelTwoData.getId()){
                            showDelTwoData.getKeyValue().setRight_value("");
                            showDelTwoData.getKeyValue().setListValue(new ArrayList<String>());
                            showDataList.remove(i);
                            notifyItemRangeRemoved(i, 1);//top有个view
                            continue;
                        }
                    }
                }
                for (int i = 0; i < showDataList.size(); i++) {
                    ProLayerTwoData showTwoData=showDataList.get(i);
                    if (delData.getId()==showTwoData.getId()){
                        showTwoData.getKeyValue().setRight_value("");
                        showTwoData.getKeyValue().setListValue(new ArrayList<String>());
                        showDataList.remove(i);
                        notifyItemRangeRemoved(i, 1);//top有个view
                        continue;
                    }
                }
            }
        }
        showDataList.addAll(adapter_position+1,proLayerAdd);
        notifyItemRangeInserted(adapter_position+1, proLayerAdd.size());
    }

    private void linkSelectTwoBean(ProLayerTwoData layerTwoData, String state,int adapter_position){
        List<ProLayerTwoData> proLayerAdd=new ArrayList<>();
        List<ProLayerTwoData> proLayerDel=new ArrayList<>();

        for (ProLayerTwoData allTwoData: layerTwoData.getRecordlinkvalues()){
            switch (allTwoData.getLinkstate()){
                case 0://不比较
                    break;
                case 1://等于
                    if (allTwoData.getLinkvalue().equalsIgnoreCase(state)) {
                        KeyValue keyNew=new KeyValue(String.valueOf(allTwoData.getId()));

                        if(StringJudge.isEmpty(allTwoData.getContent())){
                            setKeyValueData(keyNew,allTwoData.getTitle(),"未完成","","");
                        }else{
                            allTwoData.setIs_select(true);
                            setKeyValueData(keyNew,allTwoData.getTitle(),"",allTwoData.getDisplaycontent(),allTwoData.getContent());
                        }

                        keyNew.setType(allTwoData.getValuetype());
                        List<String> names=StringUtils.listToStringSplitCharacters(allTwoData.getValuetype(), "_");
                        switch (names.get(0)){
                            case "datetime":
                                allTwoData.setView_type(TagFinal.TYPE_DATE);
                                break;
                            case "select":
                                if (StringJudge.isEmpty(allTwoData.getRecordlinkvalues())){
                                    allTwoData.setView_type(TagFinal.TYPE_SELECT_SINGLE);
                                }else{
                                    allTwoData.setView_type(TagFinal.TYPE_SELECT_ONE);
                                }
                                break;
                            case "list":
                                if (StringJudge.isEmpty(allTwoData.getRecordlinkvalues())){
                                    allTwoData.setView_type(TagFinal.TYPE_LIST_SINGLE);

                                }else{
                                    allTwoData.setView_type(TagFinal.TYPE_LIST_ONE);
                                }
                                break;
                            case "selectuser":
                                allTwoData.setView_type(TagFinal.TYPE_SELECT_USER);
                                break;
                            case "longtext":
                                keyNew.setKey(StringUtils.getTextJoint("请输入%1$s",allTwoData.getTitle()));

                                allTwoData.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                                break;
                            case "image":
                                if(!StringJudge.isEmpty(allTwoData.getContent())){
                                    keyNew.setListValue(StringUtils.listToStringSplitCharacters(allTwoData.getContent(),","));
                                }
                                allTwoData.setView_type(TagFinal.TYPE_IMAGE);
                                break;
                            case "check":
                                allTwoData.setView_type(TagFinal.TYPE_CHECK);
                                break;
                            case "group":
                                allTwoData.setView_type(TagFinal.TYPE_GROUP);
                                break;
                            case "decimal":
                            case "text":
                            case "int":

                                allTwoData.setView_type(TagFinal.TYPE_TXT_EDIT);
                                keyNew.setKey(StringUtils.getTextJoint("请输入%1$s",allTwoData.getTitle()));
                                break;
                            default:
                                allTwoData.setView_type(TagFinal.TYPE_DATE);
                                break;
                        }
                        allTwoData.setKeyValue(keyNew);
                        proLayerAdd.add(allTwoData);
                    }else{
                        proLayerDel.add(allTwoData);
                    }
                    break;
                case 2://不等于
                    break;
                case 3://大于
                    break;
                case 4://小于
                    break;
                case 5://大于等于
                    break;
                case 6://小于等于
                    break;
            }
        }
        for (ProLayerTwoData delData:proLayerDel){
            if (StringJudge.isEmpty(delData.getRecordlinkvalues())){
                for (int i = 0; i < showDataList.size(); i++) {
                    ProLayerTwoData showTwoData=showDataList.get(i);
                    if (delData.getId()==showTwoData.getId()){
                        showDataList.remove(i);
                        notifyItemRangeRemoved(i, 1);//top有个view
                        continue;
                    }
                }
            }else{
                for (ProLayerTwoData delThreeData:delData.getRecordlinkvalues()){
                    Logger.e(delThreeData.getRecordlinkvalues().size()+"");
                    for (int i = 0; i < showDataList.size(); i++) {
                        ProLayerTwoData showDelTwoData=showDataList.get(i);
                        if (delThreeData.getId()==showDelTwoData.getId()){
                            showDataList.remove(i);
                            notifyItemRangeRemoved(i, 1);//top有个view
                            continue;
                        }
                    }
                }
                for (int i = 0; i < showDataList.size(); i++) {
                    ProLayerTwoData showTwoData=showDataList.get(i);
                    if (delData.getId()==showTwoData.getId()){
                        showDataList.remove(i);
                        notifyItemRangeRemoved(i, 1);//top有个view
                        continue;
                    }
                }
            }
        }
        showDataList.addAll(adapter_position+1,proLayerAdd);
        notifyItemRangeInserted(adapter_position+1, proLayerAdd.size());
    }







}

