package com.yfy.app.integral;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.MainSetViewActivity;
import com.yfy.app.WebActivity;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.login.AlterCllActivity;
import com.yfy.app.login.ChangePasswordActivity;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.base.UserGetBaseInfoReq;
import com.yfy.app.net.login.UserLogoutReq;
import com.yfy.app.net.login.UserResetPicReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.integral.adapter.IntegralMainAdapter;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.login.UserBaseActivity;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.base.UserGetCallReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.Variables;
import com.yfy.final_tag.*;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.glide.ZoomImage;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.db.GreenDaoManager;
import com.yfy.db.UserPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.view.SQToolBar;

import retrofit2.Call;
import retrofit2.Response;

public class IntegralMainActivity extends BaseActivity {

    private static final String TAG = IntegralMainActivity.class.getSimpleName();

    @Bind(R.id.integral_main_list)
    ListView list;
    private final  int REFRESH = 3;

    private ImageView head_iamge;
    private ImageView head_bg;
    private TextView name;
    private TextView type,replace_protraits;
    private TextView integral_text;



    private List<String> s_name=new ArrayList<>();
    private  List<Map<String,Object>> activitys;
    private IntegralMainAdapter adapter;


    public String uploadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_main);
        initSQToolbar();
        initDialog();
        getData();
        initView();
        refresh();
        getCall();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.CAMERA:
                    uploadPath=FileCamera.photo_camera;
                    mtask=new MyAsyncTask();
                    mtask.execute(uploadPath);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    uploadPath=photo_a.get(0).getPath();

                    mtask=new MyAsyncTask();
                    mtask.execute(uploadPath);
                    break;
                case TagFinal.UI_TAG:
                    getCall();
                    break;

            }
        }

    }



    @OnClick(R.id.integral_user_out)
    void setBack(){
        mDialog("确定要退出登录！",oklistener);

    }


    private DialogInterface.OnClickListener oklistener= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            logout();
        }
    };



    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("用户信息");
//        toolbar.addMenu(TagFinal.ONE_INT,R.drawable.ic_settings,ColorRgbUtil.getWhite());
//        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                switch (position){
//                    case TagFinal.ONE_INT:
//                        Intent intent = new Intent(mActivity, MainSetViewActivity.class);
//                        startActivity(intent);
//                        break;
//                }
//            }
//        });
        toolbar.setOnNaviClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();

    }

    public TextView call_phone,alter_pass;
    private void initView() {
        adapter=new IntegralMainAdapter(mActivity,s_name);
        View header= LayoutInflater.from(mActivity).inflate(R.layout.integral_main_header,null);
        head_iamge=  header.findViewById(R.id.integral_main_head_pic);
        head_bg=  header.findViewById(R.id.integral_main_head_bg);


        RelativeLayout call=header.findViewById(R.id.integral_main_item_call_layout);
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            call.setVisibility(View.VISIBLE);
        }else{
            call.setVisibility(View.GONE);
        }
        call_phone=header.findViewById(R.id.integral_main_item_call);
        call_phone.setTextColor(ColorRgbUtil.getGrayBackground());
        name=  header.findViewById(R.id.integral_main_name);
        TextView integral=  header.findViewById(R.id.integral_main_scroe);
        type=  header.findViewById(R.id.integral_main_type);
        integral_text= header.findViewById(R.id.integral_main_fen);
        replace_protraits=  header.findViewById(R.id.integral_replace_head_protraits);
        alter_pass=  header.findViewById(R.id.integral_alter);
        integral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
                    startActivity(new Intent(mActivity,IntegralListActivity.class));
                }else{
                    startActivity(new Intent(mActivity,IntegralListActivity.class));
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, AlterCllActivity.class);
                startActivityForResult(intent,TagFinal.UI_TAG );
            }
        });
        replace_protraits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyWord();
                album_select.showAtBottom();
            }
        });

        alter_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, ChangePasswordActivity.class);
                startActivityForResult(intent,TagFinal.UI_ADMIN);
                finish();
            }
        });

        list.setAdapter(adapter);
        list.addHeaderView(header);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i<1)return;
                if (s_name.get(i-1).equals("课表")){
                    String url = Base.RETROFIT_URI+StringUtils.stringToGetTextJoint(Base.SCHEDULE,Base.user.getIdU(),Base.user.getUsertype());
                    Intent intent=(Intent) activitys.get(i-1).get("intent");
                    Bundle b = new Bundle();
                    b.putString(TagFinal.URI_TAG, url);
                    b.putString(TagFinal.TITLE_TAG, "课程表");
                    intent.putExtras(b);
                    startActivity(intent);
                }else{
                    startActivity((Intent) activitys.get(i-1).get("intent"));
                }
            }
        });
        name.setText(Base.user.getName());
        if (Base.user.getHeadPic() != null) {
            GlideTools.chanCircle(mActivity, Base.user.getHeadPic(), head_iamge, R.drawable.user);
            Glide.with(mActivity)
                    .load(StringUtils.stringToImgToURlImg(Base.user.getHeadPic()))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                    .into(head_bg);
        } else {
            GlideTools.chanCircle(mActivity, R.drawable.user, head_iamge);
            Glide.with(mActivity)
                    .load(R.drawable.user)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                    .into(head_bg);
        }
    }


    private void refreshHeadPic() {
        if (Base.user == null) {
            return;
        }
        if (Base.user.getHeadPic() != null) {
            GlideTools.chanCircle(mActivity, Base.user.getHeadPic(), head_iamge,R.drawable.user);
            Glide.with(mActivity)
                    .load(StringUtils.stringToImgToURlImg(Base.user.getHeadPic()))
                    .apply(new RequestOptions().placeholder(R.drawable.user))
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                    .into(head_bg);
        } else {
            GlideTools.chanCircle(mActivity, R.drawable.user, head_iamge);
            Glide.with(mActivity)
                    .load(R.drawable.user)
                    .apply(new RequestOptions().error(R.drawable.user))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                    .into(head_bg);
        }
    }



    private void create(String type_s,String srcoe ){

        type.setText(type_s);
        integral_text.setText(srcoe);
    }
    public boolean isTea() {
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            return true;
        }
        return false;
    }


    public List<Map<String,Object>> getData(){
        activitys= new ArrayList<>();


        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
            addItem(activitys, "学科优生", SubjectGoodTeaActivity.class,false,0);
            addItem(activitys, "学生成果", HonorTeaActivity.class,false,0);
        }else{
            addItem(activitys, "基本信息", UserBaseActivity.class,false,1);
            addItem(activitys, "学科优生", SubjectGoodStuActivity.class,false,0);
            addItem(activitys, "成长数据", IntegralGrowActivity.class ,false,0);
            addItem(activitys, "家庭数据", IntegralFamilyActivity.class,false,0);
            addItem(activitys, "个人成果", IntegralHonorActivity.class,false,0);
        }


        addItem(activitys, "学期评语", TermidCommtActivity.class,false,0);
        addItem(activitys, "课表", WebActivity.class,false,10);
        addItem(activitys, "积分详情", IntegralListActivity.class,true,0);

        return activitys;
    }
    public void addItem(List<Map<String,Object>> activitys,String text,Class<?> obj,boolean is,int num) {
        Intent intent=new Intent(mActivity,obj);
        switch (num){
            case 1:

                break;
        }
        addItem(activitys, text,intent ,is);

    }
    protected void addItem(List<Map<String, Object>> data, String name, Intent intent,boolean is) {
        Map<String, Object> temp = new HashMap<String, Object>();
        s_name.add(name);
        temp.put("intent", intent);
        temp.put("type", is);

        data.add(temp);
    }




    private ConfirmAlbumWindow album_select;
    private void initDialog() {
        album_select = new ConfirmAlbumWindow(mActivity);
        album_select.setOne_select(getResources().getString(R.string.take_photo));
        album_select.setTwo_select(getResources().getString(R.string.album));
        album_select.setName(getResources().getString(R.string.upload_type));
        album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.popu_select_one:
                        PermissionTools.tryCameraPerm(mActivity);
                        break;
                    case R.id.popu_select_two:
                        PermissionTools.tryWRPerm(mActivity);
                        break;
                }
            }
        });
    }






    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        FileCamera camera=new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }
    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent;
        intent = new Intent(mActivity,AlbumOneActivity.class);
        Bundle b = new Bundle();
        b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
        b.putBoolean(TagFinal.ALBUM_SINGLE, true);
        intent.putExtras(b);
        startActivityForResult(intent,TagFinal.PHOTO_ALBUM);
    }
    @PermissionFail(requestCode = TagFinal.CAMERA)
    private void showCamere() {
        Toast.makeText(getApplicationContext(), R.string.permission_fail_camere, Toast.LENGTH_SHORT).show();
    }
    @PermissionFail(requestCode = TagFinal.PHOTO_ALBUM)
    private void showTip1() {
        Toast.makeText(getApplicationContext(), R.string.permission_fail_album, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     *----------------------- retrofit -----------------
     */



    private void refresh(){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserGetBaseInfoReq req = new UserGetBaseInfoReq();
        //获取参数
        reqBody.userGetBaseInfoReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_get_base_info(reqEnv);
        call.enqueue(this);
    }

    public void getCall() {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserGetCallReq req = new UserGetCallReq();
        //获取参数
        reqBody.userGetCallReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_get_call(reqEnv);
        call.enqueue(this);
    }

    private void saveImg(String flie_string){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SaveImgReq req = new SaveImgReq();
        //获取参数

        req.setImage_file(flie_string);
        req.setFileext("jpg");
        reqBody.saveImgReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnv);
        call.enqueue(this);
        Logger.e(reqEnv.toString());
    }



    private void uploadPic(String path) {
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserResetPicReq req = new UserResetPicReq();
        //获取参数
        req.setFilename(path);
        reqBody.userResetPicReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_reset_head(reqEnv);
        call.enqueue(this);
    }

    private void logout() {

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        UserLogoutReq req = new UserLogoutReq();
        //获取参数
        reqBody.userLogoutReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_logout(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }
    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.userGetCallRes !=null){
                dismissProgressDialog();
                String result=b.userGetCallRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				IntegralResult info=gson.fromJson(result,IntegralResult.class);
				if (StringJudge.isEmpty(info.getMobile())){
				    call_phone.setText("点击录入");
				    call_phone.setTextColor(ColorRgbUtil.getGrayBackground());
                }else{
                    call_phone.setText(info.getMobile());
                    call_phone.setTextColor(ColorRgbUtil.getBaseText());
                    UserPreferences.getInstance().saveTell(info.getMobile());
				}
            }
            if (b.userResetPicRes !=null){
                dismissProgressDialog();
                String result=b.userResetPicRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                BaseRes res=gson.fromJson(result,BaseRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    toastShow(R.string.success_send_head);
                    Base.user.setHeadPic(res.getHeadpic());
                    GreenDaoManager.getInstance().clearUser();
                    GreenDaoManager.getInstance().saveUser(Base.user);
                    refreshHeadPic();
                }else{
                    toast(res.getError_code());
                }

            }
            if (b.userLogoutRes !=null){
                dismissProgressDialog();
                String result=b.userLogoutRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                BaseRes res=gson.fromJson(result,BaseRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    Variables.clearAll(mActivity);
                    Base.user=null;
                    GreenDaoManager.getInstance().clearUser();
                    GreenDaoManager.getInstance().clearAdminDb();
                    GreenDaoManager.getInstance().clearMainIndex();
                    UserPreferences.getInstance().clearUserAll();
                    toastShow(R.string.success_user_out);
                    Base.adminDb=null;
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toast(res.getError_code());
                    Variables.clearAll(mActivity);
                    Base.user=null;
                    GreenDaoManager.getInstance().clearUser();
                    GreenDaoManager.getInstance().clearAdminDb();
                    GreenDaoManager.getInstance().clearMainIndex();
                    UserPreferences.getInstance().clearUserAll();
                    toastShow(R.string.success_user_out);
                    Base.adminDb=null;
                    setResult(RESULT_OK);
                    finish();
                }
            }

            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                BaseRes res=gson.fromJson(result,BaseRes.class);
                if (res.getResult().equalsIgnoreCase(TagFinal.FALSE)){
                    dismissProgressDialog();
                    toast(res.getError_code());
                }else{
                    uploadPic(res.getImg());
                }
            }
            if (b.userGetBaseInfoRes!=null) {
                String result = b.userGetBaseInfoRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                IntegralResult info=gson.fromJson(result, IntegralResult.class);
                create(info.getInfo(),info.getScore());
            }
        }else{
            dismissProgressDialog();
            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e("onResponse: IOException");
                e.printStackTrace();
            }
            switch (name){
                case TagFinal.USER_LOGOUT:
                    Variables.clearAll(mActivity);
                    Base.user=null;
                    GreenDaoManager.getInstance().clearUser();
                    GreenDaoManager.getInstance().clearAdminDb();
                    GreenDaoManager.getInstance().clearMainIndex();
                    UserPreferences.getInstance().clearUserAll();
                    toastShow(R.string.success_user_out);
                    Base.adminDb=null;
                    setResult(RESULT_OK);
                    finish();
                    break;
                    default:
                        toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
                        break;

            }
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





















    private MyAsyncTask mtask;
    private int num=0;
    private List<String> base64_list=new ArrayList<>();
    public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
        //内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(String... arg0) {
            if (isCancelled()) {
                return null;
            }
            List<String> list = Arrays.asList(arg0);
            base64_list.clear();
            num=0;
            for (String s:list){
                base64_list.add(ZoomImage.fileToBase64Str(s));
                num++;
            }
            return null;
        }
        //onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (StringJudge.isEmpty(base64_list)){
                toastShow("没有数据");
            }
            for (String s:base64_list){
                saveImg(s);
            }
        }
        //onProgressUpdate方法用于更新进度信息
        protected void onProgressUpdate(Integer... integers) {
            super.onProgressUpdate(integers);

        }
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("");
        }
    }
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //判断AsyncTask不为null且Status.RUNNING在运行状态
        if (mtask!=null&&mtask.getStatus()==AsyncTask.Status.RUNNING) {//为mtask标记cancel(true)状态
            mtask.cancel(true);
        }
    }

}
