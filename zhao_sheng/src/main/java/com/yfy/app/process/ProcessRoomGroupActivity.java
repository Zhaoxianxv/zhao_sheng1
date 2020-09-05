package com.yfy.app.process;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.process.ProcessGetGroupReq;
import com.yfy.app.process.adapter.ProcessRoomGroupAdapter;
import com.yfy.app.process.bean.ProGroupResult;
import com.yfy.app.process.bean.ProGroupResultRes;
import com.yfy.app.process.bean.ProLayerOneData;
import com.yfy.app.process.bean.ProLayerTwoData;
import com.yfy.app.process.bean.ProcessMainRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ProcessRoomGroupActivity extends BaseActivity {
    private static final String TAG = ProcessRoomGroupActivity.class.getSimpleName();



    public ProcessRoomGroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initSQtoobar();

        initRecycler();
        getData();

    }
    private List<ProLayerTwoData> layerTwoDataList;
    private int index_position;
    private void getData(){
        Intent intent=getIntent();
        ProLayerOneData proLayerOneData=intent.getParcelableExtra(Base.data);
        index_position=intent.getIntExtra(Base.index,0);
        layerTwoDataList =proLayerOneData.getRecordlinkvalue();
        initViews(layerTwoDataList);
    }

    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("选择");
        toolbar.addMenuText(TagFinal.ONE_INT,"确定");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                List<ProLayerTwoData> adapterData=adapter.getDataList();
                for (ProLayerTwoData data:adapterData){
                    if (data.getCannull().equalsIgnoreCase("0")){
                        String content=data.getKeyValue().getRight_value();
                        if (StringJudge.isEmpty(content)){
                            toast(StringUtils.getTextJoint("%1$s为必填项",data.getTitle()));
                            return ;
                        }
                    }
                }
                Intent intent=new Intent();
                intent.putExtra(Base.index,index_position);
                intent.putParcelableArrayListExtra(Base.data,  (ArrayList<? extends Parcelable>) adapterData);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }



    private void  initViews(List<ProLayerTwoData> layerTwoDataList){
        List<ProLayerTwoData> showTwoData=new ArrayList<>();
        for (ProLayerTwoData twoData:layerTwoDataList){
            if (twoData.getLinkstate()==8&&StringJudge.isEmpty(twoData.getKeyValue().getValue()))continue;

            setViewType(twoData);


            showTwoData.add(twoData);
        }
        adapter.setDataList(showTwoData);
    }


    private void setViewType(ProLayerTwoData twoData){

        List<String> names=StringUtils.listToStringSplitCharacters(twoData.getValuetype(), "_");
        switch (names.get(0)){
            case "datetime":
                twoData.setView_type(TagFinal.TYPE_DATE);
                break;
            case "select":
                twoData.setView_type(TagFinal.TYPE_SELECT_SINGLE);
                break;
            case "list":
                twoData.setView_type(TagFinal.TYPE_LIST_SINGLE);
                break;
            case "selectuser":
                twoData.setView_type(TagFinal.TYPE_SELECT_USER);
                break;
            case "longtext":
                twoData.setView_type(TagFinal.TYPE_REFRECH);
                break;
            case "image":
                twoData.setView_type(TagFinal.TYPE_IMAGE);
                break;
            case "check":
                twoData.setView_type(TagFinal.TYPE_CHECK);
                break;
            case "group":
                twoData.setView_type(TagFinal.TYPE_GROUP);
                break;
            case "decimal":
            case "text":
            case "int":
                twoData.setView_type(TagFinal.TYPE_PARENT);
                break;
            default:
                twoData.setView_type(TagFinal.TYPE_DATE);
                break;
        }
    }

    public RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView =  findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                ColorRgbUtil.getGainsboro()));
        adapter=new ProcessRoomGroupAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setSealChoice(new ProcessRoomGroupAdapter.SealChoice() {
            @Override
            public void refresh(ProLayerTwoData bean, int position_index,List<String> ids,List<String> values) {
                getGroupResult(ids,values);
            }
        });

    }

    private void initEigth(ProGroupResult eigth){

        adapter.removeItemData(String.valueOf((long) eigth.getId()));

        int i=0;
        for (ProLayerTwoData twoData:layerTwoDataList){
            if (twoData.getLinkstate()==8){
                twoData.setId(eigth.getId());
                twoData.setStep(eigth.getId());
                twoData.setHeadPic(eigth.getHeadPic());
                twoData.setContent(eigth.getContent());
                twoData.setValuetype(eigth.getValuetype());
                twoData.setAdddate(eigth.getAdddate());
                twoData.setTitle(eigth.getTitle());
                twoData.setCannull(eigth.getCannull());
                twoData.setUnit(eigth.getUnit());
                twoData.setMin(eigth.getMin());
                twoData.setMax(eigth.getMax());
                twoData.setSelects(eigth.getSelects());
                twoData.setIsshow(eigth.getIsshow());
                twoData.setRecordlinkvalues(eigth.getRecordlinkvalue());
                twoData.setRecordvaluearray(eigth.getRecordvaluearray());
                KeyValue key = twoData.getKeyValue();
                key.setValue("");
                twoData.setIs_select(false);
                setViewType(twoData);

                List<ProLayerTwoData> dataList=new ArrayList<>();
                dataList.add(twoData);
                adapter.addItemData(i,dataList);
            }
            i++;
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_SELECT_USER_MORE:
                    int adapter_position=data.getIntExtra(Base.index,0);
                    List<KeyValue> adapterData=data.getParcelableArrayListExtra(Base.data);
                    ProLayerTwoData bean=adapter.getDataList().get(adapter_position);
                    KeyValue adapterKeyValue = bean.getKeyValue();
                    Logger.e(String.valueOf(adapterData.size()));
                    if (adapterData.size()==1){
                        adapterKeyValue.setRight_value(adapterData.get(0).getValue());
                        adapterKeyValue.setRight_name(adapterData.get(0).getName());
                    }else{
                        List<String> values=new ArrayList<>();
                        for (KeyValue k:adapterData){
                            values.add(k.getValue());
                        }
                        Logger.e(StringUtils.stringToArraysGetString(values,","));
                        adapterKeyValue.setRight_value(StringUtils.stringToArraysGetString(values,","));
                        adapterKeyValue.setRight_name(StringUtils.getTextJoint("当前已选择%1$d项",values.size()));
                    }

                    bean.setIs_select(true);
                    adapter.notifyItemChanged(adapter_position,bean);
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */

    public void getGroupResult(List<String> ids,List<String> values){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        ProcessGetGroupReq req = new ProcessGetGroupReq();
        //获取参数
        req.setSession_key(Base.user.getSession_key());
        req.setType(Base.process_type);
        req.setId(StringUtils.arraysToListString(ids));
        req.setValue(StringUtils.arraysToListString(values));

        reqBody.processGetGroupReq=req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_group(reqEnv);
        call.enqueue(this);
        Logger.e(reqEnv.toString());
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
            if (b.processGetUserListRes !=null){
                String result=b.processGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProcessMainRes res=gson.fromJson(result,ProcessMainRes.class);

            }
            if (b.processGetGroupRes !=null){
                String result=b.processGetGroupRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ProGroupResultRes res=gson.fromJson(result,ProGroupResultRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    initEigth(res.getData());
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
