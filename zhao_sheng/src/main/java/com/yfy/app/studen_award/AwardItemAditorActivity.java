package com.yfy.app.studen_award;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.yfy.app.album.SingePicShowActivity;
import com.yfy.app.notice.adapter.GalleryAdapter;
import com.yfy.final_tag.Base;
import com.yfy.base.activity.WcfActivity;
import com.yfy.final_tag.dialog.LoadingDialog;
import com.yfy.final_tag.TagFinal;
import com.yfy.json.JsonParser;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.net.loading.task.ParamsTask;
import com.yfy.final_tag.ConvertObjtect;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class AwardItemAditorActivity extends WcfActivity {


    private final String award_submit="award_submit";


    @Bind(R.id.award_aditor_name)
    TextView name;
    @Bind(R.id.award_aditor_grade)
    TextView grade;
    @Bind(R.id.award_aditor_content)
    TextView content;
    @Bind(R.id.award_aditor_time)
    TextView time;
    @Bind(R.id.award_aditor_type)
    TextView type;
    @Bind(R.id.award_aditor_rating)
    RatingBar rating;
    @Bind(R.id.award_icon_gallery)
    Gallery gallery;

    LoadingDialog dialog;

    private float rating_float;
    private String id;
    private String aditor_name,aditor_type,aditor_time,aditor_grade,aditor_conten;
    private ArrayList<String> images;
    private GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_aditor);
        dialog=new LoadingDialog(mActivity);
        getDataIntent();
        initSQToolbar();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

    }



    public void getDataIntent() {

        Intent intent=getIntent();
        id=intent.getStringExtra("aditor_id")==null?"":intent.getStringExtra("aditor_id");
        aditor_name=intent.getStringExtra("aditor_name")==null?"":intent.getStringExtra("aditor_name");
        aditor_type=intent.getStringExtra("aditor_type")==null?"":intent.getStringExtra("aditor_type");
        aditor_grade=intent.getStringExtra("aditor_grade")==null?"":intent.getStringExtra("aditor_grade");
        aditor_conten=intent.getStringExtra("aditor_conten")==null?"":intent.getStringExtra("aditor_conten");
        aditor_time=intent.getStringExtra("aditor_time")==null?"":intent.getStringExtra("aditor_time");
        images=intent.getStringArrayListExtra("aditor_images")==null?new ArrayList<String>():intent.getStringArrayListExtra("aditor_images");
    }

    private void initSQToolbar() {
        assert toolbar!=null;
        toolbar.setTitle("");



    }
    private void initView() {

        type.setText(aditor_type);
        time.setText(aditor_time.replace("/","-"));
        name.setText(aditor_name);
        grade.setText(aditor_grade);
        content.setText(aditor_conten);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating_float=v;
            }
        });

        adapter=new GalleryAdapter(mActivity,images);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (images.size()==0) {
                    return;
                }
                ArrayList<String> urlList = new ArrayList<String>();
                urlList.addAll(images);

                Intent intent=new Intent(mActivity, SingePicShowActivity.class);
                Bundle b=new Bundle();
                b.putString(TagFinal.ALBUM_SINGE_URI, urlList.get(i));
                intent.putExtras(b);
                startActivity(intent);

            }
        });
    }


    public void aditorScore(int issh){
        Object[] parmas=new Object[]{
                Base.user.getSession_key(),
                ConvertObjtect.getInstance().getInt(id),//条目id
                issh,//2不通过，1通过
                rating_float+""

        };
        ParamsTask grade= new ParamsTask(parmas,award_submit,award_submit,dialog);
        execute(grade);
    }

    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {

        if (JsonParser.isSuccess(result)){
            onPageBack();
        }

        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {
        toastShow("失败");
    }


    @OnClick(R.id.award_aditor_ok)
    void setOk(){
        aditorScore(2);
    }
    @OnClick(R.id.award_aditor_no)
    void setNo(){
        aditorScore(0);
    }


    @Override
    public void onPageBack() {
        setResult(RESULT_OK);
        finish();
    }
}
