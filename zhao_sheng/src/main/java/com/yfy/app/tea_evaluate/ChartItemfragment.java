package com.yfy.app.tea_evaluate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.bean.ChartBean;
import com.yfy.app.tea_evaluate.bean.ChartInfo;
import com.yfy.app.tea_evaluate.bean.JudgeBean;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.judge.TeaJudgeGetTjTypeReq;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.base.fragment.WcfFragment;
import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.Logger;
import com.yfy.lib.hellocharts.formatter.LineChartValueFormatter;
import com.yfy.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import com.yfy.lib.hellocharts.model.Axis;
import com.yfy.lib.hellocharts.model.AxisValue;
import com.yfy.lib.hellocharts.model.Line;
import com.yfy.lib.hellocharts.model.LineChartData;
import com.yfy.lib.hellocharts.model.PointValue;
import com.yfy.lib.hellocharts.model.ValueShape;
import com.yfy.lib.hellocharts.model.Viewport;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.view.Chartline;
import com.yfy.view.textView.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartItemfragment extends WcfFragment implements Callback<ResEnv> {
    private static final String TAG = "zxx";

    private Gson json=new Gson();

    @Bind(R.id.name_flow)
    FlowLayout flowLayout;

    private List<JudgeBean> judgeBeans=new ArrayList<>();

    @Bind(R.id.item_chart)
    Chartline chart;
    private LineChartData data;

    private ValueShape shape = ValueShape.CIRCLE;
    private boolean hasLabelForSelected = true;//选中状态
    private boolean pointsHaveDifferentColor=false;//节点颜色


    private String[] s=new String[]{"    我的分数  ","  中位数"};
    private float max=0;
    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.tea_item_fragment);
        getShapeKind();

        setHasOptionsMenu(true);
        chart.setValueSelectionEnabled(hasLabelForSelected);
        int height=chart.getMinimumHeight();
        Logger.e(TAG, "onCreateView: "+height );
        getChartData();

        setChild(s,flowLayout);

    }





    private float getFloat(boolean score,ChartBean bean ){

        float f;
        if (score){
            f = ConvertObjtect.getInstance().getFloat(bean.getScore());
        }else{
            f = ConvertObjtect.getInstance().getFloat(bean.getMiddle_score());
        }
        float m = ConvertObjtect.getInstance().getFloat(bean.getMax_score());
//        Log.e(TAG, "getFloat: "+max );
//        Log.e(TAG, "getFloat: "+bean.getTitle() );
//        if (max<m)max=m;
//        if (max<1)max=1;
        float d=f/m*100;
        if (m==0)
            return 0f;
        return d;
    }


    private void resetViewport(float f,float top) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        v.top = top;//Y轴下限，固定
        v.left = 0;//X轴左边界，变化
        v.right = f-1;//X轴右边界，变化
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        chart.setInteractive(true);//设置图表是否可以与用户互动
        chart.setZoomEnabled(false);//chart.isZoomEnabled()

//        chart.setZoomEnabled(boolean isZoomEnabled)//设置是否支持缩放
//        chart.setOnValueTouchListener(LineChartOnValueSelectListener touchListener);//为图表设置值得触摸事件
//        chart.setValueSelectionEnabled(boolean idValueSelectionEnabled);//设置图表数据是否选中进行显示
//        chart.setLineChartData(LineChartData data);//为图表设置数据，数据类型为LineChartData

    }


    LineChartValueFormatter chartValueFormatter=new SimpleLineChartValueFormatter(2);
    private void generateData(List<ChartInfo> chartinfo) {
        data = new LineChartData(initOneLine(chartinfo));
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
        ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
        for (int n = 0; n < chartinfo.size(); ++n) {
            axisValuesX.add(new AxisValue(n).setLabel(chartinfo.get(n).getYear()));
        }
        axisValuesY.add(new AxisValue(10).setLabel(" 10%"));
        axisValuesY.add(new AxisValue(20).setLabel(" 20%"));
        axisValuesY.add(new AxisValue(30).setLabel(" 30%"));
        axisValuesY.add(new AxisValue(40).setLabel(" 40%"));
        axisValuesY.add(new AxisValue(50).setLabel(" 50%"));
        axisValuesY.add(new AxisValue(60).setLabel(" 60%"));
        axisValuesY.add(new AxisValue(70).setLabel(" 70%"));
        axisValuesY.add(new AxisValue(80).setLabel(" 80%"));
        axisValuesY.add(new AxisValue(90).setLabel(" 90%"));
        axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
        axisY.setValues(axisValuesY);//为X轴显示的刻度值设置数据集合
//        axisX.setLineColor(Color.BLACK);// 设置X轴轴线颜色
//        axisY.setLineColor(Color.BLACK);// 设置Y轴轴线颜色
        axisX.setTextColor(Color.BLACK);// 设置X轴文字颜色
        axisY.setTextColor(Color.BLACK);// 设置Y轴文字颜色
        axisX.setTextSize(14);// 设置X轴文字大小
        axisY.setTextSize(14);// 设置X轴文字大小
//        axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
        axisX.setHasTiltedLabels(true);// 设置X轴文字向左旋转45度
        axisX.setHasLines(true);// 是否显示X轴网格线
        axisY.setHasLines(true);// 是否显示Y轴网格线
        axisX.setHasSeparationLine(true);// 设置是否有分割线
//        axisX.setInside(boolean isInside);// 设置X轴文字是否在X轴内部


        data.setAxisXBottom(axisX);// 将X轴属性设置到底部
        data.setAxisYLeft(axisY);// 将Y轴属性设置到左边
//        chartData.setAxisYRight(axisYRight);//设置右边显示的轴
//        chartData.setAxisXTop(axisXTop);//设置顶部显示的轴
//

        data.setBaseValue(Float.NEGATIVE_INFINITY);// 设置反向覆盖区域颜色
//        chartData.setBaseValue(20);
        data.setValueLabelBackgroundEnabled(false);
        data.setValueLabelsTextColor(ColorRgbUtil.getBaseText());
        chart.setLineChartData(data);

//        if (max!=0){
//            resetViewport(chartinfo.size(),max);//限制界面
//        }else{
            resetViewport(chartinfo.size(),100);//限制界面
//        }

    }


    private Line getOneLine(boolean is,int color,int index,List<ChartInfo> chartinfo){
        List<PointValue> values = new ArrayList<>();
        for (int j = 0; j < chartinfo.size(); ++j) {
            ChartBean bean=chartinfo.get(j).getInfo().get(index);
            if (bean.getId().equals(getTag()))
                values.add(new PointValue(j, getFloat(is,bean), is?bean.getScore():bean.getMiddle_score()));
//            values.add(new PointValue())
        }
        Line line = new Line(values);//将值设置给折线
        line.setHasLines(true);// 是否显示折线
        line.setColor(color);// 设置折线颜色
        line.setShape(shape);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setCubic(false);// 是否设置为立体的
        line.setFilled(false);// 设置折线覆盖区域是否填充
        line.setStrokeWidth(1);// 设置折线宽度
        line.setPointRadius(3);// 设置节点半径
        line.setFormatter(chartValueFormatter);//显示数据
        line.setHasPoints(true);// 是否显示节点
        line.setHasLabels(true);// 是否显示节点数据
//        line.setHasLabelsOnlyForSelected(hasLabelForSelected);// 隐藏数据，触摸可以显示
//        line.setHasGradientToTransparent(hasGradientToTransparent);
        if (pointsHaveDifferentColor){
            line.setPointColor(color);// 设置节点颜色
        }

        return line;
    }

    private  List<Line> initOneLine(List<ChartInfo> chartinfo){

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartinfo.get(0).getInfo().size(); ++i) {
            lines.add(getOneLine(true,ColorRgbUtil.getTeaOne(),i,chartinfo));// 将数据集合添加线
        }
        for (int i = 0; i < chartinfo.get(0).getInfo().size(); ++i) {
            lines.add(getOneLine(false,ColorRgbUtil.getTeaTwo(),i,chartinfo));// 将数据集合添加线
        }
        return lines;
    }






    @Override
    public boolean onSuccess(String result, WcfTask wcfTask) {
        return false;
    }

    @Override
    public void onError(WcfTask wcfTask) {

    }





    public void setChild(String[] names, FlowLayout flow){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (int i=0;i<names.length;i++){
            final TextView tv = (TextView) mInflater.inflate(R.layout.order_tv,flow, false);
            tv.setText(names[i]);
            tv.setBackgroundColor(Color.TRANSPARENT);


            if (i==0){
                tv.setTextColor(ColorRgbUtil.getTeaOne());
                //
            }else{
                tv.setTextColor(ColorRgbUtil.getTeaTwo());
            }
            flow.addView(tv);
        }
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

    public void getShapeKind()  {

        ReqEnv reqEnvelop = new ReqEnv();
        ReqBody reqBody = new ReqBody();
        TeaJudgeGetTjTypeReq request = new TeaJudgeGetTjTypeReq();
        //获取参数

        reqBody.teaJudgeGetTjTypeReq = request;
        reqEnvelop.body = reqBody;

        Call<ResEnv> call = RetrofitGenerator.getWeatherInterfaceApi().judge_get_tj_type(reqEnvelop);
        call.enqueue(this);
//        showProgressDialog("正在加载");

    }

    @Override
    public void onResponse(Call<ResEnv> call, Response<ResEnv> response) {
        dismissProgressDialog();
        List<String> names=StringUtils.listToStringSplitCharacters(call.request().headers().toString().trim(), "/");
        String name=names.get(names.size()-1);
        ResEnv respEnvelope = response.body();
        if (respEnvelope != null) {
            ResBody b = respEnvelope.body;

            if (b.teaJudgeGetTjDataRes != null) {
                String result = b.teaJudgeGetTjDataRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=json.fromJson(result, ResultInfo.class);
                List<ChartInfo> chartinfo=info.getJudge_statistics();

                generateData(chartinfo);

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
