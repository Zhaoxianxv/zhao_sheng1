package com.yfy.app.check;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.check.adapter.CheckAddParentAdapter;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.app.check.bean.IllBean;
import com.yfy.app.check.bean.IllType;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.SaveImgReq;
import com.yfy.app.net.check.CheckAddChildReq;
import com.yfy.app.seal.bean.SealRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.recycerview.DefaultItemAnimator;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStuAddChildActivity extends BaseActivity implements Callback<ResEnv> {
    private static final String TAG = CheckStuAddChildActivity.class.getSimpleName();

    private CheckAddParentAdapter addParentAdapter;

    private List<IllBean> adapterData=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swip_recycler_main);
        initRecycler();
        getData();
        initSQtoolbar();
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("新增病情");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        closeKeyWord();
                        addChildItem();
                        break;
                }
            }
        });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private CheckStu checkStu;
    private DateBean dateBean;
    private ClasslistBean classlistBean;
    private IllType select_bean;
    private CheckState checkState;
    private void getData(){
        checkStu=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        dateBean=getIntent().getParcelableExtra(TagFinal.TYPE_TAG);
        checkState=getIntent().getParcelableExtra(TagFinal.type);
        classlistBean=getIntent().getParcelableExtra(TagFinal.CLASS_BEAN);
        select_bean=getIntent().getParcelableExtra(TagFinal.ID_TAG);
        IllBean one=new IllBean(TagFinal.TYPE_TOP,"基础信息");
        adapterData.add(one);
//
        IllBean item_one=new IllBean(TagFinal.TYPE_ITEM,"姓名",checkStu.getUsername());
        item_one.setTablevaluetype("base");
        adapterData.add(item_one);
        IllBean item_two=new IllBean(TagFinal.TYPE_ITEM,"所属",classlistBean.getGroupname());
        item_two.setTablevaluetype("base");
        adapterData.add(item_two);

        IllBean item_four=new IllBean(TagFinal.TYPE_ITEM,"记录日期",dateBean.getName());
        item_four.setTablevaluetype("base");
        adapterData.add(item_four);

        IllBean item_three=new IllBean(TagFinal.TYPE_PARENT,"是否返校",dateBean.getName());
        item_three.setTablevaluetype("bool_one");
        item_three.setTablevaluecannull("0");
        adapterData.add(item_three);

        IllBean item_six=new IllBean(TagFinal.TYPE_ITEM,"日期",DateUtils.getCurrentDateValue());
        item_six.setTablevaluetype("date_one");
        item_six.setTablename("日期");
        item_six.setTablevaluecannull("0");
        adapterData.add(item_six);


        IllBean two=new IllBean(TagFinal.TYPE_TOP,select_bean.getIlltypename());
        adapterData.add(two);
        for (IllBean bean:select_bean.getIlltypetable()){
            String type=bean.getTablevaluetype().split("_")[0];
            switch (type){
                case "longtxt":
                    bean.setView_type(TagFinal.TYPE_ITEM);
                    break;
                case "txt":
                    bean.setView_type(TagFinal.TYPE_ITEM);
                    break;
                case "datetime":
                    bean.setView_type(TagFinal.TYPE_ITEM);
                    break;
                case "int":
                    bean.setView_type(TagFinal.TYPE_ITEM);
                    break;
                case "bool":
                    bean.setView_type(TagFinal.TYPE_PARENT);
                    break;
                case Base.DATA_TYPE_IMG:
                    bean.setView_type(TagFinal.TYPE_CHILD);
                    KeyValue zip=new KeyValue(
                            StringUtils.getTextJoint("请填写%1$s",bean.getTablename()),
                            bean.getTablename());
                    zip.setListValue(new ArrayList<String>());
                    bean.setKeyValue(zip);
                    break;
                default:
                    bean.setView_type(TagFinal.TYPE_ITEM);
                    break;
            }
            adapterData.add(bean);
        }

        addParentAdapter.setDataList(adapterData);
        addParentAdapter.setLoadState(TagFinal.LOADING_COMPLETE);

    }




    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.public_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        addParentAdapter=new CheckAddParentAdapter(this);
        recyclerView.setAdapter(addParentAdapter);
        addParentAdapter.setSealChoice(new CheckAddParentAdapter.CheckChoice() {
            @Override
            public void refresh(IllBean bean, int pos_index) {
                position_index=pos_index;
                multi_bean=bean;
            }
        });

    }








    private IllBean multi_bean;
    private int position_index;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.CAMERA:
                    mtask=new MyAsyncTask();
                    mtask.execute(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    Logger.e(photo_a.get(0).getPath());
                    List<String> list_a=new ArrayList<>();
                    for (Photo photo:photo_a){
                        if (photo==null) continue;
                        list_a.add(photo.getPath());
                    }

                    mtask=new MyAsyncTask();
                    mtask.execute(StringUtils.arraysToListString(list_a));
                    break;
                case TagFinal.UI_ADD:


                    Bundle bundle=data.getExtras();
                    IllBean selcet=bundle.getParcelable(TagFinal.OBJECT_TAG);
                    int adapter_index=bundle.getInt("position",-1 );
                    IllBean bean=addParentAdapter.getDataList().get(adapter_index);
                    bean.setValue(selcet.getValue());
                    addParentAdapter.notifyItemChanged(adapter_index, bean);

                    break;
            }
        }
    }


    /**
     * ----------------------------retrofit-----------------------
     */




    public void addChildItem(){

        ArrayList<String> ids=new ArrayList<>();
        ArrayList<String> contants=new ArrayList<>();
        String date="", is_ret="";
        List<IllBean>  datas=addParentAdapter.getDataList();
        for (IllBean illBean:datas){
            if (illBean.getView_type()==TagFinal.TYPE_TOP)continue;
            if (illBean.getTablevaluetype().equals("base"))continue;

            if (illBean.getTablevaluetype().equals("bool")){
                if(StringJudge.isEmpty(illBean.getValue())){
                    illBean.setValue("否");
                }
            }
            if (illBean.getTablevaluetype().equals("date_one")){
                date=illBean.getValue();
                continue;
            }
            if (illBean.getTablevaluetype().equals("bool_one")){
                is_ret=illBean.getValue();
                if (StringJudge.isEmpty(is_ret)){
                    is_ret="否";
                }
                continue;
            }

            String type=illBean.getTablevaluetype().split("_")[0];
            if (type.equalsIgnoreCase(Base.DATA_TYPE_IMG)){
                if (StringJudge.isEmpty(illBean.getKeyValue().getListValue())){
                    illBean.setValue("");
                }else{
                    illBean.setValue(StringUtils.stringToArraysGetString(illBean.getKeyValue().getListValue(),"," ));
                }
            }
            if (is_ret.equals("是")){

            }else{
                if (StringJudge.isEmpty(illBean.getValue())){
                    if (illBean.getTablevaluecannull().equals("0")){
                        toastShow(illBean.getTablename()+"未完成数据");
                        return;
                    }else{
                        continue;
                    }
                }else{
                    ids.add(illBean.getTableid());
                    contants.add(illBean.getValue());
                }
            }


        }
        if(StringJudge.isEmpty(is_ret)){
            is_ret="否";
        }
        if(StringJudge.isEmpty(date)){
            toastShow("未完成日期数据");
            return;
        }



        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckAddChildReq req = new CheckAddChildReq();
        //获取参数
        req.setIllid(checkStu.getIllid());
        req.setIsreturn(is_ret.equals("是")?1:0);
        req.setUsertype(Base.user_check_type);
        req.setReturndate(date);
        req.setIds(StringUtils.arraysToListString(ids));
        req.setContents(StringUtils.arraysToListString(contants));

        reqBody.checkAddChildReq= req;
        evn.body = reqBody;
        Logger.e(evn.toString());
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_add_child(evn);
        call.enqueue(this);
        showProgressDialog("正在加载");
    }

    private void saveImg(String flie_string){
        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        SaveImgReq request = new SaveImgReq();
        //获取参数
        request.setImage_file(flie_string);
        request.setFileext("jpg");
        reqBody.saveImgReq = request;
        reqEnvelop.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().base_save_img(reqEnvelop);
        call.enqueue(this);
        Logger.e(reqEnvelop.toString());
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        Logger.e(TagFinal.ZXX, "onResponse: "+response.code());
        if (response.code()==500){
            toastShow("数据出差了");
        }
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.checkAddChildRes!=null){
                String result=b.checkAddChildRes.result;
                Logger.e(call.request().headers().toString()+result );
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.saveImgRes!=null) {
                String result = b.saveImgRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                SealRes res=gson.fromJson(result,SealRes.class );
                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    List<String> list_c=multi_bean.getKeyValue().getListValue();
                    list_c.add(res.getImg());
                    addParentAdapter.notifyItemChanged(position_index,multi_bean );
                }else{
                    toastShow(res.getError_code());
                }
                if (num==1){
                    dismissProgressDialog();
                }else{
                    num--;
                }
            }

        }else{
            Logger.e(name+"---ResEnv:null");
        }

    }


    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toastShow(R.string.fail_do_not);
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
                base64_list.add(Base64Utils.fileToBase64Str(s));
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





