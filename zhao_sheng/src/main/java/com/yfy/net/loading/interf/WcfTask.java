/**
 * 
 */
package com.yfy.net.loading.interf;

import java.util.concurrent.Callable;

/**
 * @author yfy1
 * @Date 2015-11-12
 * @version 1.0
 * @Desprition
 */
public interface WcfTask extends Callable<String> {

	public abstract Indicator getIndicator();

	public abstract Dialog getDialog();

	public abstract String getResult();

	public abstract void setResult(String result);

	public abstract String getName();

	public abstract int getId();

	public abstract Object[] getParams();

}
