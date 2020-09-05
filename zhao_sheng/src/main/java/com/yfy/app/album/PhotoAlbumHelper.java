package com.yfy.app.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;


import com.yfy.final_tag.glide.Photo;
import com.yfy.final_tag.glide.PhotoAlbum;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PhotoAlbumHelper extends AsyncTask<Object, Object, Object> {

	private static final String TAG = PhotoAlbumHelper.class.getSimpleName();
	private ContentResolver resolver;
	// 缩略图列表
	private HashMap<String, String> thumbnailList = new HashMap<String, String>();
	private HashMap<String, PhotoAlbum> albumList = new HashMap<String, PhotoAlbum>();

	private OnEndListenner onEndListenner;

	public PhotoAlbumHelper(Context context) {
		this.resolver = context.getContentResolver();
	}

	public void getThumnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
		Cursor cursor1 = Thumbnails.queryMiniThumbnails(
				resolver,
				Thumbnails.EXTERNAL_CONTENT_URI,
				Thumbnails.MINI_KIND,
				projection
		);
		getThumnailColumnData(cursor1);
		cursor1.close();
	}


	public void getThumnailColumnData(Cursor cursor) {
		if (cursor.moveToFirst()) {
			int image_id;
			String image_path;
			int image_idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
			do {
				image_id = cursor.getInt(image_idColumn);
				image_path = cursor.getString(dataColumn);
				thumbnailList.put(image_id + "", image_path);
			} while (cursor.moveToNext());
		}
	}

	private boolean hasBuildPhotoAlbumList = false;

	public void buildPhotoAlbumList() {
		getThumnail();
		String columns[] = new String[] {
				Media._ID,
				Media.BUCKET_ID,
				Media.PICASA_ID,
				Media.DATA,
				Media.DISPLAY_NAME,
				Media.TITLE,
				Media.SIZE,
				Media.BUCKET_DISPLAY_NAME
		};
		Cursor cursor = resolver.query(
				Media.EXTERNAL_CONTENT_URI,
				columns,
				null,
				null,
				Media.DATE_MODIFIED + " desc");
		int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
		int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
		int albumDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
		int albumIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
		int photoSizeIndex = cursor.getColumnIndexOrThrow(Media.SIZE);
		cursor.moveToFirst();
		int i = 0;
		PhotoAlbum recentAlbum = new PhotoAlbum();
		recentAlbum.setName("最近相片");
		recentAlbum.photoList = new ArrayList<Photo>();
		albumList.put("", recentAlbum);
		do {
//			if (cursor.getColumnCount()==0)continue;
//			Log.e(TagFinal.ZXX, cursor.getColumnCount()+"");
			String photoPath;
			try {
				photoPath= cursor.getString(photoPathIndex);
			}catch (Exception io){
				photoPath="";
				Logger.d(TAG, "出现了异常图片的地址：");
				continue;
			}
			if (photoPath.substring(photoPath.lastIndexOf("/") + 1, photoPath.lastIndexOf(".")).replaceAll(" ", "").length() <= 0) {
				Logger.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)=" + photoPath);
			} else {
				String photo_id = cursor.getString(photoIDIndex);
				long photoSize = cursor.getLong(photoSizeIndex);
				String ablumName = cursor.getString(albumDisplayNameIndex);
				String ablumId = cursor.getString(albumIdIndex);
				PhotoAlbum ablum = albumList.get(ablumId);
				if (ablum == null) {
					ablum = new PhotoAlbum();
					ablum.setId(ablumId);
					ablum.setName(ablumName);
					ablum.photoList = new ArrayList<Photo>();
					albumList.put(ablumId, ablum);
				}
				Photo photo = new Photo();
				photo.setId(photo_id);
				photo.setPath(photoPath);
				photo.setAlbumName(ablumName);
				photo.setSize(photoSize);
				ablum.photoList.add(photo);
				if (i < 100) {
					recentAlbum.photoList.add(photo);
					i++;
				}
			}
		} while (cursor.moveToNext());
		cursor.close();
		hasBuildPhotoAlbumList = true;

	}

	public ArrayList<PhotoAlbum> getPhotoAlbumList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildPhotoAlbumList)) {
			buildPhotoAlbumList();
		}
		ArrayList<PhotoAlbum> tmpList = new ArrayList<PhotoAlbum>();
		Iterator<Entry<String, PhotoAlbum>> itr = albumList.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, PhotoAlbum> entry = (Entry<String, PhotoAlbum>) itr.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	public void destoryList() {
		thumbnailList.clear();
		albumList.clear();
		thumbnailList = null;
		albumList = null;
	}

	public void setOnEndListenner(OnEndListenner onEndListenner) {
		this.onEndListenner = onEndListenner;
	}

	public interface OnEndListenner {
		public void OnEnd(ArrayList<PhotoAlbum> list);
	}

	@Override
	protected Object doInBackground(Object... params) {
		return getPhotoAlbumList((Boolean) (params[0]));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (onEndListenner != null) {
			onEndListenner.OnEnd((ArrayList<PhotoAlbum>) result);
		}
	}
}
