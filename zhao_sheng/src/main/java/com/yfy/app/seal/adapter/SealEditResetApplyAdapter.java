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
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.seal.SealEditResetApplyActivity;
import com.yfy.app.seal.bean.Approval;
import com.yfy.app.seal.bean.SealApply;
import com.yfy.app.seal.bean.SealState;
import com.yfy.app.seal.bean.SealType;
import com.yfy.db.GreenDaoManager;
import com.yfy.db.GreenDaoString;
import com.yfy.greendao.KeyValueDb;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
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
public class SealEditResetApplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private GreenDaoString greenDaoString;
    private List<Approval> Approvallist;

    public void setApprovallist(List<Approval> approvallist) {
        Approvallist = approvallist;
    }

    private List<KeyValueDb> dataList;
    private SealEditResetApplyActivity mContext;

    public void setDataList(List<KeyValueDb> dataList) {
        this.dataList = dataList;
    }

    public List<KeyValueDb> getDataList() {
        return dataList;
    }

    public SealEditResetApplyAdapter(SealEditResetApplyActivity mContext){
        this.mContext=mContext;
        this.dataList = new ArrayList<>();
        greenDaoString=new GreenDaoString();
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        return dataList.get(position).getView_type();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TagFinal.TYPE_TXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_txt, parent, false);
            return new TxtHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHOICE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceHolder(view);
        }
        if (viewType == TagFinal.TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceHolder(view);
        }
        if (viewType == TagFinal.TYPE_DATE_TIME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new ChoiceHolder(view);
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
            return new MultiAddHolder(view);
        }
        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TxtHolder) {
            TxtHolder reHolder = (TxtHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.txt_key.setText(reHolder.bean.getKey());
            reHolder.txt_value.setText(reHolder.bean.getName());
        }
        if (holder instanceof ChoiceHolder) {
            ChoiceHolder choice = (ChoiceHolder) holder;
            choice.initDateDialog();
            choice.initDialogList();
            choice.index_position=position;
            choice.bean=dataList.get(position);
            choice.apply_name.setText(choice.bean.getKey());
            choice.apply_value.setText(choice.bean.getName());
        }
        if (holder instanceof LongTxtEditHolder) {
            LongTxtEditHolder edit = (LongTxtEditHolder) holder;
            edit.index=position;
            edit.bean=dataList.get(position);
            edit.apply_name.setText(edit.bean.getKey());
//            edit.apply_edit.setFocusable(false);
            edit.apply_edit.setText(edit.bean.getValue());
        }
        if (holder instanceof TxtEditHolder) {
            TxtEditHolder edittxt = (TxtEditHolder) holder;
            edittxt.index=position;
            edittxt.bean=dataList.get(position);
            edittxt.apply_name.setText(edittxt.bean.getKey());
//            edittxt.apply_edit.setFocusable(false);
            edittxt.apply_edit.setText(edittxt.bean.getValue());
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
        if (holder instanceof MultiAddHolder) {
            MultiAddHolder multiHolder = (MultiAddHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);
            multiHolder.multi_name.setText(multiHolder.bean.getKey());
            multiHolder.multi.setVisibility(View.VISIBLE);
            List<String> image=new ArrayList<>();
            if (StringJudge.isEmpty(multiHolder.bean.getValue())){
                multiHolder.multi.setList(image);
            }else{
                image=StringUtils.listToStringSplitCharacters(multiHolder.bean.getValue(),",");
                multiHolder.multi.setList(image);
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class TxtHolder extends RecyclerView.ViewHolder {
        TextView txt_key;
        AppCompatTextView txt_value;
        KeyValueDb bean;
        TxtHolder(View itemView) {
            super(itemView);
            txt_key=  itemView.findViewById(R.id.public_type_txt_key);
            txt_value=  itemView.findViewById(R.id.public_type_txt_value);
        }
    }

    private class ChoiceHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        KeyValueDb bean;
        int index_position;
        ChoiceHolder(View itemView) {
            super(itemView);
            apply_name=  itemView.findViewById(R.id.public_type_choice_key);
            apply_value=  itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (bean.getView_type()){
                        case TagFinal.TYPE_DATE_TIME:
                            mContext.closeKeyWord();
                            date_dialog.showAtBottom();
                            break;
                        case TagFinal.TYPE_DATE:
                            mContext.closeKeyWord();
                            date_dialog.showAtBottom();
                            break;
                        case TagFinal.TYPE_CHOICE:
                            cpwListBeanView.setType(bean.getType());
                            switch (bean.getType()){
                                case "person":
                                    setCPWlListMasterData();
                                    cpwListBeanView.setDatas(cpwBeans);
                                    cpwListBeanView.showAtCenter();
                                    break;
                                case "seal_type":
                                    setCPWlListSealTypeData();
                                    cpwListBeanView.setDatas(cpwBeans);
                                    cpwListBeanView.showAtCenter();
                                    break;
                                case "do_type":
                                    setCPWlListTypeData();
                                    cpwListBeanView.setDatas(cpwBeans);
                                    cpwListBeanView.showAtCenter();
                                    break;
                            }

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

                            bean.setName(date_dialog.getTimeName());
                            bean.setValue(date_dialog.getTimeValue());
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
        private CPWListBeanView cpwListBeanView;
        List<CPWBean> cpwBeans=new ArrayList<>();
        private void setCPWlListMasterData(){
            if (StringJudge.isEmpty(Approvallist)){
                mContext.toastShow(R.string.success_not_details);
                return;
            }else{
                cpwBeans.clear();
                for(Approval opear:Approvallist){
                    CPWBean cpwBean =new CPWBean();
                    cpwBean.setName(opear.getApprovalname());
                    cpwBean.setId(opear.getApprovalid());
                    cpwBeans.add(cpwBean);
                }
            }

        }
        private void setCPWlListSealTypeData(){
            if (StringJudge.isEmpty(sealStateList)){
                mContext.toastShow(R.string.success_not_details);
                return;
            }else{
                cpwBeans.clear();
                for(SealState opear:sealStateList){
                    CPWBean cpwBean =new CPWBean();
                    cpwBean.setName(opear.getSignetname());
                    cpwBean.setId(opear.getSignetid());
                    cpwBeans.add(cpwBean);
                }
            }

        }
        private void setCPWlListTypeData(){


            if (StringJudge.isEmpty(sealStateList)){
                mContext.toastShow(R.string.success_not_details);
                return;
            }else{

                if (two==null) two=dataList.get(3);
                SealState sealState=null;
                for (SealState bean:sealStateList){
                    if (bean.getSignetid().equalsIgnoreCase(two.getValue()))sealState=bean;
                }
                if (sealState==null)return;
                cpwBeans.clear();
                for(SealType opear:sealState.getTypes()){
                    CPWBean cpwBean =new CPWBean();
                    cpwBean.setName(opear.getTypename());
                    cpwBean.setId(opear.getTypeid());
                    cpwBeans.add(cpwBean);
                }
            }

        }
        private void initDialogList(){
            cpwListBeanView = new CPWListBeanView(mContext);
            cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
                @Override
                public void onClick(CPWBean cpwBean,String type) {
                    switch (type){
                        case "person":
                            bean.setName(cpwBean.getName());
                            bean.setValue(cpwBean.getId());
                            notifyItemChanged(index_position,bean);
                            cpwListBeanView.dismiss();
                            break;
                        case "seal_type":
                            setSealState(cpwBean.getId());
                            cpwListBeanView.dismiss();
                            break;
                        case "do_type":
                            setSealType(cpwBean.getId());
                            cpwListBeanView.dismiss();
                            break;
                    }

                }
            });
        }

    }

    private class LongTxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        KeyValueDb bean;
        int index;

        LongTxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_long_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_long_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    bean.setValue(s.toString().trim());
                }
            });



        }
    }
    private class TxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        KeyValueDb bean;
        int index;

        TxtEditHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_txt_edit_key);
            apply_edit = itemView.findViewById(R.id.public_type_txt_edit_value);
            apply_edit.addTextChangedListener(new MyWatcher() {
                @Override
                public void afterTextChanged(Editable s) {


                    bean.setValue(s.toString().trim());
                }
            });



        }
    }


    private class MultiAddHolder extends RecyclerView.ViewHolder {
        TextView multi_name;
        MultiPictureView multi;
        KeyValueDb bean;
        int position_index;

        MultiAddHolder(View itemView) {
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
                    bean.setImage(StringUtils.stringToArraysGetString(multi.getList(),","));
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

        void refresh(KeyValueDb bean,int position_index);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }











    public List<SealState> sealStateList =new ArrayList<>();
    private KeyValueDb two;

    public void setSealStateList(List<SealState> sealStateList) {
        this.sealStateList = sealStateList;
    }

    private void setSealState(String id){
        if (dataList.get(3).getValue().equalsIgnoreCase(id)){
        }else{//person

            while (dataList.size()>3){
                dataList.remove(3);
            }
            SealState sealState=null;
            for (SealState bean:sealStateList){
                if (bean.getSignetid().equalsIgnoreCase(id))sealState=bean;
            }
            if (sealState==null)return;
            List<KeyValueDb> twos=GreenDaoManager.getInstance().getKeyValueDbList("where type = \"seal_type\" and value = \""+id+"\"");
            if (StringJudge.isEmpty(twos)){
                two = new KeyValueDb();
                two.setKey("印章类型:");
                two.setValue(sealState.getSignetid());
                two.setName(sealState.getSignetname());
                two.setView_type(TagFinal.TYPE_CHOICE);


                two.setType("seal_type");
                two.setModel_type(sealState.getSignetid());
                two.setKey_value_id(sealState.getSignetid());
            }else{
                two=twos.get(0);
            }
            dataList.add(two);

            KeyValueDb seal_do=new KeyValueDb();
            seal_do.setKey("用章类型:");
            seal_do.setValue("");
            seal_do.setName("请选择用章类型");
            seal_do.setView_type(TagFinal.TYPE_CHOICE);

            seal_do.setModel_type(sealState.getSignetid());
            seal_do.setType("do_type");
            dataList.add(seal_do);

            notifyDataSetChanged();
            GreenDaoManager.getInstance().saveAllKeyValueDb(dataList);
        }

    }

    private void setSealType(String id) {

        if (dataList.get(4).getValue().equalsIgnoreCase(id)){
        }else{

            while (dataList.size()>4){
                dataList.remove(4);
            }
            if (two==null)two=dataList.get(3);
            SealState sealState=null;
            for (SealState bean:sealStateList){
                if (bean.getSignetid().equalsIgnoreCase(two.getValue()))sealState=bean;

            }
            if (sealState==null)return;
            for (SealType sealType:sealState.getTypes()){
                if (sealType.getTypeid().equalsIgnoreCase(id)){

                    KeyValueDb doType = new KeyValueDb();
                    doType.setKey("用章类型:");
                    doType.setValue(sealType.getTypeid());
                    doType.setName(sealType.getTypename());
                    doType.setView_type(TagFinal.TYPE_CHOICE);

                    doType.setType("do_type");
                    doType.setModel_type(two.getValue());
                    doType.setKey_value_id(sealType.getTypeid());
                    dataList.add(doType);
                    setType(sealType);
                }
            }
        }

        notifyDataSetChanged();
        GreenDaoManager.getInstance().saveAllKeyValueDb(dataList);
    }


    private DateBean dateBean;

    private void setType(SealType sealType) {
        List<KeyValueDb> doTypeList = GreenDaoManager.getInstance()
                .getKeyValueDbList("where child_id = \""+sealType.getTypeid()+"\" and parent_id = \""+two.getValue()+"\"");


//        Logger.e( GreenDaoManager.getInstance().loadAllKeyValueDb().size()+"");

        Logger.e(doTypeList.size()+"");
        if (StringJudge.isEmpty(doTypeList)){
            dateBean=new DateBean();
            dateBean.setValue_long(System.currentTimeMillis(),false );
            if (sealType.getTypename().equalsIgnoreCase("盖章")) {


                KeyValueDb four = new KeyValueDb();
                four.setKey("用章时间:");
                four.setValue(dateBean.getValue());
                four.setView_type(TagFinal.TYPE_DATE_TIME);
                four.setName(dateBean.getName());
                four.setModel_type(two.getValue());
                four.setKey_value_id(sealType.getTypeid());
                dataList.add(four);
            } else {
                KeyValueDb five = new KeyValueDb();
                five.setKey("开始时间:");
                five.setValue(dateBean.getValue());
                five.setName(dateBean.getName());
                five.setView_type(TagFinal.TYPE_DATE_TIME);

                five.setModel_type(two.getValue());
                five.setKey_value_id(sealType.getTypeid());
                dataList.add(five);
                KeyValueDb six = new KeyValueDb();
                six.setKey("还章时间:");
                six.setValue(dateBean.getValue());
                six.setName(dateBean.getName());
                six.setView_type(TagFinal.TYPE_DATE_TIME);

                six.setModel_type(two.getValue());

                six.setKey_value_id(sealType.getTypeid());
                dataList.add(six);
            }
            for (SealApply table:sealType.getTables()){
                List<String> listOne=StringUtils.listToStringSplitCharacters(table.getValuetype(), "_");
                KeyValueDb keyValue = new KeyValueDb();
                keyValue.setKey(StringUtils.getTextJoint("%1$s:", table.getTablename()));
                keyValue.setValue("");
                keyValue.setName(table.getTablename());
                keyValue.setKey_value_id(table.getTableid());
                keyValue.setParent_id(two.getValue());
                keyValue.setChild_id(sealType.getTypeid());
                keyValue.setType(table.getValuetype());
                switch (listOne.get(0)){
                    case "txt":
                        keyValue.setView_type(TagFinal.TYPE_TXT_EDIT);
                        break;
                    case "float":
                        keyValue.setView_type(TagFinal.TYPE_TXT_EDIT);
                        break;
                    case "longtxt":
                        keyValue.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                        break;
                    case "img":
                        keyValue.setView_type(TagFinal.TYPE_IMAGE);
                        break;
                    default:
                        keyValue.setView_type(TagFinal.TYPE_LONG_TXT_EDIT);
                        break;
                }
                dataList.add(keyValue);
            }
        }else{

            dataList.addAll(doTypeList);

        }
    }
}
