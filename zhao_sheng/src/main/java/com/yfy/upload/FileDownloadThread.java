package com.yfy.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import android.util.Log;

import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;

/**
 * 文件下载类
 *
 * @author yangxiaolong
 * @2014-5-6
 */
public class FileDownloadThread extends Thread {

	private static final String TAG = FileDownloadThread.class.getSimpleName();

	/** 当前下载是否完成 */
	private boolean isCompleted = false;
	/** 当前下载文件长度 */
	private int downloadLength = 0;
	/** 文件保存路径 */
	private File file;
	/** 文件下载路径 */
	private URL downloadUrl;
	/** 当前下载线程ID */
	private int threadId;
	/** 线程下载数据长度 */
	private int blockSize;

	/**
	 *
	 * @param downloadUrl:文件下载地址
	 * @param file:文件保存路径
	 * @param blocksize:下载数据长度
	 * @param threadId:线程ID
	 */
	/**
	 * model各个参数详解
	 * r 代表以只读方式打开指定文件
	 * rw 以读写方式打开指定文件
	 * rws 读写方式打开，并对内容或元数据都同步写入底层存储设备
	 * rwd 读写方式打开，对文件内容的更新同步更新至底层存储设备
	 * **/
	public FileDownloadThread(URL downloadUrl, File file, int blocksize, int threadId) {
		this.downloadUrl = downloadUrl;
		this.file = file;
		this.threadId = threadId;
		this.blockSize = blocksize;
	}

	@Override
	public void run() {

		BufferedInputStream bis = null;
		RandomAccessFile raf = null;
		try {
			URLConnection conn = downloadUrl.openConnection();
			conn.setAllowUserInteraction(true);
			int startPos = blockSize * (threadId - 1);//开始位置
			int endPos = blockSize * threadId - 1;//结束位置
			//设置当前线程下载的起点、终点
			conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
			Logger.e(TagFinal.ZXX, Thread.currentThread().getName() + "  bytes=" + startPos + "-" + endPos);

			byte[] buffer = new byte[1024];
			bis = new BufferedInputStream(conn.getInputStream());

			raf = new RandomAccessFile(file, "rw");
			raf.seek(startPos);
			int len;
			while ((len = bis.read(buffer, 0, 1024)) != -1) {
				raf.write(buffer, 0, len);
				downloadLength += len;
			}
			isCompleted = true;
			Log.d(TagFinal.ZXX, "current thread task has finished,all size:" + downloadLength);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 线程文件是否下载完毕
	 */
	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * 线程下载文件长度
	 */
	public int getDownloadLength() {
		return downloadLength;
	}

}
