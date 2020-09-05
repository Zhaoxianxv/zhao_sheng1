package com.yfy.app.duty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.duty.DutyGetReportDetailReq;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.duty.adpater.DutyAddAdapter;
import com.yfy.app.duty.bean.AddBean;
import com.yfy.app.duty.bean.Addinfo;
import com.yfy.app.duty.bean.DutyRes;
import com.yfy.app.duty.bean.PlaneA;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.SQToolBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DutyAddActivity extends WcfActivity {
    private static final String TAG = DutyAddActivity.class.getSimpleName();

    private DutyAddAdapter adapter;
    private List<Addinfo> list=new ArrayList<>();
    private List<AddBean> addBeans=new ArrayList<>();
    private String time="";
    private int image_postion,image_index;
    private AddBean beanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_recycler_view);
        initAdapter();
        initRecycler();
        getData();

        initDialog();

    }


    private void getData(){
        Intent intent=getIntent();
        PlaneA beanA=intent.getParcelableExtra(TagFinal.OBJECT_TAG);
        initSQToolbar(beanA.getDate());
        time=beanA.getDate();
        typeid=beanA.getTypeid();

        AddBean bean=new AddBean(true);
        bean.setType_name(beanA.getDutyreporttype());
        addBeans.add(bean);
        adapter.setDataList(addBeans);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);

        getPlane(beanA.getDate(),beanA.getTypeid());

    }
    private void initSQToolbar(String title){
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT,"完成" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        if (getDuty()){
                            add();
                        };
                        break;
                }
            }
        });
    }


    private void initAdapter(){
        adapter=new DutyAddAdapter(this);

        adapter.setAddmult(new DutyAddAdapter.Addmult() {
            @Override
            public void addIcon(String id, int index) {
                postion=index;
                typeDialog.showAtBottom();
            }
            @Override
            public void delImage(AddBean bean, int postion, int index) {
                beanAdapter=bean;
                image_index=index;
                image_postion=postion;
                DialogTools.getInstance().showDialog(mActivity,
                        "", "是否删除图片", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                recyclerView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<String> images=StringUtils.listToStringSplitCharacters(beanAdapter.getImage(),"|");
                                        delIm(beanAdapter.getId(),images.get(image_index));
                                    }
                                }, 200);
                            }
                        });
            }
        });
    }
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.setAdapter(adapter);
    }


    public void delIm(String id,String image_path){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                time,
                id,
                image_path
        };
        ParamsTask get_plane = new ParamsTask(params, TagFinal.DUTY_DEL_IMAGE, TagFinal.DUTY_DEL_IMAGE);
        execute(get_plane);
        showProgressDialog("");

    }

    private String typeid="";
    public void add(){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                Base.user.getName(),
                typeid,
                time,
                getThree(ids, 1),
                getThree(isnormal, 1),
                getThree(content, 1),
                getThree(add_images, 1),
                ""
        };
        ParamsTask get_plane = new ParamsTask(params, TagFinal.DUTY_ADD, TagFinal.DUTY_ADD);
        ExtraRunTask wrapTask = new ExtraRunTask(get_plane);
        wrapTask.setExtraRunnable(extraRunnable);
        execute(wrapTask);
        showProgressDialog("");

    }



    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            params[8] = Base64Utils.filesToZipBase64(add, "");
            return params;
        }
    };



    ArrayList ids=new ArrayList();
    ArrayList isnormal=new ArrayList();
    ArrayList content=new ArrayList();
    ArrayList add_images=new ArrayList();//file_name
    ArrayList add=new ArrayList();//file_zip
    private boolean getDuty(){
        if (StringJudge.isEmpty(time)){
            toastShow("请选择值周日期");
            return false;
        }
        if (StringJudge.isEmpty(typeid)){
            toastShow("请选择值周日期");
            return false;
        }
        ids.clear();
        isnormal.clear();
        add_images.clear();
        content.clear();
        add.clear();
        List<AddBean> list=adapter.getDataList();
        for (AddBean bean:list){
            if (bean.isType_top()){
                if (StringJudge.isEmpty(bean.getType_name())){
                    toastShow("请选择值周类型");
                }
                continue;
            }else{

                if (StringJudge.isEmpty(bean.getId()))continue;
                if (bean.getId().equals("0")){//editor
                    if (StringJudge.isEmpty(bean.getContent())){
                        content.add("");
                        isnormal.add(TagFinal.FALSE);
                    }else{
                        content.add(bean.getContent());
                        isnormal.add(TagFinal.TRUE);
                    }
                    add_images.add("");
                    ids.add(bean.getId());
                }else{
                    if (StringJudge.isEmpty(bean.getIsnormal())){
                        if (StringJudge.isEmpty(bean.getAddImg())){
                            if (StringJudge.isEmpty(bean.getImage())){
                                continue;
                            }
                        }
                    }
                    ids.add(bean.getId());
                    if (StringJudge.isEmpty(bean.getAddImg())){
                        add_images.add("");
                    }else{
                        add_images.add(getZipTitle(bean.getAddImg()));
                        add.addAll(bean.getAddImg());
                    }
                    switch (bean.getIsnormal()){
                        case TagFinal.TRUE:
                            isnormal.add(TagFinal.TRUE);
                            content.add("");
                            break;
                        case TagFinal.FALSE:

                            if (StringJudge.isEmpty(bean.getContent())){
                                toastShow("填写异常说明");
                                return false;
                            }
                            isnormal.add(TagFinal.FALSE);
                            content.add(bean.getContent());
                            break;
                        case "":
                            isnormal.add("");
                            content.add("");
                            break;
                    }
                }
            }
        }
        return  true;
    }
    public static String getZipTitle(List<String> photoList) {
        StringBuilder sb = new StringBuilder();
        String[] name;
        for (String photo : photoList) {
            name=photo.split("/");
            sb.append(name[name.length-1]).append("|");
        }

        String result = sb.toString();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
//

    private String getThree(ArrayList s,int type){
        StringBuilder sb = new StringBuilder();
        for (int index=0;index<s.size();index++){
            sb.append(s.get(index)).append("*");
        }
        if (sb.length()>2){
            return sb.substring(0,sb.length()-1);
        }
        return "";
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


    private int postion;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK==resultCode){
            List<String> ps=new ArrayList<>();
            List<String> photos;
            AddBean addBean;

            switch (requestCode){
                case TagFinal.CAMERA:
                    addBean = adapter.getDataList().get(postion);
                    photos= addBean.getAddImg();
                    if (StringJudge.isEmpty(photos)){
                        ps.add(FileCamera.photo_camera);
                        addBean.setAddImg(ps);
                    }else{
                        photos.add(FileCamera.photo_camera);
                    }
                    adapter.notifyItemChanged(postion, addBean);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;

                    for (Photo photo:photo_a){
                        if (photo==null) continue;
                        ps.add(photo.getPath());
                    }
                    addBean=adapter.getDataList().get(postion);
                    photos = addBean.getAddImg();
                    if (StringJudge.isEmpty(photos)){
                        addBean.setAddImg(ps);
                    }else{
                        photos.addAll(ps);
                    }
                    adapter.notifyItemChanged(postion, addBean);
                    break;
                case TagFinal.UI_ADD:
                    break;

            }
        }
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return true;
        dismissProgressDialog();
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        String name=wcfTask.getName();

        if (name.equals(TagFinal.DUTY_ADD)){
            if (StringJudge.isSuccess(gson,result)){
                toastShow(R.string.success_do);
                setResult(RESULT_OK);
                finish();
            }else{
                DutyRes info=gson.fromJson(result,DutyRes.class );
                toastShow(info.getError_code());
            }
        }
        if (name.equals(TagFinal.DUTY_DEL_IMAGE)){
            if (StringJudge.isSuccess(gson,result)){
                toastShow(R.string.success_do);
                List<String> images=StringUtils.listToStringSplitCharacters(beanAdapter.getImage(),"|");
                images.remove(image_index);
                beanAdapter.setImage(StringUtils.stringToArraysGetString(images,"|"));
                adapter.notifyItemChanged(image_postion, beanAdapter);
            }else{
                DutyRes info=gson.fromJson(result,DutyRes.class );
                toastShow(info.getError_code());
            }
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
    }



    /**
     * ----------------------------retrofit-----------------------
     */


    public void getPlane(String date,String typeid){


        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DutyGetReportDetailReq req = new DutyGetReportDetailReq();
        //获取参数
        req.setDate(date);
        req.setTypeid(ConvertObjtect.getInstance().getInt(typeid));
        reqBody.dutyGetReportDetailReq=req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().duty_get_report_detail(reqEnv);
        call.enqueue(this);
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
            if (b.dutyGetReportDetailRes !=null){
                String result=b.dutyGetReportDetailRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                DutyRes info=gson.fromJson(result,DutyRes.class );
                if (StringJudge.isEmpty(info.getDutyreport_type())){
                    toastShow("数据出差");
                }else{
                    list = info.getDutyreport_type();
                    if (list.size()==TagFinal.ONE_INT){
                        //直接赋值
                        addBeans.addAll(list.get(0).getDutyreport_content());
                        adapter.setDataList(addBeans);
                        adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                    }
                }
            }

        }else{

            try {
                String s=response.errorBody().string();
                Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
            } catch (IOException e) {
                Logger.e(TagFinal.ZXX, "onResponse: IOException");
                e.printStackTrace();
            }
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
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


}
