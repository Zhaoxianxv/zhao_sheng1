package com.yfy.app.allclass;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.zhao_sheng.R;
import com.yfy.app.allclass.beans.ReInfor;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.view.SQToolBar;

import butterknife.Bind;

/**
 * Created by yfy1 on 2017/3/17.
 */

public class EditReplyActivity extends WcfActivity {

    private final String setCommentOrReply = "WB_did_reply";
    private String DyId;
    private String replay_id;


    @Bind(R.id.edit_conent_edit)
    EditText con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_edit_comment);
        getData();
        initSQToolbar();
    }
    @Override
    public void onResume() {
        super.onResume();
        closeKeyWord();
    }

    public void  getData() {

        DyId=getIntent().getStringExtra("DyId");
        replay_id=getIntent().getStringExtra("replay_id");

    }


    private void initSQToolbar() {

        assert toolbar!=null;
        toolbar.setTitle("写回复");
        toolbar.addMenuText(1,R.string.send_btn);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (replay_id==null)return;
                String content=con.getText().toString().trim();
                if (content.equals("")||content==null){
                    toastShow(R.string.content_hint);
                    return;
                }
                setComments(replay_id,content);
            }
        });

    }

    private void setComments(String  dynamicId, String text) {

        Object[] params = new Object[] {
                Base.user.getSession_key(),
                Base.user.getName(),
                text,
                ConvertObjtect.getInstance().getInt(dynamicId),//回复某条回复的id如果是新增回复则为0
                ConvertObjtect.getInstance().getInt(DyId),//该条动态的id


        };

        ParamsTask task = new ParamsTask(params, setCommentOrReply);
        execute(task);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        ReInfor infor=gson.fromJson(result, ReInfor.class);
        if (infor.getResult().equals("true")){
            toastShow(R.string.success_do);
            onPageBack();
        }else{
            toastShow(result);
        }
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow(R.string.fail_send);
    }


}
