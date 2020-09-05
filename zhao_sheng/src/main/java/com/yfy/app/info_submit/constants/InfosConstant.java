package com.yfy.app.info_submit.constants;


import com.yfy.app.info_submit.infos.ItemType;
import com.yfy.app.info_submit.infos.WriteItem;

import java.util.ArrayList;
import java.util.HashMap;

public class InfosConstant {

	// 招生
	public final static String[] SUBMIT_ITEM_PARENT = { "基本资料", "监护人1资料", "监护人2资料",
			"兴趣爱好", "校服尺寸统计" };

	public static String[] COMPLETE_DEGRESS = { "0%", "0%", "0%", "0%", "0%" };

	public final static String[][] SUBMIT_ITEM_NAME = {
			{ "学生姓名", "性别", "生日", "证件号码", "毕业幼儿园", "户口所在地", "住址" },
			{ "监护人1姓名", "单位", "职务", "手机", "单位电话", "宅电" },
			{ "监护人2姓名", "单位", "职务", "手机", "单位电话", "宅电" },
			{ "语言表达", "艺术才能", "运动技能", "逻辑思维", "动手操作", "其他" },
			{ "身高(cm)", "体重(kg)" }, };

	public final static String[][] SUBMIT_ITEM_KEY = {
			{ "stuname", "sex", "birthday", "sfz", "byyey", "fkszd", "szdz" },
			{ "fqxm", "fqdw", "fqzw", "fqsj", "fqdh", "fqzd" },
			{ "mqxm", "mqdw", "mqzw", "mqsj", "mqdh", "mqzd" },
			{ "yybd", "yscn", "ydjn", "ljsw", "dscz", "qt" }, { "sg", "tz" }, };

	public final static HashMap<String, ItemType> ITEM_TYPE_MAP = new HashMap<String, ItemType>() {
		/**
		 *
		 */
		private static final long serialVersionUID = 7862353329795323330L;

		{
			// 基本资料
			put("stuname", new ItemType(0, "", false, 0));
			put("sex", new ItemType(1, "", false, 0));
			put("birthday", new ItemType(2, "", false, 0));
			put("sfz", new ItemType(0, "", false, 1));
			put("byyey", new ItemType(0, "", true, 0));
			put("fkszd", new ItemType(0, "", false, 0));
			put("szdz", new ItemType(0, "", false, 0));

			// 父亲资料
			put("fqxm", new ItemType(0, "", false, 0));
			put("fqdw", new ItemType(0, "", true, 0));
			put("fqzw", new ItemType(0, "", true, 0));
			put("fqsj", new ItemType(4, "", true, 2));
			put("fqdh", new ItemType(0, "", true, 2));
			put("fqzd", new ItemType(0, "", true, 2));

			// 母亲资料
			put("mqxm", new ItemType(0, "", false, 0));
			put("mqdw", new ItemType(0, "", true, 0));
			put("mqzw", new ItemType(0, "", true, 0));
			put("mqsj", new ItemType(4, "", true, 2));
			put("mqdh", new ItemType(0, "", true, 2));
			put("mqzd", new ItemType(0, "", true, 2));

			// 兴趣爱好
			put("yybd", new ItemType(3, "", true, 0));
			put("yscn", new ItemType(3, "", true, 0));
			put("ydjn", new ItemType(3, "", true, 0));
			put("ljsw", new ItemType(3, "", true, 0));
			put("dscz", new ItemType(3, "", true, 0));
			put("qt", new ItemType(0, "", true, 0));

			// 校服尺寸
			put("sg", new ItemType(0, "cm", false, 2));
			put("tz", new ItemType(0, "kg", false, 2));
		}
	};

	public final static String[][] HOBBY = {
			{ "颂读", "交流对话", "英语", "讲故事", "无" },
			{ "音乐", "美术", "舞蹈", "器乐", "无" },
			{ "体操", "足球", "跑步", "武术", "游泳", "无" }, { "棋类", "算术", "无" },
			{ "小制作", "无" } };

	public final static int[] F_PHONE_POSITION = { 1, 3 };

	public final static int[] M_PHONE_POSITION = { 2, 3 };

	public static String stuId = null;

	public static String grade = null;

	public static String[] sjhm = null;

	public static ArrayList<ArrayList<WriteItem>> totalList = null;

	public static HashMap<String, String> paramsMap = new HashMap<String, String>();

	// share



	public static String teacherId = null;

	public static String userType = null;



	public static String APP_ID = "";

}
