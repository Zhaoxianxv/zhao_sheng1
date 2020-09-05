package com.yfy.app.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.reflect.TypeToken;
import com.yfy.app.goods.bean.Content;
import com.yfy.app.goods.bean.GoodsRes;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.net.goods.GoodNumDoReq;
import com.yfy.base.activity.BaseActivity;
import com.yfy.db.UserPreferences;
import com.yfy.final_tag.AppLess;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringJudge;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.final_tag.Logger;
import com.yfy.final_tag.tool_textwatcher.MyWatcher;
import com.yfy.view.SQToolBar;
import com.yfy.view.multi.MultiPictureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodNumDoActivity extends BaseActivity  implements Callback<ResEnv> {

    private static final String TAG = GoodNumDoActivity.class.getSimpleName();
    @Bind(R.id.goods_content)
    EditText edit;
    @Bind(R.id.goods_do_mult)
    MultiPictureView add_mult;

    @Bind(R.id.goods_do_four_icon)
    AppCompatImageView four;
    @Bind(R.id.goods_do_fiv_icon)
    AppCompatImageView fiv;

    @Bind(R.id.goods_do_four)
    RelativeLayout four_layout;
    @Bind(R.id.goods_do_fiv)
    RelativeLayout fiv_layout;



    @Bind(R.id.goods_add_content)
    RelativeLayout box_layout;
    @Bind(R.id.goods_add_content_icon)
    AppCompatImageView box_icon;
    @Bind(R.id.goods_add_content_name)
    TextView box_name;
    private String id="";
    private String state="";
    private boolean is;
    private boolean is_box=false;
    private boolean is_chioce=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_num_do);
        initSQtoolbar();
        getData();

        edit.addTextChangedListener(new MyWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (is_chioce) is_chioce=false;
                Logger.e(TagFinal.ZXX, "afterTextChanged: 1" );
            }
        });

    }


    private void initSQtoolbar() {
        box_name.setTextColor(ColorRgbUtil.getGrayText());
        box_icon.setColorFilter(ColorRgbUtil.getGrayText());
        assert toolbar!=null;
        toolbar.setTitle("物品");
        toolbar.addMenuText(TagFinal.ONE_INT,R.string.submit);
        toolbar.setOnMenuClickListener(new SQToolBar.OnMenuClickListener() {
            @Override
            public void onClick(View view, int position) {
                closeKeyWord();
                switch (position){
                    case TagFinal.ONE_INT:
                        isCan();
                        break;
                }
            }
        });
    }


    private String yes_state="1",no_state="2";
    private void getData(){
        Intent intent=getIntent();
        id=intent.getStringExtra(TagFinal.ID_TAG);
        is = intent.getBooleanExtra(TagFinal.TYPE_TAG, false);

    }
    private void isCan(){
        //新增武平 20
        String conent=edit.getText().toString().trim();
        if (StringJudge.isEmpty(conent)){
            conent="";
        }
        if (StringJudge.isEmpty(state)){
            toastShow("请选择状态 ");
            return;
        }
        admin(conent);

    }

    private void initView(AppCompatImageView view){
        fiv.setVisibility(View.GONE);
        four.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.goods_do_four)
    void setfuor(){
        state=yes_state;//同意
        initView(four);
    }
    @OnClick(R.id.goods_do_fiv)
    void setFiv(){
        state=no_state;//拒绝
        initView(fiv);
    }


    @OnClick(R.id.goods_add_content)
    void setChange(){
        if (is_box){
            is_box=false;
            box_name.setTextColor(ColorRgbUtil.getGrayText());
            box_icon.setColorFilter(ColorRgbUtil.getGrayText());
        }else{
            is_box=true;
            box_name.setTextColor(ColorRgbUtil.getOrange());
            box_icon.setColorFilter(ColorRgbUtil.getOrange());
        }

    }

    @OnClick(R.id.goods_content_list)
    void setChioce(){
        List<Content> contents= gson.fromJson( UserPreferences.getInstance().getGoodsJson(), new TypeToken<List<Content>>(){}.getType());
        if (StringJudge.isEmpty(contents)){
            toastShow("暂未常用语句");
            return;
        }
        Intent intent=new Intent(mActivity,ContentActivity.class);
        startActivityForResult(intent, TagFinal.UI_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TagFinal.UI_TAG:
                    String conten=data.getStringExtra(TagFinal.OBJECT_TAG);
                    edit.setText(conten);
                    box_name.setTextColor(ColorRgbUtil.getGrayText());
                    box_icon.setColorFilter(ColorRgbUtil.getGrayText());
                    is_chioce=true;
                    Logger.e(TagFinal.ZXX, "afterTextChanged: 2" );
                    break;
//                case TagFinal.CAMERA:
//                    addMult(FileCamera.photo_camera);
//                    break;
//                case TagFinal.PHOTO_ALBUM:
//                    ArrayList<Photo> photo_a=data.getParcelableArrayListExtra(TagFinal.ALBUM_TAG);
//                    if (photo_a==null)return;
//                    if (photo_a.size()==0)return;
//                    setMultList(photo_a);
//                    break;
            }
        }
    }




    /**
     * ----------------------------retrofit-----------------------
     */


    public void admin(String remark){

        ReqEnv evn = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        GoodNumDoReq req = new GoodNumDoReq();
        //获取参数
        req.setId(id);
        req.setState(ConvertObjtect.getInstance().getInt(state));
        req.setContent(remark);

        reqBody.goodNumDoReq= req;
        evn.body = reqBody;
        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().goods_num_do(evn);
        call.enqueue(this);
        showProgressDialog("");
        Logger.e(evn.toString());

        if (StringJudge.isEmpty(remark))return;
        if (is_box){
            List<Content> contents;
            contents = gson.fromJson( UserPreferences.getInstance().getGoodsJson(), new TypeToken<List<Content>>(){}.getType());
            Content content=new Content();
            content.setContent(remark);
            if (StringJudge.isEmpty(contents)){
                content.setInde("0");
            }else{
                content.setInde(""+contents.size());
            }

            if (StringJudge.isEmpty(contents)){
                contents=new ArrayList<>();
            }
            contents.add(content);


            String index_s=UserPreferences.getInstance().getGoodsIndex();
            UserPreferences.getInstance().saveGoodsIndex(index_s+","+content.getInde());
            UserPreferences.getInstance().saveGoodsJson(gson.toJson(contents));
        }
    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        if (!isActivity())return;
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b=respEnvelope.body;

            if (b.goodNumDoRes!=null) {
                String result = b.goodNumDoRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                GoodsRes res=gson.fromJson(result,GoodsRes.class);
                if (res.getResult().equals(TagFinal.TRUE)){
                    toastShow(R.string.success_do);
                    setResult(RESULT_OK);
                    finish();
                }else{
                    toastShow(res.getError_code());
                }
            }

        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));        }
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



}
