/**
 * 
 */
package com.yfy.net.single;

/**
 * @author yfy1
 * @Date 2015��11��13��
 * @version 1.0
 * @Desprition
 */
public class SingleArrayDeque<E> extends ArrayDeque<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 866364104288651290L;

	@Override
	public boolean offer(E e) {
		remove(e);
		return super.offer(e);
	}
}
