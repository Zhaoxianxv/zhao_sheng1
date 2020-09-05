package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnClick;
import com.example.zhao_sheng.R;
import com.google.gson.reflect.TypeToken;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.glide.FileCamera;
import com.yfy.final_tag.glide.Photo;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.goods.bean.Content;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.base.activity.WcfActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.MyDialog;
import com.yfy.final_tag.*;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ExtraRunTask;
import com.yfy.net.loading.task.ParamsTask;
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

public class GoodsDoActivity extends WcfActivity {

    @Bind(R.id.goods_content)
    EditText edit;
    @Bind(R.id.goods_do_mult)
    MultiPictureView add_mult;

    @Bind(R.id.goods_do_one_icon)
    AppCompatImageView one;
    @Bind(R.id.goods_do_two_icon)
    AppCompatImageView two;
    @Bind(R.id.goods_do_two_o_icon)
    AppCompatImageView two_;
    @Bind(R.id.goods_do_three_icon)
    AppCompatImageView three;
    @Bind(R.id.goods_do_four_icon)
    AppCompatImageView four;
    @Bind(R.id.goods_do_fiv_icon)
    AppCompatImageView fiv;

    @Bind(R.id.goods_do_one)
    RelativeLayout one_layout;
    @Bind(R.id.goods_do_two)
    RelativeLayout two_layout;
    @Bind(R.id.goods_do_two_)
    RelativeLayout twoo_layout;
    @Bind(R.id.goods_do_three)
    RelativeLayout three_layout;
    @Bind(R.id.goods_do_four)
    RelativeLayout four_layout;
    @Bind(R.id.goods_do_fiv)
    RelativeLayout fiv_layout;



    @Bind(R.id.goods_add_content)
    RelativeLayout box_layout;
    @Bind(R.id.goods_add_content_icon)
    AppCompatImageView box_icon;
    @Bind(R.id.goods_add_content_name)
    TextView box_name;
    private String id="";
    private String state="";
    private boolean is;
    private boolean is_box=false;
    private boolean is_chioce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_do);
        initSQtoolbar();
        getData();
        initAbsListView();
        initTypeDialog();
        edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (is_chioce) is_chioce=false;
                Logger.e(TagFinal.ZXX, "afterTextChanged: 1" );
            }
        });

    }


    private void initSQtoolbar() {
        box_name.setTextColor(ColorRgbUtil.getGrayText());
        box_icon.setColorFilter(ColorRgbUtil.getGrayText());
        assert toolbar!=null;
        toolbar.setTitle("物品");
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


    private String yes_state,no_state;
    private void getData(){
        Intent intent=getIntent();
        id=intent.getStringExtra(TagFinal.ID_TAG);
        yes_state=intent.getStringExtra("yes_state");
        no_state=intent.getStringExtra("no_state");
        is = intent.getBooleanExtra(TagFinal.TYPE_TAG, false);
        if (is){
            one_layout.setVisibility(View.GONE);
            two_layout.setVisibility(View.GONE);
            twoo_layout.setVisibility(View.GONE);
            three_layout.setVisibility(View.GONE);
            four_layout.setVisibility(View.VISIBLE);
            fiv_layout.setVisibility(View.VISIBLE);
        }else{

            one_layout.setVisibility(View.VISIBLE);
            two_layout.setVisibility(View.VISIBLE);
            twoo_layout.setVisibility(View.VISIBLE);
            three_layout.setVisibility(View.VISIBLE);
            four_layout.setVisibility(View.GONE);
            fiv_layout.setVisibility(View.GONE);
        }
    }
    private void isCan(){
        //新增武平 20
        String conent=edit.getText().toString().trim();
        if (StringJudge.isEmpty(conent)){
            conent="";
        }
        if (is){
            admin(state, conent);
        }else{
            if (StringJudge.isEmpty(state)){
                toastShow("请选择状态 ");
                return;
            }
            admin(state, conent);
        }

    }
    public void admin(String stuatus,String remark){


        Object[] params = new Object[] {
                Base.user.getSession_key(),
                Base.user.getName(),
                id,
                stuatus,
                remark,
                "",
                ""
        };
        ParamsTask addLeave = new ParamsTask(params, TagFinal.GOODS_ADMIN_APPLY, TagFinal.GOODS_ADMIN_APPLY);
        ExtraRunTask wrapTask = new ExtraRunTask(addLeave);
        wrapTask.setExtraRunnable(extraRunnable);
        execute(wrapTask);
        showProgressDialog("正在加载");
        if (StringJudge.isEmpty(remark))return;
        if (is_box){
            List<Content> contents;
            contents = gson.fromJson( UserPreferences.getInstance().getGoodsJson(), new TypeToken<List<Content>>(){}.getType());
            Content content=new Content();
            content.setContent(remark);
            if (StringJudge.isEmpty(contents)){
                content.setInde("0");
            }else{
                content.setInde(""+contents.size());
            }

            if (StringJudge.isEmpty(contents)){
                contents=new ArrayList<>();
            }
            contents.add(content);


            String index_s=UserPreferences.getInstance().getGoodsIndex();
            UserPreferences.getInstance().saveGoodsIndex(index_s+","+content.getInde());
            UserPreferences.getInstance().saveGoodsJson(gson.toJson(contents));
        }
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
            params[5] = Base64Utils.getZipTitle2(list);
            params[6] = Base64Utils.getZipBase64Str(list);
            return params;
        }
    };





    //==============add icon============


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



    private void initView(AppCompatImageView view){
        one.setVisibility(View.GONE);
        two.setVisibility(View.GONE);
        two_.setVisibility(View.GONE);
        three.setVisibility(View.GONE);
        fiv.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);

    }
    @OnClick(R.id.goods_do_one)
    void setOne(){
        //准备中 1
        state="1";
        initView(one);
    }
    @OnClick(R.id.goods_do_two)
    void setTwo_layout(){
        //已准备 10
        state="10";
        initView(two);
    }
    @OnClick(R.id.goods_do_two_)
    void setTw_layout(){
        //已准备 10
        state="11";
        initView(two_);
    }
    @OnClick(R.id.goods_do_three)
    void setthree(){
        //驳回 2
        state="2";
        initView(three);
    }
    @OnClick(R.id.goods_do_four)
    void setfuor(){
        state=yes_state;//同意
        initView(four);
    }
    @OnClick(R.id.goods_do_fiv)
    void setFiv(){
        state=no_state;//拒绝
        initView(fiv);
    }


    @OnClick(R.id.goods_add_content)
    void setChange(){
        if (is_box){
            is_box=false;
            box_name.setTextColor(ColorRgbUtil.getGrayText());
            box_icon.setColorFilter(ColorRgbUtil.getGrayText());
        }else{
            is_box=true;
            box_name.setTextColor(ColorRgbUtil.getOrange());
            box_icon.setColorFilter(ColorRgbUtil.getOrange());
        }

    }

    @OnClick(R.id.goods_content_list)
    void setChioce(){
        List<Content> contents= gson.fromJson( UserPreferences.getInstance().getGoodsJson(), new TypeToken<List<Content>>(){}.getType());
        if (StringJudge.isEmpty(contents)){
            toastShow("暂未常用语句");
            return;
        }
        Intent intent=new Intent(mActivity,ContentActivity.class);
        startActivityForResult(intent, TagFinal.UI_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG:
                    String conten=data.getStringExtra(TagFinal.OBJECT_TAG);
                    edit.setText(conten);
                    box_name.setTextColor(ColorRgbUtil.getGrayText());
                    box_icon.setColorFilter(ColorRgbUtil.getGrayText());
                    is_chioce=true;
                    Logger.e(TagFinal.ZXX, "afterTextChanged: 2" );
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
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (!isActivity())return false;
        dismissProgressDialog();
        Logger.e(TagFinal.ZXX, "onSuccess: "+result );
        GoodsRes res=gson.fromJson(result,GoodsRes.class);
        if (res.getResult().equals(TagFinal.TRUE)){
            setResult(RESULT_OK);
            onPageBack();
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
}
