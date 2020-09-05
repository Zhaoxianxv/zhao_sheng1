package com.yfy.app.goods;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodAddApplyReq;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.GType;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.goods.bean.GoodsType;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.*;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GoodsAddActivity extends WcfActivity implements Callback<ResEnv> {

    private static final String TAG = GoodsAddActivity.class.getSimpleName();
    @Bind(R.id.goods_add_type)
    TextView type;
    @Bind(R.id.goods_add_contacts)
    TextView contacts;

    @Bind(R.id.goods_num_layout)
    RelativeLayout num_layout;
    @Bind(R.id.goods_contacts_layout)
    RelativeLayout contact_layout;

    @Bind(R.id.goods_type_layout)
    RelativeLayout type_layout;

    @Bind(R.id.goods_content_layout)
    LinearLayout content_layout;

    @Bind(R.id.goods_add_num)
    EditText num_edit;
    @Bind(R.id.goods_content)
    EditText content_edit;
    @Bind(R.id.goods_add_mult)
    MultiPictureView add_mult;
    @Bind(R.id.goods_add_goods_icon)
    AppCompatImageView box;
    private boolean isbox=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_add);
        initView(false);
        initSQtoolbar();
        initTypeDialog();
        initAbsListView();
        num_edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>10){
                    s.delete(0, 10);
                }
            }
        });
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle("申请物品");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        isCan();
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



    public void initView(boolean is){
        if (is){
            num_layout.setVisibility(View.GONE);
            type_layout.setVisibility(View.GONE);
        }else{
            num_layout.setVisibility(View.VISIBLE);
            type_layout.setVisibility(View.VISIBLE);
            contact_layout.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.goods_add_type)
    void setChioceType(){

        Intent intent=new Intent(mActivity,GoodsTagActivity.class);
        intent.putExtra(TagFinal.TYPE_TAG,false );
        intent.putExtra(Base.type,false);
        startActivityForResult(intent, TagFinal.UI_CONTENT);


    }
    @OnClick(R.id.goods_add_contacts)
    void setChioceContacts(){
        Intent intent=new Intent(mActivity,GoodsAdminContactsActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADMIN);
    }


    private GoodsType goodsType;
    @OnClick(R.id.goods_add_goods)
    void change(){
        if (isbox){
            isbox=false;
            box.setColorFilter(ColorRgbUtil.getGrayText());
        }else{
            isbox=true;
            box.setColorFilter(ColorRgbUtil.getOrange());
        }
//        initView(isbox);

        if (isbox){
            content_layout.setVisibility(View.VISIBLE);
        }else{
            if (StringJudge.isNull(goodsType)){
                content_layout.setVisibility(View.GONE);
            }else{
                if (goodsType.getCanreply().equals(TagFinal.FALSE)){
                    content_layout.setVisibility(View.GONE);
                }else{
                    content_layout.setVisibility(View.VISIBLE);
                }
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.CAMERA:
                    addMult(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    setMultList(photo_a);
                    break;
                case TagFinal.UI_CONTENT:
                    goodsType = data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    switch (goodsType.getCanreply()){
                        case TagFinal.FALSE:
                            content_layout.setVisibility(View.GONE);
                            num_layout.setVisibility(View.VISIBLE);
                            type_layout.setVisibility(View.VISIBLE);
                            contact_layout.setVisibility(View.GONE);
                            master="0";
                            break;
                        case TagFinal.TRUE:
                            master="0";
                            content_layout.setVisibility(View.VISIBLE);
                            num_layout.setVisibility(View.VISIBLE);
                            type_layout.setVisibility(View.VISIBLE);
                            contact_layout.setVisibility(View.GONE);
                            break;
                        case TagFinal.TYPE_TAG:
                            type.setClickable(false);
                            content_layout.setVisibility(View.VISIBLE);
                            num_layout.setVisibility(View.GONE);
                            type_layout.setVisibility(View.VISIBLE);
                            contact_layout.setVisibility(View.VISIBLE);
                            break;
                    }
                    type.setText(goodsType.getName());
                    type.setTextColor(ColorRgbUtil.getBaseText());

                    break;
                case TagFinal.UI_ADMIN:
                    GType gtype=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    master=gtype.getTypeid();
                    contacts.setText(gtype.getTypename());
                    contacts.setTextColor(ColorRgbUtil.getBaseText());
                    break;

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public String master="0";
    private void isCan(){
        String reark=content_edit.getText().toString().trim();
        String num=num_edit.getText().toString().trim();
        if(StringJudge.isNull(goodsType)){
            toastShow("请选择物品类型");
            return;
        }else{
            if (goodsType.getCanreply().equals(TagFinal.TYPE_TAG)){//未在清单
                if (StringJudge.isEmpty(reark)){
                    toastShow("请填写备注");
                    return;
                }
                if (master.equals("0")){
                    toastShow("请选择审核人");
                    return;
                }
                submit("0",reark ,"0", master);
            }else if (goodsType.getCanreply().equals(TagFinal.FALSE)){//不要备注
                if (StringJudge.isEmpty(num)){
                    toastShow("请选确定物品数量");
                    return;
                }
                if (num.equals("0")){
                    toastShow("物品数量不为0");
                    return;
                }
                submit(num,"" ,goodsType.getId(),master );
            }else{//要备注
                if (StringJudge.isEmpty(num)){
                    toastShow("请选确定物品数量");
                    return;
                }
                if (StringJudge.isEmpty(reark)){
                        toastShow("请填写备注");
                        return;
                }

                if (StringJudge.isEmpty(num)){
                    toastShow("请选确定物品数量");
                    return;
                }
                submit(num,reark ,goodsType.getId(),master );

            }

        }
    }





    //==============addicon============


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

    private void initAbsListView() {

        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                Logger.e(TagFinal.ZXX, "onAddClick: ");
                typeDialog.showAtBottom();
            }
        });

        add_mult.setClickable(false);

        add_mult.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int index) {
                add_mult.removeItem(index,true);
//                Logger.e(TAG, "onDeleted: "+add_mult.getList().size() );
            }
        });
        add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Logger.e(TagFinal.ZXX, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    private MyDialog typeDialog;
    private void initTypeDialog() {
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



    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        GoodsRes res=gson.fromJson(result,GoodsRes.class);
        if (res.getResult().equals(TagFinal.TRUE)){
            finish();
        }else{
            toastShow(res.getError_code());
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
    }

    /**
     * ----------------------------retrofit-----------------------
     */
    ReqEnv reqEnv = new ReqEnv();
    ReqBody reqBody = new ReqBody();
    GoodAddApplyReq req = new GoodAddApplyReq();

    public void submit(String count,String remark,String id,String master){
        mtask=new MyAsyncTask();
        mtask.execute();



        //获取参数
        req.setCount(ConvertObjtect.getInstance().getInt(count));
        req.setRealname(Base.user.getName());
        req.setId(ConvertObjtect.getInstance().getInt(id));
        req.setMasterid(ConvertObjtect.getInstance().getInt(master));
        req.setRemark(remark);


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
            if (b.goodAddApplyRes !=null){
                String result=b.goodAddApplyRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    finish();
                }else{
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
        Logger.e( "onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }













    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        FileCamera camera = new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }

    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent = new Intent(mActivity, AlbumOneActivity.class);
        Bundle b = new Bundle();
        b.putInt(TagFinal.ALBUM_LIST_INDEX, 0);
        b.putBoolean(TagFinal.ALBUM_SINGLE, false);
        intent.putExtras(b);
        startActivityForResult(intent, TagFinal.PHOTO_ALBUM);
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
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
                i++;
                String picName = String.valueOf(System.currentTimeMillis())+String.valueOf(i);
                p.setFileName(picName+".jpg");
                p.setPath(uri);
                photo_list.add(p);
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
            req.setImages(name_s);
            req.setImage_file(content_s);
            reqBody.goodAddApplyReq = req;
            reqEnv.body = reqBody;
            Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_add_apply(reqEnv);
            call.enqueue(GoodsAddActivity.this);


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
