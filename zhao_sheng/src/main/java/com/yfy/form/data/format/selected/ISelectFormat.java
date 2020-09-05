package com.yfy.form.data.format.selected;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.yfy.form.core.TableConfig;

/**
 * Created by huang on 2018/1/12.
 * 选中操作格式化
 */

public interface ISelectFormat {

     void draw(Canvas canvas, Rect rect, Rect showRect, TableConfig config);
}
