/**
 *
 */
package com.yfy.app.notice.cyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.notice.adapter.NoticeSearchAdapter;
import com.yfy.app.notice.bean.ChildBean;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.CharacterParser;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.ClearEditText;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class ContactSearchActivity extends BaseActivity {


	private TextView tips_tv;
	private NoticeSearchAdapter adapter;
	private List<ChildBean> sourceDateList = new ArrayList<ChildBean>();
	private CharacterParser characterParser ;
	private String type;

	@Bind(R.id.clear_et)
	ClearEditText clear_et;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_recycler_layout);
		characterParser= new CharacterParser();
		initAll();
	}

	private void initAll() {
		initRecycler();
		getData();
		initView();
		initSQToolbar();

	}


	private void initSQToolbar() {
		assert toolbar!=null;
		toolbar.setTitle("查找");
		toolbar.addMenuText(1, R.string.ok);
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				getContactsChild(adapter.getDataList());
			}
		});
	}

	public void getContactsChild(List<ChildBean> groups){
		ArrayList<ChildBean> childs=new ArrayList<>();
		for (ChildBean group:groups){
			if (!group.isChick()) continue;
			childs.add(group);
		}

		Intent intent=new Intent();
		intent.putParcelableArrayListExtra(TagFinal.OBJECT_TAG, childs);
		setResult(RESULT_OK,intent);
		onPageBack();

	}



	private void getData() {
		type = getIntent().getExtras().getString(TagFinal.TYPE_TAG);
		sourceDateList=getIntent().getExtras().getParcelableArrayList(TagFinal.OBJECT_TAG);
	}


	private RecyclerView recyclerView;
	public void initRecycler(){

		recyclerView = (RecyclerView) findViewById(R.id.notice_search);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		//添加分割线
		recyclerView.addItemDecoration(new RecycleViewDivider(
				mActivity,
				LinearLayoutManager.HORIZONTAL,
				1,
				getResources().getColor(R.color.gray)));
		adapter=new NoticeSearchAdapter(mActivity);
		recyclerView.setAdapter(adapter);

	}






	private void initView() {
		tips_tv = (TextView) findViewById(R.id.tips_tv);
		clear_et.addTextChangedListener(new com.yfy.final_tag.tool_textwatcher.EmptyTextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				filterData(s.toString());
			}
		});
	}




	private void filterData(String filterStr) {
		List<ChildBean> filterDateList = new ArrayList<ChildBean>();
		filterDateList.clear();
		if (!TextUtils.isEmpty(filterStr)) {
			for (ChildBean contactMember : sourceDateList) {
				String name = contactMember.getUsername();
				if (contactMember.getType()==TagFinal.TYPE_PARENT)continue;
//				Logger.e(name);
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr)) {
					filterDateList.add(contactMember);
				}
			}
		} else {
			tips_tv.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			adapter.setDataList(filterDateList);
			adapter.setLoadState(2);
			return;
		}

		if (filterDateList.size() == 0) {
			tips_tv.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.GONE);
		} else {
			tips_tv.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			adapter.setDataList(filterDateList);
			adapter.setLoadState(2);
		}
	}


}
