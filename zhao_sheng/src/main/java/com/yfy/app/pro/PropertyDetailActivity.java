/**
 *
 */
package com.yfy.app.pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.base.activity.BaseActivity;
import com.yfy.beans.Property;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.multi.MultiPictureView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author yfy1
 * @Date 2016年1月19日
 * @version 1.0
 * @Desprition
 */
public class PropertyDetailActivity extends BaseActivity implements
		OnClickListener {

	private final static String TAG ="zxx";
	private TextView headTitle;
	private TextView supply_name;
	private TextView supply_count;
	private TextView add_date;
	private TextView modify_date;
	private TextView remark_tv;
	private ScrollView scrollView;


	private Property property;

	@Bind(R.id.notice_add_mult)
	MultiPictureView add_mult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_detail);
		initAll();
	}

	private void initAll() {
		initData();
		initViews();
		initAbsListView();
	}

	private void initData() {
		property = (Property) getIntent().getExtras().getSerializable("property");
	}

	private void initViews() {
		headTitle = (TextView) findViewById(R.id.head_title);
		supply_name = (TextView) findViewById(R.id.supply_name);
		supply_count = (TextView) findViewById(R.id.supply_count);
		add_date = (TextView) findViewById(R.id.add_date);
		modify_date = (TextView) findViewById(R.id.modify_date);
		remark_tv = (TextView) findViewById(R.id.remark_tv);
		scrollView = (ScrollView) findViewById(R.id.scrollView);

		headTitle.setText("校产详情");
		headTitle.setVisibility(View.VISIBLE);

		supply_name.setText(property.getSupplies());
		supply_count.setText(property.getNumber());
		add_date.setText(property.getAddtime());
		modify_date.setText(property.getLastmodtime());
		remark_tv.setText(property.getBz());

		setOnClickListener(mActivity, R.id.left_rela);
	}


	private void initAbsListView() {
		add_mult.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
			@Override
			public void onItemClicked(@NotNull View view, int index, @NotNull ArrayList<String> uris) {
				Intent intent=new Intent(mActivity, SingePicShowActivity.class);
				Bundle b=new Bundle();
				b.putString(TagFinal.ALBUM_SINGE_URI,uris.get(index));
				intent.putExtras(b);
				startActivity(intent);
			}
		});
		add_mult.addItem( property.getImageList());
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_rela:
				finish();
				break;
		}
	}
}
