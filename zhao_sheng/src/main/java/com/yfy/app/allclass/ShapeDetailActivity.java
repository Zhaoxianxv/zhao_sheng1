package com.yfy.app.allclass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.allclass.adapter.ShapeSingeDetailAdapter;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.app.allclass.beans.ReplyaBean;
import com.yfy.app.allclass.beans.ShapeMainList;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yfy1 on 2017/3/17.
 */

public class ShapeDetailActivity extends WcfActivity {

    ShapeSingeDetailAdapter adapter;
    @Bind(R.id.shapesinge_detail_item_xlist)
    XListView xlist;
    TextView jianx2;

    ShapeMainList bean;
    private int num;

    private List<ReplyaBean> replyaBeen=new ArrayList<>();
    private ArrayList<String> photos= new ArrayList<>();

//    private final String getDynamicById = "get_dynamic_byid";
    private final String getReplayList = "WB_get_reply";//获取评论列表
    private final String WB_detail = "WB_detail";//删除main单条数据

    private int page=0;
    private boolean xlistLoading=false;
//    private LinearLayout float_view;
    private String replyId;
    LoadingDialog loadingDialog;
    private TextView content;

    TextView who;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_item_detail);
        getData();
        initView();
        loadingDialog=new LoadingDialog(mActivity);
        initSQToolbar();
        getReplayd(bean.getId(),true);

    }
    @Override
    public void onResume() {
        super.onResume();

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

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("分享正文");
        if (Base.user.getName().equals(bean.getName())){
            toolbar.addMenuText(1,R.string.delete);
        }

        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                mDialog("是否要删除这条动态",ok_delete_d);
            }
        });

    }

    private void initView() {

        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(true);
        adapter=new ShapeSingeDetailAdapter(mActivity,replyaBeen);
        xlist.setAdapter(adapter);

        View v= LayoutInflater.from(mActivity).inflate(R.layout.shape_detail_head,null);
        ImageView head= (ImageView) v.findViewById(R.id.headPic);
        TextView name= (TextView) v.findViewById(R.id.name);
        TextView time= (TextView) v.findViewById(R.id.time);
        TextView shape_type= (TextView) v.findViewById(R.id.shape_type);
        View jianx1=v.findViewById(R.id.head_jianxi2);
        jianx2= (TextView) v.findViewById(R.id.head_jianxi1);
        who= (TextView) v.findViewById(R.id.who_praise);
        MultiPictureView  mult= (MultiPictureView) v.findViewById(R.id.shape_main_mult);
        content= (TextView) v.findViewById(R.id.shape_detail_head_content);
        if (bean!=null){
            Glide.with(mActivity)
                    .load(bean.getHead_photo())
                    .apply(new RequestOptions().circleCrop())
                    .into(head);
            name.setText(bean.getName());
            time.setText(bean.getTime());
            who.setText(bean.getPraise());
            content.setText(bean.getContent());
            jianx1.setVisibility(View.VISIBLE);
            jianx2.setVisibility(View.VISIBLE);
            shape_type.setText(bean.getTags());
        }
        content.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View view) {
                if(flag){
                    flag = false;
                    content.setEllipsize(null); // 展开
                    content.setSingleLine(flag);
                }else{
                    flag = true;
                    content.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    content.setSingleLine(flag);
                }
            }
        });
        xlist.addHeaderView(v);
        photos.addAll(bean.getImages()==null?photos:bean.getImages());
        mult.clearItem();
        if (StringJudge.isEmpty(photos)){
            mult.setVisibility(View.GONE);
            mult.addItem(photos);
        }else{
            mult.setVisibility(View.VISIBLE);
            mult.addItem(photos);
        }
        mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
                intent.putExtras(b);
                startActivity(intent);
            }
        });



        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logger.e("zxx","index "+i);
                String name=Base.user.getName();
                ReplyaBean replyaBean=replyaBeen.get(i-2);
                if (replyaBean.getAuth_name().equals(name)){
                    replyId=replyaBean.getId();
                    mDialog(R.string.is_delete,ok);
                }else{
                    Intent intent=new Intent(mActivity,EditReplyActivity.class);
                    Bundle b=new Bundle();
                    b.putString("DyId", bean.getId() );
                    intent.putExtra("replay_id",replyaBean.getId());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });
        xlist.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                getReplayd(bean.getId(),false);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                loadMoreReplayd(bean.getId());
            }
        });

    }





    private DialogInterface.OnClickListener ok=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if (replyId==null)return;
            deleteReply(replyId);
        }
    };
    private DialogInterface.OnClickListener ok_delete_d=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            deleteDynamic();
        }
    };


    public void getData() {
        bean=  getIntent().getParcelableExtra("bean");

    }

    private void deleteReply(String replyId) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                replyId
        };
        ParamsTask task = new ParamsTask(
                params,
                TagFinal.SHAPE_DID_DELETE_REPLY,
                TagFinal.SHAPE_DID_DELETE_REPLY,
                loadingDialog);
        execute(task);
    }

    private void getReplayd(String dynamicId,boolean is) {
        if (xlistLoading){
            xlist.stopRefresh();
            return;
        }
        xlistLoading=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                dynamicId ,
                FIRST_ONE,
                PAGE_NUM
        };
        ParamsTask task;
        if (is){
            task = new ParamsTask(params, getReplayList, TagFinal.REFRESH,loadingDialog);
        }else{
            task = new ParamsTask(params, getReplayList, TagFinal.REFRESH);
        }

        execute(task);
    }
    private void loadMoreReplayd(String dynamicId) {
        if (xlistLoading){
            xlist.stopLoadMore();
            return;
        }
        xlistLoading=true;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                dynamicId ,
                ++page,
                PAGE_NUM
        };
        ParamsTask task = new ParamsTask(params, getReplayList, TagFinal.REFRESH_MORE);
        execute(task);
    }

    private void setPraise(String dynamicId) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                Base.user.getName(),
                bean.getIsPraise().equals("否")?1:0,
                dynamicId
        };
        ParamsTask task = new ParamsTask(params, TagFinal.SHAPE_DID_PRAISE, TagFinal.PRAISE_TAG,loadingDialog);
        execute(task);
    }


    private void wb_detail(String dynamicId) {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                dynamicId
        };
        ParamsTask task = new ParamsTask(params, WB_detail, WB_detail);
        execute(task);
    }

    private void deleteDynamic() {
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                bean.getId()
        };
        ParamsTask task = new ParamsTask(params, TagFinal.SHAPE_DID_DELETE, TagFinal.SHAPE_DID_DELETE,loadingDialog);
        execute(task);
    }


    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        if (xlist==null){
            return false;
        }
        xlist.stopLoadMore();
        xlist.stopRefresh();
        xlistLoading=false;
        Logger.e("zxx","   "+result);
        ReInfor infor=gson.fromJson(result,ReInfor.class);
        String name=wcfTask.getName();
        if (name.equals(TagFinal.SHAPE_DID_DELETE)){
            if (infor.getResult().equals("true")){
                setResult(RESULT_OK);
                onPageBack();
            } else{
                toastShow(result);
            }
            return false;
        }
        if (name.equals(TagFinal.PRAISE_TAG)){
            if (infor.getResult().equals("true")){
                toastShow(R.string.success_do);
                wb_detail(bean.getId());
            }else{
                toastShow(result);
            }
            return false;
        }
        if (name.equals(TagFinal.SHAPE_DID_DELETE_REPLY)){
            if (infor.getResult().equals("true")){
                toastShow(R.string.success_do);
                getReplayd(bean.getId(),false);
                jianx2.setText("共有"+infor.getCount()+"条评论");
            } else{
                toastShow(result);
            }
            return false;
        }
        if (name.equals(TagFinal.REFRESH)){
            if (infor.getReplys()!=null){
                replyaBeen.clear();
//                    Log.e("'zxx"," getReplys  ");
                replyaBeen=infor.getReplys();
                adapter.notifyDataSetChanged(replyaBeen);
                jianx2.setText("共有"+infor.getCount()+"条评论");
                if (replyaBeen.size()!=TagFinal.TEN_INT){
                    xlist.setPullLoadEnable(false);
                }else{
                    xlist.setPullLoadEnable(true);
                }
            }
            return false;
        }
        if (name.equals(TagFinal.REFRESH_MORE)){
            if (infor.getReplys()!=null){
                replyaBeen.addAll(infor.getReplys());
                adapter.notifyDataSetChanged(replyaBeen);
            }
            return false;
        }
        if (name.equals(WB_detail)){
            if (infor.getResult().equals("true")){
                who.setText(infor.getWbs().get(0).getPraise());
                bean.setIsPraise(infor.getWbs().get(0).getIsPraise());
            } else{
                toastShow(result);
            }
            return false;
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        xlist.stopLoadMore();
        xlist.stopRefresh();
        xlistLoading=false;

    }

    @OnClick(R.id.shapesinge_detail_comment)
    void setComment(){
        Intent intent=new Intent(mActivity,EditReplyActivity.class);
        intent.putExtra("DyId",bean.getId());
        intent.putExtra("replay_id","0");
        startActivityForResult(intent,TagFinal.UI_REFRESH);
    }

    @OnClick(R.id.shapesinge_detail_praise)
    void setPraise(){
        setPraise(bean.getId());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_REFRESH:
                    if (bean!=null){
                        getReplayd(bean.getId(),false);
                    }
                    break;
            }
        }

    }


}
