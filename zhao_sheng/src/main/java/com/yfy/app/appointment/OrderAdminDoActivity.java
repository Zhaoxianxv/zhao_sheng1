package com.yfy.app.appointment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.DoItem;
import com.yfy.app.appointment.bean.ResInforTwo;
import com.yfy.app.appointment.bean.RoomDetail;
import com.yfy.app.appointment.bean.SameRoom;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderAdminSetReq;
import com.yfy.app.net.applied_order.OrderGetDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdminDoActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = OrderAdminDoActivity.class.getSimpleName();

    @Bind(R.id.head_pic)
    ImageView top_head;

    @Bind(R.id.head_name)
    TextView head_name;
    @Bind(R.id.head_time)
    TextView head_time;
    @Bind(R.id.head_state)
    TextView head_state;

    @Bind(R.id.room_name)
    TextView room_name;
    @Bind(R.id.room_date)
    TextView room_date;
    @Bind(R.id.room_time)
    TextView room_time;
    @Bind(R.id.room_is_logis)
    TextView is_logis;
    @Bind(R.id.room_logis_content)
    TextView logis_content;
    @Bind(R.id.room_is_maintain)
    TextView is_maintain;
    @Bind(R.id.room_maintain_content)
    TextView maintain_content;
    @Bind(R.id.room_content)
    TextView room_content;

    @Bind(R.id.bottom_layout)
    RelativeLayout bottom_layout;
    @Bind(R.id.boottom_reply_text)
    TextView relpy_text;

    @Bind(R.id.bottom_do_layout)
    RelativeLayout do_layout;
    @Bind(R.id.bottom_layout_user)
    RelativeLayout bottom_layout_user;
    @Bind(R.id.bottom_line_layout)
    LinearLayout bottom_line_layout;

    @Bind(R.id.do_score)
    Button do_score;
    @Bind(R.id.score_rating)
    RatingBar score_rating;
    @Bind(R.id.score_rating_content)
    TextView rating_content;


    private String room_tag="功能室:",date_tag="日期:",time_tag="时间:";


    public int mWidth;//屏幕的宽度
    public int mHieght;//屏幕的高度
    private boolean isMaintain=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_admin_do);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHieght = dm.heightPixels;
        getData();
        initSQToolbar();
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("审批详情");
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initView(RoomDetail admin){
        bottom_line_layout.removeAllViews();
        Glide.with(mActivity)
                .load(admin.getHeadpic())
                .apply(new RequestOptions().circleCrop())
                .into(top_head);
        head_name.setText(admin.getPerson());
        head_time.setText(admin.getApplydate());
        head_state.setText(admin.getStatus());

        switch (admin.getStatus()){
            case "已通过":
                head_state.setTextColor(ColorRgbUtil.getGreen());
                bottom_layout.setVisibility(View.VISIBLE);

                do_score.setVisibility(View.GONE);
                if (admin.getIslogistics().equals("否")){
                    bottom_layout.setVisibility(View.GONE);
                }else{
                    if (admin.getLogisticsscore().equals("未评价")){
                        relpy_text.setText("后勤评价："+admin.getLogisticsscore());
                        do_score.setVisibility(View.GONE);
                        score_rating.setVisibility(View.GONE);
                        rating_content.setVisibility(View.GONE);
                        if (isMaintain){
                            do_score.setVisibility(View.VISIBLE);
                            score_rating.setVisibility(View.GONE);
                            rating_content.setVisibility(View.GONE);
                        }
                    }else{
                        do_score.setVisibility(View.GONE);
                        relpy_text.setText("后勤评价：");
                        score_rating.setVisibility(View.VISIBLE);
                        rating_content.setVisibility(View.VISIBLE);
                        score_rating.setRating(ConvertObjtect.getInstance().getFloat(admin.getLogisticsscore()));
                        rating_content.setText(admin.getLogisticscontent());
                    }

                }
                bottom_layout_user.setVisibility(View.GONE);
                break;
            case "已拒绝":
                head_state.setTextColor(ColorRgbUtil.getBaseColor());
                do_score.setVisibility(View.GONE);
                score_rating.setVisibility(View.GONE);
                rating_content.setVisibility(View.GONE);
                bottom_layout_user.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);
                relpy_text.setText("审核回复："+admin.getReply());
                break;
            case "申请中":
                head_state.setTextColor(ColorRgbUtil.getOrangeRed());
                bottom_layout.setVisibility(View.GONE);
                if (StringJudge.isEmpty(admin.getSame_funcRoom())){
                    bottom_layout_user.setVisibility(View.GONE);
                }else{
                    bottom_layout_user.setVisibility(View.VISIBLE);
                    for (SameRoom sameRoom:admin.getSame_funcRoom()){
                        addUser(sameRoom);
                    }
                }
                break;
        }
        room_name.setText(HtmlTools.getHtmlString(room_tag,admin.getRoom()));
        room_date.setText(HtmlTools.getHtmlString(date_tag,admin.getDate()));
        room_time.setText(HtmlTools.getHtmlString(time_tag,admin.getTime()));
        is_logis.setText(HtmlTools.getHtmlString("设备需求：",admin.getDevice()));
        logis_content.setText(HtmlTools.getHtmlString("设备备注：",admin.getDevicewords()));
        is_maintain.setText(HtmlTools.getHtmlString("后勤需求：",admin.getIslogistics()));
        maintain_content.setText(HtmlTools.getHtmlString("后勤备注：",admin.getLogisticswords()));
        room_content.setText(HtmlTools.getHtmlString("预约事由：",admin.getDescription()));
        if (is){

            if (StringJudge.isEmpty(admin.getOperate())){
                do_layout.setVisibility(View.GONE);
            }else{
                do_layout.setVisibility(View.VISIBLE);
            }
        }else{
            do_layout.setVisibility(View.GONE);
        }

    }


    private int item_id;
    boolean is;
    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b,TagFinal.DELETE_TAG)){
            is = getIntent().getBooleanExtra(TagFinal.DELETE_TAG,false);
        }

        if (StringJudge.isContainsKey(b,TagFinal.ID_TAG)){
            String id=getIntent().getStringExtra(TagFinal.ID_TAG);
            if (StringJudge.isNotEmpty(id)){
                item_id=ConvertObjtect.getInstance().getInt(id);
                getApplyDetail(item_id);
            }
        }

        if (StringJudge.isContainsKey(b,"is_maintain")){
            isMaintain=getIntent().getBooleanExtra("is_maintain",false);

        }


    }



    /**
     * 动态添加输入框逻辑
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void  addUser(final SameRoom sameRoom){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        layoutParams.setMargins(6,2,6,2);
        LinearLayout layout = new LinearLayout(mActivity);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        layout.setGravity(Gravity.CENTER_VERTICAL);





        TextView textView=new TextView(mActivity);
        textView.setTextColor(ColorRgbUtil.getBaseColor());
        textView.setText(sameRoom.getRealname());
        textView.setTextSize(12);
        textView.setPadding(5,5,5,5);
        textView.setWidth((int)(mWidth/5));
        textView.setHeight(50);
        textView.setGravity(Gravity.CENTER);

//        textView.setLayoutParams(layoutParams);

//        textView.setWidth((int)(mWidth/7));
//        textView.setCompoundDrawablePadding(15);
//        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.point), null, null, null);


        ImageView head=new ImageView(mActivity);

        head.setMinimumHeight((int)(mWidth/5));
        head.setMinimumWidth((int)(mWidth/5));

        head.setMaxWidth((int)(mWidth/5));
        head.setMaxHeight((int)(mWidth/5));
        head.setPadding(2,2,2,2);




        Glide.with(mActivity)
                .load(sameRoom.getHeadpic())
                .apply(new RequestOptions().circleCrop())
                .into(head);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_id=ConvertObjtect.getInstance().getInt(sameRoom.getId());
                getApplyDetail(item_id);
            }
        });




        layout.addView(head);
        layout.addView(textView);

        bottom_line_layout.addView(layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_CONTENT:
                    String content=data.getStringExtra(TagFinal.OBJECT_TAG);
                    if (StringJudge.isEmpty(content)){
                        content="";
                        toastShow("未输入文字");
                    }else{
                        isAudit(2,content);
                    }
                    break;
                case TagFinal.UI_REFRESH:
                    getApplyDetail(item_id);
                    break;
            }
        }
    }

    public List<DoItem> list=new ArrayList();


    @OnClick(R.id.bottom_do_button)
    void setButton(){
        Intent intent=new Intent(mActivity,AdminDoActivity.class);
        intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(TagFinal.ID_TAG,item_id );
        startActivityForResult(intent, TagFinal.UI_REFRESH);
    }



    @OnClick(R.id.do_score)
    void setDoScore(){
        Intent intent=new Intent(mActivity,AdminDoScroeActivity.class);
        Bundle b=new Bundle();
        b.putInt("id",item_id);
        intent.putExtras(b);
        startActivityForResult(intent, TagFinal.UI_REFRESH);
    }






    /**
     * ----------------------------retrofit-----------------------
     */

    public void getApplyDetail(int id) {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderGetDetailReq req = new OrderGetDetailReq();
        //获取参数
        req.setId(id);

        reqBody.orderGetDetailReq= req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_get_detail(reqEnv);
        call.enqueue(this);
        showProgressDialog("正在登录");

    }






    public void isAudit(int status,String reply){
        if (status==2){
            if (reply.equals("")){
                toastShow("请填写拒绝理由");
                return;
            }
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderAdminSetReq req = new OrderAdminSetReq();
        //获取参数
        req.setId(item_id);
        req.setStatus(status);
        req.setReply(StringUtils.stringToUpJson(reply));

        reqBody.orderAdminSetReq= req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().order_admin_set(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
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
            if (b.orderGetDetailRes!=null){
                String result=b.orderGetDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResInforTwo re=gson.fromJson(result,ResInforTwo.class);
                if(!StringJudge.isEmpty(re.getMy_funcRoom())){
                    list=re.getMy_funcRoom().get(0).getOperate();
                    initView(re.getMy_funcRoom().get(0));
                }
            }
            if (b.orderAdminSetRes!=null){
                String result=b.orderAdminSetRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResInforTwo re=gson.fromJson(result,ResInforTwo.class);
                if (re.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
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
