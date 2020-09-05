package com.yfy.app.tea_evaluate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.zhao_sheng.R;
import com.google.gson.Gson;
import com.yfy.app.net.ReqBody;
import com.yfy.app.net.ReqEnv;
import com.yfy.app.net.ResBody;
import com.yfy.app.net.ResEnv;
import com.yfy.app.net.RetrofitGenerator;
import com.yfy.app.tea_evaluate.bean.ChartBean;
import com.yfy.app.tea_evaluate.bean.ChartInfo;
import com.yfy.app.tea_evaluate.bean.JudgeBean;
import com.yfy.app.tea_evaluate.bean.ResultInfo;
import com.yfy.app.net.judge.TeaJudgeGetTjDataReq;
import com.yfy.app.net.judge.TeaJudgeGetTjTypeReq;
import com.yfy.base.fragment.WcfFragment;

import com.yfy.final_tag.ColorRgbUtil;
import com.yfy.final_tag.ConvertObjtect;
import com.yfy.final_tag.StringUtils;
import com.yfy.final_tag.TagFinal;
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
import com.yfy.lib.hellocharts.util.ChartUtils;
import com.yfy.lib.hellocharts.view.LineChartView;
import com.yfy.net.loading.interf.WcfTask;
import com.yfy.view.textView.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainfragment extends WcfFragment implements Callback<ResEnv> {
    private static final String TAG = "zxx";

    private Gson json=new Gson();

    @Bind(R.id.name_flow)
    FlowLayout flowLayout;
    private List<JudgeBean> judgeBeans=new ArrayList<>();

    @Bind(R.id.chart)
    LineChartView chart;
    private LineChartData data;

//    private boolean hasAxes = true;//是否显示XY轴
    private boolean hasAxesNames = false;//x/y轴name
    private boolean hasLines = true;//节点连线
    private boolean hasPoints = true;//节点标识
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;//面积

    private boolean isCubic = false;//连接线样式

    private boolean hasLabels = false;//是否显示节点数据
    private boolean hasLabelForSelected = false;//选中状态
    private boolean pointsHaveDifferentColor=false;//节点颜色
    private boolean hasGradientToTransparent = true;


    @Override
    public void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.tea_main_fragment);
        getShapeKind();
        setHasOptionsMenu(true);
        chart.setValueSelectionEnabled(hasLabelForSelected);
        getChartData();

    }






    private float getFloat(List<ChartInfo> chartinfo,int index,int pos){
        ChartBean bean=chartinfo.get(index).getInfo().get(pos);
        float f= ConvertObjtect.getInstance().getFloat(bean.getScore());
        float max=ConvertObjtect.getInstance().getFloat(bean.getMax_score());

        float d=f/max*100;


        if (max==0)
            return 0f;
        return d;
    }


    private void resetViewport(float f) {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = f-1;
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

        data.setBaseValue(Float.NEGATIVE_INFINITY);
//        chartData.setBaseValue(20);// 设置反向覆盖区域颜色
        chart.setLineChartData(data);

    }


    private Line getOneLine(int index,List<ChartInfo> chartinfo){
        List<PointValue> values = new ArrayList<>();
        for (int j = 0; j < chartinfo.size(); ++j) {
            values.add(new PointValue(j, getFloat(chartinfo,j,index)));
//            values.add(new PointValue(j, (float) Math.random() * 10f));
        }
        Line line = new Line(values);//将值设置给折线
        line.setHasLines(true);// 是否显示折线
        line.setColor(ChartUtils.COLORS[(index) % ChartUtils.COLORS.length]);// 设置折线颜色
        line.setShape(shape);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setCubic(false);// 是否设置为立体的
        line.setFilled(false);// 设置折线覆盖区域是否填充
        line.setHasLabels(false);// 是否显示节点数据
        line.setStrokeWidth(1);// 设置折线宽度
        line.setPointRadius(3);// 设置节点半径
        line.setFormatter(chartValueFormatter);//显示数据
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);// 隐藏数据，触摸可以显示
        line.setHasPoints(true);// 是否显示节点

        line.setHasGradientToTransparent(hasGradientToTransparent);
        if (pointsHaveDifferentColor){
            line.setPointColor(ChartUtils.COLORS[(index) % ChartUtils.COLORS.length]);// 设置节点颜色
        }
        return line;
    }

    private  List<Line> initOneLine(List<ChartInfo> chartinfo){

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartinfo.get(0).getInfo().size(); ++i) {
            lines.add(getOneLine(i,chartinfo));// 将数据集合添加线
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





    public void setChild(String[] names, FlowLayout flow,int index){
        LayoutInflater mInflater = LayoutInflater.from(mActivity);
        if (flow.getChildCount()!=0){
            flow.removeAllViews();
        }
        for (int i=0;i<names.length;i++){
            final TextView tv = (TextView) mInflater.inflate(R.layout.order_tea_tv,flow, false);
            tv.setText(" "+names[i]+" ");

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name=tv.getText().toString();
                    onclick(name);
                }
            });
            tv.setTextColor(ColorRgbUtil.getWhite());

            GradientDrawable myGrad = (GradientDrawable)tv.getBackground();
            myGrad.setColor(ChartUtils.COLORS[(i) % ChartUtils.COLORS.length]);

            flow.addView(tv);
        }
    }

    private void onclick(String name){
        for (int i=0;i<judgeBeans.size();i++){
            JudgeBean bean=judgeBeans.get(i);
            if (name.equals(" "+bean.getName()+" ")){
//                setChild(getOfName(judgeBeans),flowLayout,i);
                Intent intent=new Intent(mActivity,ItemtabActivity.class);
                intent.putExtra(TagFinal.OBJECT_TAG,judgeBeans.get(i).getId());
                intent.putExtra("title",judgeBeans.get(i).getName());
                mActivity.startActivity(intent);
//                toastShow("跳转");
                break;
            }
        }
    }


    public String[] getOfName(List<JudgeBean> room_names){
        List<String> list=new ArrayList<>();
        for (JudgeBean bean:room_names) {
            String class_name=bean.getName();
            boolean is=false;
            for ( String s:list) {
                if (s.equals(class_name)) is=true;
            }
            if (is) {
                continue;
            }else{
                list.add(class_name);
            }
        }
        String[] se=new String[list.size()];
        return list.toArray(se);
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
            if (b.teaJudgeGetTjTypeRes != null) {
                String result = b.teaJudgeGetTjTypeRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=json.fromJson(result, ResultInfo.class);
                judgeBeans=info.getJudge_statistics_class();
                setChild(getOfName(judgeBeans),flowLayout,-1   );
            }
            if (b.teaJudgeGetTjDataRes != null) {
                String result = b.teaJudgeGetTjDataRes.result;
                Logger.e(StringUtils.getTextJoint("%1$s:\n%2$s",name,result));
                ResultInfo info=json.fromJson(result, ResultInfo.class);
                List<ChartInfo> chartinfo=info.getJudge_statistics();
                generateData(chartinfo);
                resetViewport(chartinfo.size());
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
        toastShow("数据出差了");
    }






}
