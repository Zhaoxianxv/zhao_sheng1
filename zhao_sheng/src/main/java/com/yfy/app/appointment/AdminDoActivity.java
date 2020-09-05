package com.yfy.app.appointment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.zhao_sheng.R;
import com.yfy.app.appointment.adpater.ListViewSingeSelectAdapter;
import com.yfy.app.appointment.bean.DoItem;
import com.yfy.app.appointment.bean.ResInforTwo;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.applied_order.OrderAdminSetReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDoActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = AdminDoActivity.class.getSimpleName();


    @Bind(R.id.edit_edit_text)
    EditText edit_content;
    @Bind(R.id.maintian_do_item)
    MultiPictureView add_mult;
    @Bind(R.id.deal_state_list)
    ListView state_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_admin_do_type);

        initSQToolbar();
        initView();
        getData();

    }

    private int item_id;
    private List<DoItem> datalist=new ArrayList<>();
    public void getData(){
        datalist=getIntent().getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
        item_id=getIntent().getIntExtra(TagFinal.ID_TAG,0);
        adapter.setDatas(datalist);
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("审批");
        toolbar.addMenuText(1,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                String content=edit_content.getText().toString();
                if (StringJudge.isEmpty(content))content="";
                isAudit(content);
            }
        });
    }



    public ListViewSingeSelectAdapter adapter;
    private void initView() {
        edit_content.setText(UserPreferences.getInstance().getContent());
        adapter=new ListViewSingeSelectAdapter(mActivity, datalist);
        state_list.setAdapter(adapter);
        state_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                if(ListView.INVALID_POSITION != position) {
                    status=datalist.get(position).getOpearid();
                }
            }
        });
    }







    /**
     * ----------------------------retrofit-----------------------
     */





    private String status;

    public void isAudit(String reply){
        if (StringJudge.isEmpty(status)){
            toastShow("请选择审批状态");
            return;
        }


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        OrderAdminSetReq req = new OrderAdminSetReq();
        //获取参数
        req.setId(item_id);
        req.setStatus(ConvertObjtect.getInstance().getInt(status));
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
