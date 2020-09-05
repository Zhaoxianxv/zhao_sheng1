package com.yfy.app.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.adpater.ApplyAdapter;
import com.yfy.app.appointment.bean.MyFunRooom;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.appointment.bean.Rooms;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderQueryRoomDayReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAddChangeActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = OrderAddChangeActivity.class.getSimpleName();

    private CustomDate customDate;

    @Bind(R.id.order_apply_address)
    TextView apply_address;


    @Bind(R.id.order_apply_date)
    TextView apply_date;



    private List<MyFunRooom> funs=new ArrayList<>();
    private Rooms room;
    private ApplyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_add_change);
        initSQToolbar();
        customDate=new CustomDate();
        initRecycler();
        apply_date.setText(customDate.toString(""));

    }

    private TextView title;
    private void initSQToolbar() {
        assert  toolbar!=null;
        title=toolbar.setTitle("选择时间");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (StringJudge.isNotEmpty(adapter.getDataList())){
                    Intent intent=new Intent();
                    intent.putParcelableArrayListExtra("selector_room",adapter.getDataList());
                    intent.putExtra("room",room);
                    intent.putExtra("date",customDate.toString());
                    setResult(RESULT_OK,intent);
                    onPageBack();
                }else{
                    toastShow("请选择占用时间");
                }

            }
        });

    }




    private SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.apply_room);
        swipeRefreshLayout =  findViewById(R.id.order_add_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                queryRoom(false);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//                getResources().getColor(R.color.gray)));
        adapter=new ApplyAdapter(this,funs);
        recyclerView.setAdapter(adapter);

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







    @OnClick(R.id.order_apply_date)
    void  settvCurrentMonth(){
        Intent intent = new Intent(mActivity, CalendarActivity.class);
        intent.putExtra("date", customDate);
        startActivityForResult(intent, TagFinal.UI_ADD);
    }

    @OnClick(R.id.order_apply_address)
    void  setAddress(){
        Intent intent = new Intent(mActivity, ChioceRoomActivity.class);
        startActivityForResult(intent, TagFinal.UI_CONTENT);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode){
                case TagFinal.UI_ADD:
                    customDate = (CustomDate) data.getSerializableExtra("date");
                    apply_date.setText(customDate.toString(""));
                    apply_date.setTextColor(ColorRgbUtil.getBaseColor());
                    if (StringJudge.isNotNull(room)){
                        queryRoom(false);
                    }
                    break;
                case TagFinal.UI_CONTENT:
                    room=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    apply_address.setText(room.getRoom_name());
                    apply_address.setTextColor(ColorRgbUtil.getBaseColor());
                    queryRoom(false);
                    break;
            }
        }

    }



//
//    public void setChild(String[] names, FlowLayout flow,int index){
//        LayoutInflater mInflater = LayoutInflater.from(mActivity);
//        if (flow.getChildCount()!=0){
//            flow.removeAllViews();
//        }
//        for (int i=0;i<names.length;i++){
//            final TextView tv = (TextView) mInflater.inflate(R.layout.order_tv,flow, false);
//            tv.setText(names[i]);
//
//            tv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String name=tv.getText().toString();
//                    onclick(name);
//                }
//            });
//            if (i==index){
//                tv.setTextColor(Color.rgb(148,35,40));
//                title.setText(tv.getText().toString());
//                room=room_names.get(index);
//                queryRoom(true,room.getRoom_id());
//                //
//            }else{
//                tv.setTextColor(Color.rgb(195,195,195));
//            }
//            flow.addView(tv);
//        }
//    }
//
//    private void onclick(String name){
//        for (int i=0;i<room_names.size();i++){
//            Rooms room=room_names.get(i);
//            if (name.equals(room.getRoom_name())){
//                setChild(getOfName(room_names),flow,i);
//                break;
//            }
//        }
//    }
//
//
//    public String[] getOfName(List<Rooms> room_names){
//        List<String> list=new ArrayList<>();
//        for (Rooms rooms:room_names) {
//            String class_name=rooms.getRoom_name();
//            boolean is=false;
//            for ( String s:list) {
//                if (s.equals(class_name)) is=true;
//            }
//            if (is) {
//                continue;
//            }else{
//                list.add(class_name);
//            }
//        }
//        String[] se=new String[list.size()];
//        return list.toArray(se);
//    }




    /**
     * ----------------------------retrofit-----------------------
     */





    /**
     * 查询功能室
     */
    public void queryRoom(boolean is){
        if (room==null){
            toastShow("请选着功能室");
            closeSwipeRefresh();
            return;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderQueryRoomDayReq req = new OrderQueryRoomDayReq();
        //获取参数
        req.setRoom_id(room.getRoom_id());
        req.setDate(customDate.toString());

        reqBody.orderQueryRoomDayReq= req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_query_room(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }






    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.orderQueryRoomDayRes!=null){
                String result=b.orderQueryRoomDayRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                funs.clear();
                OrderRes re=gson.fromJson(result,OrderRes.class);
                funs.addAll(re.getMy_funcRoom());
                adapter.setDataList(funs);
                adapter.setLoadState(2);
            }

        }else{

            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


}
