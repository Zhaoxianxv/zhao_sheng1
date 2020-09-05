package com.yfy.app.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.ebook.TagChioceActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ebook.VideoGetUserListReq;
import com.yfy.app.video.beans.VideoInfo;
import com.yfy.app.video.beans.Videobean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.ConvertObjtect;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListMainActivity extends BaseActivity implements Callback<ResEnv> {


    private static final String TAG = VideoListMainActivity.class.getSimpleName();
    private static final int  VIDEO = 3;
    private List<Videobean> video_adapter_list =new ArrayList<>();

    @Bind(R.id.video_list_main)
    XListView xList;
    private VideoListAdapter adapter;
    private TextView top;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_main);
        initView();


    }


    private String admin_right="",admin_add=TagFinal.FALSE,admin_manage=TagFinal.FALSE;
    public void getData(){
        Intent intent=getIntent();
        admin_right=intent.getStringExtra(Base.content);
        //权限
        List<String> adminList=StringUtils.listToStringSplitCharacters(admin_right,",");
        for (String s:adminList){
            switch (s){
                case "view":
                    break;
                case "add":
                    admin_add=TagFinal.TRUE;
                    break;
                case "admin":
                    admin_manage=TagFinal.TRUE;
                    break;
            }
        }
    }


    private String session_key="";
    @Override
    public void onResume() {
        super.onResume();
        if (Base.user ==null){
            session_key="gus0";
        }else{
            session_key=Base.user.getSession_key();
        }
        refresh(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==VIDEO){
                String name,id;
                name=data.getStringExtra("tag_name");
                id=data.getStringExtra("tag_id");
                top.setText(name);
                tag_id= ConvertObjtect.getInstance().getInt(id);
                refresh(true);
            }
        }
    }




    private void initView(){
        assert toolbar!=null;
        toolbar.setTitle(R.string.video);
        xList.setPullLoadEnable(true);
        xList.setPullLoadEnable(true);
        adapter=new VideoListAdapter(mActivity, video_adapter_list);
        xList.setAdapter(adapter);
        xList.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(true);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                refresh(false);
            }
        });
        xList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)return;


                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(video_adapter_list.get(i-2).getFileurl());
                intent.setData(content_url);
                startActivity(intent);
            }
        });

        View v= LayoutInflater.from(mActivity).inflate(R.layout.public_item_singe_top_txt,null);
        v.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        xList.addHeaderView(v);

        top = (TextView) v.findViewById(R.id.public_center_txt_add);
        top.setText("全部");
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivity,TagChioceActivity.class);
                intent.putExtra("is",false);
                startActivityForResult(intent,VIDEO);
            }
        });
    }


















    /**
     *------------------------------------
     */



    private int tag_id=0,page=0;
    private boolean loading =false;





    public void refresh(boolean is){
        if (is) {
            if (loading) {
                xList.stopRefresh();
                return;
            }
            page = 0;
        } else {
            if (loading) {
                xList.stopLoadMore();
                return;
            }
            page++;
        }
        loading = true;
        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        VideoGetUserListReq req = new VideoGetUserListReq();
        //获取参数
        req.setPage(page);
        req.setSession_key(session_key);
        req.setTagid(tag_id);
        req.setSize(TagFinal.FIFTEEN_INT);

        reqBody.videoGetUserListReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().video_get_main_list(env);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        loading = false;
        if (xList != null) {
            xList.stopLoadMore();
            xList.stopRefresh();
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.videoGetUserListRes !=null){
                String result=b.videoGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                VideoInfo res = gson.fromJson(result, VideoInfo.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (page==0){
                        video_adapter_list.clear();
                        video_adapter_list.addAll(res.getVideo_list());
                    }else{
                        video_adapter_list.addAll(res.getVideo_list());
                    }
                    adapter.notifyDataSetChanged(video_adapter_list);
                }else {
                    toastShow(res.getError_code());
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
        loading = false;
        if (xList != null) {
            xList.stopLoadMore();
            xList.stopRefresh();
        }
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
