package com.yfy.app.cyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.adpater.TypeAdapter;
import com.yfy.app.cyclopedia.beans.AncyclopediaList;
import com.yfy.app.cyclopedia.beans.CyclopediaType;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.tool_textwatcher.EmptyTextWatcher;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.CharacterParser;
import com.yfy.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class SearchActivity extends WcfActivity {

    private TextView tips_tv;

    private TypeAdapter adapter;
    private CharacterParser characterParser ;
    @Bind(R.id.cyc_search_et)
    ClearEditText clear_et;
    @Bind(R.id.listView)
    ListView listView;

    private List<AncyclopediaList> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyc_searc);
        initData();
        initView();
        initAbsListView();

    }

    private void initData() {
        String name=getIntent().getStringExtra("name");
        if (name.equals("")){
            return;
        }else{
            refresh(name);
        }
    }

    private void initView() {
        tips_tv = (TextView) findViewById(R.id.tips_tv);
        clear_et.addTextChangedListener(new EmptyTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                refresh(s.toString());
            }
        });
    }

    private void initAbsListView() {
        adapter=new TypeAdapter(mActivity,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mActivity,CycDetailActivity.class);
                Bundle b=new Bundle();
                b.putSerializable("data",list.get(i));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }






    public void refresh(String name){
        list.clear();
        final String CYCLOPEDIA_TYPE="ancyclopedia_list";

        Object[] params = new Object[] {
                Base.user.getSession_key()==null?"":Base.user.getSession_key(),
                0,
                name,
                0,
                PAGE_NUM
        };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE, "refresh");
        execute(gettype);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        String name =wcfTask.getName();
        Logger.e("zxx","result++"+result);
        if (name.equals("refresh")){
            list.clear();
            CyclopediaType type=gson.fromJson(result, CyclopediaType.class);
            list.addAll(type.getAncyclopediaList());
            adapter.notifyDataSetChanged(list);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }
}
