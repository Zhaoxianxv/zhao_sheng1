package com.yfy.lib.hellocharts.formatter;

import com.yfy.lib.hellocharts.model.BubbleValue;

public interface BubbleChartValueFormatter {

    public int formatChartValue(char[] formattedValue, BubbleValue value);
}
