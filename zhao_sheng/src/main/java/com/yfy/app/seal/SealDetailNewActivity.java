package com.yfy.app.seal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.yfy.app.net.seal.SealByIdItemReq;
import com.yfy.app.net.seal.SealSetDoReq;
import com.yfy.app.seal.bean.Opear;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealMainState;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.app.seal.bean.SealTable;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealDetailNewActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealDetailNewActivity.class.getSimpleName();
    private DateBean dateBean;
    @Bind(R.id.seal_detail_top_flow_layout)
    FlowLayout top_flow;
    @Bind(R.id.seal_detail_item_flow_layout)
    FlowLayout item_flow;
    @Bind(R.id.seal_detail_head)
    ImageView user_head;
    @Bind(R.id.seal_detail_name)
    TextView user_name;
    @Bind(R.id.seal_detail_state)
    TextView item_state;
    @Bind(R.id.seal_detail_tell)
    TextView user_tell;

    @Bind(R.id.seal_detail_bottom_button)
    Button button;

    @Bind(R.id.seal_detail_top_multi)
    MultiPictureView multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seal_item_detail);
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        initDialogList();
        initSQtoobar("");
        getData();

//        initData();
//        initView();

    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private SealMainBean bean;
    private String bean_id;

    private String do_type;
    private void getData(){
        bean_id=getIntent().getStringExtra(TagFinal.OBJECT_TAG);
        do_type=getIntent().getStringExtra(TagFinal.TYPE_TAG);
        getByid();
//        button.setText("管理审核");
        button.setVisibility(View.GONE);
        switch (do_type){
            case "admin":
                button.setText("操作");
                break;
            case "user":
                button.setText("操作");
                break;

        }
    }

    private TextView menu_one;
    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        menu_one=toolbar.addMenuText(TagFinal.ONE_INT,"修改用章时间" );
        menu_one.setVisibility(View.GONE);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent=new Intent(mActivity,SealResetApplyActivity.class);
//                Intent intent=new Intent(mActivity,SealEditResetApplyActivity.class);
                intent.putExtra(Base.data,bean );
                startActivityForResult(intent, TagFinal.UI_TAG);
            }
        });
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){


        if (bean.getCanedit().equals(TagFinal.TRUE)){
            switch (do_type){
                case "admin":
                    menu_one.setVisibility(View.VISIBLE);
                    break;
                case "user":
                    menu_one.setVisibility(View.GONE);
                    break;
            }
        }else{
            menu_one.setVisibility(View.GONE);
        }

        if (StringJudge.isEmpty(bean.getOpear())){
            button.setVisibility(View.GONE);
        }else{
            button.setVisibility(View.VISIBLE);
            if (bean.getOpear().size()==1){
                if (bean.getOpear().get(0).getOpeartitle().equals("删除")){
                    if (do_type.equals("user")){
                        button.setVisibility(View.VISIBLE);
                    }else{
                        button.setVisibility(View.GONE);
                    }
                }
            }
        }

        setChild(top_flow);
        setItem(bean.getStatuslist(), item_flow);
        user_name.setText(bean.getProposername());
        item_state.setText(bean.getStatus());
        GlideTools.chanCircle(mActivity, bean.getProposerheadpic(),user_head ,R.drawable.head_user );
        switch (bean.getStatus()) {
            case "待审核":
                item_state.setTextColor(ColorRgbUtil.getOrange());
                break;
            case "待盖章":
                item_state.setTextColor(ColorRgbUtil.getOrangeRed());
                break;
            case "待取章":
                item_state.setTextColor(ColorRgbUtil.getOrangeRed());
                break;
            case "已取章":
                item_state.setTextColor(ColorRgbUtil.getOrangeRed());
                break;
            case "未通过":
                item_state.setTextColor(ColorRgbUtil.getMaroon());
                break;
            case "已还章":
                item_state.setTextColor(ColorRgbUtil.getForestGreen());
                break;
            case "已盖章":
                item_state.setTextColor(ColorRgbUtil.getForestGreen());
                break;
        }
    }




    public void setChild( FlowLayout flow){
        List<KeyValue> top_jz=new ArrayList<>();
        top_jz.add(new KeyValue(bean.getProposername(),Base.user.getSession_key(),"申请人:"));
        top_jz.add(new KeyValue(bean.getAdddate(),dateBean.getValue(),"申请时间:"));
        top_jz.add(new KeyValue(bean.getApprovalname(),"","审批人:"));
        top_jz.add(new KeyValue(bean.getSignetname(),"3","印章类型:"));
        top_jz.add(new KeyValue(bean.getTypename(),"5","用章类型:"));
        if (bean.getTypename().equals("盖章")){
            top_jz.add(new KeyValue(bean.getStartdate(),"","用章时间:"));
        }else{
            top_jz.add(new KeyValue(bean.getStartdate(),"","借章时间:"));
            top_jz.add(new KeyValue(bean.getEnddate(),"","还章时间:"));
        }

        for (SealTable table:bean.getTables()){
            String type=table.getValuetype().split("_")[0];
            KeyValue keyValue=new KeyValue();
            keyValue.setType(table.getValuetype());
            switch (type){
                case Base.DATA_TYPE_IMG:
                    keyValue.setValue(table.getValue());
                    top_jz.add(keyValue);
                    break;
                default:
                    keyValue.setKey(table.getValue());
                    keyValue.setValue("");
                    keyValue.setName(StringUtils.getTextJoint("%1$s:", table.getTablename()));
                    top_jz.add(keyValue);
                    break;
            }
        }
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (KeyValue bean:top_jz){
            RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.seal_detail_top_item,flow, false);
            RelativeLayout parent_layout=layout.findViewById(R.id.seal_detail_txt_layout);
            TextView key=layout.findViewById(R.id.seal_detail_key);
            TextView value=layout.findViewById(R.id.seal_detail_value);
//                TextView tv= (TextView) mInflater.inflate(R.layout.check_flowlayout,flow, false);

            MultiPictureView multi=layout.findViewById(R.id.seal_detail_layout_multi);
//                TextView tv= (TextView) mInflater.inflate(R.layout.check_flowlayout,flow, false);

            String type=bean.getType().split("_")[0];
            switch (type){
                case Base.DATA_TYPE_IMG:
                    parent_layout.setVisibility(View.GONE);
                    List<String> photos=StringUtils.listToStringSplitCharacters(bean.getValue(),",");
                    multi.clearItem();
                    if (StringJudge.isEmpty(bean.getValue())){
                        multi.setVisibility(View.GONE);
                    }else{
                        multi.setVisibility(View.VISIBLE);
                        multi.setList(photos);
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
                    multi.setVisibility(View.GONE);
                    parent_layout.setVisibility(View.VISIBLE);
                    key.setText(bean.getName());
                    value.setText(bean.getKey());
                    break;
            }

            flow.addView(layout);
//                flow.addView(tv);
        }
    }
    
    public void setItem(List<SealMainState> list, FlowLayout flow){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (SealMainState bean:list){
            RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.seal_detail_item_item,flow, false);
            TextView time=layout.findViewById(R.id.seal_detail_item_time);
            TextView name=layout.findViewById(R.id.seal_detail_item_user);
            TextView state=layout.findViewById(R.id.seal_detail_item_state);
            TextView reason=layout.findViewById(R.id.seal_detail_item_reason);
            ImageView head=layout.findViewById(R.id.seal_detail_item_head);
            time.setText(bean.getAdddate());
            name.setText(bean.getAddusername());
            state.setText(bean.getState());
            reason.setText(bean.getContent());
            GlideTools.chanCircle(mActivity, bean.getAdduserheadpic(), head, R.drawable.head_user);
            switch (bean.getState()) {
                case "待审核":
                    state.setTextColor(ColorRgbUtil.getOrange());
                    break;
                case "待盖章":
                    state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "待取章":
                    state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "已取章":
                    state.setTextColor(ColorRgbUtil.getOrangeRed());
                    break;
                case "未通过":
                    state.setTextColor(ColorRgbUtil.getMaroon());
                    break;
                case "已还章":
                    state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
                case "已盖章":
                    state.setTextColor(ColorRgbUtil.getForestGreen());
                    break;
            }
            flow.addView(layout);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    getByid();
                    break;
                case TagFinal.UI_TAG:
                    getByid();
                    break;
            }
        }
    }

    @OnClick(R.id.seal_detail_bottom_button)
    void setDo_type(){
        Intent intent;
        switch (do_type){
//            case "master":
////                intent =new Intent(mActivity,SealMasterDoActivity.class);
////                startActivityForResult(intent,TagFinal.UI_REFRESH );
//
//                intent =new Intent(mActivity,SealAdminDoActivity.class);
//                intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) bean.getOpear());
//                intent.putExtra(TagFinal.ID_TAG,bean.getId());
//                startActivityForResult(intent,TagFinal.UI_REFRESH );
//                break;
            case "admin":
                intent =new Intent(mActivity,SealAdminDoActivity.class);
                intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, (ArrayList<? extends Parcelable>) bean.getOpear());
                intent.putExtra(TagFinal.ID_TAG,bean.getId());
                startActivityForResult(intent,TagFinal.UI_REFRESH);
                break;
            case "user"://del item
                if (StringJudge.isEmpty(bean.getOpear()))return;
                setCPWlListBeanData();
                cpwListBeanView.showAtCenter();
                break;

        }
    }


    private CPWListBeanView cpwListBeanView;
    List<CPWBean> cpwBeans=new ArrayList<>();
    private void setCPWlListBeanData(){
        if (StringJudge.isEmpty(bean.getOpear())){
            toast(R.string.success_not_details);
            return;
        }else{
            cpwBeans.clear();
            for(Opear opear:bean.getOpear()){
                CPWBean cpwBean =new CPWBean();
                cpwBean.setName(opear.getOpeartitle());
                cpwBean.setId(opear.getOpearid());
                cpwBeans.add(cpwBean);
            }
        }
        closeKeyWord();
        cpwListBeanView.setDatas(cpwBeans);
        cpwListBeanView.showAtCenter();

    }
    private void initDialogList(){
        cpwListBeanView = new CPWListBeanView(mActivity);
        cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
            @Override
            public void onClick(CPWBean cpwBean,String type) {
                Intent intent;
                switch (cpwBean.getId()){
                    case "1"://通过申请
                        toast(StringUtils.getTextJoint("\"%1$s\":请到审核列表操作",cpwBean.getName()));
                        break;
                    case "100":
                        toast(StringUtils.getTextJoint("\"%1$s\":请到审核列表操作",cpwBean.getName()));
                        break;
                    case "-1"://删除
                        setDoState(cpwBean.getId());
                        break;
                    case "-10"://撤销
                        setDoState(cpwBean.getId());
                        break;
                    case "-100":
                        intent = new Intent(mActivity,SealEditResetApplyActivity.class);
                        intent.putExtra(Base.data,bean );
                        startActivityForResult(intent, TagFinal.UI_TAG);
                        break;
                    default:
                        setDoState(cpwBean.getId());
                        break;
                }

                cpwListBeanView.dismiss();
            }
        });
    }




//    private ConfirmContentWindow confirmContentWindow;
//    private void initConfirmDialog(){
//
//        confirmContentWindow = new ConfirmContentWindow(mActivity);
//        confirmContentWindow.setTitle_s("提示","是否删除此条信息","确定");
//        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
//            @Override
//            public void onClick(View view) {
//
//                switch (view.getId()){
//                    case R.id.pop_dialog_title:
//                        break;
//                    case R.id.pop_dialog_content:
//                        break;
//                    case R.id.pop_dialog_ok:
//                        for (Opear opear:bean.getOpear()){
//                            if (opear.getOpeartitle().equals("删除")) state_value=opear.getOpearid();
//                        }
//                        if (StringJudge.isEmpty(state_value)){
//                            toast("没有权限删除信息");
//                        }else{
//                            setDoState();
//                        }
//
//                        break;
//                }
//            }
//        });
//    }





    /**
     * ----------------------------retrofit-----------------------
     */


    public void setDoState(String opearid) {

        String reason="";
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealSetDoReq req = new SealSetDoReq();
        //获取参数
        req.setId(bean_id);
        req.setOpearid(ConvertObjtect.getInstance().getInt(opearid));
        req.setContent(reason);
        reqBody.sealSetDoReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_set_do(reqEnvelop);
        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
    }

    public void getByid() {
        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealByIdItemReq req = new SealByIdItemReq();
        //获取参数
        req.setId(bean_id);
        reqBody.sealByIdItemReq = req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_get_byid(evn);
        Logger.e(evn.toString());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        if (response.code()==500){
            toastShow("数据出差了");
        }
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.sealByIdItemRes!=null) {
                String result = b.sealByIdItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    bean=res.getSignetlist().get(0);

                    initData();
                }else{
                    toast(res.getError_code());
                }
            }
            if (b.sealSetDoRes!=null) {
                String result = b.sealSetDoRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toast(R.string.success_do);
                    finish();
                }else{
                    toast(res.getError_code());
                }
            }
            if (b.sealApplyAddRes!=null) {
                String result = b.sealApplyAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toast(R.string.success_do);
                    finish();
                }else{
                    toast(R.string.success_not_more);
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
        toast(R.string.fail_do_not);
        dismissProgressDialog();
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
