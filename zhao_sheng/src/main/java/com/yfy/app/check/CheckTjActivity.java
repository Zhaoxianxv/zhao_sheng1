package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.internal.FlowLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.CheckTjAdapter;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.IllChild;
import com.yfy.app.check.bean.IllGroup;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckTjListReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckTjActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckTjActivity.class.getSimpleName();

    private DateBean dateBean;


    private CheckTjAdapter adapter;
    @Bind(R.id.check_tj_ill_flow)
    FlowLayout flowLayout;
    @Bind(R.id.check_tj_main_name)
    TextView name;
    @OnClick(R.id.check_tj_main_name)
    void setTag(){
        if (StringJudge.isEmpty(illGroups)){
            toastShow("没有数据");
            return;}
        Intent intent=new Intent(mActivity,CheckTjParentActivity.class);
        intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) illGroups);
        intent.putExtra(Base.date,dateBean);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_tj_main);
        getData();
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),true);
        name.setText("年级统计");
        initDateDialog();
        initSQToolbar();
        initRecycler();
        getTjlist(true);

    }

    private String admin_right="",admin_add=TagFinal.FALSE,admin_manage=TagFinal.FALSE;
    public void getData(){
        Intent intent=getIntent();
        admin_right=intent.getStringExtra(Base.content);
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    admin_add=TagFinal.TRUE;
                    break;
                case "admin":
                    admin_manage=TagFinal.TRUE;
                    break;
            }
        }
    }

    private TextView menu_one;
    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("统计");
        menu_one=toolbar.addMenuText(TagFinal.ONE_INT,"" );
        menu_one.setText(dateBean.getName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                date_dialog.showAtBottom();
            }
        });

    }
    private List<IllChild> illChildList=new ArrayList<>();
    public void setChild(List<IllChild> states,FlowLayout flowLayout){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flowLayout.getChildCount()!=0){
            flowLayout.removeAllViews();
        }
        for (IllChild state:states){
            final TextView tv = (TextView) mInflater.inflate(R.layout.check_flowlayout_tv,flowLayout, false);
            tv.setText(StringUtils.getTextJoint("%1$s:%2$s",state.getIlltypename(),state.getIllcount()));
            switch (state.getIlltypename()){
                case "实到人数":
                    if (state.getIllcount().equals(states.get(0).getIllcount())){
                        tv.setTextColor(ColorRgbUtil.getBaseText());
                    }else{
                        tv.setTextColor(ColorRgbUtil.getOrange());
                    }
                    break;
                case "应到人数":
                    tv.setTextColor(ColorRgbUtil.getBaseText());
                    break;
                case "缺勤人数":
                    if (state.getIllcount().equals("0")){
                        tv.setTextColor(ColorRgbUtil.getBaseText());
                    }else{
                        tv.setTextColor(ColorRgbUtil.getOrange());
                    }
                    break;
                default:
                    if (state.getIllcount().equals("0")){
                        tv.setTextColor(ColorRgbUtil.getGrayText());
                    }else{
                        tv.setTextColor(ColorRgbUtil.getMaroon());
                    }
                    break;
            }
            flowLayout.addView(tv);
        }
    }


    private ConfirmDateWindow date_dialog;
    private void initDateDialog(){
        date_dialog = new ConfirmDateWindow(mActivity);
        date_dialog.setOnPopClickListenner(new ConfirmDateWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.set:

                        dateBean.setName(date_dialog.getTimeName());
                        dateBean.setValue(date_dialog.getTimeValue());

                        menu_one.setText(dateBean.getName());
                        getTjlist(false);

                        date_dialog.dismiss();
                        break;
                    case R.id.cancel:
                        date_dialog.dismiss();
                        break;
                }

            }
        });
    }
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
//        //AppBarLayout 展开执行刷新
//        // 设置刷新控件颜色
//        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
//        // 设置下拉刷新
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // 刷新数据
//                getTjlist(false);
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                ColorRgbUtil.getGainsboro()));
        adapter=new CheckTjAdapter(mActivity);
        recyclerView.setAdapter(adapter);




    }

//    public void closeSwipeRefresh(){
//        if (swipeRefreshLayout!=null){
//            swipeRefreshLayout.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//            }, 200);
//        }
//    }



    /**
     * ----------------------------retrofit-----------------------
     */



    public void getTjlist(boolean is) {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckTjListReq req = new CheckTjListReq();
        //获取参数

        req.setDate(dateBean.getValue());
        reqBody.checkTjListReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_tj_list(evn);
        call.enqueue(this);
        if (is)showProgressDialog("正在加载");
        Logger.e(evn.toString());

    }





    private List<IllGroup> illGroups =new ArrayList<>();
    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
//        closeSwipeRefresh();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkTjListRes!=null) {
                String result = b.checkTjListRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                illGroups=res.getStatistics();
                adapter.setDataList(res.getGrouplist());
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                initDataILL(res.getGrouplist());
            }


        }else{
            List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
            String name=names.get(names.size()-1);
            Logger.e(name+"--------");
        }
    }
    private void initDataILL(List<IllGroup> list){
        if (StringJudge.isEmpty(list))return;
        illChildList.clear();
        for(IllChild one:list.get(0).getIllstatistics()){
            IllChild ill=new IllChild(one.getIlltypename(),"0");
            illChildList.add(ill);
        }
        for (IllGroup group:list){
            List<IllChild> illChildrens=group.getIllstatistics();
            for (int i=0;i<illChildrens.size();i++){
                if (illChildrens.get(i).getIllcount().equals("0"))continue;
                if (illChildList.get(i).getIllcount().equals("0")){
                    illChildList.get(i).setIllcount(illChildrens.get(i).getIllcount());
                    continue;
                }
                int num=ConvertObjtect.getInstance().getInt(illChildrens.get(i).getIllcount());
                int num_one=ConvertObjtect.getInstance().getInt(illChildList.get(i).getIllcount());
                illChildList.get(i).setIllcount(String.valueOf(num+num_one));
            }
        }
    }
    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
//        closeSwipeRefresh();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
