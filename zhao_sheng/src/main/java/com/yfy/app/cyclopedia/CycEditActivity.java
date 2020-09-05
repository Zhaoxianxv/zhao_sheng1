package com.yfy.app.cyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.cyclopedia.beans.CycSppinner;
import com.yfy.app.cyclopedia.beans.CycSppinner.AngelListBean;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.Base64Utils;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

public class CycEditActivity extends WcfActivity {

    private static final String TAG = "zxx";
    public List<AngelListBean> getAngel_list;
    private LoadingDialog loadingDialog;
    private ExtraRunTask wrapTask;


    @Bind(R.id.cyc_add_mult)
    MultiPictureView add_mult;
    @Bind(R.id.cyc_edit_pro_type)
    Spinner spinner;
    @Bind(R.id.praise_spoor_et)
    EditText edit;
    private String pid;
    private String sp;
    public String title;
    public String tit;
    public String content;
    public String resetimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyc_edit);
        loadingDialog = new LoadingDialog(mActivity);
        pid=getIntent().getStringExtra("note");
        tit=getIntent().getStringExtra("title");

        getPraise_angel();
        initSQToolbar();
        initDialog();
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.addMenuText(1,R.string.send_btn);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isSend()){
                    sendItem();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pid==null){
            pid=getIntent().getStringExtra("note");
            tit=getIntent().getStringExtra("title");
        }
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
        initAbsListView();

    }
    private void initAbsListView() {

        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                Logger.e(TAG, "onAddClick: ");
                typeDialog.showAtBottom();
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
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }


    private void initSpinner(List<AngelListBean> bean) {
        if (bean==null){
            return;
        }
        List<String> list=new ArrayList<>();
        for (int i=0;i<bean.size();i++){
            list.add(bean.get(i).getTitle());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_single_choice ,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        String name=wcfTask.getName();
        if (name.equals("praise")){
            Logger.e("zxx","result  "+result);
            CycSppinner cycsp=gson.fromJson(result,CycSppinner.class);
            getAngel_list=cycsp.getAngel_list();
            initSpinner(getAngel_list);

        }
        if (name.equals("send")){
            if (JsonParser.isSuccess(result)){
                toastShow("发送成功！");
                onPageBack();
            }else{
                toastShow(JsonParser.getErrorCode(result));
            }
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(R.string.fail_loadmore);

    }


    /**
     * 获取点评范畴
     */
    public void getPraise_angel(){
        final String method="ancyclopedia_angel";
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                pid,
                0,
                20
        };
        ParamsTask praise_angel=new ParamsTask(params,method,"praise");
        execute(praise_angel);
    }
    /**
     * 发送词条
     */
    private void sendItem(){
        final String method="ancyclopedia_comment";
        Object[] params=new Object[]{
                Base.user.getSession_key(),
                pid,
                0,
                title,//词条title评论title
                content,//内容
                "",
                "",
                resetimage,//0没有1有
                spinner.getSelectedItem().toString(),
                getAngleid(getAngel_list,spinner.getSelectedItem().toString())
        };
        ParamsTask praise_angel=new ParamsTask(params,method,"send",loadingDialog);
        wrapTask = new ExtraRunTask(praise_angel);
        wrapTask.setExtraRunnable(extraRunnable);
        execute(wrapTask);

    }

    public String getAngleid(List<AngelListBean> getAngel_list,String name){
        for (AngelListBean bean:getAngel_list){
            if (bean.getTitle().equals(name)){
                return bean.getId();
            }
        }
        return "";
    }
    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
//                String path= CompressUtils.compressFileStringSample(uri);
//                if (path==null){
                    p.setPath(uri);
//                }else{
//                    p.setPath(path);
//                }
                list.add(p);
            }
            params[5] = Base64Utils.getZipBase64Str(list);
            params[6] = Base64Utils.getZipTitle2(list);

            return params;
        }
    };

    /**
     * get send content
     */
    private boolean isSend(){
        content=edit.getText().toString();
        if (content==null&content.equals("")){
            toastShow("请输入点评内容！");
            return false;
        }
        if (spinner.getSelectedItem().toString().equals("")){
            toastShow("请选着点评范畴！");
            return false;
        }
        if (add_mult.getList().size()==0){
            resetimage="0";
        }else{
            resetimage="1";
        }
        title=tit+spinner.getSelectedItem().toString();

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


    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        FileCamera camera=new FileCamera(mActivity);
        startActivityForResult(camera.takeCamera(), TagFinal.CAMERA);
    }
    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        Intent intent;
        intent = new Intent(mActivity, com.yfy.app.album.AlbumOneActivity.class);
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
