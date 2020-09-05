package com.yfy.app.process;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.process.adapter.ProcessListSelectMoreAdapter;
import com.yfy.app.process.bean.ProGroupResultRes;
import com.yfy.app.process.bean.ProcessMainRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
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

public class ProcessListSelectMoreActivity extends BaseActivity {
    private static final String TAG = ProcessListSelectMoreActivity.class.getSimpleName();

    public ProcessListSelectMoreAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initSQtoobar();
        initRecycler();
        getData();

    }
    private List<KeyValue> layerTwoDataList;
    private int index_position;
    private String maxNum;
    private void getData(){
        Intent intent=getIntent();
        index_position=intent.getIntExtra(Base.index,0);
        maxNum=intent.getStringExtra(Base.num);
        layerTwoDataList =intent.getParcelableArrayListExtra(Base.data);
        adapter.setDataList(layerTwoDataList);
        adapter.setLoadState(TagFinal.LOADING_END);
    }

    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("选择");
        toolbar.addMenuText(TagFinal.ONE_INT,"确定");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                List<KeyValue> adapterData=adapter.getDataList();
                List<KeyValue> selectData=new ArrayList<>();
                for (KeyValue data:adapterData){
                    if (data.getType().equalsIgnoreCase(TagFinal.TRUE))selectData.add(data);
                }
                if (StringJudge.isEmpty(selectData)) {
                    toast("当前没有选择项");
                    return;
                }
                if (maxNum.equalsIgnoreCase("0")){
                }else{
                    if (selectData.size() > ConvertObjtect.getInstance().getInt(maxNum)) {
                        toast(StringUtils.getTextJoint("限制选择最多%1$s项",maxNum));
                        return;
                    }
                }
                Intent intent=new Intent();
                intent.putExtra(Base.index,index_position);
                intent.putParcelableArrayListExtra(Base.data,  (ArrayList<? extends Parcelable>) selectData);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

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
        adapter=new ProcessListSelectMoreAdapter(mActivity);
        recyclerView.setAdapter(adapter);


    }





    /**
     * ----------------------------retrofit-----------------------
     */

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
