package com.yfy.app.info_submit.utils;

import android.content.Context;
import android.widget.Toast;

import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.ItemType;
import com.yfy.app.info_submit.infos.WriteItem;

import java.util.ArrayList;
import java.util.Iterator;

public class LegalityJudger {

	public static boolean isShenFenHaoMa(String str) {
		if (str.length() != 18) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isMobilePhone(String str) {
		if (str.equals("无")) {
			return true;
		}
		for (int i = 0; i < InfosConstant.sjhm.length; i++) {
			if (str.endsWith(InfosConstant.sjhm[i])) {
				return true;
			}
		}
		if (str.length() != 11) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean MobilePhoneTip(String str) {
		if (str.equals("无")) {
			return true;
		}

		for (int i = 0; i < InfosConstant.sjhm.length; i++) {
			if (str.endsWith(InfosConstant.sjhm[i])) {
				return true;
			}
		}

		if (str.length() < 11) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean ShenFenHaoMaTip(String str) {
		if (str.length() < 18) {
			return false;
		} else {
			return true;
		}
	}



	public static boolean isLandline(String str) {
		if (str.length() > 12) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isWeightLegal(String str) {
		if (Integer.parseInt(str) > 200 || Integer.parseInt(str) < 5) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isHeightLegal(String str) {
		if (Integer.parseInt(str) > 250 || Integer.parseInt(str) < 40) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")||str.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNumOrWord(String result) {
		char[] chars = result.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if ((chars[i] >= 'A' && chars[i] <= 'Z') || (chars[i] >= 'a' && chars[i] <= 'z') || (chars[i] >= 48 && chars[i] <= 57)) {
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean judge(Context context, String result, WriteItem writeItem) {
		String itemKey = writeItem.getItemKey();
		if (result == null || result.trim().equals("")) {
			Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT).show();
			return false;
		} else if (itemKey.equals("sfz")) {
			ItemType itemType = writeItem.getItemType();
			switch (itemType.getSysmbol()) {
				case 1:
					if (!isShenFenHaoMa(result)) {
						Toast.makeText(context, "身份证号不等于18位，请重新输入", Toast.LENGTH_SHORT).show();
						return false;
					}
					break;
				default:
					if (!isNumOrWord(result)) {
						Toast.makeText(context, "其他证件只能包含字母与数字，请重新输入", Toast.LENGTH_SHORT).show();
						return false;
					}
					break;
			}
		} else if (itemKey.equals("fqsj") || itemKey.equals("mqsj")) {
			if (!isMobilePhone(result)) {
				Toast.makeText(context, "手机号不合法，请重新输入", Toast.LENGTH_SHORT).show();
				return false;
			}
		} else if (itemKey.equals("fqdh") || itemKey.equals("mqdh")
				|| itemKey.equals("fqzd") || itemKey.equals("mqzd")) {
			if (!isLandline(result)) {
				Toast.makeText(context, "固定电话不合法，请重新输入", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		} else if (itemKey.equals("sg")) {
			if (!isHeightLegal(result)) {
				Toast.makeText(context, "请查看身高是否填写错误", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		} else if (itemKey.equals("tz")) {
			if (!isWeightLegal(result)) {
				Toast.makeText(context, "请查看体重是否填写错误", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
		return true;

	}

	public static boolean can_submit(Context context, ArrayList<ArrayList<WriteItem>> totalList) {
		ArrayList<WriteItem> arrayList = null;
		WriteItem writeItem = null;
		for (Iterator<ArrayList<WriteItem>> iterator = totalList.iterator(); iterator.hasNext();) {
			arrayList = iterator.next();
			for (Iterator<WriteItem> iterator2 = arrayList.iterator(); iterator2.hasNext();) {
				writeItem = iterator2.next();
				if (isEmpty(writeItem.getItemValue())) {
					Toast.makeText(context, "请填写完整再提交", Toast.LENGTH_SHORT).show();
					return false;
				}
				InfosConstant.paramsMap.put(writeItem.getItemKey(), writeItem.getItemValue());
			}
		}
		return true;
	}
}
