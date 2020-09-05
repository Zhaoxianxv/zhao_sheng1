package com.yfy.json;

import com.yfy.app.attennew.bean.AttenType;
import com.yfy.app.notice.bean.NoticeDetail;
import com.yfy.app.notice.bean.ReadState;
import com.yfy.app.school.bean.SchoolNews;
import com.yfy.beans.NewsComment;
import com.yfy.beans.NewsMenu;
import com.yfy.beans.Property;
import com.yfy.beans.SchoolClass;
import com.yfy.beans.SchoolGrade;
import com.yfy.beans.Supply;
import com.yfy.app.vote.bean.Vote;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;



public class JsonParser {




	/**
	 * noticetion list icon
	 * @param json
	 * @return
	 */
	public static NoticeDetail getReceiveNoticeicon(String json) {
		NoticeDetail detail = new NoticeDetail();
		List<String> images=new ArrayList<String>();
		try {
			JSONObject obj = new JSONObject(json);
			if (obj.getString("images").equals("")) {
				detail.setImages(images);
			}else{
				JSONArray array =obj.getJSONArray("images");
				for (int i = 0; i < array.length(); i++) {
					String  url=array.getString(i);
					images.add(url);
				}
				detail.setImages(images);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return detail;
	}



	public static List<Property> getPropertyList(String json) {
		List<Property> propertyList = new ArrayList<Property>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray kqArray = object.getJSONArray("xclist");
			for (int i = 0; i < kqArray.length(); i++) {
				JSONObject leaveDayObj = kqArray.getJSONObject(i);
				String addtime = leaveDayObj.getString("addtime");
				String bz = leaveDayObj.getString("bz");
				String classid = leaveDayObj.getString("classid");
				String classname = leaveDayObj.getString("classname");
				String id = leaveDayObj.getString("id");
				String lastmodtime = leaveDayObj.getString("lastmodtime");
				String number = leaveDayObj.getString("nubmer");

				String supplies = "";
				String suppliesid = "";
				try {
					supplies = leaveDayObj.getString("supplies");
				} catch (Exception e) {
				}
				try {
					suppliesid = leaveDayObj.getString("suppliesid");
				} catch (Exception e) {
				}
				List<String> imageList = new ArrayList<String>();
				JSONArray imageArray = new JSONArray();
				try {
					imageArray = leaveDayObj.getJSONArray("image");
					for (int j = 0; j < imageArray.length(); j++) {
						imageList.add(imageArray.getString(j));
					}
				} catch (Exception e) {
				}

				propertyList.add(new Property(addtime, bz, classid, classname,
						id, imageList, lastmodtime, number, supplies,
						suppliesid));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return propertyList;
	}



	public static List<Supply> getSupplyList(String json) {
		List<Supply> supplyList = new ArrayList<Supply>();
		try {
			JSONObject object = new JSONObject(json);
			JSONArray xcsuppliesArray = object.getJSONArray("xcsupplieslist");
			for (int i = 0; i < xcsuppliesArray.length(); i++) {
				JSONObject itemObj = xcsuppliesArray.getJSONObject(i);
				supplyList
						.add(new Supply(itemObj.getString("bz"), itemObj
								.getString("supplies"), itemObj
								.getString("suppliesid")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return supplyList;
	}




	public static String getFavId(String json) {
		String favId = "";
		try {
			JSONObject obj = new JSONObject(json);
			String result = obj.getString("result");
			String[] str = result.split("\\|");
			favId = str[1];
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return favId;
	}



	public static List<ReadState> getReadStateList(String json) {
		List<ReadState> readStateList = new ArrayList<ReadState>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray noticestate = obj.getJSONArray("noticestate");
			for (int i = 0; i < noticestate.length(); i++) {
				JSONObject item = noticestate.getJSONObject(i);
				readStateList.add(new ReadState(
						item.getString("id"),
						item.getString("name"),
						item.getString("status")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return readStateList;
	}




	public static boolean isSuccess(String json) {
		try {
			if (json.equals("成功")) {
				return true;
			}

			if (json.equals("true")) {
				return true;
			}

			JSONObject obj = new JSONObject(json);
			String result = obj.getString("result");
			if (result.equals("true")) {
				return true;
			}

			String[] str = result.split("\\|");
			if (str[0].equals("true")) {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static String getErrorCode(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.getString("error_code");
		} catch (JSONException e) {
			e.printStackTrace();
			return "解析失败,error_code不存在";
		}

	}






	public static List<SchoolGrade> getSchoolGradeList(String json) {

		List<SchoolGrade> schoolGradeList = new ArrayList<SchoolGrade>();
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray classMenu = obj.getJSONArray("classmenu");
			for (int i = 0; i < classMenu.length(); i++) {
				JSONObject obj2 = classMenu.getJSONObject(i);

				String gradeid = obj2.getString("gradeid");
				String gradename = obj2.getString("gradename");

				JSONArray classinfo = obj2.getJSONArray("classinfo");
				List<SchoolClass> schoolClassList = new ArrayList<SchoolClass>();
				for (int j = 0; j < classinfo.length(); j++) {
					JSONObject obj3 = classinfo.getJSONObject(j);

					String classid = obj3.getString("classid");
					String classname = obj3.getString("classname");

					JSONArray classsitemenu = obj3
							.getJSONArray("classsitemenu");
					List<NewsMenu> newsMenuList = new ArrayList<NewsMenu>();
					for (int k = 0; k < classsitemenu.length(); k++) {
						JSONObject obj4 = classsitemenu.getJSONObject(k);
						String menuid = obj4.getString("menuid");
						String nmenuname = obj4.getString("nmenuname");

						NewsMenu newsMenu = new NewsMenu(menuid, nmenuname);
						newsMenu.setIslast("true");
						newsMenuList.add(newsMenu);
					}
					SchoolClass schoolClass = new SchoolClass(classid,
							classname, gradeid, gradename, newsMenuList);
					schoolClassList.add(schoolClass);
				}

				SchoolGrade schoolGrade = new SchoolGrade(gradeid, gradename,
						schoolClassList);
				schoolGradeList.add(schoolGrade);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return schoolGradeList;
	}


}
