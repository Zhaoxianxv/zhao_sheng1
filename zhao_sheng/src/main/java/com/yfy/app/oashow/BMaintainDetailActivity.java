package com.yfy.app.oashow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.maintainnew.bean.FlowBean;
import com.yfy.app.maintainnew.bean.MainBean;
import com.yfy.app.maintainnew.bean.MainRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.maintain.BMaintainDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BMaintainDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = BMaintainDetailActivity.class.getSimpleName();
    public String bean_id;
    @Bind(R.id.public_detail_item_flow_layout)
    FlowLayout item_flow;
    @Bind(R.id.public_detail_bottom_button)
    Button bottom_button;
    //    --------top_user-------------
    @Bind(R.id.public_detail_head)
    ImageView user_head;
    @Bind(R.id.public_detail_name)
    TextView user_name;
    @Bind(R.id.public_detail_state)
    TextView user_state;
    @Bind(R.id.public_detail_tell)
    TextView user_tell;
    @Bind(R.id.public_detail_title)
    TextView detail_tile;
    //---------------top detail-----------------
    @Bind(R.id.public_detail_top_flow_layout)
    FlowLayout top_flow;
    @Bind(R.id.public_detail_top_multi)
    MultiPictureView top_multi;
    @Bind(R.id.public_item_tag)
    TextView top_tag;

    //-------------layout-------------
    @Bind(R.id.public_detail_scroll)
    ScrollView scrool_layout;
    @Bind(R.id.public_detail_bg_text)
    AppCompatTextView bgtext_view;
    @OnClick(R.id.public_detail_bg_text)
    void  setBg_Txt(){
        bgtext_view.setVisibility(View.GONE);
        getDetail(bean_id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_detail);
        initView();
        initSQToolbar();
        getData();

    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("报修详情");
    }
    public void getData(){
        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle, TagFinal.ID_TAG)){
            bean_id=bundle.getString(TagFinal.ID_TAG);
        }
        getDetail(bean_id);

    }



    private void initView(){
        bottom_button.setVisibility(View.GONE);
        top_tag.setText("审批信息");
        detail_tile.setText("报修详情");
    }

    private void setdata(MainRes res){
        MainBean bean=res.getMaintains().get(0);
        List<KeyValue> top_jz=new ArrayList<>();
        user_name.setText(bean.getUser_name());
        GlideTools.chanCircle(mActivity, bean.getUser_avatar(), user_head, R.drawable.order_user_name);
        user_tell.setText(bean.getSubmit_time());
        user_state.setText(bean.getDealstate());
        switch (bean.getDealstate()){
            case "申请维修":
                user_state.setTextColor(ColorRgbUtil.getOrange());
                break;
            case "正在维修":
                user_state.setTextColor(ColorRgbUtil.getOrange());
                break;
            case "完成维修":
                user_state.setTextColor(ColorRgbUtil.getForestGreen());
                break;
            default:
                user_state.setTextColor(ColorRgbUtil.getBaseText());
                break;
        }
        top_jz.add(new KeyValue("保修部门:",bean.getDepartment_name()));
        top_jz.add(new KeyValue("保修时间:",bean.getSubmit_time()));
        top_jz.add(new KeyValue("保修地点:",bean.getAddress()));
        top_jz.add(new KeyValue("保修详情:",bean.getContent()));
        setTopFlow(top_jz);
        top_multi.clearItem();
        if (StringJudge.isEmpty(bean.getImage())){
            top_multi.setVisibility(View.GONE);
        }else{
            top_multi.setVisibility(View.VISIBLE);
            top_multi.setList(bean.getImage());
            top_multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                @Override
                public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                    Intent intent=new Intent(mActivity, MultPicShowActivity.class);
                    Bundle b=new Bundle();
                    b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                    b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }

        setItem(bean.getMaintain_info());
    }


    private void setTopFlow(List<KeyValue> top_jz){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (top_flow.getChildCount()!=0){
            top_flow.removeAllViews();
        }
        for (KeyValue bean:top_jz){
            RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.public_detail_top_item,top_flow, false);
            TextView key=layout.findViewById(R.id.seal_detail_key);
            TextView value=layout.findViewById(R.id.seal_detail_value);
            RatingBar ratingBar=layout.findViewById(R.id.seal_detail_value_star);

            key.setTextColor(ColorRgbUtil.getGrayText());
            value.setTextColor(ColorRgbUtil.getBaseText());
            key.setText(bean.getKey());
            switch (bean.getType()){
                case "rating":
                    if (bean.getValue().equals("")){
                        ratingBar.setRating(0f);
                    }else{
                        ratingBar.setRating(ConvertObjtect.getInstance().getFloat(bean.getValue()));
                    }
                    ratingBar.setVisibility(View.VISIBLE);
                    value.setVisibility(View.GONE);
                    break;
                default:
                    value.setText(bean.getValue());
                    ratingBar.setVisibility(View.GONE);
                    value.setVisibility(View.VISIBLE);
                    break;
            }
            top_flow.addView(layout);
        }
    }



    public void setItem(List<FlowBean> list){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (item_flow.getChildCount()!=0){
            item_flow.removeAllViews();
        }
        for (FlowBean bean:list){
            View layout =  mInflater.inflate(R.layout.public_detail_item_item,item_flow, false);
            TextView item_title=layout.findViewById(R.id.public_detail_item_title);
            TextView item_state=layout.findViewById(R.id.public_detail_item_state);
            TextView item_bar=layout.findViewById(R.id.public_detail_item_bar);
            TextView item_reason=layout.findViewById(R.id.public_detail_item_reason);
            ImageView item_head=layout.findViewById(R.id.public_detail_item_head);
            MultiPictureView multi=layout.findViewById(R.id.public_detail_item_multi);


            item_title.setText(bean.getName());
            item_bar.setText(bean.getState());
            item_state.setText(bean.getTime());
            item_reason.setText(bean.getContent());
            GlideTools.chanCircle(mActivity, bean.getAvatar(), item_head, R.drawable.head_user);
            multi.clearItem();
            if (StringJudge.isEmpty(bean.getImage())){
                multi.setVisibility(View.GONE);
            }else{
                multi.setVisibility(View.VISIBLE);
                multi.setList(bean.getImage());
                multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
                    @Override
                    public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                        Intent intent=new Intent(mActivity, MultPicShowActivity.class);
                        Bundle b=new Bundle();
                        b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                        b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
            }

            item_flow.addView(layout);
        }
    }


    /**
     * ----------------------------retrofit-----------------------
     */

    public void getDetail(String id){

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        BMaintainDetailReq request = new BMaintainDetailReq();
        //获取参数
        request.setId(id);

        reqBody.bMaintainDetailReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_get_b_detail(reqEnvelop);
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

            if (b.bMaintainDetailRes !=null){
                String result=b.bMaintainDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                MainRes res=gson.fromJson(result,MainRes.class);
                scrool_layout.setVisibility(View.VISIBLE);
                bgtext_view.setVisibility(View.GONE);
                if (StringJudge.isEmpty(res.getMaintains())){
                    return ;
                }
                setdata(res);
            }
        }else{
            Logger.e(name+"---ResEnv:null");
            scrool_layout.setVisibility(View.GONE);
            bgtext_view.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  :"+call.request().headers().toString());
        toast(R.string.fail_do_not);
        dismissProgressDialog();
        scrool_layout.setVisibility(View.GONE);
        bgtext_view.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
