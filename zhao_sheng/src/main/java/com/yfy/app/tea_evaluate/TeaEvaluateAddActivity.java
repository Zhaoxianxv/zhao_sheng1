package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.net.judge.TeaJudgeAddReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.adpter.AddListAdapter;
import com.yfy.app.tea_evaluate.bean.AddPararem;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.tea_evaluate.bean.ResultJudge;
import com.yfy.app.net.judge.TeaJudgeGetAddParameterReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.Variables;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeaEvaluateAddActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = TeaEvaluateAddActivity.class.getSimpleName();

    private String param_id="0";

    private AddListAdapter adapter;
    private List<AddPararem> params=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        getData();
        initSQToolbar();

        initDialog();
        initRecycler();

    }

    private void getData(){
        Bundle bundle=getIntent().getExtras();
        if (StringJudge.isContainsKey(bundle,TagFinal.ID_TAG)){
            param_id = bundle.getString(TagFinal.ID_TAG);
        }
        if (StringJudge.isContainsKey(bundle,TagFinal.NAME_TAG)){
            String name = bundle.getString(TagFinal.NAME_TAG);
            Variables.type=name;
        }
        getShapeKind(ConvertObjtect.getInstance().getInt(param_id));
    }

    private  void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("添加获奖");
        toolbar.addMenuText(TagFinal.ONE_INT,"完成");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (StringJudge.isEmpty(redactAdapter())){
                    return;
                }
                mtask=new MyAsyncTask();
                mtask.execute();

            }
        });
    }
    private String redactAdapter(){

        List<AddPararem> params=adapter.getDataList();

        StringBuilder sb=new StringBuilder();
        for (AddPararem bean :params){

            switch (bean.getType()){
                case "date":
                    if (StringJudge.isEmpty(bean.getTitle())){
                        toastShow("请选择："+bean.getName());
                        return null;
                    }
                    appenString(sb,bean);
                    break;
                case "text":
                    if (StringJudge.isEmpty(bean.getTitle())){
                        toastShow("请填写："+bean.getName());
                        return null;
                    }
                    appenString(sb,bean);
                    break;
                case "int":
                    if (StringJudge.isEmpty(bean.getTitle())){
                        toastShow("请填写："+bean.getName());
                        return null;
                    }
                    appenString(sb,bean);
                    break;
                case "select":
                    if (StringJudge.isEmpty(bean.getTitle())){
                        toastShow("请选择："+bean.getName());
                        return null;
                    }
                    appenString(sb,bean);
                    break;
                case "multifile":
                    appenString(sb,bean,"");
                    break;
            }
        }
        String content=sb.toString();
        if (content.length() > 0) {
            content = content.substring(0, content.length() - 1);
        }
        return content;
    }

    private StringBuilder appenString(StringBuilder sb,AddPararem bean){
        sb=sb.append(bean.getId()).append("^").append(bean.getTitle()).append("|");
        return sb;
    }

    private StringBuilder appenString(StringBuilder sb,AddPararem bean,String is){
        sb=sb.append(bean.getId()).append("^").append("[image]").append("|");
        return sb;
    }

    private RecyclerView recyclerView;
    public MultiPictureView add_mult;
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new AddListAdapter(this,params);

        adapter.setPic(new AddListAdapter.Pic() {
            @Override
            public void add(MultiPictureView add_mul) {
                typeDialog.showAtBottom();
                add_mult=add_mul;
            }
        });
        recyclerView.setAdapter(adapter);

    }







    private MyDialog typeDialog;
    private void initDialog() {
        typeDialog= new MyDialog(mActivity,
                R.layout.dialog_getpic_type_popup,
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle },
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle });
        typeDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        typeDialog.setOnCustomDialogListener(new AbstractDialog.OnCustomDialogListener() {
            @Override
            public void onClick(View v, AbstractDialog dialog) {
                switch (v.getId()) {
                    case R.id.take_photo:
                        PermissionTools.tryCameraPerm(mActivity);
                        dialog.dismiss();
                        break;
                    case R.id.alnum:
                        PermissionTools.tryWRPerm(mActivity);
                        dialog.dismiss();
                        break;
                    case R.id.cancle:
                        dialog.dismiss();
                        break;
                }
            }
        });

    }
//



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.UI_TAG:
                    Bundle b=data.getExtras();
                    if (StringJudge.isContainsKey(b,TagFinal.ID_TAG)){
//                        chioce_grade.setText(b.getString(TagFinal.NAME_TAG));
//                        chioce_grade.setTextColor(ColorRgbUtil.getBaseText());
                    }
                    break;
                case TagFinal.CAMERA:
                    addMult(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    setMultList(photo_a);
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }





    public void addMult(String uri){
        if (uri==null) return;
        add_mult.addItem(uri);
    }
    public void setMultList(List<Photo> list){
        for (Photo photo:list){
            if (photo==null) continue;
            addMult(photo.getPath());
        }
    }


    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        FileCamera camera=new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }
    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent;
        intent = new Intent(mActivity, AlbumOneActivity.class);
        Bundle b = new Bundle();
        b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
        b.putBoolean(TagFinal.ALBUM_SINGLE, false);
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
     * --------------------------retrofit---------------------
     */

    public void getShapeKind(int param_id)  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetAddParameterReq request = new TeaJudgeGetAddParameterReq();
        request.setId(param_id);
        //获取参数


        reqBody.teaJudgeGetAddParameterReq = request;
        reqEnvelop.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_add_parameter(reqEnvelop);
        call.enqueue(this);
        showProgressDialog("正在加载");

    }





    public void add(){
        String content=redactAdapter();

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeAddReq req = new TeaJudgeAddReq();
        //获取参数
        req.setContent(content);
        req.setId("0");
        req.setRealname(Base.user.getName());
        req.setYear(ConvertObjtect.getInstance().getInt(Base.year));
        req.setPid(ConvertObjtect.getInstance().getInt(Variables.type_id));
        req.setImages(name_s);
        req.setImage_file(content_s);



        reqBody.teaJudgeAddReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_add(reqEnv);
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
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetAddParameterRes != null) {
                String result = b.teaJudgeGetAddParameterRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=gson.fromJson(result, ResultInfo.class);
                redact(info);
            }
            if (b.teaJudgeAddRes != null) {
                String result = b.teaJudgeAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultJudge info=gson.fromJson(result, ResultJudge.class);
                if (info.getResult().equals(TagFinal.TRUE)){
                    setResult(RESULT_OK);
                    onPageBack();
                }else{
                    toastShow(info.getError_code());
                }
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }


    private void redact(ResultInfo info){
        params.clear();
        AddPararem year=new AddPararem();
        year.setName(Base.year_name);
        year.setTitle("获奖年份");
        year.setType("base");
        params.add(year);
        AddPararem type=new AddPararem();
        type.setName(Variables.type);
        type.setTitle("获奖分类");
        type.setType("base");
        params.add(type);

        params.addAll(info.getJudge_parameter());
        adapter.setDataList(params);
        adapter.setLoadState(2);

    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure "+call.request().headers().toString());
        dismissProgressDialog();
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }















    private MyAsyncTask mtask;
    private String name_s="",content_s="";
    private List<Photo> photo_list=new ArrayList<>();
    public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
        //内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Void doInBackground(String... arg0) {
            if (isCancelled()) {
                return null;
            }
            name_s="";
            content_s="";
            int i=0;
            photo_list.clear();
            if (add_mult!=null){
                for (String uri:add_mult.getList()){
                    Photo p=new Photo();
                    i++;
                    String picName = String.valueOf(System.currentTimeMillis())+String.valueOf(i);
                    p.setFileName(picName+".jpg");
                    p.setPath(uri);
                    photo_list.add(p);
                }
            }
            if (StringJudge.isEmpty(photo_list)){
            }else{
                name_s= Base64Utils.getZipTitleNotice(photo_list);
                content_s=  Base64Utils.filesToZipBase64Notice(photo_list);
            }
            return null;
        }
        //onPostExecute方法用于在执行完后台任务doInBackground后更新UI,显示结果
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            add();
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
