package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.IntegralGrowAdapter;
import com.yfy.app.integral.beans.IntegralGrow;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class IntegralFamilyActivity extends WcfActivity {


    @Bind(R.id.integral_grow_xlist)
    XListView xListView;


    private final String GROW="user_info_contet";

    private final String FAMILY="user_home_contet";
    private final String FAMILY_UP="user_home_update";
    private final int TERM=2;
    private final int UPDATA=1;

    private String content;
    private boolean isload=false;
    private int data_id;
    private int term_id= ConvertObjtect.getInstance().getInt(UserPreferences.getInstance().getTermId());
    private String term_name= UserPreferences.getInstance().getTermName();
    private IntegralGrowAdapter adapter;
    private List<IntegralGrow> grows=new ArrayList<>();

    private LoadingDialog dialog;
    TextView mune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_grow);
        initSQToolbar();
        initView();
        refresh(term_id);
    }

    private void initSQToolbar() {
        dialog=new LoadingDialog(mActivity);
        assert toolbar!=null;
        toolbar.setTitle("家庭数据");
        mune=toolbar.addMenuText(1,term_name);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,ChangeTermActivity.class);
                startActivityForResult(intent,TERM);
            }
        });
    }


    private void initView() {
        adapter=new IntegralGrowAdapter(mActivity,grows);
        xListView.setAdapter(adapter);
        xListView.setPullLoadEnable(false);
        xListView.setXListViewListener(ixListViewListener);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int position=i-1;
                Intent intent=new Intent(mActivity,IntegralEditActivity.class);
                Bundle b=new Bundle();
                b.putString("edit_type",grows.get(position).getInputtype());
                if (StringJudge.isEmpty(content)){
                    b.putString("data",grows.get(position).getResult());
                }else{
                    b.putString("data",content);
                }

                b.putString("list",grows.get(position).getOption());
                data_id=ConvertObjtect.getInstance().getInt(grows.get(position).getTagid());
                intent.putExtras(b);
                startActivityForResult(intent,UPDATA);
            }
        });
    }


    private XListView.IXListViewListener ixListViewListener=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            refresh(term_id);

        }

        @Override
        public void onLoadMore() {

        }
    };


    private void refresh(int data_id){
        if (isload){
            return;
        }
        isload=true;

        Object[] params=new Object[]{
                Base.user.getSession_key(),
                data_id
        };
        ParamsTask refresh=new ParamsTask(params,FAMILY,FAMILY);
        execute(refresh);
    }

    public void upData(String content){

        if (StringJudge.isNull(data_id)){
            toastShow("哎呀！数据出问题了？");
            return;
        }
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                data_id,
                term_id,
                content,
        };
        ParamsTask refresh=new ParamsTask(params,FAMILY_UP,FAMILY_UP,dialog);
        execute(refresh);
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        Logger.e("zxx","family   "+result);
        String name =wcfTask.getName();
        IntegralResult infor=gson.fromJson(result, IntegralResult.class);
        if (name.equals(FAMILY_UP)){
            if (infor.getResult().equals("true")){
                content=null;
                refresh(term_id);
            }else{
                toastShow(""+infor.getError_code());
            }
            return false;
        }

        if (name.equals(FAMILY)){
            if (xListView==null){
                return false;
            }
            xListView.stopRefresh();
            isload=false;

            grows=infor.getResults();
            adapter.notifyDataSetChanged(grows);

            return false;
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (xListView==null){
            return ;
        }
        xListView.stopRefresh();
        isload=false;
        toastShow("网络异常");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case UPDATA:
                    content=data.getStringExtra("data");
                    upData(content);
                    break;
                case TERM:
                    term_id=ConvertObjtect.getInstance().getInt(data.getStringExtra("data_id"));
                    refresh(term_id);
                    mune.setText(data.getStringExtra("data_name"));
                    break;
            }
        }
    }
}
