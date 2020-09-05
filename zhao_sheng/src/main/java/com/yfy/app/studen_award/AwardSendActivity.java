package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.integral.ChangeTermActivity;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

public class AwardSendActivity extends WcfActivity {
    private static final String TAG = AwardSendActivity.class.getSimpleName();

    @Bind(R.id.award_selected_term)
    TextView term_Chioce;

    @Bind(R.id.award_selected_type)
    TextView type_Chioce;



    @Bind(R.id.award_add_mult)
    MultiPictureView add_mult;
    @Bind(R.id.award_content_et)
    EditText content_et;
    @Bind(R.id.award_score_layout)
    RelativeLayout layout;

    @Bind(R.id.radio_group_award)
    RadioGroup radioGroup;
    private final int AWARD_TYPE=9;
    private String type_id;

    private List<Photo> photoList = new ArrayList<Photo>();

    private String stu_id;
    private int  class_id;
    private String id;
    private String time;
    private String term_id;
    private String score="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_send_teacher);
        getData();
        initSQToolbar();
        initView();
        initDialog();
    }
    public void getData(){

        term_id=UserPreferences.getInstance().getTermId();
        term_Chioce.setText(UserPreferences.getInstance().getTermName());
        term_Chioce.setTextColor(ColorRgbUtil.getBaseText());
        stu_id=getIntent().getStringExtra("stu_id");
        class_id=getIntent().getIntExtra("class_id",-1);
        id=getIntent().getStringExtra("id_more");
        if (stu_id==null){
            stu_id="";
            id="0";
        }
    }
    private void initView() {
        CustomDate data=new CustomDate();
        time=data.toString();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.award_score_minus:
                        score="-1";
                        break;
                    case R.id.award_score_add:
                        score="1";
                        break;

                }
            }
        });
    }
    private void initSQToolbar() {
        assert toolbar!=null;
        if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_STU)){
            layout.setVisibility(View.GONE);
        }else{
            layout.setVisibility(View.VISIBLE);
        }
        toolbar.setTitle("老师发放奖励");
        toolbar.addMenuText(1,R.string.send_btn);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isfull()){
                    if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_STU)){
                        submitAward(term_id);
                    }else{
                        send(term_id);
                    }
                }
            }
        });
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
                Logger.e( "onAddClick: ");
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

    public boolean isfull(){

        if (StringJudge.isEmpty(type_id)){
            toastShow("请选择奖励类型");
            return false;
        }
        if (StringJudge.isEmpty(term_id)){
            toastShow("请选择奖励学期");
            return false;
        }
        if (StringJudge.isEmpty(score)){
            if (Base.user.getUsertype().equals(TagFinal.USER_TYPE_TEA)){
                toastShow("请打分");
                return false;
            }
        }
        String content=content_et.getText().toString().trim();
//        if (StringJudge.isEmpty(content)){
////            toast(R.string.award_tea_send_scan);
//            return true;
//        }
        return true;
    }

    public void send(String term_id){
        String content=content_et.getText().toString().trim();
        Object[] param=new Object[]{
                Base.user.getSession_key(),
                id,//id为0是新增，不为0是修改
                time,
                stu_id,
                ConvertObjtect.getInstance().getInt(type_id),
                StringJudge.isEmpty(content)?"":content,
                1,//是否修改图片，0位不修改，1 位修改
                photoList,//
                photoList,//
                score,//
                term_id
        };
        ParamsTask send=new ParamsTask(param,TagFinal.AWARD_TEA_ADD,TagFinal.AWARD_TEA_ADD);
        ExtraRunTask wrapTask = new ExtraRunTask(send);
        wrapTask.setExtraRunnable(extraRunnable);
        showProgressDialog("");
        execute(wrapTask);
    }
    public void submitAward(String term_id){
        String content=content_et.getText().toString().trim();
        Object[] param=new Object[]{
                Base.user.getSession_key(),
                "0",//id为0是新增，不为0是修改
                time,
                ConvertObjtect.getInstance().getInt(type_id),
                StringJudge.isEmpty(content)?"":content,
                0,//是否修改图片，0位不修改，1 位修改
                photoList,//
                photoList,//
                term_id

        };
        ParamsTask submit=new ParamsTask(param,TagFinal.AWARD_STU_ADD,TagFinal.AWARD_STU_ADD);
        ExtraRunTask stu_task = new ExtraRunTask(submit);
        stu_task.setExtraRunnable(stu_extraRunnable);
        showProgressDialog("");
        execute(stu_task);
    }
    private ExtraRunTask.ExtraRunnable extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
                p.setPath(uri);
                list.add(p);
            }
            params[8] = Base64Utils.getZipBase64Str(list);
            params[7] = Base64Utils.getZipTitle2(list);
            return params;
        }
    };
    private ExtraRunTask.ExtraRunnable stu_extraRunnable = new ExtraRunTask.ExtraRunnable() {

        @Override
        public Object[] onExecute(Object[] params) {
            List<Photo> list=new ArrayList<>();
            for (String uri:add_mult.getList()){
                Photo p=new Photo();
                    p.setPath(uri);
                list.add(p);
            }
            params[7] = Base64Utils.getZipBase64Str(list);
            params[6] = Base64Utils.getZipTitle2(list);
            return params;
        }
    };

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e("zxx", "onSuccess: "+result);
        if (JsonParser.isSuccess(result)){
            toastShow(R.string.success_sned);
            setResult(RESULT_OK);
            finish();
        }else{
            toastShow(R.string.success_not_do);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        if (!isActivity())return;
        dismissProgressDialog();
        toastShow(R.string.fail_send);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case AWARD_TYPE:
//                    award_type.setText(data.getStringExtra("award_name"));
//                    award_type.setTextColor(Color.BLACK);
                    type_id=data.getStringExtra("award_id");
                    type_Chioce.setText(data.getStringExtra("award_name"));
                    type_Chioce.setTextColor(ColorRgbUtil.getBaseText());
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
                case TagFinal.UI_TAG:
                    String name;
                    name=data.getStringExtra("data_name");
                    term_id=data.getStringExtra("data_id");
                    term_Chioce.setText(name);
                    term_Chioce.setTextColor(ColorRgbUtil.getBaseText());
                    break;


            }
        }

    }

    @OnClick(R.id.award_selected_term)
    void setChoiceTerm(){
        Intent intent=new Intent(mActivity,ChangeTermActivity.class);
        startActivityForResult(intent,TagFinal.UI_TAG);
    }

    @OnClick(R.id.award_selected_type)
    void setChoiceType(){
        Intent intent=new Intent(mActivity,AwardTypeActivity.class);
        intent.putExtra("class_id",class_id);
        Logger.e("zxx"," class_id "+class_id);
        startActivityForResult(intent,AWARD_TYPE);
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
}
