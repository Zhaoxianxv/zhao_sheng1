package com.yfy.lib.hellocharts.formatter;

import com.yfy.lib.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
}
