package com.yfy.app.info_submit.utils;

import com.yfy.app.info_submit.constants.InfosConstant;
import com.yfy.app.info_submit.infos.ItemType;
import com.yfy.app.info_submit.infos.WriteItem;
import com.yfy.final_tag.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonStrParser {


	public static ArrayList<ArrayList<WriteItem>> getStuXx(String jsonStr) {
		ArrayList<ArrayList<WriteItem>> totalList = new ArrayList<ArrayList<WriteItem>>();
		ArrayList<WriteItem> itemList = null;
		WriteItem item;
		try {
			JSONObject obj = new JSONObject(jsonStr);
			for (int i = 0; i < InfosConstant.SUBMIT_ITEM_KEY.length; i++) {
				itemList = new ArrayList<WriteItem>();
				for (int j = 0; j < InfosConstant.SUBMIT_ITEM_KEY[i].length; j++) {
					item = new WriteItem();

					String itemKey = InfosConstant.SUBMIT_ITEM_KEY[i][j];
					String itemName = InfosConstant.SUBMIT_ITEM_NAME[i][j];
					String itemValue = obj.getString(itemKey);
					ItemType itemType = InfosConstant.ITEM_TYPE_MAP.get(itemKey);

					item.setItemKey(itemKey);
					item.setItemName(itemName);
					item.setItemValue(itemValue);
					item.setItemType(itemType);

					itemList.add(item);
				}
				totalList.add(itemList);
			}

			JSONArray array = obj.getJSONArray("sjhm");
			InfosConstant.sjhm = new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				InfosConstant.sjhm[i] = array.getString(i);
			}
			InfosConstant.stuId = obj.getString("stuid");
			InfosConstant.grade = obj.getString("szbj");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return totalList;
	}

	public static String getFenBan(String jsonStr) {
		String result = "";
		Logger.e("zxx", "getFenBan: "+jsonStr );
		try {
			JSONObject obj = new JSONObject(jsonStr);
			result = obj.getString("szbj");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}




}
