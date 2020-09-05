package com.yfy.app.integral;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.integral.adapter.HonorStuAdapter;
import com.yfy.app.integral.beans.HonorStu;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class IntegralHonorActivity extends WcfActivity {




    @Bind(R.id.honor_main_list)
    XListView xlist;
    private LoadingDialog loadingDialog;
    private List<HonorStu> honors=new ArrayList<>();
    private List<String> names=new ArrayList<>();


    private int page=0;
    private boolean isloading =false;
    private HonorStuAdapter adapter;
    private String TEA_GET="get_teacher_reward";
    private String MY_CLASS="get_myclass_tea";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_honor_stu);
        loadingDialog=new LoadingDialog(mActivity);
        initSQToolbar();
        initView();
        refresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.UI_REFRESH:
                    refresh(false);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        if (xlist==null)return false;
        isloading=false;
        xlist.stopLoadMore();
        xlist.stopRefresh();
        String name=wcfTask.getName();
//        Log.e("zxx","stu_reward"+result);
        if (StringJudge.isEmpty(result))return false;
        IntegralResult re=gson.fromJson(result,IntegralResult.class);
        if (name.equals("my_class")){
            return false;
        }

        if (name.equals(TagFinal.DELETE_TAG)){
            if (StringJudge.isSuccess(gson,result)){
                refresh(false);
            }else{
                toastShow("删除失败");
            }
            return false;
        }
        if (name.equals(TagFinal.REFRESH)) {
            honors=re.getReward();
            if (re.getReward().size()==TagFinal.TEN_INT){
                xlist.setPullLoadEnable(true);
            }else{
                xlist.setPullLoadEnable(false);
                toastShow(R.string.success_loadmore_end);
            }
            adapter.notifyDataSetChanged(honors);
            return false;
        }
        if (name.equals(TagFinal.REFRESH_MORE)){
            if (re.getReward().size()==TagFinal.TEN_INT){
                xlist.setPullLoadEnable(true);
            }else{
                xlist.setPullLoadEnable(false);
                toastShow(R.string.success_loadmore_end);
            }
            honors.addAll(re.getReward());
            adapter.notifyDataSetChanged(honors);
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        isloading=false;
        if (xlist!=null){
            xlist.stopLoadMore();
            xlist.stopRefresh();
        }
    }
    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("个人成果");
        toolbar.addMenu(1,R.drawable.add);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivityForResult(new Intent(mActivity,AddHonorActivity.class), TagFinal.UI_REFRESH);
            }
        });
    }
    private void initView() {
        View v= LayoutInflater.from(mActivity).inflate(R.layout.integral_detail_head,null);
        adapter=new HonorStuAdapter(mActivity,honors);
        xlist.setAdapter(adapter);
        xlist.addHeaderView(v);
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(false);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                loadMore();
            }
        });

        adapter.setItemClear(new HonorStuAdapter.ItemOnclick() {
            @Override
            public void itemClear(final
                                  HonorStu stu) {
                DialogTools.getInstance().showDialog(
                        mActivity,
                        "提示",
                        "是否删除！",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clear(stu);
                            }
                        }
                );

            }
        });

    }




    public void clear(HonorStu stu){

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                stu.getId()

        };
        WcfTask getTimid = new ParamsTask(params, TagFinal.HONOR_DELETE_ONE_REWARD, TagFinal.DELETE_TAG,loadingDialog);
        execute(getTimid);

    }



    //获取学期列表
    public void refresh(boolean is){
        if (isloading){
            xlist.stopRefresh();
        }
        isloading=true;
        page=0;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                page,
                TagFinal.TEN_INT
        };
        Logger.e("zxx",""+Base.user.getSession_key());
        WcfTask getTimid;
        if (is){
            getTimid = new ParamsTask(params, TagFinal.HONOR_GET_STU_REWARD, TagFinal.REFRESH,loadingDialog);
        }else{
            getTimid = new ParamsTask(params, TagFinal.HONOR_GET_STU_REWARD, TagFinal.REFRESH);
        }

        execute(getTimid);

    }
    //获取学期列表
    public void loadMore(){
        if (isloading){
            xlist.stopRefresh();
        }
        isloading=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ++page,
                TagFinal.TEN_INT
        };
        Logger.e("zxx",""+Base.user.getSession_key());
        WcfTask getTimid = new ParamsTask(params, TagFinal.HONOR_GET_STU_REWARD, TagFinal.REFRESH_MORE);
        execute(getTimid);
    }


}
