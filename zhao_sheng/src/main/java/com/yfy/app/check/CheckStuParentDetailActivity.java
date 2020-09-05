package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.DateBean;
import com.yfy.app.check.adapter.CheckParentAdapter;
import com.yfy.app.check.bean.ChecKParent;
import com.yfy.app.check.bean.CheckChild;
import com.yfy.app.check.bean.CheckRes;
import com.yfy.app.check.bean.CheckState;
import com.yfy.app.check.bean.CheckStu;
import com.yfy.app.check.bean.ClasslistBean;
import com.yfy.app.check.bean.IllType;
import com.yfy.app.check.bean.IllTypeGroup;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.check.CheckGetIllTypeReq;
import com.yfy.app.net.check.CheckStuDelChildReq;
import com.yfy.app.net.check.CheckStuDelParentReq;
import com.yfy.app.net.check.CheckStuParentDetailReq;
import com.yfy.final_tag.Base;
import com.yfy.final_tag.dialog.CPWListView;
import com.yfy.final_tag.recycerview.SlideRecyclerView;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.dialog.ConfirmContentWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckStuParentDetailActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = CheckStuParentDetailActivity.class.getSimpleName();


    private CheckParentAdapter adapter;
    private List<CheckChild> adapter_list=new ArrayList<>();
    @Bind(R.id.public_recycler_del)
    Button del_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_slide_recycler_del);
        del_button.setText("新增生病/缺勤信息");
        getData();
        initDialog();
        initRecycler();
        initSQtoolbar();
        getStuDetail();
        getCheckIllType();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    private CheckStu checkStu;
    private DateBean dateBean;
    private ClasslistBean classlistBean;
    private CheckState checkState;
    private boolean is_edit=false;
    private void getData(){
        is_edit=getIntent().getBooleanExtra(Base.state,false);
        checkStu=getIntent().getParcelableExtra(TagFinal.OBJECT_TAG);
        dateBean=getIntent().getParcelableExtra(TagFinal.TYPE_TAG);
        checkState=getIntent().getParcelableExtra(TagFinal.type);
        classlistBean=getIntent().getParcelableExtra(TagFinal.CLASS_BEAN);

        if (is_edit){
            del_button.setVisibility(View.VISIBLE);
        }else{
            del_button.setVisibility(View.GONE);
        }

    }

    private CPWListView cpwListView;
    private List<String> name=new ArrayList<>();
    private List<IllType> dailog_list_beans=new ArrayList<>();
    private IllType select_bean;

    @OnClick(R.id.public_recycler_del)
    void setDel(){
        if (StringJudge.isEmpty(adapter_list)){
            Intent intent=new Intent(mActivity,CheckTypeActivity.class);
            intent.putExtra(TagFinal.OBJECT_TAG, checkStu);
            intent.putExtra(TagFinal.TYPE_TAG, dateBean);
            intent.putExtra(TagFinal.CLASS_BEAN, classlistBean);
            intent.putExtra(TagFinal.type, checkState);
            startActivityForResult(intent, TagFinal.UI_REFRESH);
        }else{
            CheckChild bean=adapter_list.get(0);
            if (bean.getState().equals("已恢复")){
                Intent intent=new Intent(mActivity,CheckTypeActivity.class);
                intent.putExtra(TagFinal.OBJECT_TAG, checkStu);
                intent.putExtra(TagFinal.TYPE_TAG, dateBean);
                intent.putExtra(TagFinal.CLASS_BEAN, classlistBean);
                intent.putExtra(TagFinal.type, checkState);
                startActivityForResult(intent, TagFinal.UI_REFRESH);
            }else{
                Logger.e(bean.getIlltype());
                for (IllType illType:illTypeList){
                    if (bean.getIlltype().equals(illType.getIlltypename()))select_bean=illType;
                }
                Intent intent=new Intent(mActivity,CheckStuAddChildActivity.class);
                intent.putExtra(TagFinal.OBJECT_TAG, checkStu);
                intent.putExtra(TagFinal.TYPE_TAG, dateBean);
                intent.putExtra(TagFinal.CLASS_BEAN, classlistBean);
                intent.putExtra(TagFinal.ID_TAG, select_bean);
                intent.putExtra(TagFinal.type, checkState);
                startActivityForResult(intent,TagFinal.UI_ADD);
            }

        }
    }

    private void initSQtoolbar() {
        assert toolbar!=null;
        toolbar.setTitle(checkStu.getUsername());
        toolbar.addMenuText(TagFinal.ONE_INT,"历史记录");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:

                        Intent intent=new Intent(mActivity,CheckStuListActivity.class);
                        intent.putExtra(TagFinal.OBJECT_TAG,checkStu );
                        intent.putExtra(TagFinal.TYPE_TAG,true );
                        startActivityForResult(intent, TagFinal.UI_TAG);
                        break;
                }
            }
        });
    }

//    private SwipeRefreshLayout swipeRefreshLayout;
//    private RecyclerView recyclerView;
    private SlideRecyclerView recycler_slide;
    public void initRecycler(){



//        recyclerView =  findViewById(R.id.public_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        xlist.setLayoutManager(new GridLayoutManager(this, 1));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //添加分割线
//        recyclerView.addItemDecoration(new RecycleViewDivider(
//                mActivity,
//                LinearLayoutManager.HORIZONTAL,
//                1,
//               ColorRgbUtil.getGainsboro()));

        recycler_slide= (SlideRecyclerView) findViewById(R.id.public_recycler);
        recycler_slide.setLayoutManager(new LinearLayoutManager(
                mActivity,
                LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(
                mActivity,
                DividerItemDecoration.VERTICAL);
        recycler_slide.addItemDecoration(itemDecoration);

        adapter=new CheckParentAdapter(mActivity);
        recycler_slide.setAdapter(adapter);

        adapter.setCheckStu(checkStu);
        adapter.setDelFace(new CheckParentAdapter.DelFace() {
            @Override
            public void del(CheckChild bean) {
                confirmContentWindow.setTitle_s("提示","是否删除此条病例","确定");
                confirmContentWindow.setIs_type(true);
                confirmContentWindow.showAtCenter();
            }

            @Override
            public void del_child(CheckChild bean) {
                delItemDetail(bean);
            }
        });

    }


    private ConfirmContentWindow confirmContentWindow;
    private void initDialog(){

        confirmContentWindow = new ConfirmContentWindow(mActivity);

        confirmContentWindow.setOnPopClickListenner(new ConfirmContentWindow.OnPopClickListenner() {
            @Override
            public void onClick(View view) {

                switch (view.getId()){
                    case R.id.pop_dialog_title:
                        break;
                    case R.id.pop_dialog_content:
                        break;
                    case R.id.pop_dialog_ok:
                        delAllDetail();

                        break;
                }
            }
        });
    }


    public void closeSwipeRefresh(){
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_ADD:
                    getStuDetail();
                    break;
                case TagFinal.UI_TAG:
                    getStuDetail();
                    break;
                case TagFinal.UI_REFRESH:
                    finish();
                    break;
            }
        }
    }

    /**
     * ----------------------------retrofit-----------------------
     */

    public void getCheckIllType() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckGetIllTypeReq request = new CheckGetIllTypeReq();
        //获取参数
        reqBody.checkGetIllTypeReq= request;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_ill_type(evn);
        call.enqueue(this);
//        Logger.e(evn.toString());

    }



    public void getStuDetail() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuParentDetailReq req = new CheckStuParentDetailReq();
        //获取参数

        req.setUserid(ConvertObjtect.getInstance().getInt(checkStu.getUserid()));
        req.setId(checkStu.getIllid());
        reqBody.checkStuParentDetailReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_get_stu_parent_detail(evn);
        call.enqueue(this);
//        showProgressDialog("正在加载");
//        Logger.e(evn.toString());

    }


    public void delAllDetail() {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuDelParentReq req = new CheckStuDelParentReq();
        //获取参数

        req.setIllid(checkStu.getIllid());
        reqBody.checkStuDelParentReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_stu_del_parent(evn);
        call.enqueue(this);
//        showProgressDialog("正在加载");
//        Logger.e(evn.toString());

    }

    public void delItemDetail(CheckChild bean) {

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        CheckStuDelChildReq req = new CheckStuDelChildReq();
        //获取参数

        req.setIllid(checkStu.getIllid());
        req.setIllstateid(bean.getSeekid());
        reqBody.checkStuDelChildReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().check_stu_del_child(evn);
        call.enqueue(this);
//        showProgressDialog("正在加载");
        Logger.e(evn.toString());

    }


    private void initCreate(CheckRes res){
        adapter_list.clear();
        for (ChecKParent parent:res.getUserstate()){
            CheckChild detail=new CheckChild();
            detail.setView_type(TagFinal.TYPE_PARENT);
            detail.setAdddate(parent.getAdddate());
            detail.setAdduser(parent.getAdduser());
            detail.setAdduserheadpic(parent.getAdduserheadpic());
            detail.setUserheadpic(parent.getUserheadpic());
            detail.setUserid(parent.getUserid());
            detail.setUsername(parent.getUsername());
            detail.setUsermobile(parent.getUsermobile());
            detail.setIllcheckdate(parent.getIllcheckdate());
            detail.setIllchecktype(parent.getIllchecktype());
            detail.setIllcheckdate(parent.getIlldate());
            detail.setIllid(parent.getIllid());
            detail.setIlltype(parent.getIlltype());
            detail.setState(parent.getState());

            if (StringJudge.isEmpty(parent.getIllstate())){
                detail.setIs_show(false);
            }else{
                detail.setIs_show(true);
            }
            adapter_list.add(detail);
            for (CheckChild child:parent.getIllstate()){
                child.setView_type(TagFinal.TYPE_CHILD);
                adapter_list.add(child);
            }

        }

        if (StringJudge.isEmpty(res.getReturndate())){

        }else{
            CheckChild detail=new CheckChild();
            detail.setView_type(TagFinal.TYPE_FOOTER);
            detail.setUsername(res.getReturndate()+"已返校");
            adapter_list.add(detail);
        }
        adapter.setDataList(adapter_list);
        adapter.setLoadState(TagFinal.LOADING_COMPLETE);

        if (StringJudge.isEmpty(adapter_list)){
            toastShow("没有获取到数据");
            del_button.setText("新增生病/缺勤信息");
        }else{
            del_button.setText("新增生病/缺勤信息");
        }
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
//        closeSwipeRefresh();
        Logger.e(response.code()+"");
        if (response.code()==500){
//            toastShow("数据出差了");
        }
        ResEnv evn = response.body();
        if (evn != null) {
            ResBody b=evn.body;
            if (b.checkStuParentDetailRes!=null) {
                String result = b.checkStuParentDetailRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    initCreate(res);
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.checkGetIllTypeRes!=null) {
                String result = b.checkGetIllTypeRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    illTypeList.clear();
                    for (IllTypeGroup group:res.getInspecttype()){
                        for (IllType type:group.getIlltypegroup()){
                            illTypeList.add(type);
                        }
                    }
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.checkStuDelParentRes!=null) {
                String result = b.checkStuDelParentRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }
            if (b.checkStuDelChildRes!=null) {
                String result = b.checkStuDelChildRes.result;
                Logger.e(call.request().headers().toString() + result);
                CheckRes res=gson.fromJson(result,CheckRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    getStuDetail();
                }else{
                    toastShow(res.getError_code());
                }
            }
        }else{
//            Logger.e("evn: null"+call.request().headers().toString() );
            List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
            String name=names.get(names.size()-1);
            Logger.e(name+"--------er");
            if (name.equalsIgnoreCase("illstate_detail")){
//                Logger.e("zxx-------------------------------");
                adapter_list.clear();
                adapter.setDataList(adapter_list);
                adapter.setLoadState(TagFinal.LOADING_COMPLETE);
            }

        }
    }
    private List<IllType> illTypeList=new ArrayList<>();

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {
        if (!isActivity())return;
        Logger.e("onFailure  "+call.request().headers().toString() );
        dismissProgressDialog();
//        toast(R.string.fail_do_not);
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
