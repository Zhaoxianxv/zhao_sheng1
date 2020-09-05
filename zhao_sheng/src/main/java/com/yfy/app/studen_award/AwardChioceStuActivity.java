package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.studen_award.adapter.AwardStuAdapter;
import com.yfy.app.studen_award.bean.AwardInfor;
import com.yfy.app.studen_award.bean.AwardStuBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AwardChioceStuActivity extends WcfActivity {

    private String get_award_students="get_award_students";
    private String class_name;
    @Bind(R.id.award_xlist_main)
    XListView xlist;
    @Bind(R.id.award_clear_et)
    EditText clear_et;
    @Bind(R.id.botton)
    CardView layout;
    LoadingDialog dialog;
    private List<AwardStuBean> stubeans=new ArrayList<>();
    AwardStuAdapter xlistAdapter;

    private TextView menu1,menu2;
    private boolean isSeletor=false;
    private String selectorStu;
    private String id_Stu;
    private int class_id;


    private final int BACK=1;//选人
    private final int SUCCESS=2;//选人

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_class_chioce);
        dialog=new LoadingDialog(mActivity);
        getData();
        initSQToolbar();
        initView();
        getClassStudent();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void getData(){
        class_id =getIntent().getIntExtra("class_id",-1);
        Logger.e("zxx"," class_id "+class_id);
        class_name =getIntent().getStringExtra("class_name");
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(class_name);
        menu1=toolbar.addMenuText(1,R.string.chioce);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 1:
                        if(menu1.getText().toString().equals(getString(R.string.cancle))){
                            layout.setVisibility(View.GONE);
                            xlist.setOnItemClickListener(chiocelistenner);
                            menu1.setText(R.string.chioce);
                            if (stubeans!=null){
                                for (AwardStuBean bean:stubeans) {
                                    bean.setChick(false);
                                }
                                xlistAdapter.notifyDataSetChanged(stubeans);
                            }
                        }else{
                            layout.setVisibility(View.VISIBLE);
                            menu1.setText(R.string.cancel);
                            xlist.setOnItemClickListener(morechiocelistenner);
                        }
                        break;
                }

            }
        });

    }
    private void initView() {
        clear_et.setClickable(true);
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        xlistAdapter=new AwardStuAdapter(mActivity,stubeans);
        xlist.setAdapter(xlistAdapter);
        xlist.setXListViewListener(ixlistenner);
        xlist.setOnItemClickListener(chiocelistenner);

//        clear_et.setVisibility(View.GONE);
    }

    public boolean getSelectorStu(){
        StringBuilder sb=new StringBuilder();
        StringBuilder b=new StringBuilder();
        for (AwardStuBean bean:stubeans){
            if (bean.isChick()){
                sb.append(bean.getId()).append(",");
                b.append("0").append(",");

            }
        }
        if (sb.length()==0){
            toastShow("请选择至少一个学生");
            return true;
        }
        selectorStu=sb.substring(0,sb.length()-1).toString()==null?"":sb.substring(0,sb.length()-1).toString();
        id_Stu=b.substring(0,b.length()-1).toString()==null?"":b.substring(0,b.length()-1).toString();
        if (selectorStu.equals("")){
            toastShow("请选择至少一个学生");
            return true;
        }
        return false;

    }
    private AdapterView.OnItemClickListener morechiocelistenner=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            AwardStuBean bean=stubeans.get(i-1);
            if (bean.isChick()){
                bean.setChick(false);
                xlistAdapter.notifyDataSetChanged(stubeans);
            }else{
                bean.setChick(true);
                xlistAdapter.notifyDataSetChanged(stubeans);
            }
        }
    };
    private AdapterView.OnItemClickListener chiocelistenner=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String stu_id=stubeans.get(i-1).getId();
            Intent intent=new Intent(mActivity,AwardMainActivity.class);
            intent.putExtra("stu_id",stu_id);
            intent.putExtra("class_id",class_id);
            Logger.e("zxx"," class_id "+class_id);
            intent.putExtra("stu_name",stubeans.get(i-1).getName());
            intent.putExtra("isStu",true);
            startActivity(intent);
        }
    };

    private XListView.IXListViewListener ixlistenner=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            getClassStudent();
        }

        @Override
        public void onLoadMore() {

        }
    };
    public void getClassStudent(){

        CustomDate d=new CustomDate();
        String time=d.toString();
        Object[] parmas=new Object[]{
                Base.user.getSession_key(),
                class_id,
                time,
                ""
        };
        ParamsTask grade= new ParamsTask(parmas,get_award_students,get_award_students,dialog);
        execute(grade);
    }



    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xlist.stopRefresh();
        Logger.e("zxx", "onSuccess: "+result);
        AwardInfor infor=gson.fromJson(result,AwardInfor.class);
        String name=wcfTask.getName();
        if (name.equals(get_award_students)){
            stubeans=infor.getStudents();
            xlistAdapter.notifyDataSetChanged(stubeans);
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        xlist.stopRefresh();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case BACK:
                    stubeans=data.getParcelableArrayListExtra("data");
                    xlistAdapter.notifyDataSetChanged(stubeans);
                    Logger.e("zxx","stubeans  "+stubeans.get(1).isChick());
                    break;
                case SUCCESS:
                    xlist.setOnItemClickListener(chiocelistenner);
                    menu1.setText(R.string.chioce);
                    if (stubeans!=null){
                        for (AwardStuBean bean:stubeans) {
                            bean.setChick(false);
                        }
                        xlistAdapter.notifyDataSetChanged(stubeans);
                    }
                    break;
            }
        }
    }

    @OnClick(R.id.award_clear_et)
    void setClear(){
        Intent intent=new Intent(mActivity,AwardSearchStuActivity.class);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) stubeans);
        intent.putExtra("tag",false);
//        if (menu2.getVisibility()==View.GONE){
//            intent.putExtra("tag",true);
//        }else{
//            intent.putExtra("tag",false);
//        }
        startActivityForResult(intent,BACK);
    }
    @OnClick(R.id.all_selceter_stu)
    void setAll(){
        for (AwardStuBean bean:stubeans){
            bean.setChick(true);
        }
        xlistAdapter.notifyDataSetChanged(stubeans);
    }
    @OnClick(R.id.firech)
    void setfirech(){
        Intent intent=new Intent(mActivity,AwardSendActivity.class);
        Logger.e("zxx","getCheckedItemCount :"+xlist.getCheckedItemCount());
        if (getSelectorStu()){
            return;
        }
        intent.putExtra("stu_id",selectorStu);
        intent.putExtra("class_id",class_id);
        intent.putExtra("id_more",id_Stu);
        startActivityForResult(intent,SUCCESS);
    }

}
