package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.bean.ChartBean;
import com.yfy.app.tea_evaluate.bean.ChartInfo;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.form.core.SmartTable;
import com.yfy.form.data.column.Column;
import com.yfy.form.data.format.draw.TextDrawFormat;
import com.yfy.form.data.style.FontStyle;
import com.yfy.form.data.style.LineStyle;
import com.yfy.form.data.table.ArrayTableData;
import com.yfy.form.listener.OnColumnItemClickListener;
import com.yfy.form.utils.DensityUtils;
import com.yfy.form.utils.DrawUtils;
import com.yfy.final_tag.Logger;
import com.yfy.net.loading.interf.WcfTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Smartfragment extends WcfFragment implements Callback<ResEnv> {
    private static final String TAG = "zxx";

    private Gson json=new Gson();

    @Bind(R.id.table)
    SmartTable table;
    List<ChartInfo> chartinfo;
    //    SmartTable<UserInfo> table;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.tea_smart_fragment);


        getChartData();

    }

    private void initTable(String[] week, String[][] infos ){

        FontStyle fontStyle = new FontStyle(mActivity,10, ColorRgbUtil.getBaseText());
        table.getConfig().setColumnTitleStyle(fontStyle);

        table.getConfig().setShowXSequence(false) ;//设置是否显示顶部序号列
        table.getConfig().setShowYSequence(false);//设置是否显示左侧序号列

        table.getConfig().setContentGridStyle(new LineStyle());


        final ArrayTableData<String> tableData = ArrayTableData.create("?",week,infos,
                 new TextDrawFormat<String>(){
//                    @Override
//                    public int measureWidth(Column<String> column, int position, TableConfig config) {
//                        return DensityUtils.dp2px(mActivity,25);
//                    }
//
//                    @Override
//                    public int measureHeight(Column<String> column, int position, TableConfig config) {
//                        return DensityUtils.dp2px(mActivity,15);
//                    }
                    @Override
                    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
                        DrawUtils.drawMultiText(c, paint, rect, getSplitString(value));
                    }

        });

//        tableData.setOnItemClickListener(new ArrayTableData.OnItemClickListener<String>() {
//            @Override
//            public void onClick(Column column, String value, String s, int col, int row) {
//                tableData.getArrayColumns().get(col).getDatas().get(row);
////                if(col==0){
////                    Toast.makeText(mActivity,"列:"+col+ " 行："+row + "数据："+value,Toast.LENGTH_SHORT).show();
////                }
//
//            }
//        });

        int dp10= DensityUtils.dp2px(mActivity,20);
        table.getConfig().setVerticalPadding(dp10).setHorizontalPadding(dp10);
        int screenWith = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith); //设置最小宽度 屏幕宽度
        table.getConfig().setShowColumnTitle(true); //设置是否显示列标题
        table.getConfig().setShowTableTitle(false);  //设置是否显示表格标题
        table.setTableData(tableData);

        Column<String> com= (Column<String>) table.getTableData().getColumns().get(0);
        com.setFixed(true);
        com.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
//                Toast.makeText(mActivity, "数据："+position,Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "onClick: "+position);
//                Log.e(TAG, "onClick: "+s);
//                Log.e(TAG, "onClick: "+value);
                String[] name=s.split(Pattern.quote(" "));
                Intent intent=new Intent(mActivity,ItemtabActivity.class);
                intent.putExtra(TagFinal.OBJECT_TAG,chartinfo.get(0).getInfo().get(position).getId());
                intent.putExtra("title",name[0]);
                mActivity.startActivity(intent);
            }
        });
        com.setFast(true);
        com.setAutoCount(true);
        table.invalidate();
    }




    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }




    private String[] getData(List<ChartInfo> chartinfo){

        List<String> list=new ArrayList<>();
        list.add("     ");
        for (ChartInfo info:chartinfo){
            list.add(info.getYear());
        }

        return  list.toArray(new String[list.size()]);
    }
    private String[][] getTwoData(List<ChartInfo> chartinfo){
        String[][] names=new String[chartinfo.size()+1][chartinfo.get(0).getInfo().size()];
        for (int i=0;i<chartinfo.size()+1;i++){
            if (i<chartinfo.size()){
                List<ChartBean> chartbean=chartinfo.get(i).getInfo();
                for (int j=0;j<chartbean.size();j++){
                    if (i==0){
                        names[0][j]=chartinfo.get(i).getInfo().get(j).getTitle()+" >";
                        names[1][j]=chartbean.get(j).getScore();
                    }else{
                        names[i+1][j]=chartbean.get(j).getScore();
                    }

                }
            }
        }

        return names;
    }



    /**
     * ----------------------------retrofit-----------------------
     */
    public void getChartData()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetTjDataReq request = new TeaJudgeGetTjDataReq();
        //获取参数

        reqBody.teaJudgeGetTjDataReq = request;
        reqEnvelop.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_statistics_data(reqEnvelop);
        call.enqueue(this);
        showProgressDialog("正在加载");

    }


    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);

        dismissProgressDialog();
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;
            if (b.teaJudgeGetTjDataRes != null) {
                String result = b.teaJudgeGetTjDataRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=json.fromJson(result, ResultInfo.class);

                chartinfo = info.getJudge_statistics();
                initTable(getData(chartinfo),getTwoData(chartinfo));
            }
        }else{
            Logger.e(StringUtils.getTextJoint("%1$s:%2$d",name,response.code()));
            toastShow(StringUtils.getTextJoint("数据错误:%1$d",response.code()));
        }
    }

    @Override
    public void onFailure(Call<ResEnv> call, Throwable t) {

        Logger.e("onFailure  :"+call.request().headers().toString());
        dismissProgressDialog();
    }






}
