package com.yfy.upload;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadActivity extends BaseActivity {
    private String url;
    private String fileTitle;
    private String filepath;
    /** 显示下载进度TextView */
    private TextView mMessageView;
    /** 显示下载进度ProgressBar */
    private ProgressBar mProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_load);
        //获取传递的Intent的Bundle的url键值
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        if (b.containsKey("url")){
            url = b.getString("url");
        }else{
            onPageBack();
        }
        if (b.containsKey("fileTitle")){
            fileTitle = b.getString("fileTitle");
        }else{
            fileTitle="file";
        }
        mMessageView = (TextView) findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
        doDownload();
    }


    /**
     * 使用Handler更新UI界面信息
     */
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressbar.setProgress(msg.getData().getInt("size"));
            float temp = (float) mProgressbar.getProgress() / (float) mProgressbar.getMax();
            int progress = (int) (temp * 100);
            if (progress == 100) {
                Toast.makeText(mActivity, "下载完成！", Toast.LENGTH_LONG).show();
                UserPreferences.getInstance().saveEbook(url,filepath);
                onPageBack();
            }
            mMessageView.setText("下载进度:" + progress + " %");
        }
    };

    /**
     * 下载准备工作，获取SD卡路径、开启线程
     */
    private void doDownload() {
        // 获取SD卡路径
        String path = Environment.getExternalStorageDirectory().toString()+ "/ebook/";
        File file = new File(path);
        // 如果SD卡目录不存在创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 设置progressBar初始化
        mProgressbar.setProgress(0);

        // 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
        String downloadUrl = url;
        String fileName =fileTitle;
        int threadNum = 5;

        filepath = path + fileName;
        Logger.e(TagFinal.ZXX, "download file  path:" + filepath);
        downloadTask task = new downloadTask(downloadUrl, threadNum, filepath);
        task.start();
    }
    /**
     * 多线程文件下载
     *
     * @author yangxiaolong
     * @2014-8-7
     */
    class downloadTask extends Thread {
        private String downloadUrl;// 下载链接地址
        private int threadNum;// 开启的线程数
        private String filePath;// 保存文件路径地址
        private int blockSize;// 每一个线程的下载量

        public downloadTask(String downloadUrl, int threadNum, String fileptah) {
            this.downloadUrl = downloadUrl;
            this.threadNum = threadNum;
            this.filePath = fileptah;
        }
        @Override
        public void run() {
            FileDownloadThread[] threads = new FileDownloadThread[threadNum];
            try {
                URL url = new URL(downloadUrl);
                Logger.d(TagFinal.ZXX, "download file http path:" + downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    Logger.e(TagFinal.ZXX, "读取文件失败");
                    return;
                }
                // 设置ProgressBar最大的长度为文件Size
                mProgressbar.setMax(fileSize);
                // 计算每条线程下载的数据长度
                blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;
                Logger.d(TagFinal.ZXX, "fileSize:" + fileSize + "  blockSize:"+blockSize);
                File file = new File(filePath);
                for (int i = 0; i < threads.length; i++) {
                    // 启动线程，分别下载每个线程需要下载的部分
                    threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
                    threads[i].setName("Thread:" + i);
                    threads[i].start();
                }

                boolean isfinished = false;
                int downloadedAllSize = 0;
                while (!isfinished) {
                    isfinished = true;
                    // 当前所有线程下载总量
                    downloadedAllSize = 0;
                    for (int i = 0; i < threads.length; i++) {
                        downloadedAllSize += threads[i].getDownloadLength();
                        if (!threads[i].isCompleted()) {
                            isfinished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    Message msg = new Message();
                    msg.getData().putInt("size", downloadedAllSize);
                    mHandler.sendMessage(msg);
                    // Log.d(TAG, "current downloadSize:" + downloadedAllSize);
                    Thread.sleep(500);// 休息1秒后再读取下载进度
                }
//                Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
