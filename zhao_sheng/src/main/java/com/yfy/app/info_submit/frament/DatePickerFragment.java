package com.yfy.app.info_submit.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import com.yfy.app.info_submit.activity.FormWriteItemActivity;
import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.app.info_submit.utils.LegalityJudger;
import com.example.zhao_sheng.R;
public class DatePickerFragment extends AbstractFragment {

	private DatePicker datePicker;
	private String rusult;
	private int[] ymd;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_datepicker, null);
		datePicker = (DatePicker) view.findViewById(R.id.datepicker);

		int position1 = getArguments().getInt("position1");
		int position2 = getArguments().getInt("position2");
		WriteItem writeIrem = InfosConstant.totalList.get(position1).get(position2);
		rusult = writeIrem.getItemValue();
		if (LegalityJudger.isEmpty(rusult)) {
			rusult = "2000-01-01";
		}
		String[] nyr = rusult.split("-");
		ymd = change(nyr);

		((FormWriteItemActivity) getActivity()).setOkClicked(true);
		datePicker.init(ymd[0], ymd[1], ymd[2], new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				rusult = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
			}
		});
		return view;
	}

	public int[] change(String[] nyr) {
		int[] ymd = new int[nyr.length];
		for (int i = 0; i < ymd.length; i++) {
			ymd[i] = Integer.parseInt(nyr[i]);
		}
		ymd[1] = ymd[1] - 1;
		return ymd;
	}

	@Override
	public String getData() {
		return rusult;
	}
}
