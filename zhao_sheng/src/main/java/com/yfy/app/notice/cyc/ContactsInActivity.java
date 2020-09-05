package com.yfy.app.notice.cyc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.notice.NoticeGetStuListReq;
import com.yfy.app.net.notice.NoticeGetTeaListReq;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.app.notice.bean.NoticeRes;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsInActivity extends BaseActivity implements Callback<ResEnv>{


    private final static String TAG = ContactsInActivity.class.getSimpleName();

    private final static int TEACHER = 1;
    private final static int STUDENT = 2;
    private final static int ALL = 3;

    @Bind(R.id.notifcation_chooes_teacher_num)
    TextView teacher_num;
    @Bind(R.id.notifcation_chooes_student_num)
    TextView student_num;

    private ArrayList<ChildBean> childs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_chooes_contacts);
        initSQToolbar();
        getData();
    }

    private void getData(){
        childs=getIntent().getExtras().getParcelableArrayList(TagFinal.OBJECT_TAG);
        if (StringJudge.isEmpty(childs)) {

        }else{
            teacher_num.setText("已选" + childs.size()+ "人");
        }

    }
    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.contacts);
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        Intent intent=new Intent();
                        intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, childs);
                        setResult(RESULT_OK,intent);
                        onPageBack();
                        break;
                }
            }
        });
        teacher_num.setText("已选" + childs.size()+ "人");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TEACHER:
                    childs=data.getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
                    refreshTea(childs);
                    break;
                case STUDENT:
                    refreshStu();
                    break;
                case ALL:
//                    refreshTea();
//                    refreshStu();
                    break;
            }
        }
    }


    private void refreshTea(List<ChildBean> datas) {
        if (datas.size() > 0) {
            teacher_num.setVisibility(View.VISIBLE);
            teacher_num.setText("已选" + datas.size() + "人");
        } else {
            teacher_num.setVisibility(View.GONE);
        }
    }

    private void refreshStu() {
//        if (Variables.stuSelectedList.size() > 0) {
//            student_num.setVisibility(View.VISIBLE);
//            student_num.setText("已选" + Variables.stuSelectedList.size() + "人");
//        } else {
//            student_num.setVisibility(View.GONE);
//        }
    }



    @OnClick(R.id.notifcation_index_teacher)
    void setteacher(){
        Bundle b = new Bundle();
        Intent intent = new Intent(this, ContactsSelectActivity.class);
        b.putString(TagFinal.TYPE_TAG, TagFinal.USER_TYPE_TEA);
        b.putParcelableArrayList(TagFinal.OBJECT_TAG,childs );
        intent.putExtras(b);
        //进入老师列表
        startActivityForResult(intent, TEACHER);
    }
    @OnClick(R.id.notifcation_index_student)
    void setstudent(){
        Bundle b2 = new Bundle();
        Intent intent2 = new Intent(this, ContactsSelectActivity.class);
        b2.putString(TagFinal.TYPE_TAG, TagFinal.USER_TYPE_STU);
        intent2.putExtras(b2);
        startActivityForResult(intent2, STUDENT);
    }

    /**
     * ---------------------------retrofit-----------------------
     */

    private void getTea() {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetTeaListReq req_tea=new NoticeGetTeaListReq();
        //参数

        reqbody.noticeGetTeaListReq =req_tea;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_tea(reqenv);
        call.enqueue(this);
        showProgressDialog("正在加载...");

    }
    private void getStu() {
        ReqEnv reqenv=new ReqEnv();
        ReqBody reqbody=new ReqBody();
        NoticeGetStuListReq req_tea=new NoticeGetStuListReq();
        //参数

        reqbody.noticeGetStuListReq =req_tea;
        reqenv.body=reqbody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_get_stu(reqenv);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        Log.e(TagFinal.ZXX, "onResponse: "+response.code());
        if (response.code()==500){
            try {
                String s=response.errorBody().string();
                Log.e(TagFinal.ZXX, s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            toast("数据出差了");
        }
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;

            if (b.noticeGetTeaListRes !=null){
                String result=b.noticeGetTeaListRes.result;
                Log.e(TagFinal.ZXX, "retrofit: "+result );
                NoticeRes info=gson.fromJson(result, NoticeRes.class);
            }
            if (b.noticeGetStuListRes !=null){
                String result=b.noticeGetStuListRes.result;
            }

        }else{
            Log.e(TagFinal.ZXX, "onResponse: 22" );
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
