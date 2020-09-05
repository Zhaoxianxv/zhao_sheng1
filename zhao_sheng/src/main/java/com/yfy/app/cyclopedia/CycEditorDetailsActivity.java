package com.yfy.app.cyclopedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.cyclopedia.adpater.CycSpoorAdapter;
import com.yfy.app.cyclopedia.beans.Note;
import com.yfy.app.cyclopedia.beans.Note.NoteBean;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.Logger;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.view.SQToolBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CycEditorDetailsActivity extends WcfActivity {

    @Bind(R.id.cyc_person_editor_xlist)
    XListView xlist;
    private final String CYCLOPEDIA_TYPE="ancyclopedia_comment_list";
    private final String PRAISE="ancyclopedia_praise";

    private CycSpoorAdapter adapter;

    private int pager=0;
    private  boolean isRefresh=false;
    private String pid;
    private String title;
    private String angel_id;

    private List<NoteBean> beanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyc_editor);
        initData();
        initSQToolbar();

        initView();
        refresh();

    }

    private void initData() {
        Intent inten=getIntent();
        pid=inten.getStringExtra("pid");
        title=inten.getStringExtra("title");
        angel_id="0";

    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle(R.string.cyclopedia_school);
        toolbar.addMenu(1,R.drawable.cyc_edit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i=new Intent(mActivity,CycEditActivity.class);
                i.putExtra("note",pid);
                i.putExtra("title",title);
                Logger.e("zxx","   "+pid);
                startActivity(i);
            }
        });
    }

    public void praise(NoteBean bean){
        String type="";
        if (bean.getIspraise().equals("true")){
            type="0";
        }else{
            type="1";
        }
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                bean.getId(),
                type
        };
        ParamsTask gettype = new ParamsTask(params, PRAISE, PRAISE);
        execute(gettype);
    }



    public void refresh(){
        if (isRefresh){
            xlist.stopRefresh();
            return;
        }
        isRefresh=true;
        pager=0;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                pid,
                "",
                angel_id,
                pager,
                PAGE_NUM
        };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE, "refresh");
        execute(gettype);
    }

    public void loadMore(){
        if (isRefresh){
            xlist.startLoadMore();
            return;
        }
        isRefresh=true;
        pager++;
        Object[] params = new Object[] {
                Base.user.getSession_key(),
                pid,
                "",
                angel_id,
                pager,
                PAGE_NUM
        };
        ParamsTask gettype = new ParamsTask(params, CYCLOPEDIA_TYPE, "more");
        execute(gettype);
    }

    private XListView.IXListViewListener listenner=new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {

            refresh();
        }
        @Override
        public void onLoadMore() {

            Logger.e("zxx"," loadMore();");
            loadMore();
        }
    };

    private void initView() {
        adapter=new CycSpoorAdapter(mActivity,beanList);
        xlist.setAdapter(adapter);
        xlist.setXListViewListener(listenner);
        xlist.setPullLoadEnable(true);
        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getCount()==i+1){
                    listenner.onLoadMore();
//                    xlist.startLoadMore();
                }else{

                }
            }
        });

        adapter.setListenner(new CycSpoorAdapter.PraiseListnner() {
            @Override
            public void onitem(int id) {
                NoteBean bean=beanList.get(id);
                if (bean!=null){
                    praise(bean);
                }

            }
        });
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        xliststop();
        Logger.e("ancyclopedia_comment_list:"+result);
        String name =wcfTask.getName();
        if (name.equals("refresh")){
            beanList.clear();
            Note note=gson.fromJson(result, Note.class);
            beanList=  note.getAncyclopedia_list();
            adapter.notifyDataSetChanged(beanList);
        }
        if (name.equals("more")){
            Note note=gson.fromJson(result, Note.class);
            beanList.addAll(note.getAncyclopedia_list());
            adapter.notifyDataSetChanged(beanList);
        }
        if (name.equals(PRAISE)){
            if (JsonParser.isSuccess(result)){
                refresh();
                toastShow("成功");
            }
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        xliststop();
        toastShow(R.string.fail_loadmore);
    }


    public void xliststop(){
        isRefresh=false;
        xlist.stopRefresh();
        xlist.stopLoadMore();
    }


}
