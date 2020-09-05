package com.yfy.app.seal;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.widget.Button;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.seal.SealApplyAddReq;
import com.yfy.app.seal.adapter.SealResetApplyAdapter;
import com.yfy.app.seal.bean.SealApply;
import com.yfy.app.seal.bean.SealMainBean;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.app.seal.bean.SealTable;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SealResetApplyActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = SealResetApplyActivity.class.getSimpleName();

    private SealResetApplyAdapter adapter;
    private DateBean dateBean;

    @Bind(R.id.public_recycler_del)
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_del_view);
        dateBean=new DateBean(DateUtils.getCurrentDateName(),DateUtils.getCurrentDateValue());
        initRecycler();
        initSQtoobar("修改用章时间");
        button.setText("提交");
        initData();

    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);


    }


    private List<SealApply> adapter_list=new ArrayList();
    private SealMainBean bean;
    private void initData(){
        bean=getIntent().getParcelableExtra(Base.data);


        SparseArray<KeyValue> top_data=new SparseArray<>();
        KeyValue one=new KeyValue(bean.getProposername(),Base.user.getSession_key());
        KeyValue two=new KeyValue(bean.getAdddate(),bean.getAdddate());
//        KeyValue five=new KeyValue("产看印章使用情况","");
        KeyValue three=new KeyValue(bean.getApprovalname(),bean.getApprovalid());
        KeyValue four=new KeyValue(bean.getSignetname(),bean.getSignetid());
        top_data.put(0,one );
        top_data.put(1,two );
        top_data.put(2,three );
        top_data.put(3,four);
//        top_data.put(4,five);


        SealApply sealApplytop=new SealApply(TagFinal.TYPE_TOP);
        sealApplytop.setTop_value(top_data);
        sealApplytop.setType("top");
        adapter_list.add(sealApplytop);

        SealApply sealApply_time=new SealApply(TagFinal.TYPE_ITEM);
        SealApply start_time=new SealApply(TagFinal.TYPE_ITEM);
        SealApply end_time=new SealApply(TagFinal.TYPE_ITEM);
        sealApply_time.setType("time");
        start_time.setType("time");
        end_time.setType("time");
        adapter_list.add(new SealApply(TagFinal.TYPE_ITEM, "child",
                new KeyValue(bean.getTypename(),bean.getTypename(),"用章类型:")));

        sealApply_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","用章时间"),
                bean.getStartdate(),
                StringUtils.getTextJoint("%1$s:","用章时间"),"1"));
        start_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","借章时间"),
                bean.getStartdate(),
                StringUtils.getTextJoint("%1$s:","借章时间"),"1"));
        end_time.setDo_seal_time(new KeyValue(
                StringUtils.getTextJoint("请选择%1$s","还章时间"),
                bean.getEnddate(),
                StringUtils.getTextJoint("%1$s:","还章时间"),"2"));


        if(bean.getTypename().equals("盖章")){
            adapter_list.add(sealApply_time);
        }else{
            adapter_list.add(start_time);
            adapter_list.add(end_time);
        }

        for (SealTable table:bean.getTables()){
            List<String> listOne=StringUtils.listToStringSplitCharacters(table.getValuetype(), "_");
            switch (listOne.get(0)){
                case "txt":
                    adapter_list.add(new SealApply(TagFinal.TYPE_PARENT, table.getValuetype(),
                            new KeyValue(
                                    table.getTablename(),
                                    table.getValue(),
                                    StringUtils.getTextJoint("%1$s:",table.getTablename()),
                                    table.getTableid()), true));
                    break;
                case "float":
                    adapter_list.add(new SealApply(TagFinal.TYPE_PARENT, table.getValuetype(),
                            new KeyValue(table.getTablename(),table.getValue(), StringUtils.getTextJoint("%1$s:",table.getTablename()),table.getTableid()),true));
                    break;
                case "longtxt":
                    adapter_list.add(new SealApply(TagFinal.TYPE_REFRECH, table.getValuetype(),
                            new KeyValue(table.getValue(),table.getValue(), StringUtils.getTextJoint("%1$s:",table.getTablename()),table.getTableid()),true));
                    break;
                case "img":
                    KeyValue keyValue=new KeyValue();
                    keyValue.setId(table.getTableid());
                    keyValue.setName(table.getTablename());
                    keyValue.setListValue(StringUtils.listToStringSplitCharacters(table.getValue(),"," ));
                    adapter_list.add(new SealApply(TagFinal.TYPE_IMAGE, table.getValuetype(), keyValue,true));
                    break;
                default:
                    adapter_list.add(new SealApply(TagFinal.TYPE_PARENT, table.getValuetype(),
                            new KeyValue(table.getTablename(),table.getTableid(), StringUtils.getTextJoint("%1$s:",table.getTablename()),table.getTableid()),true));
                    break;
            }
        }
        adapter.setDataList(adapter_list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
    }






    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new SealResetApplyAdapter(this);
        recyclerView.setAdapter(adapter);


    }








    /**
     * ----------------------------retrofit-----------------------
     */



    @OnClick(R.id.public_recycler_del)
    void setTag(){
       addSubmit();
    }
    public void addSubmit() {


        List<SealApply> list=adapter.getDataList();
        List<String> ids=new ArrayList<>();
        List<String> values=new ArrayList<>();

        String start_time="",end_time="";


        for (SealApply apply:list){
            if (apply.isList()){
                if (StringJudge.isEmpty(apply.getDo_seal_time().getValue())){
                    toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getTablename()));
                    return;
                }
                ids.add(apply.getDo_seal_time().getId());
                values.add(apply.getDo_seal_time().getValue());
            }else{

                switch (apply.getType()){
                    case "time":
                        if (StringJudge.isEmpty(apply.getDo_seal_time().getValue())){
                            toastShow(StringUtils.getTextJoint("%1$s数据未完成",apply.getDo_seal_time().getKey()));
                            return;
                        }
                        if (apply.getDo_seal_time().getId().equals("1")){
                            start_time=apply.getDo_seal_time().getValue();
                        }else{
                            end_time=apply.getDo_seal_time().getValue();
                        }
                        break;

                }
            }
        }

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SealApplyAddReq req = new SealApplyAddReq();
        //获取参数



        if (StringJudge.isEmpty(end_time)){

        }else{
            long start=DateUtils.stringToLong(start_time, "yyyy/MM/dd HH:mm");
            long end=DateUtils.stringToLong(end_time, "yyyy/MM/dd HH:mm");
            if (start>=end){
                toastShow(StringUtils.getTextJoint("开始时间大于结束时间"));
                return;
            }
        }
        req.setStartdate(start_time);
        req.setEnddate(end_time);
        req.setId(bean.getId());
        req.setTypeid(ConvertObjtect.getInstance().getInt(bean.getTypeid()));
        req.setSignetid(ConvertObjtect.getInstance().getInt(bean.getSignetid()));
        req.setApprove(ConvertObjtect.getInstance().getInt(bean.getProposerid()));
        req.setTableid(StringUtils.arraysToListString(ids));

        req.setTablecontent(StringUtils.arraysToListString(values));
        reqBody.sealApplyAddReq = req;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().seal_submit(reqEnvelop);
        Logger.e(reqEnvelop.toString());
        call.enqueue(this);
        showProgressDialog("");
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
            if (b.sealApplyAddRes!=null) {
                String result = b.sealApplyAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
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
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
