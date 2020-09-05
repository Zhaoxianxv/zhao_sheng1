package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.bean.EventClass;
import com.yfy.app.delay_service.bean.EventRes;
import com.yfy.app.delay_service.bean.OperType;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ColorRgbUtil;
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
import butterknife.OnClick;

public class DelayTeaToStuMoreSetActivity extends WcfActivity {
    private static final String TAG = DelayTeaToStuMoreSetActivity.class.getSimpleName();


    @Bind(R.id.delay_tea_to_stu_set_multi)
    MultiPictureView multi;
    @Bind(R.id.delay_tea_to_stu_set_date)
    AppCompatTextView date_show;
    @Bind(R.id.delay_tea_to_stu_set_user)
    AppCompatTextView user_name;
    @Bind(R.id.delay_tea_to_stu_set_type)
    AppCompatTextView delay_type;
    @Bind(R.id.delay_tea_to_stu_set_reason_edit)
    EditText reason_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_tea_tostu_set);
        initSQtoobar("学生考勤");
        getData();
        initMulti();
        initDialog();

    }


    private String elecate_id,tea_id;
    private DateBean dateBean;
    private ArrayList<EventClass> sstu=new ArrayList<>();
    private void getData(){
        Bundle b=getIntent().getExtras();
        if (StringJudge.isContainsKey(b,TagFinal.CLASS_ID)){
            elecate_id=b.getString(TagFinal.CLASS_ID);
        }else{
            elecate_id= "0";
        }
        if (StringJudge.isContainsKey(b,TagFinal.FORBID_TAG)){
            tea_id=b.getString(TagFinal.FORBID_TAG);
        }else{
            tea_id= "0";
        }
        if (StringJudge.isContainsKey(b,Base.date)){
            dateBean=b.getParcelable(Base.date);
        }else{
            dateBean=new DateBean();
            dateBean.setValue_long(System.currentTimeMillis(),true);
        }
        sstu=b.getParcelableArrayList(TagFinal.GETNOTICENUM);

        if (StringJudge.isEmpty(sstu))return;
        stu=sstu.get(index);
        if (stu==null)return;
        user_name.setText(stu.getStuname());

        date_show.setText(dateBean.getName());
    }
    private EventClass stu;
    private int index=0;



    private void initSQtoobar(String title ) {
        assert toolbar!=null;
        toolbar.setTitle(title);
        toolbar.addMenuText(TagFinal.ONE_INT,"添加" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                closeKeyWord();
                if (stu==null){
                    toastShow("当前没有选中学生");
                    return;
                }
                List<String> stuids=new ArrayList<>();
                List<String> ids=new ArrayList<>();
                ids.add("0");
                stuids.add(stu.getStuid());
                add(StringUtils.stringToGetParamsXml(ids),StringUtils.stringToGetParamsXml(stuids));
            }
        });
    }









    private void add(String ids,String stu_ids){
        String content=reason_edit.getText().toString().trim();
        if (StringJudge.isEmpty(content))content="";

        if (select_opertype==null){
            toastShow("请选择考评类型");
            return;
        }
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                ids,
                elecate_id,
                tea_id,
                stu_ids,
                StringUtils.stringToUpJson(content),//content
                dateBean.getValue(),//data
                select_opertype.getOpearname(),//type
                "",//image
                "",//
                "0",//stucount
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
                index++;
                if (sstu.size()==index){
                    setResult(RESULT_OK);
                    finish();
                    return false;
                }else{
                    stu=sstu.get(index);
                    user_name.setText(stu.getStuname());
                    multi.clearItem();
                    reason_edit.setText("");
                    select_opertype=null;
                    delay_type.setText("请选择");
                    delay_type.setTextColor(ColorRgbUtil.getGray());
                }

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



    private OperType select_opertype;
    @OnClick(R.id.delay_tea_to_stu_set_type)
    void setOval(){
        Intent intent=new Intent(mActivity,DelayServiceOperActivity.class);
//        isclass 0为给学生打分选项 1为给班级打分选项
        int isclass=0;
        intent.putExtra(Base.id, isclass);
        startActivityForResult(intent,TagFinal.UI_ADMIN );
    }






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
                case TagFinal.UI_ADMIN:

                    select_opertype=data.getParcelableExtra(Base.data);
                    delay_type.setText(select_opertype.getOpearname());

                    delay_type.setTextColor(ColorRgbUtil.getBaseText());
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
