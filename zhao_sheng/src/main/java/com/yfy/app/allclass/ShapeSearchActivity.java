package com.yfy.app.allclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.allclass.adapter.ShapeMainAdapter;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.app.allclass.beans.ShapeMainList;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.tool_textwatcher.EmptyTextWatcher;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.recycerview.EndlessRecyclerOnScrollListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.ClearEditText;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShapeSearchActivity extends WcfActivity {

    private int page = 0;
    private final int classId = 0;
    private final int ismyself = 0;//0为查看所有，1为查看自己的
    private  boolean isRefresh=false;
    private LoadingDialog loadingDialog;
    private String kind_id="";
    @Bind(R.id.shape_search_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.shape_clear_et)
    ClearEditText clear_et;
    private ShapeMainAdapter adapter;
    private List<ShapeMainList> beans=new ArrayList<>();

    private String key_string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_search);
        loadingDialog=new LoadingDialog(mActivity);
        initSQtoolbar();
        initXlist();


    }

    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        super.onPageBack();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");
        toolbar.addMenuText(1,R.string.ok);

        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 1:
                        if (StringJudge.isEmpty(key_string)){
                            toastShow("请输入关键词");
                        }else{
                            refresh(true,key_string);
                        }
                        break;
                }

            }
        });

    }

    private void initXlist() {
        clear_et.addTextChangedListener(new EmptyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {

                key_string=s.toString().trim();
                Logger.e("zxx","  -"+key_string);
            }

        });
        initRecycler();
    }


    public void initRecycler(){

        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMore(key_string);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                0,
                Color.alpha(0)));
//        getResources().getColor(R.color.light_gray01)
        adapter = new ShapeMainAdapter(mActivity,beans);
        recyclerView.setAdapter(adapter);

    }







    private void refresh(boolean is,String search) {
        if (isRefresh){
            return;
        }
        isRefresh=true;
        page=TagFinal.ZERO_INT;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ismyself,
                classId,
                kind_id,
                search,
                page,
                TagFinal.TEN_INT };

        ParamsTask task;
        if (is){
            task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST, TagFinal.REFRESH,loadingDialog);
        }else{
            task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST, TagFinal.REFRESH);
        }
        execute(task);
    }



    private void loadMore(String search) {
        if (isRefresh){

            return;
        }
        isRefresh=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ismyself,
                classId,
                kind_id,
                search,
                ++page,
                PAGE_NUM };
        ParamsTask task = new ParamsTask(params, TagFinal.SHAPE_MAIN_LIST,TagFinal.REFRESH_MORE);
        execute(task);
    }



    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        Logger.e("zxx"," class  "+result);
        ReInfor infor;
        String nameS=wcfTask.getName();
        if (nameS.equals(TagFinal.REFRESH)){
            infor= gson.fromJson(result, ReInfor.class);
            beans.clear();
            beans.addAll(infor.getWbs());
            adapter.setDataList(beans);
            adapter.setLoadState(2);
            return false;
        }
        if (nameS.equals(TagFinal.REFRESH_MORE)){
            infor= gson.fromJson(result, ReInfor.class);
            beans.addAll(infor.getWbs());
            adapter.setDataList(beans);
            if (infor.getWbs().size()== TagFinal.TEN_INT){
                adapter.setLoadState(2);
            }else{
                adapter.setLoadState(3);
                toastShow(R.string.success_loadmore_end);
            }
            return false;
        }


        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

        toastShow(R.string.fail_loadmore);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    refresh(false,key_string);
                    break;

            }
        }
    }

}
