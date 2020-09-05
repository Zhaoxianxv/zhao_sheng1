/**
 * 
 */
package com.yfy.net.single;


/**
 * @author yfy1
 * @Date 2015��12��3��
 * @version 1.0
 * @Desprition
 */
public abstract class SingleRunnable implements Runnable {

	private Runnable runnable;

	public SingleRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SingleRunnable)) {
			return false;
		}
		Runnable oRunnable = null;
		try {
			oRunnable = ((SingleRunnable) o).getRunnable();
		} catch (Exception e) {
			return false;
		}

		return runnable.equals(oRunnable);

	}

	public Runnable getRunnable() {
		return runnable;
	}

}
