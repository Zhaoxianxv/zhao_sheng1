package com.yfy.app.maintainnew;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.EditTextActivity;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.maintainnew.bean.DepTag;
import com.yfy.app.maintainnew.bean.MainRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.maintain.MaintainApplyReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Base64Utils;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

public class MaintainNewAddActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = MaintainNewAddActivity.class.getSimpleName();
    private String section_id ;
    private String repair_place = "";
    private String repair_content = "";
    private String tell_string = "";

    private final static int PALCE = 0x1;
    private final static int CONTENT = 0x2;

    @Bind(R.id.maintian_add_item)
    MultiPictureView add_mult;
    @Bind(R.id.trouble_time)
    TextView trouble_time;
    @Bind(R.id.trouble_dep)
    TextView trouble_dep;
    @Bind(R.id.trouble_place)
    EditText trouble_place;
    @Bind(R.id.trouble_content)
    EditText trouble_content;
    @Bind(R.id.main_add_tell)
    EditText tell;
    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintain_new_add);
        dateBean=new DateBean();
        dateBean.setValue_long(System.currentTimeMillis(),false);
        initSQToolbar();
        initDialog();
        initView();
        initAbsListView();
    }



    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.maintain_add);
        toolbar.addMenuText(1,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isSend()) {
                    mtask=new MyAsyncTask();
                    mtask.execute();
                }
            }
        });
    }

    private void initView(){
        String iphone=UserPreferences.getInstance().getTell();
        if (StringJudge.isEmpty(iphone)){

        }else{
            tell.setText(iphone);
        }
    }





    ConfirmAlbumWindow album_select;
    private void initDialog() {
        album_select = new ConfirmAlbumWindow(mActivity);
        album_select.setTwo_select(getResources().getString(R.string.album));
        album_select.setOne_select(getResources().getString(R.string.take_photo));
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

    private void initAbsListView() {
        trouble_time.setText(dateBean.getName());
        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                closeKeyWord();
                album_select.showAtBottom();
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
//				Logger.e(TAG, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    private boolean isSend() {
        if (StringJudge.isEmpty(section_id)) {
            toast("请选择报修类型");
            return false;
        }
        repair_place = trouble_place.getText().toString().trim();
        if (TextUtils.isEmpty(repair_place)) {
            toast("请输入维修地点");
            return false;
        }
        repair_content = trouble_content.getText().toString().trim();
        if (TextUtils.isEmpty(repair_content)) {
            toast("请输入维修事项");
            return false;
        }
        tell_string =tell.getText().toString().trim();
        if (TextUtils.isEmpty(tell_string)) {
            toast("请输入联系电话");
            return false;
        }


        return true;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PALCE:
                    result = data.getExtras().getString("content");
                    trouble_place.setText(result);
                    break;
                case CONTENT:
                    result = data.getExtras().getString("content");
                    trouble_content.setText(result);
                    break;
                case TagFinal.CAMERA:
                    addMult(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    Logger.e(photo_a.get(0).getPath());
                    setMultList(photo_a);
                    break;
                case TagFinal.UI_TAG:
                    DepTag bean =data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    section_id=bean.getId();
                    trouble_dep.setText(bean.getName());
                    break;
            }
        }
    }




    @OnClick(R.id.trouble_dep)
    void setDep(){
        Intent intent=new Intent(mActivity, MaintainTagActivity.class);
        startActivityForResult(intent,TagFinal.UI_TAG);
    }

    @OnClick(R.id.trouble_place)
    void setPlace(){
        Intent intent = new Intent(mActivity, EditTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "维修地点");
        bundle.putString("content", trouble_place.getText().toString().trim());
        bundle.putString("hint", "请输入维修地点");
        intent.putExtras(bundle);
        startActivityForResult(intent, PALCE);
    }

    @OnClick(R.id.trouble_content)
    void setContent(){
        Intent intent = new Intent(mActivity, EditTextActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "维修事项");
        bundle.putString("content", trouble_content.getText().toString().trim());
        bundle.putString("hint", "请输入维修事项");
        intent.putExtras(bundle);
        startActivityForResult(intent, CONTENT);
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
     * ----------------------------retrofit-----------------------
     */


    public void send(){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        MaintainApplyReq req = new MaintainApplyReq();
        //获取参数

        req.setDate(dateBean.getValue());
        req.setNr(StringUtils.stringToUpJson(repair_content));
        req.setAddress(repair_place);
        req.setMobile(tell_string);
        req.setUsername(Base.user.getName());
        req.setPictures(name_s);
        req.setFileContext(content_s);
        req.setClassid(section_id);

        reqBody.maintainApplyReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().maintain_add(reqEnv);
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

            if (b.maintainApplyRes !=null){
                String result=b.maintainApplyRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                if (StringJudge.isSuccess(gson,result)) {
                    toast("发送成功");
                    setResult(RESULT_OK);
                    finish();
                }else{
                    MainRes res=gson.fromJson(result,MainRes.class);
                    toast(res.getError_code());
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
        toast(R.string.fail_do_not);
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
            send();
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
