package com.yfy.app.attennew;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.example.zhao_sheng.R;
import com.yfy.app.attennew.adapter.DetailAdapter;
import com.yfy.app.attennew.bean.AttenBean;
import com.yfy.app.attennew.bean.AttenFlow;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.atten.AttenDelItemReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttenNewDetailActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = AttenNewDetailActivity.class.getSimpleName();
    private AttenBean bean;
    @Bind(R.id.maintiain_detail_list)
    RecyclerView listView;
    @Bind(R.id.maintiain_detail_bottom)
    LinearLayout layout;
    private DetailAdapter adapter;
    private List<AttenFlow> flowBeenS=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atten_new_detail);
        initSQToolbar();
        layout.setVisibility(View.GONE);
        getData();
    }

    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("请假详情");
    }

    public void getData(){

        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle, TagFinal.OBJECT_TAG)){
            bean=bundle.getParcelable(TagFinal.OBJECT_TAG);
        }
        if (bean==null)return;
        if (bean.getAttendance_info()!=null){
            flowBeenS=bean.getAttendance_info();
            intiListView();
        }
    }




    public void intiListView(){

        listView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        listView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        adapter=new DetailAdapter(mActivity,flowBeenS,bean,true);
        adapter.setDoAdmin(new DetailAdapter.DoAdmin() {
            @Override
            public void onClickDo(final String id, String tag) {
                DialogTools.getInstance().showDialog(
                        mActivity,
                        "",
                        "是否要撤销申请！",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                deleteItem(ConvertObjtect.getInstance().getInt(id));
                            }
                });
            }
        });
        listView.setAdapter(adapter);

    }







    /**
     * ----------------------------retrofit-----------------------
     */




    private void deleteItem(int id){


        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        AttenDelItemReq req = new AttenDelItemReq();
        //获取参数

        req.setId(id);
        reqBody.attenDelItemReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().atten_del_item(evn);
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
            if (b.attenDelItemRes!=null) {
                String result = b.attenDelItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                if (StringJudge.isSuccess(gson,result)){
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(R.string.success_not_do);
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
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
