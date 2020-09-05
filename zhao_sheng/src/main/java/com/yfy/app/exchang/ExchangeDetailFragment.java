package com.yfy.app.exchang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.exchang.adapter.MyExchangeAapter;
import com.yfy.app.exchang.bean.ExchangeInfor;
import com.yfy.app.exchang.bean.MyCouyseBean;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.yfy.base.activity.BaseActivity.PAGE_NUM;

/**
 * Created by yfyandr on 2017/8/14.
 */

public class ExchangeDetailFragment extends WcfFragment {
    private static final String TAG = "zxx";
    private int page=0;
    @Bind(R.id.my_exchange_xlist)
    XListView xlist;
    private Gson gson;
    private MyExchangeAapter adapter;
    private List<MyCouyseBean> list_beans =new ArrayList<>();
    private boolean loading =false;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.exchange_exchange_detail_fragment);
        gson=new Gson();
        initView();
        refresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(true);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        Logger.e( "onSuccess: "+result );
        loading=false;
        if (xlist!=null){
            xlist.stopRefresh();
            xlist.stopLoadMore();
        }
        String name=wcfTask.getName();
        ExchangeInfor infor= gson.fromJson(result,ExchangeInfor.class);
        if (name.equals("refresh")){
            if (StringJudge.isEmpty(infor.getSchedulelist())){
                toast(R.string.success_not_more);
            }
            list_beans.clear();
            list_beans=infor.getSchedulelist();

        }
        if (name.equals("loadmore")){
            if (infor.getSchedulelist().size()!=PAGE_NUM){

                toast(R.string.success_not_more);
            }
            list_beans.addAll(infor.getSchedulelist());
        }
        adapter.notifyDataSetChanged(list_beans);
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        loading=false;
        toast(R.string.fail_loadmore);
    }



    public void initView(){

        xlist.setPullLoadEnable(true);
        xlist.setPullLoadEnable(true);

        adapter=new MyExchangeAapter(mActivity,list_beans);
        xlist.setAdapter(adapter);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==adapterView.getCount()){

                }else{
                    Intent intent=new Intent(mActivity,ExchangeDetailActivity.class);
                    intent.putExtra("data",list_beans.get(i-1));
                    intent.putExtra("is",true);
                    startActivity(intent);
                }

            }
        });
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(true);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                refresh(false);
            }
        });
    }


    private final String method="get_my_schedulelist";
    public void refresh(boolean is){
        if (is){
            if (loading){
                xlist.stopRefresh();
                return;
            }
            page=0;
        }else{
            if (loading){
                xlist.stopLoadMore();
                return;
            }
            page++;
        }

        loading=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                100,//100已处理
                page,
                PAGE_NUM,

        };
        ParamsTask able;
        if (is){
            able= new ParamsTask(params, method, "refresh");
        }else{
            able= new ParamsTask(params, method, "loadmore");
        }


        execute(able);
    }
}
