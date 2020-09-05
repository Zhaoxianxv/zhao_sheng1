package com.yfy.app.ebook;

import android.os.Bundle;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.final_tag.StringJudge;
import com.yfy.tools.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class FileAdminActivity extends BaseActivity {
    @Bind(R.id.file_admin_list)
    XListView xlist;
    private List<EbookBean> beanList=new ArrayList<>();
    private EbookAdminAdpater adminAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ebook_file_admin);
        initView();
        initSQToolbar();
        getData();

    }
    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("缓存文件管理");
    }
    public void getData(){
        List<EbookBean> ebooks=getIntent().getParcelableArrayListExtra("data");
        create(ebooks);
    }
    public void initView(){
        xlist.setPullLoadEnable(false);
        xlist.setPullRefreshEnable(false);
        adminAdpater=new EbookAdminAdpater(mActivity);
        xlist.setAdapter(adminAdpater);
        adminAdpater.setNewsAdapterDeleteonClick(
                new EbookAdminAdpater.NewsAdapterDeleteonClick() {
            @Override
            public void delete(EbookBean bean) {
                if (deleteFolder(bean.getFilePath())){
                    UserPreferences.getInstance().saveEbook(bean.getFileurl(),"");
                    onPageBack();
                }
            }
        });
    }

    public void create(List<EbookBean> ebooks){
        for (EbookBean bean:ebooks) {
            String filePath=bean.getFilePath();
            if (StringJudge.isEmpty(filePath)){
            }else{
                beanList.add(bean);
            }
        }
        adminAdpater.setAdminNewsList(beanList);
    }


    public boolean deleteFolder(String filePath) {
        Logger.e(TagFinal.ZXX, "Delete: "+filePath );
        FileTools fileTools=new FileTools();//文件炒作工具
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return fileTools.deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return fileTools.
                        deleteDirectory(filePath);
            }
        }


    }

    public void initData(List<EbookBean> ebooks){
        for (EbookBean bean:ebooks) {
            String filePath=bean.getFilePath();
            if (StringJudge.isEmpty(filePath)){
            }else{
                beanList.add(bean);
            }
        }
        adminAdpater.setAdminNewsList(beanList);
    }
}
