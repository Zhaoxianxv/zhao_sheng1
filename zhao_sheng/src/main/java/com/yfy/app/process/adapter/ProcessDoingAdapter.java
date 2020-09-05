package com.yfy.app.process.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.process.ProcessAdminDoingActivity;
import com.yfy.app.process.ProcessSelectUserActivity;
import com.yfy.app.process.ProcessSelectUserAllActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
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
public class ProcessDoingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<KeyValue> dataList;
    private String id;
    private ProcessAdminDoingActivity mContext;


    public void setId(String id) {
        this.id = id;
    }

    public void setDataList(List<KeyValue> dataList) {
        this.dataList = dataList;
    }

    public List<KeyValue> getDataList() {
        return dataList;
    }

    public ProcessDoingAdapter(ProcessAdminDoingActivity mContext){
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

        if (viewType == TagFinal.TYPE_REFRECH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_long_txt_edit, parent, false);
            return new LongTxtEditHolder(view);
        }

        if (viewType == TagFinal.TYPE_IMAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_multi_add, parent, false);
            return new MultiHolder(view);
        }

        if (viewType == TagFinal.TYPE_SELECT_SINGLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new SelectUserHolder(view);
        }

        if (viewType == TagFinal.TYPE_SELECT_GROUP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_choice, parent, false);
            return new SelectUserHolder(view);
        }
        if (viewType == TagFinal.TYPE_STAR_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_type_star, parent, false);
            return new StarHolder(view);
        }

        return null;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StarHolder) {
            StarHolder starH = (StarHolder) holder;
            starH.index_position=position;
            starH.bean=dataList.get(position);
            starH.star_title.setText(StringUtils.getTextJoint("%1$s\t",starH.bean.getName()));

            if (!starH.bean.getMax().equalsIgnoreCase("0")){
                starH.star_score.setMax(ConvertObjtect.getInstance().getInt(starH.bean.getMax()));
            }else{
                starH.star_score.setMax(5);
            }
            if (StringJudge.isEmpty(starH.bean.getValue())){
                starH.star_score.setRating(0);
            }else {
                starH.star_score.setRating(ConvertObjtect.getInstance().getFloat(starH.bean.getValue()));
            }
        }
        if (holder instanceof SelectUserHolder) {
            SelectUserHolder selectUserH = (SelectUserHolder) holder;
            selectUserH.index_position=position;
            selectUserH.bean=dataList.get(position);
            selectUserH.apply_name.setText(StringUtils.getTextJoint("%1$s\t",selectUserH.bean.getKey()));
            if (StringJudge.isEmpty(selectUserH.bean.getValue())){
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getGrayText());
                selectUserH.apply_value.setText("请选择");
            }else {
                selectUserH.apply_value.setTextColor(ColorRgbUtil.getBaseText());
                selectUserH.apply_value.setText(selectUserH.bean.getName());
            }
        }

        if (holder instanceof MultiHolder) {
            MultiHolder multiHolder = (MultiHolder) holder;
            multiHolder.position_index=position;
            multiHolder.bean=dataList.get(position);
            multiHolder.multi.setVisibility(View.VISIBLE);

            if (!multiHolder.bean.getMax().equalsIgnoreCase("0")){
                multiHolder.multi.setMax(ConvertObjtect.getInstance().getInt(multiHolder.bean.getMax()));
            }
            multiHolder.multi.setList(multiHolder.bean.getListValue());
            multiHolder.multi_name.setText(StringUtils.getTextJoint("%1$s",multiHolder.bean.getName()));
        }
        if (holder instanceof LongTxtEditHolder) {
            LongTxtEditHolder edit = (LongTxtEditHolder) holder;
            edit.index=position;
            edit.bean=dataList.get(position);

            edit.apply_name.setText(StringUtils.getTextJoint("%1$s",edit.bean.getName()));
            if (StringJudge.isEmpty(edit.bean.getValue())){
                edit.apply_edit.setHint(edit.bean.getKey());
            }else{
                edit.apply_edit.setText(edit.bean.getValue());
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class LongTxtEditHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        EditText apply_edit;
        KeyValue bean;
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
    private class MultiHolder extends RecyclerView.ViewHolder {
        TextView multi_name;
        MultiPictureView multi;
        KeyValue bean;
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

    private class SelectUserHolder extends RecyclerView.ViewHolder {
        TextView apply_name;
        TextView apply_value;
        KeyValue bean;
        int index_position;

        SelectUserHolder(View itemView) {
            super(itemView);
            apply_name = itemView.findViewById(R.id.public_type_choice_key);
            apply_value = itemView.findViewById(R.id.public_type_choice_value);
            apply_value.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> names=StringUtils.listToStringSplitCharacters(bean.getId(),"_");
                    Intent intent;
                    if(bean.getView_type()==TagFinal.TYPE_SELECT_SINGLE){
                        intent=new Intent(mContext,ProcessSelectUserActivity.class);
                        intent.putExtra(Base.type,names.get(1));
                        intent.putExtra(Base.title,bean.getTitle());
                        intent.putExtra(Base.num,names.get(2));
                        intent.putExtra(Base.id,id);
                        intent.putExtra(Base.index,index_position);
                        mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_SINGLE);
                    }else{
                        intent=new Intent(mContext,ProcessSelectUserAllActivity.class);
                        intent.putExtra(Base.type,names.get(1));
                        intent.putExtra(Base.title,bean.getTitle());
                        intent.putExtra(Base.num,names.get(2));
                        intent.putExtra(Base.index,0);
                        intent.putExtra(Base.id,id);
                        mContext.startActivityForResult(intent,TagFinal.UI_SELECT_USER_MORE);
                    }
                }
            });
        }



    }
    private class StarHolder extends RecyclerView.ViewHolder {
        TextView star_title;
        RatingBar star_score;
        KeyValue bean;
        int index_position;

        StarHolder(View itemView) {
            super(itemView);
            star_title = itemView.findViewById(R.id.public_type_star_title);
            star_score = itemView.findViewById(R.id.public_type_star_rating);
            star_score.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    bean.setValue(String.valueOf((int) ratingBar.getRating()));
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

    public void addItemData(int position,List<KeyValue> two){
        dataList.addAll(position, two);
        notifyItemRangeInserted(position+1, two.size());
    }
    public void removeItemData(String id) {
        int n=0;
        for (KeyValue remove:dataList){
            if (id.equalsIgnoreCase(remove.getId())){
                dataList.remove(n);
                notifyItemRangeRemoved(n, 1);
            }
            n++;
        }
    }



    public interface SealChoice{


        void refresh(KeyValue bean, int position_index);
    }

    public SealChoice sealChoice;

    public void setSealChoice(SealChoice sealChoice) {
        this.sealChoice = sealChoice;
    }
}
