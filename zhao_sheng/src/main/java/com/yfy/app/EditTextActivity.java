/**
 *
 */
package com.yfy.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.zhao_sheng.R;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.TagFinal;
import com.yfy.view.SQToolBar;

/**
 * @author yfy1
 * @Date 2015年10月20日
 * @version 1.0
 * @Desprition
 */
public class EditTextActivity extends BaseActivity  {

	private EditText edittext;
	private int position = -1;

	private String title, content, hint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_edittext);
		initDatas();
		initViews();
	}

	private void initDatas() {
		Bundle b = getIntent().getExtras();
		title = b.getString("title");
		content = b.getString("content");


		if (b.containsKey("hint")) {
			hint = b.getString("hint");
		}

		if (b.containsKey("position")) {
			position = b.getInt("position");
		}
		initSQToolbar(title);
	}

	private void initSQToolbar(String title){
		toolbar.setTitle(title);
		toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok);
		toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
			@Override
			public void onClick(View view, int position) {
				String result = edittext.getText().toString().trim();
				if (TextUtils.isEmpty(result)) {
					Toast.makeText(EditTextActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = getIntent();
					Bundle b = intent.getExtras();
					b.putString("content", result);
					if (position != -1) {
						b.putInt("position", position);
					}
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});
	}

	private void initViews() {
		edittext = (EditText) findViewById(R.id.edittext);

		if (TextUtils.isEmpty(content)) {
			edittext.setText("");
		} else {
			edittext.setText(content);
			setCursorEnd(edittext);
		}

		if (!TextUtils.isEmpty(hint)) {
			edittext.setHint(hint);
		}
	}

	private void setCursorEnd(EditText et) {
		String text = et.getText().toString();
		et.setSelection(text.length());
	}


}
