package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.bean.EventRes;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
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
import com.yfy.view.multi.MultiPictureView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class DelayTeaToClassSetActivity extends WcfActivity {
    private static final String TAG = DelayTeaToClassSetActivity.class.getSimpleName();


    @Bind(R.id.delay_tea_to_class_multi)
    MultiPictureView multi;
    @Bind(R.id.delay_tea_to_class_date)
    TextView date_show;
    @Bind(R.id.delay_tea_to_class_name)
    TextView class_name;
    @Bind(R.id.delay_tea_to_class_reason)
    EditText content_edit;
    @Bind(R.id.delay_tea_to_class_num_edit)
    EditText num_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_tea_to_class_set);
        getData();
        initMulti();
        initDialog();

    }





    private DateBean dateBean;
    private String elecate_id,tea_id;

    private int isallstu=0;
    private void getData(){
        Bundle b=getIntent().getExtras();

        tea_id=b.getString(Base.id);
        elecate_id=b.getString(TagFinal.CLASS_ID);
        isallstu=b.getInt(Base.num);
        String title = b.getString(Base.title );
        dateBean=b.getParcelable(Base.date);





        date_show.setText(dateBean.getName());
        class_name.setText(title);
        initSQtoobar(title);
        num_edit.setFocusable(true);
        num_edit.setHint("请填写实到人数");
        num_edit.setText(String.valueOf(isallstu) );
    }

    private void initSQtoobar(String title ) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT,"添加" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                add();
            }
        });
    }


    private void add(){
        String content=content_edit.getText().toString().trim();
        String num=num_edit.getText().toString().trim();
        if (StringJudge.isEmpty(content))content="";
        String type_ids="";//班级考情


        List<String> stuids=new ArrayList<>();
        List<String> ids=new ArrayList<>();
        ids.add("0");
        stuids.add("0");
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                StringUtils.stringToGetParamsXml(ids),
                elecate_id,
                tea_id,
                StringUtils.stringToGetParamsXml(stuids),
                content,//content
                dateBean.getValue(),//data
                type_ids,//type
                "",//image
                "",//
                num,//stucount
                "0",
                "tea",//checktype
        };
        ParamsTask choice = new ParamsTask(params, TagFinal.DELAY_TEA_ADD,TagFinal.DELAY_TEA_ADD);
        ExtraRunTask wrapTask = new ExtraRunTask(choice);
        wrapTask.setExtraRunnable(extraRunnable);
        showProgressDialog("");
        execute(wrapTask);
    }


    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            int i=0;
            for (String uri:multi.getList()){
                Photo p=new Photo();
                i++;
                String picName = String.valueOf(System.currentTimeMillis())+String.valueOf(i);
                p.setFileName(picName+".jpg");
                p.setPath(uri);
                list.add(p);
            }


            List<String> images=new ArrayList<>();
            images.add("#");
            String image=StringUtils.stringToGetParamsXml(images);

            if (StringJudge.isEmpty(list)){
                params[8] = image.replace("#", "");
            }else{
                params[8] = image.replace("#", Base64Utils.getZipTitleNotice(list));;
            }

            params[9] = Base64Utils.filesToZipBase64Notice(list);
            return params;
        }
    };


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e(result);
        String name=wcfTask.getName();
        if (name.equals(TagFinal.DELAY_TEA_ADD)){
            EventRes res=gson.fromJson(result,EventRes.class );
            if (res.getResult().equals(TagFinal.TRUE)){
                toastShow("设置成功！");
                setResult(RESULT_OK);
                finish();
            }else{
                toastShow(res.getError_code());
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


    ConfirmAlbumWindow album_select;
    private void initDialog() {
        album_select = new ConfirmAlbumWindow(mActivity);
        album_select.setTwo_select(getResources().getString(R.string.album));
        album_select.setOne_select(getResources().getString(R.string.take_photo));
        album_select.setName(getResources().getString(R.string.upload_place));
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

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }


    private void initMulti(){
        multi.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                closeKeyWord();
                album_select.showAtBottom();
            }
        });

        multi.setClickable(false);

        multi.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int index) {
                multi.removeItem(index,true);
            }
        });
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







    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TagFinal.CAMERA:
                    addMult(FileCamera.photo_camera);
                    break;
                case TagFinal.PHOTO_ALBUM:
                    ArrayList<Photo> photo_a = data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a == null) return;
                    if (photo_a.size() == 0) return;
                    setMultList(photo_a);
                    break;
            }
        }
    }






    public void addMult(String uri){
        if (uri==null) return;
        multi.addItem(uri);
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



}
