package com.yfy.app.goods;

import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import com.example.zhao_sheng.R;
import com.google.gson.reflect.TypeToken;
import com.yfy.app.goods.adapter.ContentAdapter;
import com.yfy.app.goods.bean.Content;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.OnRecyclerItemClickListener;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.ClearEditText;
import com.yfy.view.SQToolBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentActivity extends BaseActivity  {
    private static final String TAG = ContentActivity.class.getSimpleName();


    private ContentAdapter adapter;



    @Bind(R.id.clear_et)
    ClearEditText edit;
    private List<Content> contents=new ArrayList<>();
    private List<Content> contents_adapter=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_recycler_layout);
        edit.setVisibility(View.GONE);
        initSQtoobar();
        initRecycler();
        getData();


    }


    public void getData(){
//        contents= GreenDaoManager.getInstance().queryContent("where type = \"" +"goods" +"\"");
       ;
        contents= gson.fromJson( UserPreferences.getInstance().getGoodsJson(), new TypeToken<List<Content>>(){}.getType());
        initIndex();
    }


    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("常用语");
        final TextView menu=toolbar.addMenuText(TagFinal.ONE_INT, "编辑");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case TagFinal.ONE_INT:
                        if (menu.getText().equals("编辑")){
                            menu.setText("完成");
                            adapter.setIsedit(true);
                            recyclerView.addOnItemTouchListener(onitem);
                            mItemTouchHelper.attachToRecyclerView(recyclerView);
                            recyclerView.setBackgroundResource(R.color.BrulyWood);
                            recyclerView.setFocusable(false);
                        }else{
                            menu.setText("编辑");
                            adapter.setIsedit(false);
                            recyclerView.removeOnItemTouchListener(onitem);
                            recyclerView.setBackgroundResource(R.color.white);
                            recyclerView.setFocusable(true);
                            UserPreferences.getInstance().saveGoodsJson(gson.toJson(adapter.getDataList()));
                            UserPreferences.getInstance().saveGoodsIndex(StringJudge.listToString(getlist(adapter.getDataList())));
                        }
                        break;
                }
            }
        });
    }


    private List<String> getlist(List<Content> dataList){
        List<String> name=new ArrayList<>();
        for (Content s:dataList){
            name.add(s.getInde());
        }
        return name;
    }
    public RecyclerView.OnItemTouchListener onitem;

    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.notice_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        xlist.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));
        adapter=new ContentAdapter(this);
        recyclerView.setAdapter(adapter);


        onitem = new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                //判断被拖拽的是否是前两个，如果不是则执行拖拽
                //如果item不是最后一个，则执行拖拽
                if (vh.getLayoutPosition() != contents_adapter.size() - 1) {
                    mItemTouchHelper.startDrag(vh);
                    //获取系统震动服务
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                    vib.vibrate(70);
                }
            }
        };



    }
    private List<String> list;
    private void initIndex() {
        contents_adapter.clear();

        //--------------------------------
        String index_s=UserPreferences.getInstance().getGoodsIndex();
        if (StringJudge.isEmpty(index_s)){
            contents_adapter.addAll(contents);
            adapter.setDataList(contents_adapter);
            adapter.setLoadState(2);
        }else{
            list = StringJudge.stringToList(index_s);
            for (String index:list){
                dbIndex(index);
            }
            adapter.setDataList(contents_adapter);
            adapter.setLoadState(2);
        }

    }
    private void dbIndex(String index){
        for (Content bean:contents){
            if (index.equals(bean.getInde())){
                contents_adapter.add(bean);
            }

        }

    }

    public ItemTouchHelper.Callback callback=new ItemTouchHelper.Callback() {

        /**
         * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，
         * 如果是网格类RecyclerView则还应该多有LEFT和RIGHT
         * @param recyclerView
         * @param viewHolder
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
                    Collections.swap(contents_adapter, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(contents_adapter, i, i - 1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
            //保存顺序
            StringBuilder sb=new StringBuilder();
            for (Content h:contents_adapter){
                sb.append(h.getInde()).append(",");
            }
            if (sb.length()>2){
                UserPreferences.getInstance().saveGoodsIndex(sb.substring(0,sb.length()-1));
            }
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            adapter.notifyItemRemoved(position);
            contents_adapter.remove(position);
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
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
        }
    };
    public ItemTouchHelper mItemTouchHelper=new ItemTouchHelper(callback);








    @Override
    public boolean isActivity() {
        return AppLess.isTopActivy(TAG);
    }

}
