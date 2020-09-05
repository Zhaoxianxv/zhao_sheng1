package com.yfy.app.info_submit.frament;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.yfy.app.info_submit.activity.FormWriteItemActivity;
import com.yfy.app.info_submit.adapter.FormWriteItemAdapter;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.yfy.final_tag.dialog.AbstractDialog;
import com.yfy.final_tag.dialog.AbstractDialog.OnCustomDialogListener;
import com.yfy.final_tag.dialog.MyDialog;
import com.example.zhao_sheng.R;
import java.util.ArrayList;

public class EditAndButtonFragment extends AbstractFragment implements
		OnClickListener, OnItemClickListener, TextWatcher {

	private EditText item_edittext;
	private TextView add_tv;
	private TextView selected_tv;
	private GridView gridView;

	private FormWriteItemAdapter adapter;
	private MyDialog pointDialog;
	private View view;
	
	private WriteItem writeItem;
	

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_edittext_button, null);
		initView();
		return view;
	}

	private void initView() {
		item_edittext = (EditText) view.findViewById(R.id.item_edittext);
		add_tv = (TextView) view.findViewById(R.id.add_tv);
		add_tv.setOnClickListener(this);
		selected_tv = (TextView) view.findViewById(R.id.selected_tv);
		selected_tv.addTextChangedListener(this);
		gridView = (GridView) view.findViewById(R.id.item_gridview);

		int position1 = getArguments().getInt("position1");
		int position2 = getArguments().getInt("position2");
		writeItem = InfosConstant.totalList.get(position1).get(
				position2);
		String itemName = writeItem.getItemName();
		String itemValue = writeItem.getItemValue();
		if (LegalityJudger.isEmpty(itemValue)) {
			itemValue="";
		}

		item_edittext.setHint("请输入自定义的" + itemName + "...");
		selected_tv.setText(itemValue);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < InfosConstant.HOBBY[position2].length; i++) {
			list.add(InfosConstant.HOBBY[position2][i]);
		}
		adapter = new FormWriteItemAdapter(getActivity(), list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		
		
		
	}
	
	public void initDialog(){
		int[] initId = new int[] { R.id.ok, R.id.cancle };
		int[] listennerId = new int[] { R.id.ok, R.id.cancle };
		pointDialog = new MyDialog(getActivity(), R.layout.layout_isclear_dialog, initId,
				listennerId);
		pointDialog.setOnCustomDialogListener(onCustomDialogListener);
	}

	@Override
	public String getData() {
		return selected_tv.getText().toString();
	}

	
	private OnCustomDialogListener onCustomDialogListener=new OnCustomDialogListener() {
		@Override
		public void onClick(View v, AbstractDialog myDialog) {
			switch (v.getId()) {
				case R.id.ok:
					selected_tv.setText("无");
					writeItem.setItemValue("无");
					getActivity().finish();
					break;
				case R.id.cancle:
					pointDialog.dismiss();
					break;
			}
		}
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_tv:
			String s = item_edittext.getText().toString().trim();
			if (s == null || s.equals("")) {
				Toast.makeText(getActivity(), "输入不能为空，请重新输入",
						Toast.LENGTH_SHORT).show();
			} else if (s.length() > 6) {
				Toast.makeText(getActivity(), "最多输入6个汉字", Toast.LENGTH_SHORT)
						.show();
			} else {
				closeInputMethod();
				adapter.addItem(s);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long id) {
		if (position == adapter.noChange) {
			if (pointDialog==null) {
				initDialog();
			}
			pointDialog.show();
			return;
		} else {
			adapter.isselected[adapter.noChange] = false;
			adapter.isselected[position] = !adapter.isselected[position];
		}
		adapter.notifyDataSetChanged();
		selected_tv.setText(adapter.allSelectedItem());
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		String s = c.toString().trim();
		if (c == null || s.equals("")) {
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}
	}

	@Override
	public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		String s = c.toString().trim();
		if (c == null || s.equals("")) {
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}
	}
	
	private void closeInputMethod() {
	    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	        // imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//û����ʾ����ʾ
	        imm.hideSoftInputFromWindow(item_edittext.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}
