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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yfy.app.info_submit.activity.FormWriteItemActivity;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.ItemType;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.KeyboardUtil;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.yfy.app.info_submit.utils.Util;
import com.example.zhao_sheng.R;
public class EditTextFragment extends AbstractFragment implements
		OnClickListener, TextWatcher {

	private EditText item_edittext;
	private TextView postfix_tv;
	private RelativeLayout wu_rela;
	private TextView if_wu_tv;
	private KeyboardView keyboardview;
	private KeyboardUtil keyboardUtil;
	private View view;
	private WriteItem writeItem;
	private boolean isSfz;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_edittext, null);
		initView();
		return view;
	}

	@SuppressLint("CutPasteId")
	private void initView() {
		int position1 = getArguments().getInt("position1");
		int position2 = getArguments().getInt("position2");
		writeItem = InfosConstant.totalList.get(position1).get(position2);
		String itemName = writeItem.getItemName();
		String itemValue = writeItem.getItemValue();
		if (itemValue.equals("无") || LegalityJudger.isEmpty(itemValue)) {
			itemValue = "";
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}

		item_edittext = (EditText) view.findViewById(R.id.item_edittext);
		postfix_tv = (TextView) view.findViewById(R.id.postfix_tv);
		wu_rela = (RelativeLayout) view.findViewById(R.id.wu_rela);
		if_wu_tv = (TextView) view.findViewById(R.id.if_wu_tv);
		keyboardview = (KeyboardView) view.findViewById(R.id.keyboard_view);

		if_wu_tv.setText("若暂无" + "“" + itemName + "”," + "请点击...");
		item_edittext.setHint("请输入" + itemName);
		item_edittext.setText(itemValue);
		Editable etext = item_edittext.getText();
		Selection.setSelection(etext, etext.length());
		item_edittext.addTextChangedListener(this);

		ItemType itemType = writeItem.getItemType();

		if (itemType.getPostfix() != null && !itemType.getPostfix().equals("")) {
			postfix_tv.setVisibility(View.VISIBLE);
			postfix_tv.setText(itemType.getPostfix());
		}

		if (itemType.getSysmbol() == 1) {
			keyboardUtil = new KeyboardUtil(keyboardview, getActivity(), item_edittext, R.xml.symbols1);
		} else if (itemType.getSysmbol() == 2) {
			keyboardUtil = new KeyboardUtil(keyboardview, getActivity(),
					item_edittext, R.xml.symbols2);
		}
		if (keyboardUtil != null) {
			Util.hideSoftInputMethod(getActivity(), item_edittext);
			keyboardUtil.showKeyboard();
		}

		if (itemType.isCanWu()) {
			wu_rela.setVisibility(View.VISIBLE);
			wu_rela.setOnClickListener(this);
		}

		if (writeItem.getItemKey().equals("sfz") && itemType.getSysmbol() == 1) {
			isSfz = true;
		}

	}

	@Override
	public String getData() {
		return item_edittext.getText().toString();
	}

	public void setHint(String itemName) {
		item_edittext.setHint("请输入" + itemName);
	}

	@Override
	public void onClick(View v) {
		item_edittext.setText("无");
		Editable ed = item_edittext.getText();
		Selection.setSelection(ed, ed.length());
		writeItem.setItemValue("无");
		getActivity().finish();
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence c, int arg1, int arg2, int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
		String s = c.toString().trim();
		if (c == null || s.equals("")
				|| (isSfz && !LegalityJudger.ShenFenHaoMaTip(s))) {
			((FormWriteItemActivity) getActivity()).setOkClicked(false);
		} else {
			((FormWriteItemActivity) getActivity()).setOkClicked(true);
		}
	}
}
