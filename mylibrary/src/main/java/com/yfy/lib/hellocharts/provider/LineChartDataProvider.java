package com.yfy.lib.hellocharts.provider;

import com.yfy.lib.hellocharts.model.LineChartData;

public interface LineChartDataProvider {

    public LineChartData getLineChartData();

    public void setLineChartData(LineChartData data);

}
