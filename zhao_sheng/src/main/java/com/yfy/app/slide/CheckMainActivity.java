package com.yfy.app.slide;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.check.CheckAddParentReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.SlideRecyclerView;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckMainActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckMainActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_slide_main);

        initSQToolbar();
        initRecycler();

    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("zxx");
        toolbar.addMenuText(TagFinal.ONE_INT,"zxx" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                add();
            }
        });
    }
    private SwipeRefreshLayout swipeRefreshLayout;
    SlideRecyclerView recycler_view_list;

    List<Inventory> mInventories = new ArrayList<>();
    public void initRecycler(){

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                closeSwipeRefresh();
            }
        });

        Inventory inventory;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            inventory = new Inventory();
            inventory.setItemDesc("测试数据" + i);
            inventory.setQuantity(random.nextInt(100000));
            inventory.setItemCode("0120816");
            inventory.setDate("20180219");
            inventory.setVolume(random.nextFloat());
            mInventories.add(inventory);
//            mInventories.add("name");
        }
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        xlist.setLayoutManager(new GridLayoutManager(this, 1));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
//        adapter=new CheckMainAdapter(mActivity);
//        recyclerView.setAdapter(adapter);


        recycler_view_list= (SlideRecyclerView) findViewById(R.id.recycler_view_list);
        recycler_view_list.setLayoutManager(new LinearLayoutManager(
                mActivity,
                LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                mActivity,
                DividerItemDecoration.VERTICAL);
        recycler_view_list.addItemDecoration(itemDecoration);



//        InventoryAdapter adapter= new InventoryAdapter(this,mInventories);
//        recycler_view_list.setAdapter(adapter);

        CheckSlideRecyclerAdapter  adapter= new CheckSlideRecyclerAdapter(mActivity);
        recycler_view_list.setAdapter(adapter);
        adapter.setDataList(mInventories);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);

//        mInventoryAdapter.setOnDeleteClickListener(new InventoryAdapter.OnDeleteClickLister() {
//            @Override
//            public void onDeleteClick(View view, int position) {
//                mInventories.remove(position);
//                mInventoryAdapter.notifyDataSetChanged();
//                recycler_view_list.closeMenu();
//            }
//        });

    }

    public void closeSwipeRefresh(){
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }



    /**
     * ----------------------------retrofit-----------------------
     */


    private void add(){
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckAddParentReq req = new CheckAddParentReq();
        //获取参数
        req.setGroupid(3);
        req.setDate("");
        req.setMobile("a123");
        req.setIlldate("");
        req.setInspecttypeid(2);
        req.setIlltype("2");

        req.setUserid("3");



        List<String> ids=new ArrayList<>();
        List<String> contants=new ArrayList<>();
        ids.add("123");
        ids.add("123");
        contants.add("123");
        contants.add("123");

        req.setIds(StringUtils.arraysToListString(ids));
        req.setContents(StringUtils.arraysToListString(contants));
        reqBody.checkAddParentReq= req;
        evn.body = reqBody;
        Logger.e(evn.toString());
//        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_add_parent(evn);
//        call.enqueue(this);
//        showProgressDialog("正在加载");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkGetTypeRes!=null) {
                String result = b.checkGetTypeRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
            }


        }else{
            List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
            String name=names.get(names.size()-1);
            Logger.e(name+"--------");
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
