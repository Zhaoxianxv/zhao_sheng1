package com.yfy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import com.example.zhao_sheng.R;
import com.yfy.app.allclass.ShapeSingleActivity;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.bean.KeyValue;
import com.yfy.app.bean.MainValue;
import com.yfy.app.check.CheckClassActivity;
import com.yfy.app.check.CheckTjActivity;
import com.yfy.app.delay_service.ChoiceClassActivity;
import com.yfy.app.delay_service.DelayServiceMasterMainActivity;
import com.yfy.app.duty.DutyMainActivity;
import com.yfy.app.ebook.EbookListMainActivity;
import com.yfy.app.event.EventMainActivity;
import com.yfy.app.footbook.FootBookMainActivity;
import com.yfy.app.goods.GoodsIndexctivity;
import com.yfy.app.info_submit.activity.AuthenticationActivity;
import com.yfy.app.integral.IntegralMainActivity;
import com.yfy.app.login.LoginActivity;
import com.yfy.app.login.bean.UserRes;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.process.ProcessGetTypeReq;
import com.yfy.app.process.ProcessMainActivity;
import com.yfy.app.process.bean.MainState;
import com.yfy.app.school.SchoolNewsActivity;
import com.yfy.app.tea_evaluate.TeaEvaluateActivity;
import com.yfy.app.tea_evaluate.TeatabActivity;
import com.yfy.app.video.VideoListMainActivity;
import com.yfy.base.Variables;
import com.yfy.final_tag.banner.ADInfo;
import com.yfy.final_tag.banner.CycleViewPager;
import com.yfy.final_tag.banner.ViewFactory;
import com.yfy.base.App;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.GreenDaoManager;
import com.yfy.greendao.User;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.dialog.CPWBean;
import com.yfy.final_tag.dialog.ConfirmAlbumWindow;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.glide.GlideTools;
import com.yfy.greendao.KeyValueDb;
import com.yfy.final_tag.Logger;
import com.yfy.upload.UploadDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class HomeNewActivity extends BaseActivity  {

	private static final String TAG = HomeNewActivity.class.getSimpleName();

	private List<ImageView> views = new ArrayList<>();
	private List<ADInfo> infos = new ArrayList<>();
	private CycleViewPager cycleViewPager;
	@Bind(R.id.home_head)
	ImageView login_tv;

	private int[] imageUrls = {
			R.drawable.home_header_0,
			R.drawable.home_header_1,
			R.drawable.home_header_2,
			R.drawable.home_header_3,
			R.drawable.home_header_4,
			R.drawable.home_header_5,
			R.drawable.home_header_6,
			R.drawable.home_header_7,

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_new_activity);
		if (App.isServiceRunning(mActivity,UploadDataService.class.getSimpleName())){
			Logger.e(TagFinal.ZXX, "UploadDataService: " );
		}else{
			startService(new Intent(App.getApp().getApplicationContext(),UploadDataService.class));//开启更新
		}
		initialize();
		initToolbar();
		initCollapsing();
		initRecyclerView();
		getMainItem();
		initHead();
		initDialog();

	}


	private void initHead(){
		if (Base.user==null){
			GlideTools.chanCircle(mActivity,R.drawable.head_user,login_tv);
		}else{
			GlideTools.chanCircle(mActivity,Base.user.getHeadPic(),login_tv,R.drawable.head_user);
		}
	}

	/**
	 * Toolbar 的NavigationIcon大小控制mipmap
	 */
	public void initToolbar() {
		Toolbar mToolbar =  findViewById(R.id.home_title_bar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationIcon(null);  //隐藏掉返回键比如首页，可以调用
	}

	//配置CollapsingToolbarLayout布局
	public void initCollapsing() {
		CollapsingToolbarLayout mCollapsingToolbarLayout =  findViewById(R.id.home_collapsing);
		mCollapsingToolbarLayout.setTitle(" ");
		mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
		mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
	}




	public AppBarLayout appBarLayout;
	public RecyclerView recyc_base;
	public MainAdapter base_adapter;
	public List<KeyValue> adapter_data_default =new ArrayList<>();
	public List<KeyValue> adapter_data_show =new ArrayList<>();
	public void initRecyclerView(){
		recyc_base=findViewById(R.id.home_recyclerview);
		appBarLayout =  findViewById(R.id.appbar_layout);
		//AppBarLayout 展开执行刷新
		appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
			}
		});
		recyc_base.addOnScrollListener(new RecyclerView.OnScrollListener() {
			// 用来标记是否正在向上滑动

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);

			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

			}
		});


		GridLayoutManager manager = new GridLayoutManager(mActivity.getApplicationContext(),3);
		recyc_base.setLayoutManager(manager);
		manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				return adapter_data_show.get(position).getSpan_size();
			}
		});
		base_adapter=new MainAdapter(mActivity);
		base_adapter.setItemOnc(new MainAdapter.ItemOnc() {
			@Override
			public void onc(KeyValue bean) {

				if (!StringJudge.isEmpty( App.getApp().getDaoSession().getKeyValueDbDao().queryRaw("where type = \"main_point_type\" and key_value_id = \""+bean.getId()+"\""))){
					List<KeyValueDb> db_index=GreenDaoManager.getInstance().getKeyValueDbList("where type = \"main_point_type\" and key_value_id = \""+bean.getId()+"\"");
					KeyValueDb keyValueDb=db_index.get(0);
					int num=keyValueDb.getNum();
					++num;
					keyValueDb.setNum(num);
					GreenDaoManager.getInstance().saveKeyValueDb(keyValueDb);
				}
				onActivity(bean);
			}

			@Override
			public void onLoad() {
			}
		});

		recyc_base.setAdapter(base_adapter);

	}


	public void addBaseProjcet(){
		adapter_data_default.clear();

	}


	private void addProcess(List<KeyValue> data,int res,String type){
		KeyValue keyValue=new KeyValue(TagFinal.TYPE_ITEM);
		keyValue.setName("编辑排序");
		keyValue.setRes_image(res);
		keyValue.setSpan_size(1);
		keyValue.setKey("");
		keyValue.setMax("");
        keyValue.setId(String.valueOf(0));
		keyValue.setActivity_type(type);
		keyValue.setRes_color(ColorRgbUtil.getBaseColor());
		data.add(keyValue);
	}

	private void addProcess(List<KeyValue> data,MainValue bean,String type){
		KeyValue keyValue=new KeyValue(TagFinal.TYPE_ITEM);
		keyValue.setName(bean.getTitle());
		keyValue.setKey(bean.getIcon());
		keyValue.setType(bean.getType());
		keyValue.setRight(bean.getRight());
        keyValue.setId(String.valueOf(bean.getId()));
        keyValue.setUser_type(bean.getUsertype());
		keyValue.setSpan_size(1);
		keyValue.setMax(String.valueOf(bean.getMessagecount()));
		addType(keyValue,bean,type,bean.getRight());
		data.add(keyValue);
	}
	private void addProcess(List<KeyValue> data,int image,MainValue bean){
		KeyValue keyValue=new KeyValue(TagFinal.TYPE_ITEM);
		keyValue.setGroup_id(bean.getGroup());
		keyValue.setSecond_group_id(bean.getSecondgroup());
		keyValue.setItem_id(bean.getId());
		keyValue.setUser_type(bean.getUsertype());
		keyValue.setType(bean.getType());
		keyValue.setRight(bean.getRight());
		keyValue.setRes_image(image);
		keyValue.setId(String.valueOf(bean.getId()));
		keyValue.setName(bean.getTitle());
		keyValue.setKey(bean.getIcon());
		keyValue.setMax(String.valueOf(bean.getMessagecount()));
		keyValue.setSpan_size(1);
		if (!StringJudge.isEmpty(bean.getStateinfo())){
			List<CPWBean> list=keyValue.getCpwBeanArrayList();
			for (MainState state:bean.getStateinfo()){
				list.add(new CPWBean(state.getStatename(),String.valueOf(state.getStateid())));
			}
			keyValue.setCpwBeanArrayList(list);
		}
		addType(keyValue,bean,"ProcessMainActivity",bean.getRight());
		data.add(keyValue);
	}
	private void addType(KeyValue keyValue,MainValue bean,String type,String right){
		if (right==null)right="";
		switch (bean.getUsertype()){
			case "tea":
				if (Base.user==null){
					keyValue.setActivity_type("LoginActivity");
					keyValue.setRes_color(ColorRgbUtil.getGray());
				}else{
					keyValue.setActivity_type(type);
					if (Base.user.getUsertype().equalsIgnoreCase(bean.getUsertype())&&!right.equalsIgnoreCase("")){
						keyValue.setRes_color(ColorRgbUtil.getBaseColor());
					}else{
						keyValue.setRes_color(ColorRgbUtil.getGray());
					}
				}
				break;
			case "stu":
				if (Base.user==null){
					keyValue.setActivity_type("LoginActivity");
					keyValue.setRes_color(ColorRgbUtil.getGray());
				}else{
					keyValue.setActivity_type(type);
					if (Base.user.getUsertype().equalsIgnoreCase(bean.getUsertype())&&!right.equalsIgnoreCase("")){
						keyValue.setRes_color(ColorRgbUtil.getBaseColor());
					}else{
						keyValue.setRes_color(ColorRgbUtil.getGray());
					}
				}
				break;
			case "tea,stu,gus":
				keyValue.setActivity_type(type);
				keyValue.setRes_color(ColorRgbUtil.getBaseColor());
				break;
				default:
					if (Base.user==null){
						keyValue.setActivity_type("LoginActivity");
						keyValue.setRes_color(ColorRgbUtil.getGray());
					}else{
						keyValue.setActivity_type(type);
						if (!right.equalsIgnoreCase("")){
							keyValue.setRes_color(ColorRgbUtil.getBaseColor());
						}else{
							keyValue.setRes_color(ColorRgbUtil.getGray());
						}
					}
					break;
		}

	}
	private boolean isUserType(KeyValue bean){
		switch (bean.getUser_type()){
			case "tea":
				if (!Base.user.getUsertype().equalsIgnoreCase(bean.getUser_type())){
					toast("请登录老师用户");
					return false;
				}else{
					if (bean.getRight().equalsIgnoreCase("")){
						toast("用户没有权限");
						return false;
					}
				}
				break;
			case "stu":
				if (!Base.user.getUsertype().equalsIgnoreCase(bean.getUser_type())){
					toast("请登录学生用户");
					return false;
				}else{
					if (bean.getRight().equalsIgnoreCase("")){
						toast("用户没有权限");
						return false;
					}
				}
				break;
			case "tea,stu,gus":
				if (bean.getRight().equalsIgnoreCase("")){
					toast("用户没有权限");
					return false;
				}
				break;
				default:
					if (bean.getRight().equalsIgnoreCase("")){
						toast("用户没有权限");
						return false;
					}
					break;
		}
		return true;
	}
	private void onActivity(KeyValue bean){
		Intent intent;
		switch (bean.getActivity_type()){
			case "SelectedTermActivity":
				intent = new Intent(mActivity, SelectedTermActivity.class);
				startActivity(intent);
				break;
			case "AuthenticationActivity":
				intent = new Intent(mActivity, AuthenticationActivity.class);
				startActivity(intent);
				break;
			case "SchoolNewsActivity":
				intent=new Intent(mActivity,SchoolNewsActivity.class);
				startActivity(intent);
				break;
			case "LoginActivity":
				intent = new Intent(mActivity, LoginActivity.class);
				startActivityForResult(intent,TagFinal.UI_ADD);
				break;
			case "ProcessMainActivity":
				if (isUserType(bean)){
					intent = new Intent(mActivity, ProcessMainActivity.class);
					intent.putExtra(Base.id,bean.getItem_id());
					intent.putExtra(Base.title,bean.getName());
					intent.putExtra(Base.type,bean.getType());
					intent.putExtra(Base.content,bean.getRight());
					intent.putParcelableArrayListExtra(Base.data, (ArrayList<? extends Parcelable>) bean.getCpwBeanArrayList());
					intent.putExtra("group_parent",bean.getGroup_id());
					intent.putExtra("group_child",bean.getSecond_group_id());
					startActivityForResult(intent,TagFinal.UI_ADD);
				}else{

				}

				break;
			case "VoteListActivity":
				if (isUserType(bean)){
					intent = new Intent(mActivity, ProcessMainActivity.class);
					intent.putExtra(Base.id,bean.getItem_id());
					intent.putExtra(Base.title,bean.getName());
					intent.putExtra(Base.type,bean.getType());
					intent.putExtra(Base.content,bean.getRight());
					intent.putExtra("group_parent",bean.getGroup_id());
					intent.putExtra("group_child",bean.getSecond_group_id());
					startActivityForResult(intent,TagFinal.UI_ADD);
				}
				break;
			case "ChoiceClassActivity":
				if (isUserType(bean)){
					Intent intentdelay =new Intent(mActivity,ChoiceClassActivity.class);
					Bundle b_delay = new Bundle();
					b_delay.putBoolean(TagFinal.TYPE_TAG,false);
					b_delay.putBoolean("admin",false );
					intentdelay.putExtras(b_delay);
					intentdelay.putExtra(Base.content,bean.getRight());
					startActivity(intentdelay);
				}
				break;
			case "DelayServiceMasterMainActivity":
				if (isUserType(bean)){
					Intent intentdelay =new Intent(mActivity,DelayServiceMasterMainActivity.class);
					Bundle b_delay = new Bundle();
					b_delay.putBoolean(TagFinal.TYPE_TAG,false);
					b_delay.putBoolean("admin",false );
					intentdelay.putExtras(b_delay);
					intentdelay.putExtra(Base.content,bean.getRight());
					startActivity(intentdelay);
				}
				break;
			case "CheckClassActivity":
				if (isUserType(bean)){
					intentDialog =new Intent(mActivity,CheckClassActivity.class);
					intentDialog.putExtra(Base.content,bean.getRight());
					album_select.showAtCenter();
				}

				break;
			case "CheckTjActivity":
				if (isUserType(bean)){
					intentDialog =new Intent(mActivity,CheckTjActivity.class);
					intentDialog.putExtra(Base.content,bean.getRight());
					album_select.showAtCenter();
				}

				break;
			case "DutyMainActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,DutyMainActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;

			case "GoodsIndexctivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,GoodsIndexctivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "VideoListMainActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,VideoListMainActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
            case "EbookListMainActivity":
                if (isUserType(bean)){
                    intent=new Intent(mActivity,EbookListMainActivity.class);
                    intent.putExtra(Base.content,bean.getRight());
                    startActivity(intent);
                }
                break;
			case "FootBookMainActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,FootBookMainActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "DeyuCheckActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,DeyuCheckActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "WebActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,WebActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					String url = Base.RETROFIT_URI+StringUtils.stringToGetTextJoint(Base.SCHEDULE,Base.user.getIdU(),Base.user.getUsertype());
					Bundle b = new Bundle();
					b.putString(TagFinal.URI_TAG, url);
					b.putString(TagFinal.TITLE_TAG, "课程表");
					intent.putExtras(b);
					startActivity(intent);
				}
				break;
			case "ShapeSingleActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,ShapeSingleActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "EventMainActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,EventMainActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "TeaEvaluateActivity":
				if (isUserType(bean)){
					intent=new Intent(mActivity,TeaEvaluateActivity.class);
					intent.putExtra(Base.content,bean.getRight());
					startActivity(intent);
				}
				break;
			case "MainSetViewActivity":
				intent = new Intent(mActivity, MainSetViewActivity.class);
				startActivityForResult(intent,TagFinal.UI_ADD);
				break;
		}
	}
	private Intent intentDialog;
	private ConfirmAlbumWindow album_select;
	private void initDialog() {
		album_select = new ConfirmAlbumWindow(mActivity);
		album_select.setOne_select("教师");
		album_select.setTwo_select("学生");
		album_select.setName("请选择");
		album_select.setOnPopClickListenner(new ConfirmAlbumWindow.OnPopClickListenner() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
					case R.id.popu_select_one:
						Base.user_check_type="tea";
						startActivity(intentDialog);
						break;
					case R.id.popu_select_two:
						Base.user_check_type="stu";
						startActivity(intentDialog);
						break;
				}
			}
		});
	}

	@OnClick(R.id.home_head)
	void setHead(){
		Intent intent;
		if (Base.user !=null){
			intent = new Intent(this, IntegralMainActivity.class);
            startActivityForResult(intent,TagFinal.UI_ADD);

		}else{
			intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent,TagFinal.UI_ADD);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK){
			switch (requestCode){
				case TagFinal.UI_ADD:
					initHead();
					getMainItem();
					break;
			}
		}
	}

	/**
	 * ----------------------------retrofit-----------------------
	 */

	public void getMainItem() {
		ReqEnv reqEnv = new ReqEnv();
		ReqBody reqBody = new ReqBody();
		ProcessGetTypeReq req = new ProcessGetTypeReq();
		//获取参数
		if (Base.user==null){
			req.setSession_key("gus");
		}else{
			req.setSession_key(Base.user.getSession_key());
		}

		reqBody.processGetTypeReq = req;
		reqEnv.body = reqBody;
		Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().process_get_type(reqEnv);
		call.enqueue(this);
		showProgressDialog("");
	}

	@Override
	public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
		if (!isActivity())return;
		ResEnv respEnvelope = response.body();
		List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
		String name=names.get(names.size()-1);
		if (respEnvelope != null) {
			ResBody b=respEnvelope.body;
			if (b.processGetTypeRes !=null){
				String result=b.processGetTypeRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				BaseRes res= gson.fromJson(result,BaseRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)){
					dismissProgressDialog();
					addBaseProjcet();
					initGetViewItem(res);
				}else{
					switch (res.getError_code()){
						case Base.error_code:
                            toast("登录过期");
                            Base.user=null;
                            GreenDaoManager.getInstance().clearUser();
                            initHead();
                            getMainItem();
							break;
							default:
								dismissProgressDialog();
								toast(res.getError_code());
								break;
					}
				}

			}
			if (b.userLoginRes !=null){
				String result=b.userLoginRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				UserRes res= gson.fromJson(result,UserRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
					resetLogin(res);
				} else {
					dismissProgressDialog();
					toast(res.getError_code());
				}
			}
			if (b.userDuplicationLoginRes !=null){
				String result=b.userDuplicationLoginRes.result;
				Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
				UserRes res= gson.fromJson(result,UserRes.class);
				if (res.getResult().equalsIgnoreCase(TagFinal.TRUE)) {
					resetLogin(res);
				} else {
					dismissProgressDialog();
					toast(res.getError_code());
				}
			}
		}else{
			dismissProgressDialog();
			try {
				String s=response.errorBody().string();
				Logger.e(StringUtils.getTextJoint("%1$s:%2$d:%3$s",name,response.code(),s));
			} catch (IOException e) {
				Logger.e(TagFinal.ZXX, "onResponse: IOException");
				e.printStackTrace();
			}
			toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
		}

	}

	private void resetLogin(UserRes res){
		User user=Base.user;
		user.setSession_key(res.getSession_key());
		user.setToken(res.getToken());
		UserPreferences.getInstance().saveUserID(user.getIdU());
		GreenDaoManager.getInstance().saveUser(user);
		switch (token){
			case "admin":
				break;
			case "term":
				break;
			case "sign":
				break;
		}
	}
	@Override
	public void onFailure(Call<ResEnv> call, Throwable t) {
		if (!isActivity())return;
		Logger.e("onFailure  :"+call.request().headers().toString());
		dismissProgressDialog();
	}

	@Override
	public boolean isActivity() {
		return AppLess.isTopActivy(TAG);
	}


	private void initGetViewItem(BaseRes res){
		for (MainValue bean:res.getData()){
//			Logger.e(bean.getType());
			switch (bean.getType()){
//				case "teaevaluationadmin":
//					break;
				case "teaevaluation":
					addProcess(adapter_data_default, bean, "TeaEvaluateActivity");
					break;
				case "event":
					addProcess(adapter_data_default, bean, "EventMainActivity");
					break;
				case "sharing":
					addProcess(adapter_data_default, bean, "ShapeSingleActivity");
					break;
				case "classschedule":
					addProcess(adapter_data_default, bean, "WebActivity");
					break;
				case "classevaluation":
					addProcess(adapter_data_default, bean, "DeyuCheckActivity");
					break;
				case "cookbook":
					addProcess(adapter_data_default, bean, "FootBookMainActivity");
					break;
				case "stuvote":
					addProcess(adapter_data_default,  bean,"VoteListActivity");
					break;
				case "news":
					addProcess(adapter_data_default,  bean,"SchoolNewsActivity");
					break;
				case "electiveattendance":
					addProcess(adapter_data_default,  bean,"ChoiceClassActivity");
					break;
				case "electivemanage":
					addProcess(adapter_data_default,  bean,"DelayServiceMasterMainActivity");
					break;
				case "dutyreport":
					addProcess(adapter_data_default,  bean,"DutyMainActivity");
					break;
				case "health":
					addProcess(adapter_data_default,  bean,"CheckClassActivity");
					break;
				case "healthstatistics":
					addProcess(adapter_data_default,  bean,"CheckTjActivity");
					break;
				case "signup":
					addProcess(adapter_data_default, bean, "AuthenticationActivity");
					break;
				case "office_supply":
					addProcess(adapter_data_default, bean, "GoodsIndexctivity");
					break;
				case "video":
					addProcess(adapter_data_default, bean, "VideoListMainActivity");
					break;
                case "ebook":
                    addProcess(adapter_data_default, bean, "EbookListMainActivity");
                    break;
				default:
					addProcess(adapter_data_default, R.drawable.affiche, bean);
					break;
			}
		}
        adapter_data_show.clear();
		if (UserPreferences.getInstance().getUserSetView().equalsIgnoreCase("")){
            adapter_data_show.addAll(adapter_data_default);
        }else {
            isDbset(UserPreferences.getInstance().getUserSetView());
        }
		addProcess(adapter_data_show, R.drawable.main_settings, "MainSetViewActivity");
        base_adapter.setDataList(adapter_data_show);
        base_adapter.setLoadState(TagFinal.LOADING_END);


	}




	private void saveDb(KeyValue default_data){

        KeyValueDb key=new KeyValueDb();
        key.setNum(default_data.getNum());
        key.setName(default_data.getName());
        key.setType("");
        key.setKey_value_id(default_data.getId());
        GreenDaoManager.getInstance().saveKeyValueDb(key);
    }

    private void isDbset(String type){
        if (type.equals(TagFinal.TRUE)){
            List<KeyValueDb> db_index=GreenDaoManager.getInstance().getKeyValueDbList("where type = \"main_point_type\"");//auto
            if (StringJudge.isEmpty(db_index)){
                int i=0;
                for (KeyValue default_data:adapter_data_default){
                    default_data.setNum(i);
                    i++;
                    saveDb(default_data);
                }
            }else{
                if (db_index.size()!=adapter_data_default.size()){
                    int i=0;
                    for (KeyValue default_data:adapter_data_default){
                        boolean is_have=false;
                        for (KeyValueDb db:db_index){
                            if (default_data.getId().equalsIgnoreCase(db.getKey_value_id())){
                                is_have=true;
                            }
                        }
                        if (!is_have){
                            default_data.setNum(i);
                            saveDb(default_data);
                        }
                        i++;
                    }
                }
            }
            db_index=GreenDaoManager.getInstance().getKeyValueDbList("where type = \"main_point_type\"");
            Collections.sort(db_index, new Comparator<KeyValueDb>() {
                @Override
                public int compare(KeyValueDb p1, KeyValueDb p2) {
                    if(p1.getNum() > p2.getNum()){
                        return -1;
                    }
                    if(p1.getNum() == p2.getNum()){
                        return 0;
                    }
                    return 1;
                    //可以按User对象的其他属性排序，只要属性支持compareTo方法
                }
            });
            for (KeyValueDb one:db_index){
                selectedAdapterType(one.getKey_value_id());
            }
        }else{
            String index_s=UserPreferences.getInstance().getIndex();//hand
            if (StringJudge.isEmpty(index_s)){
                adapter_data_show.addAll(adapter_data_default);
                StringBuilder sb=new StringBuilder();
                for (KeyValue h: adapter_data_show){
                    sb.append(h.getId()).append(",");
                }
                if (sb.length()>2){
                    UserPreferences.getInstance().saveIndex(sb.substring(0,sb.length()-1));
                }
            }else{
                List<String> list = StringUtils.listToStringSplitCharacters(index_s,",");
                if (list.size()!=adapter_data_default.size()){
                    for (KeyValue default_data:adapter_data_default){
                        boolean is_have=false;
                        for (String index:list){
                            if (default_data.getId().equalsIgnoreCase(index)){
                                is_have=true;
                            }
                        }
                        if (!is_have){
                            list.add(default_data.getId());
                        }
                    }
                    StringBuilder sb=new StringBuilder();
                    for (String index:list){
                        selectedAdapterType(index);
                        sb.append(index).append(",");
                    }
                    if (sb.length()>2){
                        UserPreferences.getInstance().saveIndex(sb.substring(0,sb.length()-1));
                    }
                }else{
                    for (String index:list){
                        selectedAdapterType(index);
                    }
                }

            }
        }

    }



    private void selectedAdapterType(String id){
        for (KeyValue default_data:adapter_data_default){
            if (id.equalsIgnoreCase(default_data.getId())){
                adapter_data_show.add(default_data);
            }
        }
    }



	/**
	 * 双击退出函数
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true;
			toastShow("再按一次退出程序");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);

		} else {
			finish();
			System.exit(0);
		}
	}




	@SuppressLint("NewApi")
	private void initialize() {
		cycleViewPager = (CycleViewPager) getSupportFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
		for (int i = 0; i < imageUrls.length; i++) {
			ADInfo info = new ADInfo();
			info.setDi_d(imageUrls[i]);
			info.setContent("图片-->" + i);
			infos.add(info);
		}

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageViewToR(this, infos.get(infos.size() - 1).getDi_d()));
		for (int i = 0; i < infos.size(); i++) {
			views.add(ViewFactory.getImageViewToR(this, infos.get(i).getDi_d()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageViewToR(
				this, infos.get(0).getDi_d()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, infos, mAdCycleViewListener);
		//设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(4000);
		//设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

		@Override
		public void onImageClick(ADInfo info, int position, View imageView) {
			if (cycleViewPager.isCycle()) {

			}
		}

	};

}
