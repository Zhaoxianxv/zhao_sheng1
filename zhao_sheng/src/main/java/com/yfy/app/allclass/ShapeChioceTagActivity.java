package com.yfy.app.allclass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhao_sheng.R;
import com.yfy.app.XlistLefttTxtAdapter;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.app.allclass.beans.ShapeKind;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.view.swipe.xlist.XListView;
import com.yfy.final_tag.StringJudge;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShapeChioceTagActivity extends WcfActivity{

    @Bind(R.id.shape_kind_list_item)
    XListView xlist;

    private final String FUNC="WB_get_tag";
    private List<ShapeKind> kinds=new ArrayList<>();
    private List<String> txts=new ArrayList<>();

    private XlistLefttTxtAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_chioce_tag);
        loadingDialog=new LoadingDialog(mActivity);
        getShapeKind();
        initSQtoobar();
        initView();


    }


    private void initSQtoobar() {
        assert toolbar!=null;
        toolbar.setTitle("");
    }


    private void initView() {
        xlist.setPullRefreshEnable(false);
        xlist.setPullLoadEnable(false);
        adapter=new XlistLefttTxtAdapter(mActivity,txts);
        xlist.setAdapter(adapter);

        xlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Logger.e("zxx'",i-1+" "+kinds.get(i-1).getTag_id()+"  "+kinds.get(i-1).getTag_name());
                    Intent intent=new Intent();
                    intent.putExtra("shape_kind",kinds.get(i-1).getTag_id());
                    intent.putExtra("shape_name",kinds.get(i-1).getTag_name());
                    setResult(RESULT_OK,intent);
                    onPageBack();

            }
        });

    }




    public void getShapeKind(){
        Object[] params = new Object[] {
                Base.user.getSession_key(),
               };

        ParamsTask task = new ParamsTask(params, FUNC,loadingDialog);
        execute(task);
    }
    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        ReInfor infor=gson.fromJson(result, ReInfor.class);
        if (StringJudge.isNotNull(infor)){
            Logger.e("zxx'"," "+result);
            kinds=infor.getTags();
            txts.clear();
            for (ShapeKind kind:kinds) {
                txts.add(kind.getTag_name());
            }
            adapter.notifyDataSetChanged(txts);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }
}
