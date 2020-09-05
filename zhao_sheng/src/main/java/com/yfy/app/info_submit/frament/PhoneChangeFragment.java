package com.yfy.app.info_submit.frament;

import android.annotation.SuppressLint;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yfy.app.info_submit.activity.FormWriteItemActivity;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.KeyboardUtil;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.yfy.app.info_submit.utils.Util;
import com.example.zhao_sheng.R;
public class PhoneChangeFragment extends AbstractFragment implements
		OnClickListener, TextWatcher {
	private View view;
	private WriteItem writeItem;
	private WriteItem writeItem_f;
	private WriteItem writeItem_m;

	private EditText phone_et;
	private TextView phone_tv, other_phone, change_phone, wu_phone;
	private KeyboardView keyboardView;

	private String itemValue;

	private KeyboardUtil keyboardUtil;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_phone_change, null);
		phone_et = (EditText) view.findViewById(R.id.phone_et);
		phone_tv = (TextView) view.findViewById(R.id.phone_tv);
		other_phone = (TextView) view.findViewById(R.id.other_phone);
		change_phone = (TextView) view.findViewById(R.id.change_phone);
		wu_phone = (TextView) view.findViewById(R.id.wu_phone);
		keyboardView = (KeyboardView) view.findViewById(R.id.keyboard_view);
		keyboardUtil = new KeyboardUtil(keyboardView, getActivity(), phone_et, R.xml.symbols2);
		Util.hideSoftInputMethod(getActivity(), phone_et);
		keyboardUtil.showKeyboard();
		phone_et.addTextChangedListener(this);
		change_phone.setOnClickListener(this);
		wu_phone.setOnClickListener(this);
		getActivityData();
		return view;
	}

	private void getActivityData() {
		writeItem_f = InfosConstant.totalList.get(
				InfosConstant.F_PHONE_POSITION[0]).get(InfosConstant.F_PHONE_POSITION[1]);

		writeItem_m = InfosConstant.totalList.get(
				InfosConstant.M_PHONE_POSITION[0]).get(InfosConstant.M_PHONE_POSITION[1]);

		int position1 = getArguments().getInt("position1");
		int position2 = getArguments().getInt("position2");
		writeItem = InfosConstant.totalList.get(position1).get(position2);

		whatLayout();
	}

	private void whatLayout() {
		itemValue = writeItem.getItemValue();

		if (writeItem.equals(writeItem_f)) {
			other_phone.setText("监护人2的手机号码：" + writeItem_m.getItemValue());
			change_phone.setText("与＜监护人2＞手机号码交换");
			phone_et.setHint("请输入监护人1电话");
			wu_phone.setText("暂无监护人1电话");
		} else {
			change_phone.setText("与＜监护人1＞手机号码交换");
			phone_et.setHint("请输入监护人2电话");
			wu_phone.setText("暂无监护人2电话");
		}
		for (int i = 0; i < InfosConstant.sjhm.length; i++) {
			if (itemValue.equals(InfosConstant.sjhm[i])) {
				firstLayout();
				return;
			}
		}
		secondLayout();
		
		
	}

	private void firstLayout() {
		phone_tv.setVisibility(View.GONE);
		phone_et.setVisibility(View.VISIBLE);
		wu_phone.setVisibility(View.VISIBLE);
		keyboardView.setVisibility(View.VISIBLE);
		
		setText();
	}

	private void secondLayout() {
		phone_tv.setVisibility(View.GONE);
		phone_et.setVisibility(View.VISIBLE);
		wu_phone.setVisibility(View.VISIBLE);
		keyboardView.setVisibility(View.VISIBLE);
		
		setText();
	}
	
	private void setText(){
		phone_tv.setText(itemValue);
		if (itemValue.equals("无")||LegalityJudger.isEmpty(itemValue)) {
			phone_et.getText().clear();
		}else {
			phone_et.setText(itemValue);
			Editable etext = phone_et.getText();
			Selection.setSelection(etext, etext.length());
		}
	}

	@Override
	public String getData() {
		if (phone_et.isShown()) {
			return phone_et.getText().toString();
		} else {
			return phone_tv.getText().toString();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_phone:
			String s = writeItem_f.getItemValue();
			writeItem_f.setItemValue(writeItem_m.getItemValue());
			writeItem_m.setItemValue(s);
			whatLayout();
			break;

		case R.id.wu_phone:
			phone_et.setText("无");
			Editable ed=phone_et.getText();
			Selection.setSelection(ed, ed.length());
			writeItem.setItemValue("无");
			getActivity().finish();
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		String s = c.toString().trim();
		if (c == null || !LegalityJudger.MobilePhoneTip(s)) {
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}
	}

	@Override
	public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		String s = c.toString().trim();
		
		if (c == null || !LegalityJudger.MobilePhoneTip(s)) {
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}
	}
}
