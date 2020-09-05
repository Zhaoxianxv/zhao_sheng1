package com.yfy.app.maintainnew;

        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.LinearLayout;

        import android.widget.Toast;
        import com.example.zhao_sheng.R;
        import com.yfy.app.maintainnew.adapter.DetailAdapter;
        import com.yfy.app.maintainnew.bean.FlowBean;
        import com.yfy.app.maintainnew.bean.MainBean;
        import com.yfy.app.maintainnew.bean.MainRes;
        import com.yfy.app.net.ReqBody;
        import com.yfy.app.net.ReqEnv;
        import com.yfy.app.net.ResBody;
        import com.yfy.app.net.ResEnv;
        import com.yfy.app.net.RetrofitGenerator;
        import com.yfy.app.net.maintain.MaintainDelItemReq;
        import com.yfy.base.activity.BaseActivity;
        import com.yfy.final_tag.AppLess;
        import com.yfy.final_tag.CallPhone;
        import com.yfy.final_tag.ConvertObjtect;
        import com.yfy.final_tag.dialog.DialogTools;
        import com.yfy.final_tag.StringJudge;
        import com.yfy.final_tag.StringUtils;
        import com.yfy.final_tag.TagFinal;
        import com.yfy.final_tag.Logger;
        import com.yfy.final_tag.permission.PermissionFail;
        import com.yfy.final_tag.permission.PermissionGen;
        import com.yfy.final_tag.permission.PermissionSuccess;

        import java.util.ArrayList;
        import java.util.List;

        import butterknife.Bind;
        import retrofit2.Call;
        import retrofit2.Callback;
        import retrofit2.Response;

public class MaintainNewDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = MaintainNewDetailActivity.class.getSimpleName();
    private MainBean bean;

    @Bind(R.id.maintiain_detail_list)
    RecyclerView listView;
    private DetailAdapter adapter;
    private List<FlowBean> flowBeenS=new ArrayList<>();
    @Bind(R.id.maintiain_detail_bottom)
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_new_detail);
        initSQToolbar();
        getData();
        layout.setVisibility(View.GONE);
    }



    private void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("报修详情");
    }

    public void getData(){

        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle, TagFinal.OBJECT_TAG)){
            bean=bundle.getParcelable(TagFinal.OBJECT_TAG);
        }
        if (bean==null)return;
        if (bean.getMaintain_info()!=null){
            flowBeenS=bean.getMaintain_info();
            intiListView();
        }
    }




    public void intiListView(){
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(this);
        listView.setLayoutManager(mLayoutManager);
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
                                dialogInterface.dismiss();
                            }
                        });
            }
        });
        listView.setAdapter(adapter);

    }




    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        CallPhone.callDirectly(mActivity, bean.getMobile());
    }
    @PermissionFail(requestCode = TagFinal.CALL_PHONE)
    public void callFail(){
        Toast.makeText(getApplicationContext(), R.string.permission_fail_call, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }




    /**
     * ----------------------------retrofit-----------------------
     */



    private void deleteItem(int id){

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainDelItemReq req = new MaintainDelItemReq();
        //获取参数

        req.setId(id);
        reqBody.maintainDelItemReq = req;
        evn.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_del_item(evn);
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
            if (b.maintainDelItemRes!=null) {
                String result = b.maintainDelItemRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                MainRes res=gson.fromJson(result, MainRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toast(R.string.success_do);
                    onPageBack();
                }else{
                    toast(res.getError_code());
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
        toast(R.string.fail_do_not);
        dismissProgressDialog();
    }


    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
