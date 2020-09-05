package com.yfy.app.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.TermId;
import com.yfy.app.integral.IntegralEditActivity;
import com.yfy.app.login.bean.UserBaseData;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class UserBaseActivity extends WcfActivity implements TimePickerDialog.TimePickerDialogInterface {

    private static final String TAG = "zxx";
    private String title;
    @Bind(R.id.user_base_xlist)
    XListView xlist;
    private List<UserBaseData> baseDatas=new ArrayList<>();
    private UserBaseAdapter adapter;
    LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_base);
        dialog=new LoadingDialog(mActivity);
        getData();
        initView();
        initSQToolbar();
        refresh();
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (xlist!=null){
            xlist.stopRefresh();
        }
        Logger.e(TAG, "onSuccess: "+result );
        String name=wcfTask.getName();
        if (name.equals(TagFinal.USER_BASE_DATA)){
            TermId infor=gson.fromJson(result,TermId.class);
            if (StringJudge.isEmpty(infor.getStuinfo())){
                toast(R.string.success_not_details);
            }else{
                adapter.notifyDataSetChanged(infor.getStuinfo());
            }
            return false;
        }
        if (name.equals(TagFinal.USER_BASE_UPDATA)){
            if (StringJudge.isSuccess(gson,result)){
                refresh();
            }else{
                toast(R.string.success_not_do);
            }
            return false;
        }



        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (xlist!=null){
            xlist.stopRefresh();
        }
        toast(R.string.fail_do_not);
    }

    //选择时间dialog

    private TimePickerDialog timePickerDialog;
    public void mDialog(){
        if (timePickerDialog==null){
            timePickerDialog=new TimePickerDialog(mActivity);
        }

        timePickerDialog.showDatePickerDialog();

    }
    @Override
    public void positiveListener() {

        int year=timePickerDialog.getYear();
        int month=timePickerDialog.getMonth();
        int day=timePickerDialog.getDay();
        String title_time =year+"/"+month+"/"+day;

        upData(title_time,"-1","-1","-1","-1","-1");
    }
    @Override
    public void negativeListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 3:
                    String name=data.getStringExtra("data");
                    upData("-1","-1",name,"-1","-1","-1");
                    break;
                case 4:
                    String phone=data.getStringExtra("data");
                    upData("-1","-1","-1",phone,"-1","-1");
                    break;
                case 5:
                    String name1=data.getStringExtra("data");
                    upData("-1","-1","-1","-1",name1,"-1");
                    break;
                case 6:
                    String phone1=data.getStringExtra("data");
                    upData("-1","-1","-1","-1","-1",phone1);
                    break;
            }
        }
    }

    public void getData(){
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
    }

    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle(Base.user.getName());

    }
    public void initView(){
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        adapter=new UserBaseAdapter(mActivity,baseDatas);
        adapter.setItemOnc(itemOnc);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh();
            }
        });


    }

    private void refresh() {
        Object[] params = new Object[] {Base.user.getSession_key() };
        ParamsTask refreshTask = new ParamsTask(params, TagFinal.USER_BASE_DATA, TagFinal.USER_BASE_DATA);
        execute(refreshTask);
    }

    private void upData(String birthday,String sex,String name,String phone,String name2,String phone2) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                birthday,
                sex,
                name,
                phone,
                name2,
                phone2,
        };
        ParamsTask refreshTask = new ParamsTask(params, TagFinal.USER_BASE_UPDATA, TagFinal.USER_BASE_UPDATA,dialog);
        execute(refreshTask);
    }
    private UserBaseAdapter.ItemOnc itemOnc=new UserBaseAdapter.ItemOnc() {
        @Override
        public void itemOn(String content, String inputtype,int num) {
            if (inputtype.equals("日期")){
                mDialog();
                return;
            }
            if (inputtype.equals("选择")){
                mChioce();
                return;
            }
            if (inputtype.equals("文字")){
                Intent intent=new Intent(mActivity,IntegralEditActivity.class);
                Bundle b=new Bundle();
                b.putString("edit_type",inputtype);
                if (StringJudge.isEmpty(content)){
                    b.putString("data","");
                }else{
                    b.putString("data",content);
                }
                intent.putExtras(b);
                startActivityForResult(intent,num+1);
            }
        }
    };

    public void mChioce(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle("提示");
        builder.setMessage("更改信息，请选择：");
        builder.setPositiveButton("女", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                upData("-1","女","-1","-1","-1","-1");
            }
        });
        builder.setNegativeButton("男", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                upData("-1","男","-1","-1","-1","-1");
            }
        });
        builder.create().show();
    }


}
