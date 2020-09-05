package com.yfy.net.single;

import java.util.concurrent.Callable;

public abstract class SingleCallable<E> implements Callable<E> {

	private Callable<E> callable;

	public SingleCallable(Callable<E> callable) {
		this.callable = callable;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SingleCallable)) {
			return false;
		}
		Callable<E> oCallable = null;
		try {
			oCallable = ((SingleCallable) o).getCallable();
		} catch (Exception e) {
			return false;
		}

		return callable.equals(oCallable);
	}

	public Callable<E> getCallable() {
		return callable;
	}

}
