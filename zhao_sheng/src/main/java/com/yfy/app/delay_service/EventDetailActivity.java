package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.delay_service.bean.DelayServiceRes;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayGetItemDetailReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.CallPhone;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = EventDetailActivity.class.getSimpleName();

   private String bean_id;
   private EventClass bean;

    @Bind(R.id.event_detail_head)
    ImageView head;

    @Bind(R.id.event_detail_stu_name)
    TextView stu_name;
    @Bind(R.id.event_detail_stu_phone)
    TextView stu_phone;
    @Bind(R.id.event_detail_type)
    TextView type_tag;

    @Bind(R.id.event_class_name)
    TextView class_name;
    @Bind(R.id.event_check_one)
    TextView check_one;
    @Bind(R.id.event_check_two)
    TextView check_two;
    @Bind(R.id.event_check_three)
    TextView check_three;
    @Bind(R.id.event_check_four)
    TextView check_four;
    @Bind(R.id.event_detail_show_multi)
    MultiPictureView multi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_service_check_detail);
        getData();
        initDialog();
    }


    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b,TagFinal.OBJECT_TAG )){
            bean_id=b.getString(TagFinal.OBJECT_TAG);
            getItemDetail(bean_id);

        }else{
            toast("数据错误");
        }

        initSQtoobar("");
    }

    private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
    }




    public void initView(EventClass bean){

        GlideTools.chanCircle(mActivity, bean.getAdduserheadpic(),head, R.mipmap.icon_account_fill);
        stu_name.setText(StringUtils.getTextJoint("考勤教师:%1$s",bean.getAddusername()));
        type_tag.setText(bean.getType());
        stu_phone.setText(bean.getAdduserphonenumber());
        class_name.setText(StringUtils.getTextJoint("考勤对象:%1$s",bean.getStuname()));
        check_one.setText(StringUtils.getTextJoint("考勤时间:%1$s",bean.getAdddate()));
        check_two.setText(StringUtils.getTextJoint("托管班级:%1$s",bean.getElectivename()));
        check_three.setText(StringUtils.getTextJoint("学生电话:%1$s",bean.getStuphonenumber()));
        check_four.setText(StringUtils.getTextJoint("考勤备注:%1$s",bean.getContent()));

        if (StringJudge.isEmpty(bean.getImage())){
            multi.setVisibility(View.GONE);
        }else{
            multi.setVisibility(View.VISIBLE);
            multi.setList(bean.getImage());
        }
        multi.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Intent intent=new Intent(mActivity, MultPicShowActivity.class);
                Bundle b=new Bundle();
                b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }









    private ConfirmContentWindow confirmContentWindow;
    private void initDialog(){

        confirmContentWindow = new ConfirmContentWindow(mActivity);
        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        if (StringJudge.isEmpty(phone_s)){
                        }else{
                            PermissionTools.tryTellPhone(mActivity);
                        }

                        break;
                }
            }
        });
    }

    private void showDialog(String title,String content,String ok){
        if (confirmContentWindow==null)return;
        if (StringJudge.isEmpty(content))content="未检测出电话号码！";
        confirmContentWindow.setTitle_s(title,content,ok);
        confirmContentWindow.showAtCenter();
    }



    @OnClick(R.id.event_detail_stu_phone)
    void setTea(){
        phone_s=bean.getAdduserphonenumber();
        showDialog("拨打电话",phone_s ,"确定" );
    }
    @OnClick(R.id.event_check_three)
    void setStuPhone(){
        phone_s=bean.getStuphonenumber();
        showDialog("拨打电话",phone_s ,"确定" );
    }

    private String phone_s;
    @PermissionSuccess(requestCode = TagFinal.CALL_PHONE)
    public void call(){
        CallPhone.callDirectly(mActivity, phone_s);
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



    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

    private void getItemDetail(String id){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetItemDetailReq req = new DelayGetItemDetailReq();
        //获取参数

        req.setId(ConvertObjtect.getInstance().getInt(id));
        reqBody.delayGetItemDetailReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_item_detail(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }





    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (response.code()==500){
            toastShow("数据出差了");
        }
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;

            if (b.delayGetItemDetailRes !=null) {
                String result = b.delayGetItemDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DelayServiceRes res=gson.fromJson(result, DelayServiceRes.class);

                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    bean=res.getElective_detail().get(0);
                    if (bean==null){
                        toast("数据出错");
                    }else{
                        initView(bean);
                        initSQtoobar(bean.getElectivename());
                    }
                }else{
                    toast(res.getError_code());
                }

                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){


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
        toast(R.string.fail_do_not);
    }


}
