package com.yfy.app.process.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.process.ProcessListSelectMoreActivity;
import com.yfy.app.process.ProcessRoomGroupActivity;
import com.yfy.app.process.bean.ProLayerTwoData;
import com.yfy.app.process.bean.ProLinkListBean;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;


public class ProcessRoomGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProLayerTwoData> dataList;
    private ProcessRoomGroupActivity mContext;

    public List<ProLayerTwoData> getDataList() {
        return dataList;
    }

    public void addItemData(int position,List<ProLayerTwoData> two){
        dataList.addAll(position, two);
        Logger.e(two.size()+"");
        notifyItemRangeInserted(position+1, two.size());
    }
    public void removeItemData(String id) {
        int n=0;
        for (ProLayerTwoData proLayerTwoData:dataList){
            if (id.equalsIgnoreCase(String.valueOf((long) proLayerTwoData.getId()))){
                Logger.e(id);
                dataList.remove(n);
                notifyItemRangeRemoved(n, 1);
            }
            n++;
        }
    }
    public void setDataList(List<ProLayerTwoData> dataList) {
        this.dataList = dataList;
    }

    public ProcessRoomGroupAdapter(ProcessRoomGroupActivity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getView_type();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TagFinal.TYPE_SELECT_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProSelectSingleH(view);

        }
        if (viewType == TagFinal.TYPE_LIST_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ProListSingleH(view);

        }
        if (viewType == TagFinal.TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceDateHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChoiceDateHolder) {
            ChoiceDateHolder choice = (ChoiceDateHolder) holder;
            choice.index_position=position;
            choice.initDateTimeDialog();
            choice.initDateDialog();
            choice.bean=dataList.get(position);
            choice.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",choice.bean.getTitle()));
            if (choice.bean.isIs_select()){
                choice.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                choice.apply_value.setText(choice.bean.getKeyValue().getRight_name());
            }else {
                choice.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                choice.apply_value.setText("请选择");
            }
        }
        if (holder instanceof ProListSingleH) {
            ProListSingleH listSingleH = (ProListSingleH) holder;
            listSingleH.initTeaDialog();
            listSingleH.index_position=position;
            listSingleH.bean= dataList.get(position);
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
            selectSingleH.bean= dataList.get(position);
            selectSingleH.apply_name.setText(StringUtils.getTextJoint("%1$s\t:",selectSingleH.bean.getTitle()));
            if (selectSingleH.bean.isIs_select()){
                selectSingleH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectSingleH.apply_value.setText(selectSingleH.bean.getKeyValue().getRight_name());
            }else {
                selectSingleH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectSingleH.apply_value.setText("请选择");
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
                    mContext.closeKeyWord();
                    switch (bean.getSelects()){
                        case "yyyy/MM/dd":
                            dateDialog.showAtBottom();
                            break;
                        case "yyyy/MM/dd HH:mm":
                            dateAndTimeDialog.showAtBottom();
                            break;
                    }

                }
            });
        }
        private ConfirmDateWindow dateDialog;
        private void initDateDialog(){
            dateDialog = new ConfirmDateWindow(mContext);
            dateDialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.set:
                            bean.setIs_select(true);
                            KeyValue value=bean.getKeyValue();
                            value.setRight_name(dateDialog.getTimeName());
                            value.setRight_value(dateDialog.getTimeValue());
                            notifyItemChanged(index_position,bean);
                            linkEigthBean(bean,index_position);
                            dateDialog.dismiss();
                            break;
                        case R.id.cancel:
                            dateDialog.dismiss();
                            break;
                    }

                }
            });
        }
        private ConfirmDateAndTimeWindow dateAndTimeDialog;
        private void initDateTimeDialog() {
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
                            linkEigthBean(bean,index_position);
                            break;
                        case R.id.cancel:
                            dateAndTimeDialog.dismiss();
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
                    if (bean.getMax().equalsIgnoreCase("1")){
                        setProcessSelect(bean.getRecordvaluearray());
                    }else{
                        List<KeyValue> dataList=new ArrayList<>();
                        for (ProLinkListBean listBean:bean.getRecordvaluearray()){
                            dataList.add(new KeyValue(listBean.getName(),String.valueOf((long)listBean.getId()),TagFinal.TYPE_SELECT_SINGLE));
                        }
                        Intent intent =new Intent(mContext,ProcessListSelectMoreActivity.class);
                        intent.putParcelableArrayListExtra(Base.data, (ArrayList<? extends Parcelable>) dataList);
                        intent.putExtra(Base.index,index_position);
                        intent.putExtra(Base.num,bean.getMax());
                        mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_MORE);
                    }
                }
            });
        }
        private void setProcessSelect(List<ProLinkListBean> types){
            cpwListView.setType("type");
            Logger.e(types.size()+"");
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

                            if (StringJudge.isEmpty(bean.getKeyValue().getValue())){

                            }else{

                                if (bean.getKeyValue().getValue().equalsIgnoreCase(cpwBean.getValue())){
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
                            linkEigthBean(bean,index_position);
                            break;
                    }
                }
            });
        }
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
                            }else{
                                List<KeyValue> dataList=new ArrayList<>();
                                for (String s:StringUtils.listToStringSplitCharacters(bean.getSelects(),",")){
                                    dataList.add(new KeyValue(s,s,TagFinal.TYPE_SELECT_SINGLE));
                                    Logger.e(s);
                                }
                                Intent intent =new Intent(mContext,ProcessListSelectMoreActivity.class);
                                intent.putParcelableArrayListExtra(Base.data, (ArrayList<? extends Parcelable>) dataList);
                                intent.putExtra(Base.index,index_position);
                                intent.putExtra(Base.num,bean.getMax());
                                mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_MORE);
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
                            linkEigthBean(bean,index_position);
                            break;
                    }
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




    private void linkEigthBean(ProLayerTwoData bean,int position_index){
        if (bean.getLinkstate()!=7)return;
        List<String> ids=new ArrayList<>();
        List<String> values=new ArrayList<>();
        for (ProLayerTwoData twoData:dataList){
            if (twoData.getLinkstate()==7){
                if (StringJudge.isEmpty(twoData.getKeyValue().getRight_value())){
                    return ;
                }else{
                    ids.add(String.valueOf((long) twoData.getId()));
                    values.add(twoData.getKeyValue().getRight_value());
                }
            }else{
                continue;
            }
        }
        if (sealChoice!=null){
            sealChoice.refresh(bean,position_index,ids,values);
        }

    }




    public interface SealChoice{

        void refresh(ProLayerTwoData bean, int position_index,List<String> ids,List<String> values);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }



}
