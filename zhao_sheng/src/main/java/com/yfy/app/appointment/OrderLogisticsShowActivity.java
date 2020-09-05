package com.yfy.app.appointment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.ResInforTwo;
import com.yfy.app.appointment.bean.RoomDetail;
import com.yfy.app.appointment.bean.SameRoom;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.applied_order.OrderGetDetailReq;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.HtmlTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderLogisticsShowActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = OrderLogisticsShowActivity.class.getSimpleName();

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
    @Bind(R.id.score_rating)
    RatingBar score_rating;
    @Bind(R.id.score_rating_content)
    TextView rating_content;


    @Bind(R.id.bottom_layout_user)
    RelativeLayout bottom_layout_user;
    @Bind(R.id.bottom_line_layout)
    LinearLayout bottom_line_layout;


    private String room_tag="功能室:",date_tag="日期:",time_tag="时间:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_logistics_show);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth = dm.widthPixels;
        mHieght = dm.heightPixels;
        getData();
    }
    private void initView(RoomDetail admin){
        Glide.with(mActivity)
                .load(admin.getHeadpic())
                .apply(new RequestOptions().circleCrop())
                .into(top_head);
        head_name.setText(admin.getPerson());
        head_time.setText(admin.getApplydate());
        head_state.setText(admin.getStatus());
        switch (admin.getStatus()){
            case "已通过":
                bottom_layout_user.setVisibility(View.GONE);
                head_state.setTextColor(ColorRgbUtil.getGreen());
                bottom_layout.setVisibility(View.VISIBLE);
                relpy_text.setText("后勤评价："+admin.getLogisticsscore());
                if (admin.getIslogistics().equals("否")){
                    bottom_layout.setVisibility(View.GONE);
                }else{
                    if (admin.getLogisticsscore().equals("未评价")){
                        relpy_text.setText("后勤评价："+admin.getLogisticsscore());
                        score_rating.setVisibility(View.GONE);
                    }else{
                        relpy_text.setText("后勤评价：");
                        score_rating.setVisibility(View.VISIBLE);
                        rating_content.setVisibility(View.VISIBLE);
                        score_rating.setRating(ConvertObjtect.getInstance().getFloat(admin.getLogisticsscore()));
                        rating_content.setText(admin.getLogisticscontent());
                    }

                }
                break;
            case "已拒绝":
                bottom_layout_user.setVisibility(View.GONE);
                head_state.setTextColor(ColorRgbUtil.getBaseColor());
                score_rating.setVisibility(View.GONE);
                rating_content.setVisibility(View.GONE);
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


    }



    /**
     * 动态添加输入框逻辑
     */


    public int mWidth;//屏幕的宽度
    public int mHieght;//屏幕的高度
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

        layout.addView(head);
        layout.addView(textView);

        bottom_line_layout.addView(layout);
    }
    private int item_id;
    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b,TagFinal.ID_TAG)){
            String id=getIntent().getStringExtra(TagFinal.ID_TAG);
            if (StringJudge.isNotEmpty(id)){
                item_id=ConvertObjtect.getInstance().getInt(id);
                getApplyDetail(item_id);
            }
        }
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
                    initView(re.getMy_funcRoom().get(0));
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
