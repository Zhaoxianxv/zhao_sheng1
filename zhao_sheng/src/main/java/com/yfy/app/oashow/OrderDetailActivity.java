package com.yfy.app.oashow;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.appointment.bean.ResInforTwo;
import com.yfy.app.appointment.bean.RoomDetail;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
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
import com.yfy.final_tag.glide.GlideTools;

import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = OrderDetailActivity.class.getSimpleName();

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




    public String room_tag="功能室:",date_tag="日期:",time_tag="时间:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_detail);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getData();
    }
    private void initView(RoomDetail admin){
        GlideTools.chanCircle(mActivity,admin.getHeadpic(),top_head,R.drawable.head_user);
        head_name.setText(admin.getPerson());
        head_time.setText(admin.getApplydate());
        head_state.setText(admin.getStatus());
        switch (admin.getStatus()){
            case "已通过":
                head_state.setTextColor(ColorRgbUtil.getGreen());
                bottom_layout.setVisibility(View.GONE);
//                relpy_text.setText("后勤详情："+admin.getReply());
                break;
            case "已拒绝":
                head_state.setTextColor(ColorRgbUtil.getBaseColor());
                score_rating.setVisibility(View.GONE);
                rating_content.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);
                relpy_text.setText("审核回复："+admin.getReply());
                break;
            case "申请中":
                head_state.setTextColor(ColorRgbUtil.getOrangeRed());
                bottom_layout.setVisibility(View.GONE);
                break;
            case "已安排":
                head_state.setTextColor(ColorRgbUtil.getBaseColor());
                score_rating.setVisibility(View.GONE);
                rating_content.setVisibility(View.GONE);
                bottom_layout.setVisibility(View.VISIBLE);
                relpy_text.setText("后勤详情："+admin.getReply());
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
    private int item_id;
    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b, TagFinal.ID_TAG)){
            String id=getIntent().getStringExtra(TagFinal.ID_TAG);
            if (StringJudge.isNotEmpty(id)){
                item_id= ConvertObjtect.getInstance().getInt(id);
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
        Logger.e("onFailure"+call.request().headers().toString() );
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }



}
