package com.yfy.app.property;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.property.adapter.PropertyBadAdapter;
import com.yfy.app.property.bean.ArticleRoom;
import com.yfy.app.property.bean.BadObj;
import com.yfy.base.activity.BaseActivity;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.recycerview.RecycleViewDivider;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

public class PropertyBadActivity extends BaseActivity {

    private static final String TAG = "zxx";
    private ArrayList<ArticleRoom> rooms;
    private String room_id;
    private List<BadObj> badObjs=null;
    private ArticleRoom room;

    private PropertyBadAdapter adapter;
    private ConvertObjtect obj;
    private int max_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_bad);
        obj=ConvertObjtect.getInstance();
        getData();

        initSQToolbar();
        initRecycler();

    }

    @Override
    public void onPageBack() {
        finish();
    }

    @Override
    public void finish() {

        room.setBad_num(adapter.getBadObj());
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra("data",rooms);
        setResult(RESULT_OK,intent);
        super.finish();


    }

    public void getData(){
        Intent intent=getIntent();
        rooms=intent.getParcelableArrayListExtra("data");
        room_id=intent.getStringExtra("index_id");
        Logger.e(TAG, "getData: "+room_id );

    }

    public void showDialog(){
        View dialog_view= LayoutInflater.from(mActivity).inflate(R.layout.property_bad_dialog_edit,null);
        final EditText content= (EditText) dialog_view.findViewById(R.id.property_bad_diaolog_edit);
        AlertDialog.Builder builder_view = new AlertDialog.Builder(mActivity);
        builder_view.create();
        builder_view.setTitle("请添加栏目内容");
        builder_view.setView(dialog_view);
        builder_view.setPositiveButton("定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BadObj bad=new BadObj();
                bad.setContent(content.getText().toString());
                bad.setNum("1");
                badObjs.add(bad);
                adapter.setBadObj(badObjs);
                adapter.setLoadState(3);
                dialog.dismiss();
            }
        });
        builder_view .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder_view.show();
    }

    public void initSQToolbar(){
        assert toolbar!=null;
        toolbar.setTitle("校产详情");
        toolbar.addMenuText(TagFinal.ONE_INT,"添加");
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isAdd(adapter.getBadObj())){
                    showDialog();
                }else{
                    toastShow("饱和");
                }
            }
        });
    }

    public boolean isAdd(List<BadObj> list){
        int num=0;
        for (BadObj bad:list){
            num=+obj.getInt(bad.getNum());
        }
        if (num>=max_num)return false;
        return true;
    }
    private RecyclerView recyclerView;
    public void initRecycler(){

        recyclerView = (RecyclerView) findViewById(R.id.proprety_bad_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecycleViewDivider(
                mActivity,
                LinearLayoutManager.HORIZONTAL,
                1,
                getResources().getColor(R.color.gray)));



        for (ArticleRoom room:rooms){
            if (room.getId().equals(room_id)){
                this.room=room;
            }
        }
        badObjs=room.getBad_num();

        int all=obj.getInt(room.getAll_num());
        int deficiency=obj.getInt(room.getDefi_num());
        max_num=all-deficiency;
        Logger.e(TAG, "initRecycler: "+deficiency );
        if (badObjs==null){
            badObjs=new ArrayList<>();
            Logger.e( "initRecycler:badObjs null" );
        }else{

        }

        adapter=new PropertyBadAdapter(PropertyBadActivity.this,badObjs,max_num);
        recyclerView.setAdapter(adapter);
    }


}
