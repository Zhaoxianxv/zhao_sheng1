package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.bean.ChildData;
import com.yfy.app.bean.UserInfo;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.bean.ChartBean;
import com.yfy.app.tea_evaluate.bean.ChartInfo;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.fragment.BaseFragment;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
import com.yfy.form.core.SmartTable;
import com.yfy.form.core.TableConfig;
import com.yfy.form.data.CellInfo;
import com.yfy.form.data.column.Column;
import com.yfy.form.data.format.bg.BaseCellBackgroundFormat;
import com.yfy.form.data.format.bg.ICellBackgroundFormat;
import com.yfy.form.data.table.TableData;
import com.yfy.form.listener.OnColumnItemClickListener;
import com.yfy.form.utils.DensityUtils;
import com.yfy.final_tag.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemSmartfragment extends BaseFragment implements Callback<ResEnv> {
    private static final String TAG = "zxx";

    private Gson json=new Gson();
    @Bind(R.id.smart_tag)
    TextView smart_tag;
    @Bind(R.id.table)
    SmartTable<UserInfo> table;
    //    SmartTable<UserInfo> table;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.tea_smart_fragment);

        smart_tag.setVisibility(View.GONE);
        getChartData();

    }









    private void initData(List<ChartInfo> chartinfo){
        final List<UserInfo> testData = new ArrayList<>();
        for (ChartInfo info:chartinfo){
            List<ChartBean> list=info.getInfo();
            for (ChartBean bean:list){
               if (bean.getId().equals(getTag())){
                   UserInfo userData = new UserInfo(info.getYear()+" >", bean.getScore(),bean.getMiddle_score() ,new ChildData("测试"));
                   testData.add(userData);
               }
            }
        }
        List<Column> columns = new ArrayList<>();
        Column column = new Column<>("    ", "name");
        column.setFast(true);
        column.setAutoCount(true);
        column.setOnColumnItemClickListener( new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
//                Toast.makeText(mActivity, "数据："+position,Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "onClick: "+position);
//                Log.e(TAG, "onClick: "+s);
//                Log.e(TAG, "onClick: "+value);
                String[] name=s.split(Pattern.quote(" "));

                Intent intent=new Intent(mActivity,ChartEvaluateActivity.class);
                intent.putExtra(TagFinal.ID_TAG,getTag());
                intent.putExtra("year",name[0]);
                mActivity.startActivity(intent);
            }
        });
        columns.add(column);
        Column column1 = new Column<>("我的得分", "scroe");
        column1.setAutoCount(true);
        columns.add(column1);

        Column column2 = new Column<>("中位数", "mill_scroe");
        column2.setAutoCount(true);
        columns.add(column2);


        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor( CellInfo cellInfo) {
                if(cellInfo.row %2 == 0) {
                    return ColorRgbUtil.getWhite();
                }
                return TableConfig.INVALID_COLOR;
            }

        };

        ICellBackgroundFormat<Integer> backgroundFormat2 = new BaseCellBackgroundFormat<Integer>() {
            @Override
            public int getBackGroundColor(Integer position) {
                if(position%2 == 0){
                    return ColorRgbUtil.getWhite();
                }
                return TableConfig.INVALID_COLOR;

            }

            @Override
            public int getTextColor(Integer position) {
                if(position%2 == 0) {
                    return ColorRgbUtil.getWhite();
                }
                return TableConfig.INVALID_COLOR;
            }
        };
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);//

        final TableData<UserInfo> tableData = new TableData<>("测试",testData,columns);
        table.getConfig().setShowTableTitle(false);  //设置是否显示表格标题
        table.getConfig().setShowXSequence(false) ;//设置是否显示顶部序号列
        table.getConfig().setShowYSequence(false);//设置是否显示左侧序号列

        int screenWith = mActivity.getWindowManager().getDefaultDisplay().getWidth();
        table.getConfig().setMinTableWidth(screenWith); //设置最小宽度 屏幕宽度
        table.getConfig().setSequenceHorizontalPadding(50);//
        tableData.setShowCount(false);//设置是否显示统计
        int dp10= DensityUtils.dp2px(mActivity,20);
        table.getConfig().setVerticalPadding(dp10).setHorizontalPadding(dp10);
        table.setTableData(tableData);



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
                List<ChartInfo> chartinfo=info.getJudge_statistics();

                initData(chartinfo);
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
