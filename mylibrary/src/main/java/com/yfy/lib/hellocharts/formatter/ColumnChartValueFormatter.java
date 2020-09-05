package com.yfy.lib.hellocharts.formatter;

import com.yfy.lib.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SubcolumnValue value);

}
