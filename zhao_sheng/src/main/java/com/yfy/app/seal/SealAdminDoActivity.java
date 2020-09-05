package com.yfy.app.seal;

import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.seal.SealSetDoReq;
import com.yfy.app.seal.bean.Opear;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealAdminDoActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealAdminDoActivity.class.getSimpleName();
    @Bind(R.id.seal_do_state)
    FlowLayout state_flow;
    @Bind(R.id.seal_do_reason_edit)
    EditText reason_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seal_master_do);
        initSQtoobar("印章管理");
        getData();
    }


    private void initSQtoobar(String title) {
        assert toolbar != null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT, "确定");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                setDoState();
            }
        });
    }


    private String item_id;
    private ArrayList<Opear> opear_list=new ArrayList<>();
    private List<KeyValue> do_states=new ArrayList<>();
    private String state_value="";
    private void getData(){
        opear_list=getIntent().getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
        item_id=getIntent().getStringExtra(TagFinal.ID_TAG);
        initData();
    }

    private void initData(){
        do_states.clear();
        for (Opear opear:opear_list){
//            if (opear.getOpeartitle().equals("删除"))continue;
            do_states.add(new KeyValue(TagFinal.FALSE,opear.getOpearid(),opear.getOpeartitle()));
        }
        setChild(do_states, state_flow);
    }

    public void setChild(List<KeyValue> list, FlowLayout flow){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (final KeyValue bean:list){
            RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.seal_do_item,flow, false);
            layout=layout.findViewById(R.id.seal_do_layout);
            TextView key=layout.findViewById(R.id.seal_do_key);
            final AppCompatImageView value=layout.findViewById(R.id.seal_do_value);
            key.setText(bean.getName());
            value.setVisibility(View.GONE);
            if (bean.getKey().equalsIgnoreCase(TagFinal.TRUE)){
                value.setVisibility(View.VISIBLE);
            }else{
                value.setVisibility(View.GONE);
            }
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initCrate(bean);

                }
            });
            flow.addView(layout);
        }
    }


    private void initCrate(KeyValue bean){
        state_value=bean.getValue();
        for (KeyValue value:do_states){
            value.setKey(TagFinal.FALSE);
            if (bean.getValue().equals(value.getValue())){
                value.setKey(TagFinal.TRUE);
            }
        }
        setChild(do_states, state_flow);
    }


    /**
     * ----------------------------retrofit-----------------------
     */



    public void setDoState() {
        if (StringJudge.isEmpty(state_value)){
            toastShow("未选择审核状态");
            return;
        }
        String reason=reason_edit.getText().toString().trim();
        if (StringJudge.isEmpty(reason)){
            reason="";
        }
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealSetDoReq req = new SealSetDoReq();
        //获取参数
        req.setId(item_id);
        req.setOpearid(ConvertObjtect.getInstance().getInt(state_value));
        req.setContent(reason);
        reqBody.sealSetDoReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_set_do(reqEnvelop);
        Logger.e(reqEnvelop.toString());
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
            if (b.sealSetDoRes!=null) {
                String result = b.sealSetDoRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
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
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}