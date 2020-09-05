package com.yfy.app.tea_evaluate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.judge.TeaJudgeAddReq;
import com.yfy.app.net.judge.TeaJudgeDelImageReq;
import com.yfy.app.net.judge.TeaJudgeGetInfoDetailReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.tea_evaluate.adpter.RedactListAdapter;
import com.yfy.app.tea_evaluate.bean.ParamBean;
import com.yfy.app.tea_evaluate.bean.ResultJudge;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.Variables;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ColorRgbUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedactActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = RedactActivity.class.getSimpleName();

    private RedactListAdapter adapter;
    private List<ParamBean> dataList=new ArrayList<>();
    private int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        getData();
        initDialog();
        initSQToolbar();
        initRecycler();

    }

    @Override
    public void onPageBack() {
        finish();
    }
    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }


    private void getData(){
        id=getIntent().getIntExtra(TagFinal.ID_TAG,-1);
        getInfoData(true,id);
    }
    private  void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("获奖编辑");
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

        List<ParamBean> params=adapter.getDataList();

        StringBuilder sb=new StringBuilder();
        for (ParamBean bean :params){
            switch (bean.getType()){
                case "date":
                    appenString(sb,bean);
                    break;
                case "text":
                    if (StringJudge.isEmpty(bean.getContent())){
                        toast("请填写："+bean.getTitle());
                        return null;
                    }
                    appenString(sb,bean);
                    break;
                case "select":
                    appenString(sb,bean);
                    break;
                case "int":
                    if (StringJudge.isEmpty(bean.getContent())){
                        toastShow("请填写："+bean.getTitle());
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

    private StringBuilder appenString(StringBuilder sb,ParamBean bean){
        sb=sb.append(bean.getId()).append("^").append(bean.getContent()).append("|");
        return sb;
    }

    private StringBuilder appenString(StringBuilder sb, ParamBean bean, String is){
        sb=sb.append(bean.getId()).append("^").append("[image]").append("|");
        return sb;
    }
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    public MultiPictureView add_mult;//陈明富
    public void initRecycler(){

        recyclerView = findViewById(R.id.public_recycler);
        swipeRefreshLayout = findViewById(R.id.public_swip);
        //AppBarLayout 展开执行刷新
        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(ColorRgbUtil.getBaseColor());
        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getInfoData(false, id);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new RedactListAdapter(this,dataList);


        adapter.setRedact(new RedactListAdapter.Redact() {
            @Override
            public void del(final String image, final String id) {
                DialogTools.getInstance().showDialog(mActivity, "", "确定删除！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        delectImage(id,image);
                    }
                });

            }

            @Override
            public void addMult(MultiPictureView mult) {
                typeDialog.showAtBottom();
                add_mult=mult;
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


    public void closeSwipeRefresh(){
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }, 200);
        }
    }


    private void redact(ResultJudge info){
        dataList.clear();
        ParamBean year=new ParamBean();
        year.setContent(Variables.year);
        year.setTitle("获奖年份");
        year.setType("base");
        dataList.add(year);
        ParamBean type=new ParamBean();
        type.setContent(Variables.type);
        type.setTitle("获奖分类");
        type.setType("base");
        dataList.add(type);

        if (StringJudge.isNotEmpty(info.getAttachment())){
            String[] s=info.getAttachment().split(Pattern.quote(","));
            List<String> name= Arrays.asList(s);
            for (String s1:name){
                ParamBean bean=new ParamBean();
                String[] names=s1.split(Pattern.quote("/"));
                bean.setTitle(names[names.length-1]);
                bean.setContent(s1);
                bean.setId(id+"");
                bean.setType("del");
                dataList.add(bean);
            }
        }
        //--------------
//        ParamBean icon=new ParamBean();
//        icon.setType("icon");
//        dataList.add(icon);
        dataList.addAll(info.getJudge_record());
        adapter.setDataList(dataList);
        adapter.setLoadState(2);
    }







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



    public void getInfoData(boolean is,int id_){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetInfoDetailReq req = new TeaJudgeGetInfoDetailReq();
        //获取参数
        req.setId(id_);

        reqBody.teaJudgeGetInfoDetailReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_info_detail(reqEnv);
        call.enqueue(this);
        if (is)showProgressDialog("");
    }



    public void delectImage(String id,String image){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeDelImageReq req = new TeaJudgeDelImageReq();
        //获取参数
        req.setId(String.valueOf(id));
        req.setImage(image);
        req.setTypeid(id);

        reqBody.teaJudgeDelImageReq = req;
        reqEnv.body= reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_del_image(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }


    public void add(){
        String content=redactAdapter();


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeAddReq req = new TeaJudgeAddReq();
        //获取参数
        req.setContent(content);
        req.setId(String.valueOf(id));
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
        closeSwipeRefresh();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);


        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetInfoDetailRes != null) {
                String result = b.teaJudgeGetInfoDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));


                ResultJudge res=gson.fromJson(result, ResultJudge.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    redact(res);
                }

            }
            if (b.teaJudgeDelImageRes != null) {
                String result = b.teaJudgeDelImageRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultJudge res= gson.fromJson(result, ResultJudge.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    setResult(RESULT_OK);
                    onPageBack();
                }else{
                    toast(res.getError_code());
                }
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

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("error  "+call.request().headers().toString() );
        dismissProgressDialog();
        closeSwipeRefresh();
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
