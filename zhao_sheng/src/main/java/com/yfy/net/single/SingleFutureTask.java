/**
 * 
 */
package com.yfy.net.single;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author yfy1
 * @version 1.0
 * @Desprition
 */
public class SingleFutureTask<V> extends FutureTask<V> {

	private Callable<V> singleCall;

	public SingleFutureTask(Callable<V> callable) {
		super(callable);
		this.singleCall = callable;
	}

	public Callable<V> getCallable() {
		return singleCall;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SingleFutureTask)) {
			return false;
		}
		Callable<V> oCall = null;
		try {
			oCall = ((SingleFutureTask<V>) o).getCallable();
		} catch (Exception e) {
			return false;
		}
		return singleCall.equals(oCall);
	}

}
