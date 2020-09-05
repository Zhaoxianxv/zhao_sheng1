package com.yfy.form.data.format.sequence;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.yfy.form.core.TableConfig;
import com.yfy.form.data.format.IFormat;

/**
 * Created by huang on 2017/11/7.
 *
 *序号格式化
 */

public interface ISequenceFormat extends IFormat<Integer> {


   void draw(Canvas canvas, int sequence, Rect rect, TableConfig config);

}
