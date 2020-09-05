package com.yfy.app.delay_service;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.ChoiceStuAdapter;
import com.yfy.app.delay_service.bean.AbsentInfo;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.app.delay_service.bean.EventRes;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayDelStuItemReq;
import com.yfy.app.net.delay_service.DelayGetClassStuListReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.CallPhone;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.recycerview.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventStuActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = EventStuActivity.class.getSimpleName();


    private ChoiceStuAdapter adapter;
    private String bean_id,tea_id;
    private String canedit=TagFinal.FALSE;

    @Bind(R.id.public_bottom_button)
    Button bottom_b;
    @Bind(R.id.public_bottom_b)
    Button bottom_c;
    @Bind(R.id.public_bottom_layout)
    LinearLayout button_layout;


    @Bind(R.id.top_button_one)
    AppCompatTextView button_one;
    @Bind(R.id.top_button_two)
    AppCompatTextView button_two;
    @Bind(R.id.top_button_three)
    AppCompatTextView button_three;

    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_service_stu_main);

        initTop();

        initView();
        getData();
        initDialog();


    }

    private int index_position=0;
    private AbsentInfo bean;
    private void getData(){
        Bundle b=getIntent().getExtras();
        dateBean=b.getParcelable(Base.date);
        index_position=b.getInt("index_position");
        canedit=b.getString(Base.can_edit);
        tea_id=b.getString(Base.id);
        bean=b.getParcelable(Base.data);
        bean_id=bean.getElectiveid();

        initSQtoobar(bean.getElectivename());
        getStu(bean.getElectiveid(), true);

    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra("index_position",index_position );
        setResult(RESULT_OK,intent);
        super.finish();
    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    public DividerGridItemDecoration gray_line=new DividerGridItemDecoration();
    public GridLayoutManager manager;

    public SwipeRefreshLayout.OnRefreshListener lis= new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 刷新数据
            getStu(bean_id,false);
        }
    };
    public SwipeRefreshLayout.OnRefreshListener lis1= new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // 刷新数据
        closeSwipeRefresh();
        }
    };
    private int del_position;
    private EventClass del_bean;
    private void initView() {
        recyclerView =  findViewById(R.id.public_recycler);
        swipeRefreshLayout = findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(lis);
        manager = new GridLayoutManager(mActivity,4, LinearLayoutManager.VERTICAL,false);
        adapter = new ChoiceStuAdapter(mActivity);
        adapter.setTell(new ChoiceStuAdapter.Tell() {
            @Override
            public void tell(String phone) {
                phone_tell=phone;
                showDialog("拨打电话",phone ,"确定" );
            }

            @Override
            public void clear(EventClass item_bean, int index) {
                del_position=index;
                if (canedit.equals(TagFinal.FALSE)){
                    toastShow("当前日期无法考勤");
                    return;
                }
                del_teaDetail(item_bean.getDetail().get(0).getId());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(gray_line);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return select_stu.get(position).getSpan_size();
            }
        });
        recyclerView.setLayoutManager(manager);
    }



    private ConfirmContentWindow confirmContentWindow;
    private void initDialog(){

        confirmContentWindow = new ConfirmContentWindow(mActivity);
        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        PermissionTools.tryTellPhone(mActivity);
                        break;
                }
            }
        });
    }

    private void showDialog(String title,String content,String ok){
        if (confirmContentWindow==null)return;
        confirmContentWindow.setTitle_s(title,content,ok);
        confirmContentWindow.showAtCenter();
    }




    private String phone_tell="";
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


    @OnClick(R.id.public_bottom_button)
    void setbutton(){
        if (canedit.equals(TagFinal.FALSE)){
            toastShow("当前日期无法考勤");
            return;
        }
        if (StringJudge.isEmpty(check_stu)){
            toastShow("未录入学生");
            return;
        }
        ArrayList<EventClass> sstu=new ArrayList<>();
        select_stu=adapter.getDataList();
        boolean isall=true;
        for (EventClass s:select_stu) {
            if (s.isCheck())continue;
            if (s.getStuid()==null)continue;
            if (s.getStuid().equals("$"))continue;
            isall=false;
            sstu.add(s);
        }

        Logger.e(""+sstu.size());
        if (select_stu.get(0).getView_type()==TagFinal.TYPE_PARENT){
            isall=true;
        }


        if (isall){
            ArrayList<EventClass> nums=new ArrayList<>();
            for (EventClass s:select_stu) {
                if (s.getStuid()==null)continue;
                if (s.getStuid().equals("$"))continue;
                if (s.getIsabsent().equals("正常"))nums.add(s);
                if (s.getIsabsent().equals("未考评")) nums.add(s);
            }
            Intent intent=new Intent(mActivity,DelayTeaToClassSetActivity.class);
            Bundle b=new Bundle();
            b.putString(Base.id,tea_id);//date
            b.putString(TagFinal.CLASS_ID, bean_id);//Electiveid
            b.putString(Base.title,"班级考勤");//Electiveid
            b.putParcelable(Base.date,dateBean);//date
            b.putInt(Base.num,nums.size());//date
            intent.putExtras(b);
            startActivityForResult(intent,TagFinal.UI_ADD);
            //班级考勤
        }else {
            Intent intent=new Intent(mActivity,DelayTeaToStuMoreSetActivity.class);
            Bundle b=new Bundle();
            b.putString(TagFinal.CLASS_ID, bean_id);//Electiveid
            b.putParcelableArrayList(TagFinal.GETNOTICENUM,sstu);//ids
            b.putParcelable(Base.date,dateBean);//date
            b.putString(TagFinal.FORBID_TAG,tea_id);//date
            intent.putExtras(b);
            startActivityForResult(intent,TagFinal.UI_ADD);
        }

    }


    @OnClick(R.id.public_bottom_b)
    void setB(){
        if (canedit.equals(TagFinal.FALSE)){
            toastShow("当前日期无法考勤");
            return;
        }
        if (select_stu.size()<=1){
            toastShow("暂未考勤不能添加");
            return;
        }
        Intent intent=new Intent(mActivity,EventAlterActivity.class);
        intent.putExtra(Base.date, dateBean);
        intent.putExtra(TagFinal.ID_TAG, bean_id);
        startActivityForResult(intent,TagFinal.UI_ADD);
    }




    private List<EventClass> stu_s=new ArrayList<>();
    private List<EventClass> check_s=new ArrayList<>();
    private List<EventClass> check_stu=new ArrayList<>();
    private List<EventClass> select_stu=new ArrayList<>();

    private void initChcekData(EventRes res){
        check_s.clear();
        ////            top_one.setText(StringUtils.getTextJoint("实到人数：%1$s",res.getArrive_count()));
////            top_two.setText(StringUtils.getTextJoint("缺席人数：%1$s",res.getAbsent_count()));
        EventClass top=new EventClass();
        top.setView_type(TagFinal.TYPE_PARENT);
        top.setElectiveid(res.getArrive_count());
        top.setElectivename(res.getAbsent_count());
        top.setSpan_size(4);
        check_s.add(top);
        for (EventClass bean:res.getElective_classdetail()){
            bean.setView_type(TagFinal.TYPE_CHILD);
            bean.setSpan_size(4);
            check_s.add(bean);
        }
    }
    private void initStu(EventRes res){
        stu_s.clear();
        check_stu.clear();
        for (EventClass bean:res.getElective_stuetail()){
            bean.setView_type(TagFinal.TYPE_ITEM);
            bean.setSpan_size(4);
            stu_s.add(bean);
            EventClass b=new EventClass();
            b.setView_type(TagFinal.TYPE_TOP);
            b.setSpan_size(1);
            b.setStuname(bean.getStuname());
            b.setStuid(bean.getStuid());
            b.setStuheadpic(bean.getStuheadpic());
            b.setIsabsent(bean.getIsabsent());
            check_stu.add(b);
        }
        EventClass b=new EventClass();
        b.setView_type(TagFinal.TYPE_TOP);
        b.setSpan_size(1);
        b.setStuname("全选");
        b.setStuid("$");
        check_stu.add(b);

//        for (EventClass bean:res.getElective_stuetail()){
//            bean.setView_type(TagFinal.TYPE_TOP);
//            bean.setSpan_size(1);
//            check_stu.add(bean);
//        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    getStu(bean_id,false);
                    break;
            }
        }
    }




    private VectorDrawable one,two,three;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initTop(){
        one = (VectorDrawable) button_one.getBackground();
        two= (VectorDrawable) button_two.getBackground();
        three= (VectorDrawable) button_three.getBackground();

        button_one.setTextColor(ColorRgbUtil.getGrayText());
        button_two.setTextColor(ColorRgbUtil.getGrayText());
        button_three.setTextColor(ColorRgbUtil.getGrayText());

        one.setTint(getResources().getColor( R.color.ee_gray));
        two.setTint(getResources().getColor( R.color.ee_gray));
        three.setTint(getResources().getColor( R.color.ee_gray));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.top_button_one)
    void setOne(){
        one.setTint(ColorRgbUtil.getBaseColor());
        two.setTint(getResources().getColor( R.color.ee_gray));
        three.setTint(getResources().getColor( R.color.ee_gray));

        button_one.setTextColor(ColorRgbUtil.getBaseColor());
        button_two.setTextColor(ColorRgbUtil.getGrayText());
        button_three.setTextColor(ColorRgbUtil.getGrayText());

        select_stu.clear();
        for(EventClass item:stu_s){
            switch (item.getIsabsent()){
                case "未考评":

                    break;
                case "正常":

                    break;

                default:
                    select_stu.add(item);
                    break;
            }
        }
        for(EventClass item:stu_s){
            switch (item.getIsabsent()){
                case "未考评":
                    select_stu.add(item);
                    break;
                case "正常":
                    select_stu.add(item);
                    break;

                default:

                    break;
            }
        }
        adapter.setDataList(select_stu);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        adapter.setDate(dateBean);
        adapter.setTea_id(tea_id);
        swipeRefreshLayout.setOnRefreshListener(lis);

        button_layout.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.top_button_three)
    void setThree(){
        three.setTint(ColorRgbUtil.getBaseColor());
        two.setTint(getResources().getColor( R.color.ee_gray));
        one.setTint(getResources().getColor( R.color.ee_gray));

        button_one.setTextColor(ColorRgbUtil.getGrayText());
        button_two.setTextColor(ColorRgbUtil.getGrayText());
        button_three.setTextColor(ColorRgbUtil.getBaseColor());
        select_stu.clear();
        select_stu.addAll(check_stu);
        adapter.setDataList(select_stu);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        adapter.setDate(dateBean);
        adapter.setTea_id(tea_id);
        swipeRefreshLayout.setOnRefreshListener(lis1);
//            submitNarmal();

        button_layout.setVisibility(View.VISIBLE);
        bottom_b.setVisibility(View.VISIBLE);
        bottom_c.setVisibility(View.VISIBLE);

        bottom_b.setText("考勤");
        bottom_b.setClickable(true);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.top_button_two)
    void setTop_two(){
        one.setTint(getResources().getColor( R.color.ee_gray));
        three.setTint(getResources().getColor( R.color.ee_gray));
        two.setTint(ColorRgbUtil.getBaseColor());

        button_three.setTextColor(ColorRgbUtil.getGrayText());
        button_one.setTextColor(ColorRgbUtil.getGrayText());
        button_two.setTextColor(ColorRgbUtil.getBaseColor());
        select_stu.clear();
        select_stu.addAll(check_s);

        adapter.setDataList(select_stu);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
        swipeRefreshLayout.setOnRefreshListener(lis);
//        adapter.setDate(date);
//        adapter.setTea_id(tea_id);
        button_layout.setVisibility(View.VISIBLE);
        bottom_b.setVisibility(View.VISIBLE);
        bottom_c.setVisibility(View.GONE);
        if (select_stu.size()<=1){
            bottom_b.setText("暂未考勤不能添加");
            bottom_b.setClickable(false);
            toastShow("暂未考勤不能添加");

        }else{
            bottom_b.setText("考勤");
            bottom_b.setClickable(true);
        }
    }
    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        if (StringJudge.isEmpty(phone_tell))return;
        CallPhone.callDirectly(mActivity, phone_tell);
    }
    @PermissionFail(requestCode = TagFinal.CALL_PHONE)
    public void callFail(){
        Toast.makeText(getApplicationContext(), R.string.permission_fail_call, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }










    /**
     * ----------------------------retrofit-----------------------
     */



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }






    private void del_teaDetail(String id){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayDelStuItemReq req = new DelayDelStuItemReq();
        //获取参数

        req.setId(ConvertObjtect.getInstance().getInt(id));
        reqBody.delayDelStuItemReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_del_stu_item(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }


    private void getStu(String id, boolean is){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetClassStuListReq req = new DelayGetClassStuListReq();
        //获取参数
        req.setElectiveid(ConvertObjtect.getInstance().getInt(id));
        req.setTeacherid(ConvertObjtect.getInstance().getInt(tea_id));
        req.setDate(dateBean.getValue());

        reqBody.delayGetClassStuListReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_class_stu(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }




    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (response.code()==500){
            toastShow("数据出差了");
        }
        if (!isActivity())return;
        dismissProgressDialog();
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayGetClassStuListRes !=null) {
                String result = b.delayGetClassStuListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    initStu(res);
                    initChcekData(res);
                    adapter.setCanedit(canedit);
                    button_three.performClick();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.delayDelStuItemRes !=null) {
                String result = b.delayDelStuItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    del_bean=adapter.getDataList().get(del_position);
                    del_bean.setIsabsent("正常");
                    del_bean.getDetail().clear();
                    adapter.notifyItemChanged(del_position, del_bean);
                }else{
                    toastShow(res.getError_code());
                }

            }
        }else{
            Logger.e(name+"---ResEnv:null");
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
        closeSwipeRefresh();
        toast(R.string.fail_do_not);
    }


}
