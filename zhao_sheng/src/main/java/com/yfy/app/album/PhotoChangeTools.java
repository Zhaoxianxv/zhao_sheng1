package com.yfy.app.album;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.yfy.final_tag.BuildVersionMethod;

import java.io.File;

/**
 * Created by yfyandr on 2018/5/3.
 */

public class PhotoChangeTools {
    /**
     * 通知系统更新媒体
     * @param context
     * @param filePath
     * @param endDis
     */
    public static void addPhoto(Context context,String filePath,String endDis){
        if (BuildVersionMethod.isLaterKITKAT()) {
            MediaScanner mediaScanner = new MediaScanner(context);
            String[] filePaths = new String[]{filePath};
            String[] mimeTypes = new String[]{MimeTypeMap.getSingleton().getMimeTypeFromExtension(endDis)};
            mediaScanner.scanFiles(filePaths, mimeTypes);
        }else{
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));//uri 24
        }

    }
    //删除文件后更新数据库  通知媒体库更新文件夹
    public static void updateFileFromDatabase(Context context,File file){
        if (BuildVersionMethod.isLaterKITKAT()) {
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(context, paths, null, null);
            MediaScannerConnection.scanFile(
                    context,
                    new String[] {file.getAbsolutePath()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
        } else {
            context.sendBroadcast(
                    new Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }
}
