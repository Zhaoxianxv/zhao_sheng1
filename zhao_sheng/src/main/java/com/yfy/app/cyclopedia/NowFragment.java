package com.yfy.app.cyclopedia;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.cyclopedia.adpater.TypeAdapter;
import com.yfy.app.cyclopedia.beans.AncyclopediaList;
import com.yfy.app.cyclopedia.beans.CyclopediaType;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.yfy.base.activity.BaseActivity.PAGE_NUM;


public class NowFragment extends WcfFragment {

    @Bind(R.id.cyc_item_xlist)
    XListView xlist;
    private TypeAdapter adapter;
    private Gson gson;
    private List<AncyclopediaList> list=new ArrayList<>();
    private final String CYCLOPEDIA_TYPE="ancyclopedia_list";
    private int pager=0;
    private  boolean isRefresh=false;
//




    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.cyc_item_layout);
        gson=new Gson();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (list.size()!=0){

        }else{
            refresh();
        }
        Logger.e("zxx","onResume");
    }


    public void refresh(){
        if (isRefresh){
            xlist.stopRefresh();
            return;
        }
        isRefresh=true;
        pager=0;
        Object[] params = new Object[] {

                Base.user.getSession_key()==null?"":Base.user.getSession_key(),
                1,
                "",
                pager,
                PAGE_NUM
               };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE, "refresh");
        execute(gettype);
    }
    public void loadMore(){
        if (isRefresh){
            xlist.startLoadMore();
            return;
        }
        isRefresh=true;
        pager++;
        Object[] params = new Object[] {
                Base.user.getSession_key()==null?"":Base.user.getSession_key(),
                1,
                "",
                pager,
                PAGE_NUM
        };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE, "more");
        execute(gettype);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xliststop();
        String name =wcfTask.getName();
        if (name.equals("refresh")){
            Logger.e("zxx","result++"+result);
            list.clear();
            CyclopediaType type=gson.fromJson(result, CyclopediaType.class);
            list.addAll(type.getAncyclopediaList());
            adapter.notifyDataSetChanged(list);
        }
        if (name.equals("more")){


            CyclopediaType type=gson.fromJson(result, CyclopediaType.class);
            if (type.getAncyclopediaList().size()<PAGE_NUM){
                toastShow(R.string.success_not_more);
            }
            if (type.getAncyclopediaList().size()==0){
                pager--;
            }
            list.addAll(type.getAncyclopediaList());
            adapter.notifyDataSetChanged(list);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        xliststop();
        toastShow(R.string.fail_loadmore);

    }
    public void xliststop(){
        isRefresh=false;
        xlist.stopRefresh();
        xlist.stopLoadMore();
    }




    private XListView.IXListViewListener listenner=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            refresh();
        }
        @Override
        public void onLoadMore() {
            loadMore();
        }
    };

    private void initView() {
        adapter=new TypeAdapter(mActivity,list);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(listenner);
        xlist.setPullLoadEnable(true);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getCount()==i+1){
                    listenner.onLoadMore();
                }else{
                    Intent intent=new Intent(mActivity,CycDetailActivity.class);
                    Bundle b=new Bundle();
                    b.putSerializable("data",list.get(i-1));
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
    }

}
