package com.yfy.app.seal;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.seal.SealAllDetailReq;
import com.yfy.app.net.seal.SealGetSealTypeReq;
import com.yfy.app.seal.adapter.SealTagAdapter;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.app.seal.bean.SealState;
import com.yfy.app.seal.bean.SealTag;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.dialog.ConfirmDateWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealTagActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealTagActivity.class.getSimpleName();


    private SealTagAdapter adapter;
    private List<SealTag> depTags=new ArrayList<>();
    private DateBean dateBean;

    @Bind(R.id.seal_admin_top)
    TextView top_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seal_admin_main);
//        do_seal_dateBean=new DateBean(DateUtils.getCurrentTimeName(),DateUtils.getCurrentTimeValue());
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        top_txt.setText("选择印章类型");
        initTeaDialog();
        getSealType();

        initDateDialog();
        initSQtoobar();
        initRecycler();


    }


    private String seal_id="";
    private TextView menu;
    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("印章详情");
        menu=toolbar.addMenuText(TagFinal.ONE_INT,"" );
        menu.setText(dateBean.getName());
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                date_dialog.showAtBottom();
            }
        });

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
                        menu.setText(dateBean.getName());
                        getallDetail();
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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getallDetail();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                ColorRgbUtil.getGainsboro()));
        adapter=new SealTagAdapter(mActivity);
        recyclerView.setAdapter(adapter);


//        depTags.add(new SealTag("zxx", "type", "", do_seal_dateBean.getName(),  do_seal_dateBean.getName()));
//        depTags.add(new SealTag("zxx", "ty",  do_seal_dateBean.getName(), "", ""));




    }

//    private DateBean do_seal_dateBean;
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


    public void getSealType() {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealGetSealTypeReq req = new SealGetSealTypeReq();
        //获取参数
        reqBody.sealGetSealTypeReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_seal_type(reqEnvelop);
//        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
    }

    public void getallDetail() {
        if (StringJudge.isEmpty(seal_id)){
            toastShow("请选择印章类型");
            return;
        }
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealAllDetailReq req = new SealAllDetailReq();
        //获取参数
        req.setSignetid(ConvertObjtect.getInstance().getInt(seal_id));
        req.setDate(dateBean.getValue());
        reqBody.sealAllDetailReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_all(evn);
        Logger.e(evn.toString());
        call.enqueue(this);
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
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealAllDetailRes!=null) {
                String result = b.sealAllDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    depTags.clear();
                    depTags=res.getBorrowlist();
                    if (StringJudge.isEmpty(depTags))toastShow(R.string.success_not_more);
                    adapter.setDataList(depTags);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.sealGetSealTypeRes!=null) {
                String result = b.sealGetSealTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    sealStateList=res.getSignets();
                    setSealStateData(sealStateList);
                }else{
                    toastShow(res.getError_code());
                }
            }
        }else{
            Logger.e(name+"---ResEnv:null");
        }
    }




    private CPWListView cpwListView;
    private List<String> dialog_name_list=new ArrayList<>();
    public List<SealState> sealStateList =new ArrayList<>();
    private void initTeaDialog(){
        cpwListView = new CPWListView(mActivity);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
            @Override
            public void onClick(int index, String type) {
                switch (type){
                    case "seal_type":
                        cpwListView.dismiss();
                        seal_id=sealStateList.get(index).getSignetid();
                        top_txt.setText(sealStateList.get(index).getSignetname());
                        getallDetail();
                        break;
                }
            }
        });
    }



    private void setSealStateData(List<SealState> dialog_beans){
        cpwListView.setType("seal_type");
        if (StringJudge.isEmpty(dialog_beans)){
            toastShow(R.string.success_not_details);
            return;
        }else{
            dialog_name_list.clear();
            for(SealState s:dialog_beans){
                dialog_name_list.add(s.getSignetname());
            }
        }
        closeKeyWord();
        cpwListView.setDatas(dialog_name_list);
        cpwListView.showAtCenter();
    }

    @OnClick(R.id.seal_admin_top)
    void setTop_txt(){
        setSealStateData(sealStateList);
    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        closeSwipeRefresh();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
