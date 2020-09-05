package com.yfy.app.info_submit.frament;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yfy.app.info_submit.activity.FormWriteItemActivity;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.example.zhao_sheng.R;
public class SingleSelectFragment extends AbstractFragment implements
		OnClickListener {
	private RelativeLayout man_rela, woman_rela;
	private TextView man_tv, woman_tv;
	private ImageView man_iv, woman_iv;
	private String result;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_single_select, null);
		man_tv = (TextView) view.findViewById(R.id.man_tv);
		woman_tv = (TextView) view.findViewById(R.id.woman_tv);

		man_rela = (RelativeLayout) view.findViewById(R.id.man_rela);
		woman_rela = (RelativeLayout) view.findViewById(R.id.woman_rela);

		man_iv = (ImageView) view.findViewById(R.id.man_iv);
		woman_iv = (ImageView) view.findViewById(R.id.woman_iv);

		man_rela.setOnClickListener(this);
		woman_rela.setOnClickListener(this);
		
		
		int position1 = getArguments().getInt("position1");
		int position2 = getArguments().getInt("position2");
		WriteItem writeItem = InfosConstant.totalList.get(position1).get(
				position2);
		
		if (writeItem.getItemValue().equals("男")) {
			man_iv.setVisibility(View.VISIBLE);
			result = man_tv.getText().toString();
		}else {
			woman_iv.setVisibility(View.VISIBLE);
			result = woman_tv.getText().toString();
		}
		return view;
	}

	@Override
	public String getData() {
		return result;
	}

	@Override
	public void onClick(View v) {
		((FormWriteItemActivity)getActivity()).setOkClicked(true);
		switch (v.getId()) {
		case R.id.man_rela:
			man_iv.setVisibility(View.VISIBLE);
			woman_iv.setVisibility(View.GONE);
			result = man_tv.getText().toString();
			break;
		case R.id.woman_rela:
			woman_iv.setVisibility(View.VISIBLE);
			man_iv.setVisibility(View.GONE);
			result = woman_tv.getText().toString();
			break;
		}
	}
}
