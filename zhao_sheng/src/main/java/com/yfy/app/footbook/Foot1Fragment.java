package com.yfy.app.footbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.bean.DateBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.footbook.FootBookGetReq;
import com.yfy.app.net.footbook.FootBookPraiseReq;
import com.yfy.final_tag.Base;
import com.yfy.base.fragment.BaseFragment;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.swipe.expandable.XExpandableListView;
import com.yfy.final_tag.ConvertObjtect;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yfyandr on 2017/8/1.
 */

public class Foot1Fragment extends BaseFragment implements Callback<ResEnv> {

    @Bind(R.id.foot_expandListView)
    XExpandableListView xlist;
    @Bind(R.id.top_linearlayout)
    LinearLayout float_view;
    @Bind(R.id.foot_name)
    TextView group_tv;
    private MyListAdapter adapter;
    private List<WeekFoot> group;
    private Gson gson=new Gson();
    private int type;



    private DateBean dateBean;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.foot_tab1_fragment);
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(), true);
        group=new ArrayList<>();
        FootBookMainActivity.setFootBookInterFace(0, foot);
        initView();

    }

    private String session_key="";
    @Override
    public void onResume() {
        super.onResume();
        if (Base.user ==null){
            session_key="gus0";
        }else{
            session_key=Base.user.getSession_key();
        }
        refreshData();
    }



    public void initView(){


        group_tv.setText("");
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        adapter=new MyListAdapter(getActivity(),group);
        adapter.setIsPriase(isPriase);
        xlist.setOnScrollListener(onScrollListener);//setAdapter(adapter)之前
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(new XExpandableListView.IXListViewListener() {
            @Override
            public void onRefresh() {
              refreshData();
            }
            @Override
            public void onLoadMore() {
            }
        });
        xlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent=new Intent(mActivity, FootDetailActivity.class);
                Bundle b=new Bundle();
                b.putString("title",group.get(i).getFoods().get(i1).getName());
                b.putString("url",group.get(i).getFoods().get(i1).getImage());
                b.putString("content",group.get(i).getFoods().get(i1).getContent());
                b.putString("id",group.get(i).getFoods().get(i1).getId());
                b.putString("ispraise",group.get(i).getFoods().get(i1).getIspraise());
                b.putString("praise",group.get(i).getFoods().get(i1).getPraise());
                intent.putExtras(b);
//                startActivity(intent);
                return true;
            }
        });
        xlist.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });


    }
    private void expandAll(ExpandableListView listView, BaseExpandableListAdapter adpater) {
        if (adpater == null) {
            return;
        }
        int groupCount = adapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            if (!listView.isGroupExpanded(i)) {
                listView.expandGroup(i);
            }
        }
    }


    public FootBookMainActivity.FootBookInterFace foot = new FootBookMainActivity.FootBookInterFace() {
        @Override
        public void foot(int i) {
            type = i;
            refreshData();
        }
    };



    private int indicatorGroupHeight;
    private int indicatorGroupId = -1;

    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            final ExpandableListView listView = (ExpandableListView) view;

            int npos = view.pointToPosition(0, 0);
            if (npos == AdapterView.INVALID_POSITION)
                return;
            long pos = listView.getExpandableListPosition(npos);
            int childPos = ExpandableListView.getPackedPositionChild(pos);
            int groupPos = ExpandableListView.getPackedPositionGroup(pos);

            if (childPos == AdapterView.INVALID_POSITION) {
                Logger.e(TagFinal.ZXX, "childPos == AdapterView.INVALID_POSITION");
                View groupView = listView.getChildAt(npos - listView.getFirstVisiblePosition());
                indicatorGroupHeight = groupView.getHeight();

                float_view.setVisibility(View.GONE);
            } else {
                float_view.setVisibility(View.VISIBLE);
            }
            if (groupPos != AdapterView.INVALID_POSITION) {
                float_view.setVisibility(View.VISIBLE);
            }
            if (indicatorGroupHeight == 0) {
                return;
            }

            /**
             * 加刷新时 groupPos == -1
             */
            Logger.e(TagFinal.ZXX, "onScroll: "+groupPos+"  "+ indicatorGroupId);
            if (groupPos != indicatorGroupId) {
                //判断下拉加载
                if (groupPos < 0) {
                }else{
                    group_tv.setText(group.get(groupPos).toString());
                }
                indicatorGroupId = groupPos;
            }

            if (indicatorGroupId == -1)
                return;
            /**
             * calculate point (0,indicatorGroupHeight) 下面是形成往上推出的效果
             */
            int showHeight = indicatorGroupHeight;
            int nEndPos = listView.pointToPosition(0, indicatorGroupHeight);
            if (nEndPos == AdapterView.INVALID_POSITION)
                return;
            long pos2 = listView.getExpandableListPosition(nEndPos);
            int groupPos2 = ExpandableListView.getPackedPositionGroup(pos2);
            if (groupPos2 != indicatorGroupId) {
                View viewNext = listView.getChildAt(nEndPos - listView.getFirstVisiblePosition());
                showHeight = viewNext.getTop();
            }
            // update group position
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) float_view.getLayoutParams();
            layoutParams.topMargin = -(indicatorGroupHeight - showHeight);
            float_view.setLayoutParams(layoutParams);
        }
    };


    private MyListAdapter.IsPriaseTab isPriase=new MyListAdapter.IsPriaseTab() {
        @Override
        public void isPriase(String id, String state) {
            int id_foot= ConvertObjtect.getInstance().getInt(id);
            if (state.equals("true")){
//                ispriase(id_foot,0);
            }else{
                ispriase(id_foot,1);
            }
        }
    };









    /**
     *------------------------------------
     */




    public void ispriase(int id,int state){
        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        FootBookPraiseReq req = new FootBookPraiseReq();
        //获取参数
        req.setSession_key(session_key);
        req.setDate(dateBean.getValue());
        req.setState(state);
        req.setId(String.valueOf(id));

        reqBody.footBookPraiseReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().footbook_praise(env);
        call.enqueue(this);
        showProgressDialog("");
    }


    public void refreshData(){

        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        FootBookGetReq req = new FootBookGetReq();
        //获取参数

        req.setSession_key(session_key);
        req.setDate(dateBean.getValue());
        req.setType(String.valueOf(type));

        reqBody.footBookGetReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().footbook_get(env);
        call.enqueue(this);
        showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        dismissProgressDialog();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        if (xlist!=null){
            xlist.stopRefresh();
            xlist.stopLoadMore();
        }
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.footBookGetRes !=null){
                String result=b.footBookGetRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                FootRes res=gson.fromJson(result, FootRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    group.clear();
                    group=res.getCookbook();
                    adapter.setGroup(group);
                    expandAll(xlist,adapter);
                }else {
                    toastShow(res.getError_code());
                }
            }
            if (b.footBookPraiseRes !=null){
                String result=b.footBookPraiseRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                FootRes res=gson.fromJson(result, FootRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    refreshData();
                }else {
                    toastShow(res.getError_code());
                }
            }

        }else{
            Logger.e(name+"---ResEnv:null");
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        Logger.e("onFailure  :"+call.request().headers().toString());
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        if (xlist!=null){
            xlist.stopRefresh();
            xlist.stopLoadMore();
        }
    }

}
