package com.yfy.app.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.MyFunRooom;
import com.yfy.app.appointment.bean.OrderRes;
import com.yfy.app.appointment.bean.RoomType;
import com.yfy.app.appointment.bean.Rooms;
import com.yfy.app.bean.TeaBean;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderApplyNewReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderApplicationActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = OrderApplicationActivity.class.getSimpleName();
    @Bind(R.id.order_room_name)
    TextView chioce_room;
    @Bind(R.id.order_apply_data)
    TextView chioce_date;
    @Bind(R.id.order_apply_time)
    TextView chioce_time;

    @Bind(R.id.order_grade_chioce)
    TextView chioce_grade;


    @Bind(R.id.order_checkbox1_layout)
    LinearLayout box1_layout;
    @Bind(R.id.order_checkbox2_layout)
    LinearLayout box2_layout;



    @Bind(R.id.order_is_logis)
    CheckBox is_logis;
    @Bind(R.id.order_is_maintain)
    CheckBox is_maintain;





    @Bind(R.id.order_application_content)
    EditText orderContent;

    @Bind(R.id.order_application_checkbox1)
    EditText deiver_edit;
    @Bind(R.id.order_application_checkbox2)
    EditText maintain_edit;


    private String date="",time_name="";
    private String room_id,roomtype_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_application);
        initSQToolbar();
        initView();
        initTeaDialog();
    }


    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("申请预约");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit1);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isSend()){
//                    submit(orderContent.getText().toString());
                }
            }
        });

    }
    public String room_tag="功能室:",date_tag="日期:",time_tag="时间:",chioce_no="未选择";
    private void initView() {
        chioce_room.setText(room_tag+chioce_no);
        chioce_date.setText(date_tag+chioce_no);
        chioce_time.setText(time_tag+chioce_no);
        chioce_grade.setText(chioce_no);

    }



    public boolean isSend(){
        String content=orderContent.getText().toString();

        if (content==null||content.equals("")){
            toastShow(R.string.order_please_achieve_content);
            return false;
        }

        if (StringJudge.isEmpty(time_name)){
            toastShow("请完善功能室信息！");
            return false;
        }
        if (StringJudge.isEmpty(roomtype_id)){
            toastShow("请完选择等级！");
            return false;
        }
        if (StringJudge.isEmpty(room.getCanchoose()))return false;
        if (room.getCanchoose().equals(TagFinal.TRUE)){
            name.clear();
            for (TeaBean tea:room.getTeachers()){
                name.add(tea.getTeachername());
            }
            cpwListView.setDatas(name);
            closeKeyWord();
            cpwListView.showAtCenter();
        }else {
            submit(orderContent.getText().toString(),"0");
        }

        return true;
    }

    private CPWListView cpwListView;
    private List<String> name=new ArrayList<>();
    private void initTeaDialog(){
        cpwListView = new CPWListView(mActivity);
//        for (TeaBean tea:users){
//            name.add(tea.getTeachername());
//        }
//        cpwListView.setDatas(name);
        cpwListView.setOnPopClickListenner(new CPWListView.OnPopClickListenner() {
            @Override
            public void onClick(int index, String type) {
                submit(orderContent.getText().toString(),room.getTeachers().get(index).getTeacherid());
                cpwListView.dismiss();
            }
        });

    }




    @OnClick(R.id.order_is_maintain)
    void setBox2(){
        if (is_maintain.isChecked()){
            box2_layout.setVisibility(View.VISIBLE);
        }else{
            box2_layout.setVisibility(View.GONE);
        }

    }
    @OnClick(R.id.order_is_logis)
    void setBox1(){
        if (is_logis.isChecked()){
            box1_layout.setVisibility(View.VISIBLE);
        }else{
            box1_layout.setVisibility(View.GONE);
        }

    }




    private Rooms room;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    ArrayList<MyFunRooom> list= data.getParcelableArrayListExtra("selector_room");
                    if (StringJudge.isEmpty(list)){
                        toastShow("未选中时间");
                    }else{
                        StringBuilder name=new StringBuilder();
                        for (MyFunRooom room:list){
                            name.append(",").append(room.getTime_name());
                        }
                        time_name=name.delete(0,1).toString();
                        chioce_time.setText(time_tag+time_name);
//                        getChild(name.delete(0,1).toString(),time  );
                    }
                    room = data.getParcelableExtra("room");
                    room_id=room.getRoom_id();
                    date=data.getStringExtra("date");
                    chioce_room.setText(room_tag+room.getRoom_name());
                    chioce_date.setText(date_tag+date);
                    break;
                case TagFinal.UI_CONTENT:
                    RoomType roomType=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    chioce_grade.setText(roomType.getRoom());
                    roomtype_id=roomType.getId();
                    break;
            }
        }
    }






    @OnClick(R.id.order_user_room_layout)
    void setChioce(){
        Intent intent=new Intent(mActivity,OrderAddChangeActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADD);
    }
    @OnClick(R.id.order_grade_chioce)
    void setChioceGrade(){
        Intent intent=new Intent(mActivity,ChioceTagActivity.class);
        startActivityForResult(intent, TagFinal.UI_CONTENT);
    }




    /**
     * ----------------------------retrofit-----------------------
     */

    //发送预约申请

    public void submit( String content,String tea_id){

        int islogistics=is_maintain.isChecked()?1:0;
        int device=is_logis.isChecked()?1:0;
        String devicewords="";
        if (device==1){
            devicewords=deiver_edit.getText().toString().trim();
        }
        String logisticswords="";
        if (islogistics==1){
            logisticswords=maintain_edit.getText().toString().trim();
        }



        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderApplyNewReq req = new OrderApplyNewReq();
        //获取参数

        req.setUsername(Base.user.getName());
        req.setDate(date);
        req.setRoom_id(ConvertObjtect.getInstance().getInt(room_id));
        req.setTime_name(time_name);

        req.setDevice(device);
        req.setDevicewords(devicewords);
        req.setIslogistics(islogistics);
        req.setLogisticswords(logisticswords);
        req.setTypeid(ConvertObjtect.getInstance().getInt(roomtype_id));
        req.setDescription(StringUtils.stringToUpJson(content));
        req.setTeacherid(ConvertObjtect.getInstance().getInt(tea_id));

        reqBody.orderApplyNewReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_apply_new(reqEnv);
        call.enqueue(this);
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
            if (b.orderApplyNewRes!=null){
                String result=b.orderApplyNewRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                OrderRes re=gson.fromJson(result, OrderRes.class);
                if (re.getResult().equals("true")){
                    toastShow(R.string.success_sned);
                    setResult(RESULT_OK);
                    onPageBack();
                }else{
                    toastShow(re.getError_code());
                }
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
