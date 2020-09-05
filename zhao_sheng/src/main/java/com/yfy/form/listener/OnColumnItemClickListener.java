package com.yfy.form.listener;

import com.yfy.form.data.column.Column;

/**
 * Created by huang on 2017/11/4.
 */

public interface OnColumnItemClickListener<T> {

    void onClick(Column<T> column, String value, T t, int position);
}
