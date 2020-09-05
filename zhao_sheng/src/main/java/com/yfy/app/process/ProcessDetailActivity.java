package com.yfy.app.process;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.process.ProcessDelDetailReq;
import com.yfy.app.net.process.ProcessGetDetailReq;
import com.yfy.app.process.bean.ProBeanChild;
import com.yfy.app.process.bean.ProDetailInfo;
import com.yfy.app.process.bean.ProDetailRes;
import com.yfy.app.process.bean.ProLayerTwoData;
import com.yfy.app.process.bean.ProOperation;
import com.yfy.app.process.bean.ProRecordList;
import com.yfy.app.process.bean.ProLayerOneData;
import com.yfy.app.process.bean.ProcessMainBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessDetailActivity extends BaseActivity  {
    private static final String TAG = ProcessDetailActivity.class.getSimpleName();
    private DateBean dateBean;
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
        getDetail();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_detail);
        getData();
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        initDialogList();
        initDialog();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    ProcessMainBean main_bean;
    private boolean is_admin=false;
    private void getData(){
        main_bean=getIntent().getExtras().getParcelable(Base.data);
        is_admin=getIntent().getExtras().getBoolean(Base.type);
        getDetail();
        initView();
        initSQtoobar("详情");

    }


    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);

        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }



    @OnClick(R.id.public_detail_bottom_button)
    void setDoing(){
        if (StringJudge.isEmpty(cpwBeans)){
            return;
        }
        cpwListBeanView.showAtCenter();
    }
    private void initView(){
        bottom_button.setText("处理");
        bottom_button.setTextColor(ColorRgbUtil.getWhite());
//        if (is_admin){
//            bottom_button.setVisibility(View.VISIBLE);
//        }else{
//            bottom_button.setVisibility(View.VISIBLE);
//        }
        top_multi.setVisibility(View.GONE);
        top_multi.clearItem();
        top_tag.setText("审批信息");
        switch (Base.process_type){
            case "":
                break;
                default:
                    detail_tile.setText("流程详情");
                    break;
        }
    }
    private void setdata(ProDetailRes res){
        ProDetailInfo  info=res.getData();

        user_name.setText(info.getUsername());
        GlideTools.chanCircle(mActivity, Base.RETROFIT_URI+info.getHeadPic(), user_head, R.drawable.order_user_name);
        user_tell.setText("");
        user_state.setText(info.getState());
//        switch (info.getDealstate()){
//
//            default:
//                user_state.setTextColor(ColorRgbUtil.getBaseText());
//                break;
//        }
        List<KeyValue> top_jz=new ArrayList<>();
        for (ProLayerOneData oneData:info.getRecordvalue()){
            KeyValue keyValueNew=new KeyValue(StringUtils.getTextJoint("%1$s:\t", oneData.getTitle()), StringUtils.getTextJoint("%1$s\t%2$s", oneData.getDisplaycontent(), oneData.getUnit()));

            keyValueNew.setType(oneData.getValuetype());
            switch (oneData.getValuetype()){
                case "image":
                    keyValueNew.setListValue(StringUtils.listToStringSplitCharacters(oneData.getContent(),","));
                    break;
            }
            if (oneData.getIsshow().equalsIgnoreCase("0")||StringJudge.isEmpty(oneData.getContent())){
            }else{
                top_jz.add(keyValueNew);
            }

            if (StringJudge.isEmpty(oneData.getRecordlinkvalue()))continue;
            for (ProLayerTwoData twoData:oneData.getRecordlinkvalue()){
                KeyValue value_two=new KeyValue(StringUtils.getTextJoint("%1$s:\t", twoData.getTitle()), StringUtils.getTextJoint("%1$s\t%2$s", twoData.getDisplaycontent(), twoData.getUnit()));
                value_two.setType(twoData.getValuetype());
                switch (twoData.getValuetype()){
                    case "image":
                        value_two.setListValue(StringUtils.listToStringSplitCharacters(twoData.getContent(),","));
                        break;
                }
                if (twoData.getIsshow().equalsIgnoreCase("0")||StringJudge.isEmpty(twoData.getContent())){
                }else{
                    top_jz.add(value_two);
                }
                if (StringJudge.isEmpty(twoData.getRecordlinkvalues()))continue;
                for (ProLayerTwoData threeData:twoData.getRecordlinkvalues()){
                    KeyValue value_three=new KeyValue(StringUtils.getTextJoint("%1$s:\t", threeData.getTitle()), StringUtils.getTextJoint("%1$s\t%2$s", threeData.getDisplaycontent(), threeData.getUnit()));
                    value_three.setType(threeData.getValuetype());
                    switch (threeData.getValuetype()){
                        case "image":
                            value_three.setListValue(StringUtils.listToStringSplitCharacters(threeData.getContent(),","));
                            break;
                    }
                    if (threeData.getIsshow().equalsIgnoreCase("0")||StringJudge.isEmpty(threeData.getContent())){
                    }else{
                        top_jz.add(value_three);
                    }
                }
            }
        }

        setFlowLayoutTop(top_jz);
        setFlowLayoutChild(info.getRecords());

        setCPWlListBeanData(info);
    }




    private void setFlowLayoutTop(List<KeyValue> top_jz){

        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (top_flow.getChildCount()!=0){
            top_flow.removeAllViews();
        }
        for (KeyValue bean:top_jz){
            RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.public_detail_top_item,top_flow, false);
            TextView key=layout.findViewById(R.id.seal_detail_key);
            TextView value=layout.findViewById(R.id.seal_detail_value);
            RatingBar ratingBar=layout.findViewById(R.id.seal_detail_value_star);
            LinearLayout linearLayout=layout.findViewById(R.id.public_detail_txt_layout);
            MultiPictureView multi=layout.findViewById(R.id.public_detail_layout_multi);

            key.setTextColor(ColorRgbUtil.getGrayText());
            value.setTextColor(ColorRgbUtil.getBaseText());
            key.setText(bean.getKey());
            switch (bean.getType()){
                case "star":
                    linearLayout.setVisibility(View.VISIBLE);
                    multi.setVisibility(View.GONE);
                    if (bean.getValue().equals("")){
                        ratingBar.setRating(0f);
                    }else{
                        ratingBar.setRating(ConvertObjtect.getInstance().getFloat(bean.getValue()));
                    }
                    ratingBar.setVisibility(View.VISIBLE);
                    value.setVisibility(View.GONE);
                    break;
                case "image":
                    linearLayout.setVisibility(View.GONE);
                    multi.setVisibility(View.VISIBLE);
                    for (String s:bean.getListValue()){

                        Logger.e(s);
                    }
                    multi.clearItem();
                    if (StringJudge.isEmpty(bean.getListValue())){
                        multi.setVisibility(View.GONE);
                    }else{
                        multi.setVisibility(View.VISIBLE);
                        multi.setList(bean.getListValue());
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
                    break;
                default:
                    linearLayout.setVisibility(View.VISIBLE);
                    multi.setVisibility(View.GONE);
                    value.setText(bean.getValue());
                    ratingBar.setVisibility(View.GONE);
                    value.setVisibility(View.VISIBLE);
                    break;
            }
            top_flow.addView(layout);
        }
    }

    public void setFlowLayoutChild(List<ProRecordList> list){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (item_flow.getChildCount()!=0){
            item_flow.removeAllViews();
        }
        for (ProRecordList recordList:list){
            View layout =  mInflater.inflate(R.layout.public_detail_item_item,item_flow, false);
            TextView item_title=layout.findViewById(R.id.public_detail_item_title);
            TextView item_state=layout.findViewById(R.id.public_detail_item_state);
            TextView item_bar=layout.findViewById(R.id.public_detail_item_bar);
            TextView item_reason=layout.findViewById(R.id.public_detail_item_reason);
            ImageView item_head=layout.findViewById(R.id.public_detail_item_head);
            MultiPictureView multi=layout.findViewById(R.id.public_detail_item_multi);


            item_title.setText(recordList.getRealname());
            item_bar.setText(recordList.getState());
            String color_s=StringJudge.isEmpty(recordList.getStatecolor())?"696969":recordList.getStatecolor();
            item_bar.setTextColor(Color.parseColor(StringUtils.stringToGetTextJoint("#%1$s",color_s)));
            item_state.setText(recordList.getAdddate());
            item_reason.setText(recordList.getContent());
            GlideTools.chanCircle(mActivity,Base.RETROFIT_URI+recordList.getHeadPic(), item_head, R.drawable.head_user);
            multi.clearItem();
            if (StringJudge.isEmpty(recordList.getImages())){
                multi.setVisibility(View.GONE);
            }else{
                List<String> images=StringUtils.listToStringSplitCharacters(recordList.getImages(),",");
                multi.setVisibility(View.VISIBLE);
                multi.setList(images);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getDetail();
                    break;
                case TagFinal.UI_SELECT_USER_SINGLE:
                    ProBeanChild childBean=data.getParcelableExtra(Base.data);
                    break;
                case TagFinal.UI_SELECT_USER_MORE:

                    List<ProBeanChild> childBeans=data.getParcelableArrayListExtra(Base.data);
                    break;
                case TagFinal.UI_ADD:
                    getDetail();
                    break;
            }
        }
    }











    private CPWListBeanView cpwListBeanView;
    List<CPWBean> cpwBeans=new ArrayList<>();
    private void setCPWlListBeanData(ProDetailInfo res){
        if (StringJudge.isEmpty(res.getRecordoperation())){
            bottom_button.setVisibility(View.GONE);
            return;
        }else{
            bottom_button.setVisibility(View.VISIBLE);
            cpwBeans.clear();
            for(ProOperation operation:res.getRecordoperation()){
                CPWBean cpwBean =new CPWBean();
                cpwBean.setName(operation.getOperationname());
                cpwBean.setId(operation.getOperationtype());
                cpwBean.setValue(operation.getContenttype());
                cpwBeans.add(cpwBean);
            }
        }
        cpwListBeanView.setDatas(cpwBeans);
    }


    private void initDialogList(){
        cpwListBeanView = new CPWListBeanView(mActivity);
        cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
            @Override
            public void onClick(CPWBean cpwBean,String type) {
                List<String> names=StringUtils.listToStringSplitCharacters(cpwBean.getId(), "_");
                Intent intent;
                switch (names.get(0)) {
                    case "del":
                        cpwListBeanView.dismiss();
                        showDialog("提示","是否删除本条记录","确定");
                        break;
                    case "setstate":
                        cpwListBeanView.dismiss();
                        intent=new Intent(mActivity,ProcessAdminDoingActivity.class);
                        intent.putExtra(Base.data,cpwBean);
                        intent.putExtra(Base.type,names.get(0));
                        intent.putExtra(Base.id,String.valueOf(main_bean.getId()));
                        startActivityForResult(intent,TagFinal.UI_ADD);
                        break;
                    case "edit":
                        cpwListBeanView.dismiss();
                        intent=new Intent(mActivity,ProcessAddSealActivity.class);
                        intent.putExtra(Base.id,String.valueOf((long) main_bean.getId()));
                        startActivityForResult(intent,TagFinal.UI_ADD);
                        break;


                    case "selectuser"://转交
                        if (names.size()==3){
                            cpwListBeanView.dismiss();
                            intent=new Intent(mActivity,ProcessAdminDoingActivity.class);
                            intent.putExtra(Base.data,cpwBean);
                            intent.putExtra(Base.type,names.get(0));
                            intent.putExtra(Base.id,String.valueOf(main_bean.getId()));
                            startActivityForResult(intent,TagFinal.UI_ADD);
                        }else{
                            toast(cpwBean.getName()+"出错");
                        }

                        break;
                    default:
                        break;
                }

            }
        });
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
                        delDetail();
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



    /**
     * ----------------------------retrofit-----------------------
     */



    public void getDetail() {
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessGetDetailReq  req = new ProcessGetDetailReq();
        //获取参数
        req.setId(String.valueOf(main_bean.getId()));
        reqBody.processGetDetailReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_detail(evn);
        Logger.e(evn.toString());
        call.enqueue(this);
        showProgressDialog("");
        Logger.e(evn.toString());
    }


    public void delDetail() {
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessDelDetailReq req = new ProcessDelDetailReq();
        //获取参数
        req.setId(String.valueOf(main_bean.getId()));
        reqBody.processDelDetailReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_del_detail(evn);
        Logger.e(evn.toString());
        call.enqueue(this);
        showProgressDialog("");
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.processGetDetailRes!=null) {
                String result = b.processGetDetailRes.result;
//                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProDetailRes res=gson.fromJson(result,ProDetailRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    setdata(res);
                    scrool_layout.setVisibility(View.VISIBLE);
                    bgtext_view.setVisibility(View.GONE);
                }else{
                    toast(res.getError_code());
                    scrool_layout.setVisibility(View.GONE);
                    bgtext_view.setVisibility(View.VISIBLE);
                }
            }
            if (b.processDelDetailRes!=null) {
                String result = b.processDelDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProDetailRes res=gson.fromJson(result,ProDetailRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toast(res.getError_code());
                }
            }

        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
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

    //
    /**
     * -----
     */
    private long lastClickTime = 0;
    //设置拦截的时间间隔 500毫秒
    private long RESTRICT_TIME = 200;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**
         * 拦截两次时间差小于RESTRICT_TIME
         */
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFrequentlyClick()) {
                //可以在这里给点提示
                //ToastUtils.showShort("不要频繁点击！");
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public boolean isFrequentlyClick() {
        long clickTime = System.currentTimeMillis();
        long value = clickTime - lastClickTime;
        lastClickTime = clickTime;
        return value <= RESTRICT_TIME;
    }
}
