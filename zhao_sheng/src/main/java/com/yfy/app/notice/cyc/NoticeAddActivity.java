package com.yfy.app.notice.cyc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.notice.NoticeAddReq;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.UnicodeUtil;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.view.SQToolBar.OnMenuClickListener;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeAddActivity extends BaseActivity implements Callback<ResEnv> {

    private final static String TAG = NoticeAddActivity.class.getSimpleName();

    private String receiverid;
    private String receiveruser;
    private String title;
    private String content;

    @Bind(R.id.edit_title_et)
    EditText title_et;
    @Bind(R.id.edit_content_et)
    EditText content_et;
    @Bind(R.id.edit_selected_num)
    TextView selected_num;
    @Bind(R.id.notice_add_mult)
    MultiPictureView add_mult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_add_details);
        initSQToolbar();
        childs.clear();

    }
    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.notice_edit_news);
        toolbar.addMenuText(2,R.string.notice_edit_send);
        toolbar.setOnMenuClickListener(new OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (position==2){
                    if (isSend()){
                        mtask=new MyAsyncTask();
                        mtask.execute();
                    }
                }
            }
        });
        initDialog();
        initAbsListView();

    }


    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        super.onPageBack();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
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
//                Log.e(TAG, "onDeleted: "+add_mult.getList().size() );
            }
        });
        add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Log.e(TagFinal.ZXX, "onItemClicked: "+index );
                Intent intent=new Intent(mActivity, MultPicShowActivity.class);
                Bundle b=new Bundle();
                b.putStringArrayList(TagFinal.ALBUM_SINGE_URI,uris);
                b.putInt(TagFinal.ALBUM_LIST_INDEX,index);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }



    private ArrayList<ChildBean> childs=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.UI_ADD:
                    childs = data.getParcelableArrayListExtra(TagFinal.OBJECT_TAG);
                    selected_num.setText(childs.size()+"");
                    receiverid = StringUtils.subIdStr(childs);
                    receiveruser = StringUtils.subNameStr(childs);
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




    private boolean isSend() {
        title = title_et.getText().toString();
        if (TextUtils.isEmpty(title)) {
            toast("标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(receiverid)) {
            toast("接收人不能为空");
            return false;
        }
        content = content_et.getText().toString().trim();
        String stri = UnicodeUtil.unicodeToString(UnicodeUtil.stringToUnicode(content));
        if (TextUtils.isEmpty(stri) && add_mult.getList().size() == 0) {
            toast("正文、图片需至少选择一项");
            return false;
        }
        return true;
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

    /**
     *   监听部分
     */
    @OnClick(R.id.edit_contacts_chooes)
    void setchooes(){

        Bundle b=new Bundle();
        Intent intent = new Intent(this, ContactsInActivity.class);
        b.putParcelableArrayList(TagFinal.OBJECT_TAG,childs );
        intent.putExtras(b );
        startActivityForResult(intent, TagFinal.UI_ADD);
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
    /**
     * --------------------------retrofit------------------
     */


    public void sendNotice(){
        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        NoticeAddReq req = new NoticeAddReq();
        //获取参数
        req.setTitle(title);
        req.setReceiverid(receiverid);
        req.setReceiveruser(receiveruser);
        req.setContent(StringUtils.stringToUpJson(content));
        req.setName(Base.user.getName());
        req.setPictures_file(content_s);
        req.setPictures_content(name_s);
        reqBody.noticeAddReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().notice_send(reqEnv);
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.noticeAddRes !=null){
                String result=b.noticeAddRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                if (JsonParser.isSuccess(result)) {
                    toast(R.string.success_sned);
                    childs.clear();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    toast(JsonParser.getErrorCode(result));
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
        dismissProgressDialog();
        toast(R.string.fail_do_not);
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
            sendNotice();
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
