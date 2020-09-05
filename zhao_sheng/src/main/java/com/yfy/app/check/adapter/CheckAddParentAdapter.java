package com.yfy.app.check.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.check.CEditActivity;
import com.yfy.app.check.bean.IllBean;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.dialog.ConfirmDateAndTimeWindow;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2016/10/17.
 */
public class CheckAddParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<IllBean> dataList;
    private BaseActivity mContext;

    public List<IllBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<IllBean> dataList) {
        this.dataList = dataList;
//        getRandomHeights(dataList);
    }
    // 当前加载状态，默认为加载完成
    private int loadState = 2;


    public CheckAddParentAdapter(BaseActivity mContext){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_add_tag, parent, false);
            return new BaseTagHolder(view);
        }
        if (viewType == TagFinal.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_add_txt, parent, false);
            return new BaseTxtHolder(view);
        }
        if (viewType == TagFinal.TYPE_PARENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_add_check, parent, false);
            return new CheckHolder(view);
        }
        if (viewType == TagFinal.TYPE_CHILD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_multi_add, parent, false);
            return new MultiHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);

            multiHolder.multi_name.setText(multiHolder.bean.getTablename());
            multiHolder.multi.setVisibility(View.VISIBLE);
            List<String> list=StringUtils.listToStringSplitCharacters( multiHolder.bean.getTablevaluetype(),"_" );
            if (list.size()>1)
            multiHolder.multi.setMax(ConvertObjtect.getInstance().getInt(list.get(1)));
            multiHolder.multi.setList(multiHolder.bean.getKeyValue().getListValue());
        }

        if (holder instanceof BaseTagHolder) {
            BaseTagHolder reHolder = (BaseTagHolder) holder;
            reHolder.bean=dataList.get(position);
            reHolder.name.setText(reHolder.bean.getParent_name());
        }
        if (holder instanceof CheckHolder) {
            CheckHolder checkHolder = (CheckHolder) holder;
            checkHolder.bean=dataList.get(position);
            switch (checkHolder.bean.getTablevaluetype()){
                case "bool_one":
                    checkHolder.name.setText(checkHolder.bean.getKey());
                    break;
                    default:
                        checkHolder.name.setText(checkHolder.bean.getTablename());
                        break;
            }
            if (checkHolder.bean.getTablevaluecannull().equals("0")){
                checkHolder.name.setTextColor(ColorRgbUtil.getMaroon());
            }else{
                checkHolder.name.setTextColor(ColorRgbUtil.getBaseText());
            }
        }

        if (holder instanceof BaseTxtHolder) {
            BaseTxtHolder base = (BaseTxtHolder) holder;
            base.bean=dataList.get(position);
            base.index=position;
            String valuedate=base.bean.getValue();
            switch (base.bean.getTablevaluetype()){
                case "longtxt":
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请填写%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }

                    break;
                case "txt":
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请填写%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
                case "datetime":
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.initDateDialog();
                    base.key.setText(base.bean.getTablename());
                    String value=base.bean.getValue();
                    if (StringJudge.isEmpty(value)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请选择%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(value);
                    }

                    break;
                case "tel":
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请填写%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
                case "int":
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请填写%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
                case "date":
                    base.initDialog();
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请选择%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
                case "date_one":
                    base.initDialog();
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());
                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请选择%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
                case "base":
                    base.key.setText(base.bean.getKey());
                    base.value.setText(base.bean.getValue());
                    base.key.setTextColor(ColorRgbUtil.getGrayText());
                    base.value.setTextColor(ColorRgbUtil.getGrayText());
                    break;
                default:
                    if (base.bean.getTablevaluecannull().equals("0")){
                        base.key.setTextColor(ColorRgbUtil.getMaroon());
                    }else{
                        base.key.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    base.key.setText(base.bean.getTablename());

                    if (StringJudge.isEmpty(valuedate)){
                        base.value.setTextColor(ColorRgbUtil.getGrayText());
                        base.value.setText(StringUtils.getTextJoint("请填写%1$s",base.bean.getTablename()));
                    }else {
                        base.value.setTextColor(ColorRgbUtil.getBaseText());
                        base.value.setText(valuedate);
                    }
                    break;
            }

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class BaseTagHolder extends RecyclerView.ViewHolder {
        TextView name;
        IllBean bean;
        BaseTagHolder(View itemView) {
            super(itemView);
            name=  itemView.findViewById(R.id.check_add_tag_name);

        }
    }
    private class BaseTxtHolder extends RecyclerView.ViewHolder {
        TextView key;
        TextView value;
        RelativeLayout layout;
        int index;
        IllBean bean;
        BaseTxtHolder(View itemView) {
            super(itemView);
            layout=  itemView.findViewById(R.id.check_add_parent_layout);
            key=  itemView.findViewById(R.id.check_add_parent_key);
            value=  itemView.findViewById(R.id.check_add_parent_value);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //单/多选
//                    Intent intent=new Intent(mContext,CheckClassActivity.class);
//                    intent.putExtra(TagFinal.OBJECT_TAG,bean);
//                    mContext.startActivity(intent);
                    Intent intent = new Intent(mContext, CEditActivity.class);
                    switch (bean.getTablevaluetype()){
                        case "longtxt":

                            intent.putExtra(TagFinal.OBJECT_TAG,bean);
                            intent.putExtra("position", index);
                            mContext.startActivityForResult(intent, TagFinal.UI_ADD);
                            break;
                        case "txt":
                            intent.putExtra(TagFinal.OBJECT_TAG,bean);
                            intent.putExtra("position", index);
                            mContext.startActivityForResult(intent, TagFinal.UI_ADD);
                            break;
                        case "datetime":

                            date_dialog.showAtBottom();
                            break;
                        case "int":
                            intent.putExtra(TagFinal.OBJECT_TAG,bean);
                            intent.putExtra("position", index);
                            mContext.startActivityForResult(intent, TagFinal.UI_ADD);
                            break;
                        case "tel":
                            intent.putExtra(TagFinal.OBJECT_TAG,bean);
                            intent.putExtra("position", index);
                            mContext.startActivityForResult(intent, TagFinal.UI_ADD);
                            break;
                        case "base":
                            break;
                        case "date":
                            dateDialog.showAtBottom();
                            break;
                        case "date_one":
                            dateDialog.showAtBottom();
                            break;
                            default:
                                intent.putExtra(TagFinal.OBJECT_TAG,bean);
                                intent.putExtra("position", index);
                                mContext.startActivityForResult(intent, TagFinal.UI_ADD);
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

                            bean.setValue(date_dialog.getTimeValue());
                            notifyItemChanged(index, bean);
                            date_dialog.dismiss();
                            break;
                        case R.id.cancel:
                            date_dialog.dismiss();
                            break;
                    }

                }
            });
        }
        private ConfirmDateWindow dateDialog;
        private void initDialog(){
            dateDialog = new ConfirmDateWindow(mContext);
            dateDialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.set:
                            bean.setValue(dateDialog.getTimeValue());
                            notifyItemChanged(index, bean);
                            dateDialog.dismiss();
                            break;
                        case R.id.cancel:
                            dateDialog.dismiss();
                            break;
                    }

                }
            });
        }
    }
    private class CheckHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        AppCompatTextView name;
        IllBean bean;
        CheckHolder(View itemView) {
            super(itemView);
            checkBox= itemView.findViewById(R.id.check_add_parent_box);
            name=  itemView.findViewById(R.id.check_add_parent_name);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    bean.setValue(isChecked?"是":"否");
                }
            });

        }
    }




    private class MultiHolder extends RecyclerView.ViewHolder {
        TextView multi_name;
        MultiPictureView multi;
        IllBean bean;
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
    /**
     * 设置上拉加载状态
     *
     * @param loadState 1.正在加载 2.加载完成 3.加载到底
     */
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }






    public interface CheckChoice{
        void refresh(IllBean bean,int position_index);
    }

    public CheckChoice sealChoice;

    public void setSealChoice(CheckChoice sealChoice) {
        this.sealChoice = sealChoice;
    }



}
