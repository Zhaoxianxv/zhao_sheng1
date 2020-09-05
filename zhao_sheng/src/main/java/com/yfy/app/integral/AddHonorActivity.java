package com.yfy.app.integral;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.integral.beans.IntegralResult;
import com.yfy.app.login.bean.TermBean.TermEntity;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.DialogTools;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.calendar.CustomDate;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ExtraRunTask.ExtraRunnable;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.final_tag.Base64Utils;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.DateUtils;
import com.yfy.final_tag.StringJudge;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AddHonorActivity extends WcfActivity {

    private static final String TAG = "zxx";

    private final  int SHOW_PIC = 6;
    private final  int HONOR_TYPE = 7;
    private final  int HONOR_RANK = 8;

    private  String add_date = "";
    private List<TermEntity> terms=new ArrayList<>();
    private List<String> names=new ArrayList<>();

    private MyDialog typeDialog;
    private LoadingDialog loadingDialog;



    @Bind(R.id.integral_honor_add_mult)
    MultiPictureView add_mult;
    @Bind(R.id.honor_chioce_rank)
    TextView chioce_rank;
    @Bind(R.id.honor_chioce_type)
    TextView chioce_type;
    @Bind(R.id.honor_get_date)
    TextView get_date;
    @Bind(R.id.integral_honor_add_et)
    EditText add_content;
    @Bind(R.id.honor_util)
    EditText honorUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_honor_add);
        initSQToolbar();
        initView();
        initDialog();

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
//					Log.e(TAG, "onActivityResult:imgPath  "+photo_a.get(0).toString());
                    break;
                case HONOR_TYPE:
                    String type_name=data.getStringExtra("type_name");
//                    String id=data.getStringExtra("name");
                    chioce_type.setText(type_name);
                    chioce_rank.setText(R.string.un_chioce);
                    chioce_type.setTextColor(Color.BLACK);
                    chioce_rank.setTextColor(Color.GRAY);

                    break;
                case HONOR_RANK:
                    String rank_name=data.getStringExtra("type_name");
//                    String id=data.getStringExtra("name");
                    chioce_rank.setText(rank_name);
                    chioce_rank.setTextColor(Color.BLACK);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //
    public void refresh(){
        String content=add_content.getText().toString().trim();
        String rewardtype=chioce_type.getText().toString();
        String rewardrank=chioce_rank.getText().toString();
        String org=honorUtil.getText().toString();
        if (rewardrank.equals(getResources().getString(R.string.un_chioce))){
            toastShow("请选择荣誉级别");
            return;
        }
        if (rewardtype.equals(getResources().getString(R.string.un_chioce))){
            toastShow("请选择荣誉类型");
            return;
        }
        if (StringJudge.isEmpty(add_mult.getList())){

            toastShow("请上传图片");
            return;
        }
        if (StringJudge.isEmpty(org)){
            toastShow("请填写发奖单位");
            return;
        }
        if (StringJudge.isEmpty(content)){
            content="";
        }

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                0,//id为奖励id  如果新增则为0
                ConvertObjtect.getInstance().getInt(Base.user.getIdU()),
                "stu",
                content,
                add_date,
                rewardtype,
                rewardrank,
                add_mult.getList().size()==0?0:1,//ischangeimage 需要传图片为1，不需要传0
                "",
                "",
                org
        };

        ParamsTask send = new ParamsTask(params, TagFinal.HONOR_ADD,TagFinal.HONOR_ADD,loadingDialog);
        ExtraRunTask wrapTask = new ExtraRunTask(send);
        wrapTask.setExtraRunnable(extraRunnable);
        execute(wrapTask);
//        Log.e("zxx","execute");

    }
    private ExtraRunnable extraRunnable = new ExtraRunnable() {

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
            params[10] = Base64Utils.fileToBase64Str(list.get(0).getPath());
            params[9] = Base64Utils.getFirstPic(list);
            return params;
        }
    };
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        String name=wcfTask.getName();
        IntegralResult re=gson.fromJson(result,IntegralResult.class);
        if (re.getResult().equals("true")){
            toastShow("提交成功");
            setResult(RESULT_OK);
            onPageBack();
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }

    @OnClick(R.id.honor_chioce_rank)
    void setRank(){
        String type=chioce_type.getText().toString();
        if (chioce_type.getCurrentTextColor()==Color.GRAY){
            mDialog("请选择一个荣誉类型", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivityForResult(new Intent(mActivity,HonorTypeActivity.class),HONOR_TYPE);
                }
            });
        }else{
            Intent intent=new Intent(mActivity,HonorRankActivity.class);
            Logger.e("zxx",type);
            intent.putExtra("type",type);
            startActivityForResult(intent,HONOR_RANK);
        }

    }
    @OnClick(R.id.honor_chioce_type)
    void setType(){
        startActivityForResult(new Intent(mActivity,HonorTypeActivity.class),HONOR_TYPE);

    }
    @OnClick(R.id.honor_get_date)
    void setDate(){
        DialogTools
                .getInstance()
                .showDatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


                        String date = DateUtils.getDate(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                "-"
                        );
                        get_date.setText(date);
                        add_date=DateUtils.getDate(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                    }
                });
    }
    @OnClick(R.id.honor_util)
    void setHonorUtil(){
        honorUtil.setSelection(honorUtil.getText().toString().length());
    }


    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("新增荣誉");
        toolbar.addMenuText(1,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {


                refresh();
            }
        });

        initAbsListView();

    }
    private void initAbsListView() {

        add_mult.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
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



    private void initView() {
        chioce_type.setTextColor(Color.GRAY);
        chioce_rank.setTextColor(Color.GRAY);
        chioce_rank.setText(R.string.un_chioce);
        chioce_type.setText(R.string.un_chioce);
        CustomDate date=new CustomDate();
        add_date=date.toString();
        get_date.setText(date.toString("-"));
        honorUtil.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()>15){
                    editable.delete(15,editable.toString().length());
                }
            }
        });
//
    }






    private void initDialog() {
        typeDialog = new MyDialog(mActivity, R.layout.dialog_getpic_type_popup,
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle },
                new int[] { R.id.take_photo, R.id.alnum, R.id.cancle });
        loadingDialog = new LoadingDialog(mActivity);
        typeDialog.setOnCustomDialogListener(listener);

        typeDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);

    }
    private AbstractDialog.OnCustomDialogListener listener = new AbstractDialog.OnCustomDialogListener() {
        @Override
        public void onClick(View v, AbstractDialog dialog) {
            Intent intent;
            switch (v.getId()) {


                case R.id.take_photo:
                    PermissionTools.tryCameraPerm(mActivity);
                    dialog.dismiss();
                    break;
                case R.id.alnum:
                    //访问sd卡权限
                    PermissionTools.tryWRPerm(mActivity);
                    dialog.dismiss();
                    break;
                case R.id.cancle:
                    dialog.dismiss();
                    break;
            }
        }
    };






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
        b.putBoolean(TagFinal.ALBUM_SINGLE, true);
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
