package com.yfy.app.ebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ebook.BookGetTypeReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ebook.VideoGetTypeReq;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.video.beans.Tag;
import com.yfy.app.video.beans.VideoInfo;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.view.swipe.xlist.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagChioceActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = TagChioceActivity.class.getSimpleName();
    private XlistLefttTxtAdapter adapter;
    private List<String> names=new ArrayList<>();
    private List<Tag> tags=new ArrayList<>();
    private String session_key="";
    private boolean is;
    @Bind(R.id.tag_list)
    XListView xlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_chioce);
        getData();
        initView();

    }

    public void getData(){
        is=getIntent().getBooleanExtra("is",false);
        tags.clear();
        names.clear();

        if (Base.user ==null){
            session_key="gus0";
        }else{
            session_key=Base.user.getSession_key();
        }
        if (is) {
            getBookTag();
        }else{
            getVideoTag();
        }

    }
    private void initView() {
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(false);
        adapter=new XlistLefttTxtAdapter(mActivity,names);
        xlist.setAdapter(adapter);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra("tag_name",tags.get(i-1).getName());
                intent.putExtra("tag_id",tags.get(i-1).getId());
                setResult(RESULT_OK,intent);
                onPageBack();
            }
        });
    }



    public void instanceTags(List<Tag> tags){
        for (Tag tag:tags) {
            names.add(tag.getName());
        }
        adapter.notifyDataSetChanged(names);
    }





    /**
     *------------------------------------
     */

    public void getBookTag() {

        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        BookGetTypeReq req = new BookGetTypeReq();
        req.setSession_key(session_key);
        //获取参数
        reqBody.bookGetTypeReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().book_get_type(env);
        call.enqueue(this);
    }

    public void getVideoTag() {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        VideoGetTypeReq req = new VideoGetTypeReq();
        req.setSession_key(session_key);
        //获取参数
        reqBody.videoGetTypeReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().video_get_type(reqEnv);
        call.enqueue(this);
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
            if (b.bookGetTypeRes !=null){
                String result=b.bookGetTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                VideoInfo res=gson.fromJson(result,VideoInfo.class);
                tags=res.getBook_tag();
                instanceTags(tags);
            }
            if (b.videoGetTypeRes !=null){
                String result=b.videoGetTypeRes.video_tag_Res;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                VideoInfo res=gson.fromJson(result,VideoInfo.class);
                tags=res.getVideo_tag();
                instanceTags(tags);
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
