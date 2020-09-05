/**
 *
 */
package com.yfy.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.login.bean.Stunlist;
import com.yfy.app.login.bean.UserRes;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.login.UserDuplicationLoginReq;
import com.yfy.app.net.login.UserGetDuplicationListReq;
import com.yfy.app.net.login.UserLoginReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.ResBody;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.greendao.AdminDb;
import com.yfy.db.GreenDaoManager;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.CPWListBeanView;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.rsa.AES;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.RxCaptcha;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.greendao.User;
import com.yfy.final_tag.Logger;
import com.yfy.keyboard.password.KeyboardTouchListener;
import com.yfy.keyboard.password.KeyboardUtil;
import com.yfy.final_tag.ConvertObjtect;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

import static com.yfy.final_tag.RxCaptcha.TYPE.NUMBER;


/**
 * @version 1.0
 */
public class LoginActivity extends BaseActivity {

	private final static String TAG =LoginActivity.class.getSimpleName();



	@Bind(R.id.login_phone)
	EditText account;
	@Bind(R.id.login_password)
	EditText password;
	@Bind(R.id.login_code)
	EditText code;
	@Bind(R.id.login_code_image)
	ImageView code_icon;
	private String edit_name = "";
	private String edit_password = "";
	public String edit_code = "";
	private String type = "";
	private String code_s="";
	private RxCaptcha rxCaptcha;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		initSQToolbar();
		initDialog();
		rxCaptcha=RxCaptcha.build();
		rxCaptcha.backColor(R.color.exchange_able_4)
				.codeLength(4)
				.fontSize(60)
				.lineNumber(2)
				.size(200, 70)
				.type(NUMBER)
				.into(code_icon);
		code_s=rxCaptcha.getCode();
		initMoveKeyBoard();
		initDialogList();
	}

	@OnClick(R.id.login_code_image)
	void setImage(){
		rxCaptcha=RxCaptcha.build();
		rxCaptcha.backColor(R.color.exchange_able_4)
				.codeLength(4)
				.fontSize(60)
				.lineNumber(2)
				.size(200, 70)
				.type(NUMBER)
				.into(code_icon);
		code_s=rxCaptcha.getCode();
	}

	private void initSQToolbar(){
		assert toolbar!=null;
		toolbar.setTitle("登录");
	}
	ConfirmAlbumWindow album_select;
	private void initDialog() {
		album_select = new ConfirmAlbumWindow(mActivity);
		album_select.setOne_select("教师");
		album_select.setTwo_select("学生");
		album_select.setName("请选择账号类型");
		album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.popu_select_one:
						type = "2";
						showProgressDialog("");
						getToken("login");
						break;
					case R.id.popu_select_two:
						type = "1";
						showProgressDialog("");
						getToken("login");
						break;
				}

			}
		});
	}

	@OnClick(R.id.login_button)
	void setlogin(){
		if (isCanSend()){
			album_select.showAtBottom();
		}
	}



	@OnClick(R.id.login_forget_password)
	void setForget(){
		Intent intent=new Intent(mActivity,ResetPasswordActivity.class);
		startActivity(intent);
	}




	public CPWListBeanView cpwListBeanView;
	List<CPWBean> cpwBeans=new ArrayList<>();
	private void setCPWlListBeanData(List<Stunlist> stunlist){
		if (StringJudge.isEmpty(stunlist)){
			toast(R.string.success_not_details);
			return;
		}else{
			cpwBeans.clear();
			for(Stunlist opear:stunlist){
				CPWBean cpwBean =new CPWBean();
				cpwBean.setName(StringUtils.getTextJoint("%1$s(%2$s)",opear.getStuname(),opear.getClassname()));
				cpwBean.setId(opear.getStuid());
				cpwBeans.add(cpwBean);
			}
		}

		closeKeyWord();
		cpwListBeanView.setDatas(cpwBeans);
		cpwListBeanView.showAtCenter();

	}
	private void initDialogList(){
		cpwListBeanView = new CPWListBeanView(mActivity);
		cpwListBeanView.setOnPopClickListenner(new CPWListBeanView.OnPopClickListenner() {
			@Override
			public void onClick(CPWBean cpwBean,String type) {
				duplicationLogin_(cpwBean.getId());
				cpwListBeanView.dismiss();
			}
		});
	}




	private boolean isCanSend() {
		closeKeyWord();
		edit_name = account.getText().toString().trim();
		edit_password = password.getText().toString().trim();
		edit_code = code.getText().toString().trim();
		if (StringJudge.isEmpty(edit_code)) {
			toast(R.string.please_edit_code);
			return false;
		}
		if (!edit_code.equals(code_s)){
			toastShow(R.string.please_edit_yse_code);
			return false;
		}
		if (StringJudge.isEmpty(edit_name)) {
			toast(R.string.please_edit_account);
			return false;
		}
		if (StringJudge.isEmpty(edit_password)) {
			toast(R.string.please_edit_password);
			return false;
		}
		keyboardUtil.hideKeyboardLayout();
		return true;
	}


    /**
	 * ----------------------------retrofit-----------------------
	 */




	private void getDuplicationList() {
		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		UserGetDuplicationListReq req = new UserGetDuplicationListReq();
		String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
		//获取参数
		String firstTo=AES.decrypTToString(edit_name+edit_password+type+ANDROID_ID+"and");
		req.setUsername(edit_name);
		req.setPassword(edit_password);
		req.setRole_id(type);
		req.setAppid(ANDROID_ID);
		req.setFirsttoken(firstTo);


		reqBody.userGetDuplicationListReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_get_duplication_user(reqEnv);
		call.enqueue(this);
		showProgressDialog("");
		Logger.e(reqEnv.toString());
	}
	private void duplicationLogin_(String stu_id) {
		//登陆时传的Constants.APP_ID：
		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		UserDuplicationLoginReq req = new UserDuplicationLoginReq();
		//获取参数
		String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
		String firstTo=AES.decrypTToString(edit_name+edit_password+type+ANDROID_ID+"and");

		req.setUsername(edit_name);
		req.setPassword(edit_password);
		req.setStuid(ConvertObjtect.getInstance().getInt(stu_id));
		req.setRole_id(type);
		req.setAppid(ANDROID_ID);
		req.setAndios("and");
		req.setFirsttoken(firstTo);


		reqBody.userDuplicationLoginReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_duplication_login(reqEnv);
		call.enqueue(this);
	}
	private void login_(String token) {
		//登陆时传的Constants.APP_ID：
		String apikey=JPushInterface.getRegistrationID(mActivity);
		if(apikey==null){
			apikey="";
		}
		String user_name,password_name;
		String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);

		user_name=AES.decrypTToString(edit_name);
		password_name=AES.decrypTToString(edit_password);

		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		UserLoginReq req = new UserLoginReq();
		//获取参数
		req.setUsername(user_name);
		req.setPassword(password_name);
		req.setRole_id(type);
		req.setAppid(ANDROID_ID);
		req.setFirsttoken(token);

		reqBody.userLoginReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().user_login(reqEnv);
		call.enqueue(this);
		Logger.e(reqEnv.toString());

	}

	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		if (!isActivity())return;
		List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
		String name=names.get(names.size()-1);
		ResEnv respEnvelope = response.body();
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;
			if (b.userLoginRes !=null){
				String result=b.userLoginRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				UserRes res= gson.fromJson(result,UserRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
					dismissProgressDialog();
					saveUser(res,TagFinal.FALSE);
				} else {
					switch (res.getError_code()){
						case "重名":
							getDuplicationList();
							break;
						case "password":
							dismissProgressDialog();
							Intent intent=new Intent(mActivity,ResetPasswordToHavePasswordActivity.class);
							intent.putExtra(Base.id, type.equals("1") ? TagFinal.USER_TYPE_STU + "_" + res.getUserid() : TagFinal.USER_TYPE_TEA + "_" + res.getUserid());
							startActivity(intent);
							break;
							default:
								dismissProgressDialog();
								toastShow(res.getError_code());
								break;
					}

				}
			}
			if (b.userGetDuplicationListRes !=null){

				String result=b.userGetDuplicationListRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));

				UserRes res= gson.fromJson(result,UserRes.class);
				setCPWlListBeanData(res.getStunlist());
				dismissProgressDialog();
			}
			if (b.userDuplicationLoginRes !=null){

				String result=b.userDuplicationLoginRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				dismissProgressDialog();
				UserRes res= gson.fromJson(result,UserRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
					saveUser(res,TagFinal.TRUE);
				}else{
					switch (res.getError_code()){
						case "password":
							Intent intent=new Intent(mActivity,ResetPasswordToHavePasswordActivity.class);
							intent.putExtra(Base.id, type.equals("1") ? TagFinal.USER_TYPE_STU + "_" + res.getUserid() : TagFinal.USER_TYPE_TEA + "_" + res.getUserid());
							startActivity(intent);
							break;
						default:
							toastShow(res.getError_code());
							break;
					}
				}
			}
			if (b.baseGetTookenRes !=null){
				String result=b.baseGetTookenRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				BaseRes res=gson.fromJson(result,BaseRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					if (token.equalsIgnoreCase("login")){
						login_(res.getTooken());
					}
				}else{
					toast(res.getError_code());
				}
			}
		}else{
			dismissProgressDialog();
			Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
			toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
		}

	}

	private void saveUser(UserRes res,String login_type){
		toastShow("登录成功");
		GreenDaoManager.getInstance().clearUser();
		User user=new User(null	);
		user.setSession_key(res.getSession_key());
		user.setHeadPic(res.getHeadPic());
		user.setFxid(res.getFxid());
		user.setName(res.getName());
		user.setPwd(edit_password);
		user.setUsertype( type.equals("1")? TagFinal.USER_TYPE_STU : TagFinal.USER_TYPE_TEA);
		user.setIdU(res.getId());
		user.setClassid(res.getClassid());
		user.setUsername(res.getUsername());
		user.setRightlist(res.getRightlist());
//		user.setToken(res.getToken());
		user.setIsDuplication(login_type);
		Base.user=user;
		UserPreferences.getInstance().saveUserID(user.getIdU());
		GreenDaoManager.getInstance().saveUser(user);
		if (!StringJudge.isEmpty(res.getRightlist())){
			saveAdmin(res.getRightlist());
		}
		setResult(RESULT_OK);
		finish();
	}

	private void saveAdmin(String res){
		GreenDaoManager.getInstance().clearAdminDb();
		AdminDb adminDb=new AdminDb(null);
		setAdminString(adminDb,"");
		if (!StringJudge.isEmpty(res)){

		List<String>  names=StringUtils.listToStringSplitCharacters(res,",");


		for (String s:names){
			switch (s){
				case "attendance":
					adminDb.setIsqjadmin(TagFinal.TRUE);
					break;
				case "logisticsrepair":
					adminDb.setIshqadmin(TagFinal.TRUE);
					break;
			}
		}

		}

		Base.adminDb=adminDb;

		GreenDaoManager.getInstance().saveAdminDb(adminDb);
	}
	private void setAdminString(AdminDb adminDb,String value){

		adminDb.setIsassessadmin(value);
		adminDb.setIsheadmasters(value);
		adminDb.setIsclassmaster(value);
		adminDb.setIsstuillcheck(value);
		adminDb.setIshqadmin(value);
		adminDb.setIsnoticeadmin(value);
		adminDb.setIsqjadmin(value);
		adminDb.setIsxcadmin(value);
		adminDb.setIsfuncRoom(value);
		adminDb.setIshqlader(value);
		adminDb.setIslogistics(value);
		adminDb.setClassinfo("");
		adminDb.setIsdutyreport(value);
		adminDb.setIseventadmin(value);
		adminDb.setIsoffice_supply(value);
		adminDb.setIsoffice_supply_master(value);
		adminDb.setIssupplycount(value);
		adminDb.setIssignetadmin(value);

		adminDb.setIselectiveadmin(value);
	}

	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		if (!isActivity())return;
		dismissProgressDialog();
		Logger.e( "onFailure  "+call.request().headers().toString() );
		toast(R.string.fail_do_not);

	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}




	//password edit keyboard
	@Bind(R.id.all_ed)
	LinearLayout rootView;
	@Bind(R.id.sv_main)
	ScrollView scrollView;
	private KeyboardUtil keyboardUtil;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
			if(keyboardUtil.isShow){
				keyboardUtil.hideSystemKeyBoard();
				keyboardUtil.hideAllKeyBoard();
				keyboardUtil.hideKeyboardLayout();
			}else {
				return super.onKeyDown(keyCode, event);
			}

			return false;
		} else
			return super.onKeyDown(keyCode, event);
	}

	private void initMoveKeyBoard() {
		keyboardUtil = new KeyboardUtil(this, rootView, scrollView);
		keyboardUtil.setOtherEdittext(account,code);
		// monitor the KeyBarod state
		keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
		// monitor the finish or next Key
		keyboardUtil.setInputOverListener(new inputOverListener());
		password.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1));
	}

	class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

		@Override
		public void KeyBoardStateChange(int state, EditText editText) {
//            System.out.println("state" + state);
//            System.out.println("editText" + editText.getText().toString());
		}
	}

	class inputOverListener implements KeyboardUtil.InputFinishListener {

		@Override
		public void inputHasOver(int onclickType, EditText editText) {
//            System.out.println("onclickType" + onclickType);
//            System.out.println("editText" + editText.getText().toString());
		}
	}

}
