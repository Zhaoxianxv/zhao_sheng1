package com.yfy.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.zhao_sheng.R;
import com.yfy.app.bean.BaseRes;
import com.yfy.app.bean.MainValue;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.process.ProcessGetTypeReq;
import com.yfy.base.App;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.GreenDaoManager;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.greendao.KeyValueDb;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.OnRecyclerItemClickListener;
import com.yfy.final_tag.recycerview.RecycAnimator;
import com.yfy.upload.UploadDataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

public class MainSetViewActivity extends BaseActivity {
    private static final String TAG = MainSetViewActivity.class.getSimpleName();
	private MainSetAdapter adapter;
	@Bind(R.id.main_set_checked_title)
	AppCompatTextView checked_title;
	@Bind(R.id.main_set_recycler_title)
	AppCompatTextView recycler_title;
	@Bind(R.id.main_set_checked_group)
	RadioGroup radio_group;
	@Bind(R.id.main_set_checked_auto)
	RadioButton radio_button_auto;
	@Bind(R.id.main_set_checked_hand)
	RadioButton radio_button_hand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_set_view);
		if (App.isServiceRunning(mActivity,UploadDataService.class.getSimpleName())){
			Logger.e(TagFinal.ZXX, "UploadDataService: " );
		}else{
			startService(new Intent(App.getApp().getApplicationContext(),UploadDataService.class));//开启更新
		}
		initSQtoobar("设置");
		initRecycler();
        getMainItem();
	}

	@Override
	public void finish() {
		setResult(RESULT_OK);
		super.finish();
	}

	private void initSQtoobar(String title) {
        assert toolbar!=null;
        toolbar.setTitle(title);
//        toolbar.addMenuText(TagFinal.ONE_INT,R.string.ok);
//        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//            }
//        });
		toolbar.setOnNaviClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
    }

    private String set_state;
    private void initView(){
		checked_title.setText("排序类型");
		set_state=UserPreferences.getInstance().getUserSetView();
		switch (set_state){
			case TagFinal.TRUE:
				recycler_title.setText("当前顺序（智能）");
                getABCType(set_state);
				radio_button_hand.setTextColor(ColorRgbUtil.getBaseColor());
				radio_button_auto.setTextColor(ColorRgbUtil.getWhite());
				radio_button_auto.setBackgroundResource(R.drawable.radius_left_bottom4_top4);
				radio_button_hand.setBackgroundColor(Color.TRANSPARENT);

				break;
			case TagFinal.FALSE:
				recycler_title.setText("当前顺序（手动）");
                getABCType(set_state);
				radio_button_hand.setTextColor(ColorRgbUtil.getWhite());
				radio_button_auto.setTextColor(ColorRgbUtil.getBaseColor());
				radio_button_hand.setBackgroundResource(R.drawable.radius_rigth_bottom4_top4);
				radio_button_auto.setBackgroundColor(Color.TRANSPARENT);
				break;
			case "":
				recycler_title.setText("当前顺序（默认）");
                getABCType(set_state);
				radio_button_auto.setBackgroundColor(Color.TRANSPARENT);
				radio_button_hand.setBackgroundColor(Color.TRANSPARENT);
				radio_button_auto.setTextColor(ColorRgbUtil.getBaseColor());
				radio_button_hand.setTextColor(ColorRgbUtil.getBaseColor());
				break;
		}

		radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.main_set_checked_auto://auto  true
						UserPreferences.getInstance().saveUserSetView(TagFinal.TRUE);
						set_state=TagFinal.TRUE;
						recycler_title.setText("当前顺序（智能）");
						getABCType(set_state);
						radio_button_hand.setTextColor(ColorRgbUtil.getBaseColor());
						radio_button_auto.setTextColor(ColorRgbUtil.getWhite());
						radio_button_auto.setBackgroundResource(R.drawable.radius_left_bottom4_top4);
						radio_button_hand.setBackgroundColor(Color.TRANSPARENT);
						break;
					case R.id.main_set_checked_hand://hand  false
						UserPreferences.getInstance().saveUserSetView(TagFinal.FALSE);
						set_state=TagFinal.FALSE;
						recycler_title.setText("当前顺序（手动）");
                        getABCType(set_state);

						radio_button_hand.setTextColor(ColorRgbUtil.getWhite());
						radio_button_auto.setTextColor(ColorRgbUtil.getBaseColor());
						radio_button_hand.setBackgroundResource(R.drawable.radius_rigth_bottom4_top4);
						radio_button_auto.setBackgroundColor(Color.TRANSPARENT);
						break;
				}
			}
		});
	}



	private void getABCType(String type){
    	switch (type){
			case TagFinal.TRUE:
				recyclerView.removeOnItemTouchListener(onitem);
				recyclerView.setFocusable(true);

                isDbset(type);
				break;
			case TagFinal.FALSE:
                isDbset(type);
				recyclerView.addOnItemTouchListener(onitem);
				mItemTouchHelper.attachToRecyclerView(recyclerView);
				recyclerView.setFocusable(false);
				break;
			case "":
				recyclerView.removeOnItemTouchListener(onitem);
				recyclerView.setFocusable(true);

			    adapter.setDataList(adapter_data_default);
			    adapter.setLoadState(TagFinal.LOADING_END);
				break;
		}
	}


	public RecyclerView recyclerView;
	public RecyclerView.OnItemTouchListener onitem;
	public void initRecycler(){
		recyclerView=findViewById(R.id.main_set_recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
		recyclerView.setItemAnimator(new RecycAnimator());
		//添加分割线
//		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//			// 用来标记是否正在向上滑动
//			private boolean isSlidingUpward = false;
//			@Override
//			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//				super.onScrollStateChanged(recyclerView, newState);
//				GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
//				// 当不滑动时
//				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//					// 获取最后一个完全显示的itemPosition
//
//
//				}
//			}
//			@Override
//			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//				super.onScrolled(recyclerView, dx, dy);
//				// 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
//				isSlidingUpward = dy > 0;
//			}
//		});

		adapter = new MainSetAdapter(this);
		recyclerView.setAdapter(adapter);
		mItemTouchHelper = new ItemTouchHelper(callback);
		onitem = new OnRecyclerItemClickListener(recyclerView) {
			@Override
			public void onItemClick(RecyclerView.ViewHolder vh) {
			}

			@Override
			public void onItemLongClick(RecyclerView.ViewHolder vh) {
				//判断被拖拽的是否是前两个，如果不是则执行拖拽
				//如果item不是最后一个，则执行拖拽
				if (vh.getLayoutPosition() != adapter_data_default.size() - 1) {
					mItemTouchHelper.startDrag(vh);
					//获取系统震动服务
					Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
					vib.vibrate(70);
				}
			}
		};


	}




	public ItemTouchHelper.Callback callback=new ItemTouchHelper.Callback() {
		/**
		 * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，
		 * 如果是网格类RecyclerView则还应该多有LEFT和RIGHT
		 * @return
		 */
		@Override
		public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
				final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
				final int swipeFlags = 0;
				return makeMovementFlags(dragFlags, swipeFlags);
			} else {
				final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
				final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
				return makeMovementFlags(dragFlags, swipeFlags);
			}
		}

		@Override
		public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
			//得到当拖拽的viewHolder的Position
			int fromPosition = viewHolder.getAdapterPosition();
			//拿到当前拖拽到的item的viewHolder
			int toPosition = target.getAdapterPosition();
			Logger.e(TagFinal.ZXX, "onMove: "+fromPosition+" " +toPosition);
			Logger.e(TagFinal.ZXX, "Count : "+adapter.getItemCount());
			if (fromPosition < toPosition) {
				for (int i = fromPosition; i < toPosition; i++) {
					Collections.swap(adapter_data_selected, i, i + 1);
				}
			} else {
				for (int i = fromPosition; i > toPosition; i--) {
					Collections.swap(adapter_data_selected, i, i - 1);
				}
			}
			adapter.notifyItemMoved(fromPosition, toPosition);
			//保存顺序
			StringBuilder sb=new StringBuilder();
			for (KeyValueDb h:adapter_data_selected){
				sb.append(h.getKey_value_id()).append(",");
			}
			if (sb.length()>2){
				UserPreferences.getInstance().saveIndex(sb.substring(0,sb.length()-1));
			}

			return true;
		}

		@Override
		public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
			int position = viewHolder.getAdapterPosition();
			adapter.notifyItemRemoved(position);
            adapter_data_selected.remove(position);
		}

		/**
		 * 重写拖拽可用
		 * @return
		 */
		@Override
		public boolean isLongPressDragEnabled() {
			return false;
		}

		/**
		 * 长按选中Item的时候开始调用
		 *
		 * @param viewHolder
		 * @param actionState
		 */
		@Override
		public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
			if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
				viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);//拖动背景色
			}
			super.onSelectedChanged(viewHolder, actionState);
		}
		/**
		 * 手指松开的时候还原
		 */
		@Override
		public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			super.clearView(recyclerView, viewHolder);
			viewHolder.itemView.setBackgroundColor(0);
		}
	};
	public ItemTouchHelper mItemTouchHelper=new ItemTouchHelper(callback);














	private void isDbset(String type){
        adapter_data_selected.clear();
		if (type.equals(TagFinal.TRUE)){
            List<KeyValueDb> db_index=GreenDaoManager.getInstance().getKeyValueDbList("where type = \"main_point_type\"");//auto
            if (StringJudge.isEmpty(db_index)){
                int i=0;
                for (KeyValueDb default_data:adapter_data_default){
                    default_data.setNum(i);
                    i++;
                    GreenDaoManager.getInstance().saveKeyValueDb(default_data);
                }
            }else{
                if (db_index.size()!=adapter_data_default.size()){
                    int i=0;
                    for (KeyValueDb default_data:adapter_data_default){
                        boolean is_have=false;
                        for (KeyValueDb db:db_index){
                            if (default_data.getKey_value_id().equalsIgnoreCase(db.getKey_value_id())){
                                is_have=true;
                            }
                        }
                        if (!is_have){
                            default_data.setNum(i);
                            GreenDaoManager.getInstance().saveKeyValueDb(default_data);
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
                adapter_data_selected.addAll(adapter_data_default);
				StringBuilder sb=new StringBuilder();
				for (KeyValueDb h: adapter_data_selected){
					sb.append(h.getKey_value_id()).append(",");
				}
				if (sb.length()>2){
					UserPreferences.getInstance().saveIndex(sb.substring(0,sb.length()-1));
				}
			}else{
				List<String> list = StringUtils.listToStringSplitCharacters(index_s,",");
                if (list.size()!=adapter_data_default.size()){
                    for (KeyValueDb default_data:adapter_data_default){
                        boolean is_have=false;
                        for (String index:list){
                            if (default_data.getKey_value_id().equalsIgnoreCase(index)){
                                is_have=true;
                            }
                        }
                        if (!is_have){
                            list.add(default_data.getKey_value_id());
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
		adapter.setDataList(adapter_data_selected);
		adapter.setLoadState(2);
	}



	private void selectedAdapterType(String id){
        for (KeyValueDb default_data:adapter_data_default){
            if (id.equalsIgnoreCase(default_data.getKey_value_id())){
                adapter_data_selected.add(default_data);
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
                    initGetViewItem(res);
                }else{
                    switch (res.getError_code()){
                        case Base.error_code:
                            toast("登录过期");
                            Base.user=null;
                            GreenDaoManager.getInstance().clearUser();
                            getMainItem();
                            break;
                        default:
                            dismissProgressDialog();
                            toast(res.getError_code());
                            break;
                    }
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





	private List<KeyValueDb> adapter_data_default =new ArrayList<>();
	private List<KeyValueDb> adapter_data_selected =new ArrayList<>();
	private void initGetViewItem(BaseRes res){
		for (MainValue bean:res.getData()){
			switch (bean.getType()){
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
				default:
					addProcess(adapter_data_default, bean);
					break;
			}
		}


        initView();
	}

	private void addProcess(List<KeyValueDb> data, MainValue bean, String type){
		KeyValueDb keyValue=new KeyValueDb();
		keyValue.setName(bean.getTitle());
		keyValue.setType("main_point_type");
		keyValue.setKey_value_id(String.valueOf(bean.getId()));
		data.add(keyValue);
	}
	private void addProcess(List<KeyValueDb> data,MainValue bean){
		KeyValueDb keyValue=new KeyValueDb();
		keyValue.setKey_value_id(String.valueOf(bean.getId()));
		keyValue.setName(bean.getTitle());
		keyValue.setType("main_point_type");
		data.add(keyValue);
	}

}
