/**
 * 
 */
package com.yfy.app.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.glide.PhotoAlbum;

import java.util.ArrayList;


/**
 * @author yfy
 * @date 2015-10-6
 * @version 1.0
 * @description AllAlbumActivity
 */
public class AlbumAllActivity extends BaseActivity {


	private ListView album_listview;
	private AlbumAllAdapter adapter;
	private Intent intent;
	public ArrayList<PhotoAlbum> allPhotoAlbumList = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.album_new_all);
		getData();
		init();
		initSQToolbar();
	}
	/**
	 * @description
	 */
	public void getData(){
		Bundle b = getIntent().getExtras();
		if (b != null) {
			if (b.containsKey("allPhotoAlbumList")) {
				allPhotoAlbumList=b.getParcelableArrayList("allPhotoAlbumList");
			}
		}
	}

	private void initSQToolbar() {

		Toolbar toolbar= (Toolbar) findViewById(R.id.album_all_title_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationIcon(R.mipmap.app_head_back);
		toolbar.setTitle("相册");
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}
	private void init() {
		intent = getIntent();
		album_listview = (ListView) findViewById(R.id.album_listview);
		album_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle b = new Bundle();
				b.putInt("position", position);
				intent.putExtras(b);
				setResult(RESULT_OK, intent);
				onPageBack();
				overridePendingTransition(R.anim.activity_close_enter,R.anim.activity_close_exit);
			}
		});
		adapter = new AlbumAllAdapter(mActivity, allPhotoAlbumList);
		album_listview.setAdapter(adapter);

	}


}
