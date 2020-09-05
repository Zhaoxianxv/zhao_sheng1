/**
 *
 */
package com.yfy.app.check;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhao_sheng.R;
import com.yfy.app.check.bean.IllBean;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.SQToolBar;

import java.util.List;

/**
 * @author yfy1
 * @Date 2015年10月20日
 * @version 1.0
 * @Desprition
 */
public class CEditActivity extends BaseActivity  {

	private EditText edittext;
	private int adapter_index = -1;

	private String title, content, hint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acticity_edittext);
		initDatas();

	}

	IllBean bean;
	private void initDatas() {
		Intent  intent = getIntent();
		bean=intent.getParcelableExtra(TagFinal.OBJECT_TAG);
		title = bean.getTablename();
		content = bean.getValue();
		if (StringJudge.isEmpty(content))content="";
		hint = "请输入"+bean.getTablename();

		adapter_index = intent.getIntExtra("position",-1);
		initViews();

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
					Toast.makeText(CEditActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent();
					bean.setValue(result);
					intent.putExtra(TagFinal.OBJECT_TAG, bean);
					intent.putExtra("position", adapter_index);
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

		switch (bean.getTablevaluetype()){
			case "longtxt":

				edittext.setInputType(InputType.TYPE_CLASS_TEXT);
				break;
			case "txt":
				edittext.setInputType(InputType.TYPE_CLASS_TEXT);
				break;
			case "int":
				edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
				break;
			case "tel":
				edittext.setInputType(InputType.TYPE_CLASS_PHONE);
				break;
			default:
				List<String> list=StringUtils.listToStringSplitCharacters(bean.getTablevaluetype(), "_");
				if (list.size()>1){
					final String num=list.get(list.size()-1);
					edittext.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_CLASS_NUMBER);
					edittext.addTextChangedListener(new MyWatcher() {
						@Override
						public void afterTextChanged(Editable s) {
							iedit(s,ConvertObjtect.getInstance().getInt(num));
						}
					});

				}else{
					edittext.setInputType(InputType.TYPE_CLASS_TEXT);
				}

				break;
		}
	}

	public void iedit(Editable s, int num){
		String edit_String=s.toString();
		int posDot=edit_String.indexOf(".");
		if (posDot<0){
			return;
		}
		if (posDot==0){
			s.delete(0,edit_String.length());
			return;
		}
		if (edit_String.length()-posDot-1>num)
			s.delete(posDot+num+1,edit_String.length());//限制2位小数.
	}
	private void setCursorEnd(EditText et) {
		String text = et.getText().toString();
		et.setSelection(text.length());
	}


}
