package com.yfy.lib.hellocharts.formatter;


import com.yfy.lib.hellocharts.model.PointValue;

public interface LineChartValueFormatter {

    public int formatChartValue(char[] formattedValue, PointValue value);
}
