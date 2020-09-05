package com.yfy.app.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.login.bean.TermBean.TermEntity;
import com.yfy.app.login.bean.TermBean;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.base.UserGetTermListReq;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTermActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = ChangeTermActivity.class.getSimpleName();
    private XlistLefttTxtAdapter adapter;
    private List<TermEntity> terms=new ArrayList<>();
    private List<String> names=new ArrayList<>();
    @Bind(R.id.integral_term_change_list)
    XListView xlist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_change_term);
        initSQToolbar();
        initView();
        getTerm();
    }

    private void initView() {
        xlist.setPullLoadEnable(false);
        adapter=new XlistLefttTxtAdapter(mActivity,names);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(xlistener);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("data_id",terms.get(i-1).getId());
                intent.putExtra("data_name",terms.get(i-1).getName());
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");


    }
    public void getAdapterData(){
        names.clear();
        for (TermEntity b:terms) {
            names.add(b.getName());
        }
        adapter.notifyDataSetChanged(names);
    }


    private XlistListener xlistener=new XlistListener() {
        @Override
        public void onRefresh() {
            getTerm();

        }
    };




    public void getTerm() {
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserGetTermListReq request = new UserGetTermListReq();
        //获取参数
        reqBody.userGetTermListReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().get_term_list(reqEnvelop);
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        xlist.stopRefresh();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.userGetTermListRes !=null){
                String result=b.userGetTermListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                TermBean termId=gson.fromJson(result, TermBean.class);
                if (termId.getResult().equals("true")){
                    terms.clear();
                    terms=termId.getTerm();
                    getAdapterData();
                }else{
                    toastShow(JsonParser.getErrorCode(result));
                }
            }

        }else{
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e("onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        toastShow(R.string.fail_do_not);
        Logger.e("onFailure  :"+call.request().headers().toString());
        xlist.stopRefresh();
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
