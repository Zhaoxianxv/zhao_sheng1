package com.yfy.app.delay_service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.ContactSingeActivity;
import com.yfy.app.album.AlbumOneActivity;
import com.yfy.app.album.MultPicShowActivity;
import com.yfy.app.bean.DateBean;
import com.yfy.app.delay_service.adapter.TagAdapter;
import com.yfy.app.delay_service.bean.AbsStu;
import com.yfy.app.delay_service.bean.DelayAbsenteeismClass;
import com.yfy.app.delay_service.bean.EventRes;
import com.yfy.app.delay_service.bean.OperType;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.delay_service.DelayGetTypeReq;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
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
import com.yfy.final_tag.recycerview.RecycleViewDivider;
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

public class DelayAdminToTeaEventActivity extends WcfActivity implements Callback<ResEnv> {
    private static final String TAG =DelayAdminToTeaEventActivity.class.getSimpleName();
    @Bind(R.id.delay_admin_to_tea_event_multi)
    MultiPictureView multi;
    @Bind(R.id.delay_admin_to_tea_event_date)
    TextView date_show;
    @Bind(R.id.delay_admin_to_tea_event_site)
    TextView event_site;
    @Bind(R.id.delay_admin_to_tea_event_tea_name)
    TextView tea_name;
    @Bind(R.id.delay_admin_to_tea_event_replace_tea_layout)
    RelativeLayout repleace_layout;
    @Bind(R.id.delay_admin_to_tea_event_replace_tea)
    TextView replace_tea;
    @Bind(R.id.delay_admin_to_tea_event_class_name)
    TextView class_name;
    @Bind(R.id.delay_admin_to_tea_event_reason_edit)
    EditText content_edit;


    @Bind(R.id.delay_admin_to_tea_event_num_edit)
    EditText num_edit;
    @Bind(R.id.delay_admin_to_tea_event_type_layout)
    RelativeLayout type_layout;


    private DateBean dateBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delay_admin_to_tea_event);
        getData();
//        getOper(1);
        initMulti();
        initDialog();
        initSQtoobar();


    }





    public DelayAbsenteeismClass bean;
    private void getData(){
        Bundle b=getIntent().getExtras();
//        String title = b.getString(Base.title );
        dateBean=b.getParcelable(Base.date);
        bean=b.getParcelable(Base.data);
        date_show.setText(dateBean.getName());


        if (bean!=null)initView();
        if (StringJudge.isEmpty(bean.getTeacher_list())){
            toast("班级尚未安排教师");
        }else{
            if (bean.getTeacher_list().size()==1){
                selcet_CPWBean=new CPWBean(bean.getTeacher_list().get(0).getTeachername(), bean.getTeacher_list().get(0).getTeacherid());
                tea_name.setText(selcet_CPWBean.getName());
            }
        }
    }



    private RecyclerView list_view;
    private List<OperType> select_oval=new ArrayList<>();
    private TagAdapter adapter;
    private void initView(){
        class_name.setText(StringUtils.getTextJoint("%1$s(总数:%2$s)",bean.getElectivename(),bean.getStucount()));
        date_show.setText(dateBean.getName());
        event_site.setText(bean.getElectiveaddr());

        initDialogList();
        num_edit.setText(bean.getStucount() );

        //-----------list----------
        list_view = (RecyclerView) findViewById(R.id.delay_admin_to_tea_event_type_list);

        list_view.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        list_view.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        list_view.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new TagAdapter(mActivity);
        list_view.setAdapter(adapter);

        type_layout.setVisibility(View.GONE);
    }




    public CPWListBeanView cpwListBeanView;
    List<CPWBean> cpwBeans=new ArrayList<>();
    private CPWBean selcet_CPWBean;
    private void setCPWlListBeanData(){
        if (StringJudge.isEmpty(bean.getTeacher_list())){
            toast(R.string.success_not_details);
            return;
        }else{
            cpwBeans.clear();
            for(AbsStu opear:bean.getTeacher_list()){
                CPWBean cpwBean =new CPWBean();
                cpwBean.setName(opear.getTeachername());
                cpwBean.setId(opear.getTeacherid());
                cpwBeans.add(cpwBean);
            }
        }

        closeKeyWord();
        cpwListBeanView.setDatas(cpwBeans);
        cpwListBeanView.showAtCenter();

    }
    private void initDialogList(){
        cpwListBeanView = new CPWListBeanView(mActivity);
        cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
            @Override
            public void onClick(CPWBean cpwBean,String type) {
                tea_name.setText(cpwBean.getName());
                selcet_CPWBean=cpwBean;
                switch (cpwBean.getId()) {
                    case "-1":
                        tea_name.setTextColor(ColorRgbUtil.getOrange());
                        break;
                    default:
                        tea_name.setTextColor(ColorRgbUtil.getBaseText());
                        break;
                }
                cpwListBeanView.dismiss();
            }
        });
    }


    @OnClick(R.id.delay_admin_to_tea_event_tea_name)
    void setChoiceTea(){
        if (cpwListBeanView!=null){
            setCPWlListBeanData();
        }else{
            toast("没有匹配的教师");
        }
    }

    @OnClick(R.id.delay_admin_to_tea_event_replace_tea)
    void setChoiceReplaceTea(){
        Intent intent=new Intent(mActivity,ContactSingeActivity.class);
        startActivityForResult(intent, TagFinal.UI_ADMIN);
    }
    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("巡查");
        toolbar.addMenuText(TagFinal.ONE_INT,"添加" );
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {

                add();
            }
        });
    }









    private String substituteteacher="0";

    private void add(){
        String content=content_edit.getText().toString().trim();
        if (StringJudge.isEmpty(content))content="";
        String type_ids="";

        String stucount=num_edit.getText().toString().trim();
        if(StringJudge.isEmpty(stucount)){
            toast("请填写学生数量");
            return;
        }
        if(selcet_CPWBean==null){
            toast("请选择教师");
            return;
        }
        List<String> stuids=new ArrayList<>();
        List<String> ids=new ArrayList<>();
        ids.add("0");
        stuids.add("0");

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                StringUtils.stringToGetParamsXml(ids),
                bean.getElectiveid(),
                selcet_CPWBean.getId(),
                StringUtils.stringToGetParamsXml(stuids),
                content,//content
                dateBean.getValue(),//data
                type_ids,//type
                "",//image
                "",//
                stucount,//stucount
                substituteteacher.replace("tea","" ),//substituteteacher,默认传0
                "check",//checktype
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
                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
                    if (photo_a==null)return;
                    if (photo_a.size()==0)return;
                    setMultList(photo_a);
                    break;
                case TagFinal.UI_ADMIN:
                    ChildBean bean=data.getParcelableExtra(TagFinal.OBJECT_TAG);
                    substituteteacher=bean.getUserid();
                    replace_tea.setText(bean.getUsername());
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





    /**
     * ----------------------------retrofit-----------------------
     */

    private void getOper(int isclass){

        ReqEnv reqEnv = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        DelayGetTypeReq req = new DelayGetTypeReq();
        //获取参数

        req.setIsclass(isclass);
        reqBody.delayGetTypeReq = req;
        reqEnv.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().delay_get_type(reqEnv);
        call.enqueue(this);
        showProgressDialog("");
    }



    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (response.code()==500){
            toastShow("数据出差了");
        }
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.delayGetTypeRes !=null) {
                String result = b.delayGetTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                EventRes res=gson.fromJson(result,EventRes.class );
                if (StringJudge.isEmpty(res.getElective_opear())){
                    toast("无考评类型");
                }else{
                    select_oval.clear();
                    list_view.setVisibility(View.VISIBLE);
                    select_oval= res.getElective_opear();
                    for (OperType type:select_oval){
                        type.setIselect(true);
                    }
                    adapter.setDataList(select_oval);
                    adapter.setLoadState(TagFinal.LOADING_COMPLETE);
                }

            }
        }else{
            Logger.e(name+"---ResEnv:null");
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


}
