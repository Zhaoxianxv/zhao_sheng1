package com.yfy.app.ebook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ebook.BookGetUserListReq;
import com.yfy.app.video.beans.VideoInfo;
import com.yfy.base.App;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.permission.PermissionTools;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.swipe.xlist.XlistListener;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.upload.DownLoadActivity;
import com.yfy.view.SQToolBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.yfy.final_tag.permission.PermissionFail;
import com.yfy.final_tag.permission.PermissionGen;
import com.yfy.final_tag.permission.PermissionSuccess;

public class EbookListMainActivity extends BaseActivity implements Callback<ResEnv> {

    private static final String TAG = EbookListMainActivity.class.getSimpleName();
    private static final int E_BOOK = 2;
    private static final int E_DOWN = 5;
    private List<EbookBean> ebook_adapter_list = new ArrayList<>();

    @Bind(R.id.video_list_main)
    XListView xList;
    private EbookListAdapter adapter;
    private TextView top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list_main);
        initSQtoolbar();
        initView();

    }

    private String session_key = "";

    @Override
    public void onResume() {
        super.onResume();
        if (Base.user == null) {
            session_key = "gus0";
        } else {
            session_key = Base.user.getSession_key();
        }
        refresh(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == E_BOOK) {
                String name, id;
                name = data.getStringExtra("tag_name");
                id = data.getStringExtra("tag_id");
                top.setText(name);
                tag_id = ConvertObjtect.getInstance().getInt(id);
                refresh(true);
            }
            if (requestCode == E_DOWN) {
                create(ebook_adapter_list);
            }
        }
    }

    public void create(List<EbookBean> ebooks) {
        UserPreferences user = UserPreferences.getInstance();
        for (EbookBean bean : ebooks) {
            String file = bean.getFileurl();
            String filePath = user.getEbook(file);
            if (StringJudge.isEmpty(filePath)) {
                bean.setFilePath(filePath);
                bean.setIsdown(false);
            } else {
                bean.setIsdown(true);
                bean.setFilePath(filePath);
            }
        }
        adapter.notifyDataSetChanged(ebooks);
    }

    private void initSQtoolbar() {
        assert toolbar != null;
        toolbar.setTitle(R.string.e_book_radio);
        toolbar.addMenuText(1, R.string.file_massge);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (StringJudge.isEmpty(ebook_adapter_list)) {
                    return;
                } else {
                    if (ebook_adapter_list.get(0).getFilePath() == null) {
                        return;
                    }
                }
                Intent intent = new Intent(mActivity, FileAdminActivity.class);
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) ebook_adapter_list);
                startActivity(intent);
            }
        });
    }

    private void initView() {

        xList.setPullLoadEnable(true);
        xList.setPullLoadEnable(true);
        adapter = new EbookListAdapter(mActivity, ebook_adapter_list);
        adapter.setDoButton(dobutton);
        xList.setAdapter(adapter);
        xList.setXListViewListener(new XlistListener() {
            @Override
            public void onRefresh() {
                super.onRefresh();
                refresh(true);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                refresh(false);
            }
        });

        View v = LayoutInflater.from(mActivity).inflate(R.layout.public_item_singe_top_txt, null);
        v.setBackgroundColor(getResources().getColor(R.color.DarkGray));
        xList.addHeaderView(v);

        top =  v.findViewById(R.id.public_center_txt_add);
        top.setText("全部");
        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TagChioceActivity.class);
                intent.putExtra("is", true);
                startActivityForResult(intent, E_BOOK);
            }
        });
    }

    private int tag_id = 0, page = 0;
    private boolean loading = false;


    private String url, title_name,filename;
    private EbookListAdapter.DoButton dobutton = new EbookListAdapter.DoButton() {
        @Override
        public void button(TextView button, String ur, String title, String fileName) {
            if (button.getText().toString().equals("打开")) {
                filename=fileName;
                PermissionGen
                        .needPermission(mActivity, TagFinal.CAMERA,
                                new String[]{
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                });

            } else {
                url = ur;
                title_name = title.trim();
                PermissionTools.tryWRPerm(mActivity);
            }
        }
    };

    public static Intent getWordFileIntent(String param) {
        Intent intent = null;
        try {
            intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri;
            Logger.e("zxx","   "+param);
            File file=new File(param);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri= FileProvider.getUriForFile(App.getApp().getApplicationContext(), Base.AUTHORITY, file);
                Logger.e("zxx"," 7.0  ");
            } else {
                Logger.e("zxx"," 5.0 ");
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, "application/msword");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Logger.e("zxx","  Exception ");

        }
        return intent;
    }

    /**
     * 判断Intent 是否存在 防止崩溃
     *
     * @param context
     * @param intent
     * @return
     */
    @SuppressWarnings("WrongConstant")
    private boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }


    @PermissionSuccess(requestCode = TagFinal.CAMERA)
    private void takePhoto() {
        Intent intent = getWordFileIntent(filename);
        if (isIntentAvailable(mActivity, intent)) {
            startActivity(intent);
        } else {
            toastShow("请安装 WPS Office 查看");
        }
    }

    @PermissionSuccess(requestCode = TagFinal.PHOTO_ALBUM)
    private void photoAlbum() {
        String pass = url.substring(url.lastIndexOf("."), url.length());
        Intent intent = new Intent(mActivity, DownLoadActivity.class);
        Bundle b = new Bundle();
        b.putString("url", url);
        b.putString("fileTitle", title_name + pass);
        intent.putExtras(b);
        startActivityForResult(intent, E_DOWN);
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
     *------------------------------------
     */





    public void refresh(boolean is){
        if (is) {
            if (loading) {
                xList.stopRefresh();
                return;
            }
            page = 0;
        } else {
            if (loading) {
                xList.stopLoadMore();
                return;
            }
            page++;
        }
        loading = true;
        ReqEnv env = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        BookGetUserListReq req = new BookGetUserListReq();
        //获取参数
        req.setPage(page);
        req.setSession_key(session_key);
        req.setTagid(tag_id);
        req.setSize(TagFinal.FIFTEEN_INT);

        reqBody.bookGetUserListReq = req;
        env.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().book_get_user_list(env);
        call.enqueue(this);
        if (is) showProgressDialog("");
    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();

        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);

        loading = false;
        if (xList != null) {
            xList.stopLoadMore();
            xList.stopRefresh();
        }
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;
            if (b.bookGetUserListRes !=null){
                String result=b.bookGetUserListRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                VideoInfo res = gson.fromJson(result, VideoInfo.class);

                if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
                    if (page==0){
                        ebook_adapter_list.clear();
                        ebook_adapter_list.addAll(res.getBook_list());
                    }else{
                        ebook_adapter_list.addAll(res.getBook_list());
                    }
                    adapter.notifyDataSetChanged(ebook_adapter_list);
                    if (res.getBook_list().size()!=TagFinal.FIFTEEN_INT) toastShow(R.string.success_loadmore_end);

                }else {
                    toastShow(res.getError_code());
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
        toastShow(R.string.fail_do_not);
        dismissProgressDialog();
        loading = false;
        if (xList != null) {
            xList.stopLoadMore();
            xList.stopRefresh();
        }
    }

    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }
}
