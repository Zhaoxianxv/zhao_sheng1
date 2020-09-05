package com.yfy.app.vote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.vote.bean.VoteBean;
import com.yfy.app.vote.bean.VoteInfo;
import com.yfy.view.ExtendedEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yfy1 on 2017/3/22.
 */

public class VoteOperationAdapter extends BaseExpandableListAdapter {

    private LayoutInflater inflater;
    private boolean isCheck;
    private int mTouchItemPosition= -1;
    private int gray;
    private int black;

    private List<VoteInfo> voteDetailList = new ArrayList<VoteInfo>();
    private ArrayList<ExtendedEditText> edits = new ArrayList<>();
    public void setVoteDetailList(List<VoteInfo> voteDetailList) {
        this.voteDetailList = voteDetailList;

    }

    public List<VoteInfo> getVoteDetailList() {
        return voteDetailList;
    }

    public VoteOperationAdapter(Context context, List<VoteInfo> voteDetailList, boolean isCheck) {
        super();
        inflater=LayoutInflater.from(context);
        this.voteDetailList=voteDetailList;
        this.isCheck=isCheck;
        gray=context.getResources().getColor(R.color.a3_gray);
        black=context.getResources().getColor(R.color.app_base_text_color);

    }





    @Override
    public int getGroupCount() {

        return voteDetailList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return voteDetailList.get(i).getVotecontent().size();
    }

    @Override
    public Object getGroup(int i) {
        return voteDetailList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {

        return voteDetailList.get(i).getVotecontent().size();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        // 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID。
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return getGrouptext(view,i);
    }


    private View getGrouptext(View view,int group){
        ViewHolderGroup viewHolderCheckBox;
        VoteInfo vote=voteDetailList.get(group);
        if (view==null){
            viewHolderCheckBox=new ViewHolderGroup();
            view=inflater.inflate(R.layout.vote_checkbox_view,null);
            viewHolderCheckBox.title= (TextView) view.findViewById(R.id.vote_group_title);
            view.setTag(viewHolderCheckBox);
        }else{
            viewHolderCheckBox= (ViewHolderGroup) view.getTag();
        }
        viewHolderCheckBox.title.setText(vote.getTitle());
        return view;
    }





    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String type=voteDetailList.get(i).getType();
        if (type.equals("3")){
            return getChoiceQuestion(view,i,i1,true);
        }else if (type.equals("1")){
            return getChoiceQuestion(view,i,i1,false);
        }
        return view;
    }



    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }





    private View getChoiceQuestion(View view, final int group, final int child,boolean hide) {

        final ViewHolderChildChoice choice;
        final VoteBean bean=voteDetailList.get(group).getVotecontent().get(child);
//        Log.e("zx",bean.toString());
        final int max=voteDetailList.get(group).getMaxsize();
        if (view==null){
            choice=new ViewHolderChildChoice();
            view=inflater.inflate(R.layout.vote_chioce_question_view,null);
            choice.content= (TextView) view.findViewById(R.id.vote_child_text);
            choice.layout= (LinearLayout) view.findViewById(R.id.vote_chioce_layout);
            choice.checkbox= (CheckBox) view.findViewById(R.id.vote_child_checkbox);
            choice.edit= (ExtendedEditText) view.findViewById(R.id.vote_question_edit);

            view.setTag(choice);
        }else{
            choice= (ViewHolderChildChoice) view.getTag();
        }
        choice.content.setText(bean.getContent());

        final boolean check=bean.getIsselect().equals("true")?true:false;
        if (check){
            choice.content.setTextColor(black);
        }else{
            choice.content.setTextColor(gray);
        }

        if (hide){
            choice.edit.setVisibility(View.VISIBLE);
            choice.layout.setVisibility(View.GONE);
        }else{
            choice.layout.setVisibility(View.VISIBLE);
            choice.edit.setVisibility(View.GONE);
        }

        choice.edit.setTag(group + ":" + child);
        choice.edit.clearTextChangedListeners();
        setInput(choice.edit, group, child);

        edits.add(group,choice.edit);

        choice.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
//                    for (ExtendedEditText e:edits) {
//
////                        e.setFocusable(false);
//                    }
                    choice.content.setTextColor(black);
                    if (max==1){
                        for(VoteBean bean1:voteDetailList.get(group).getVotecontent()) {
                            bean1.setIsselect("false");
                        }
                        bean.setIsselect("true");
                        if (click!=null){
                            click.oniBox(group);
                        }
                    }else{
                        bean.setIsselect("true");
                    }
                }else{
                    choice.content.setTextColor(gray);
                    bean.setIsselect("false");
                }
            }
        });
        if (isCheck){
            choice.checkbox.setClickable(false);

        }else{
            choice.checkbox.setClickable(true);
        }
        choice.checkbox.setChecked(check);
        if (isCheck){
            choice.edit.setFocusable(false);
            choice.edit.setText(bean.getContent());
        }else{
            choice.edit.setFocusable(true);
            choice.edit.setText(voteDetailList.get(group).getReply());
        }
        return view;
    }







    class ViewHolderGroup{
        TextView title;
    }

    class ViewHolderChildChoice {
        ExtendedEditText edit;

        LinearLayout layout;
        CheckBox checkbox;
        TextView content;

    }
    private void setInput(final ExtendedEditText a, final int g, final int c) {
        a.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                VoteInfo bean=voteDetailList.get(g);
                bean.setReply(a.getText().toString());
            }
        });
        a.setOnTouchListener(new  View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    public void notifyDataSetChanged(List<VoteInfo> voteDetailList ) {
        super.notifyDataSetChanged();
    }

    private CheckBoxOnclick  click;

    public void setClick(CheckBoxOnclick click) {
        this.click = click;
    }

    public interface CheckBoxOnclick {
        void oniBox(int group);
    }
}
