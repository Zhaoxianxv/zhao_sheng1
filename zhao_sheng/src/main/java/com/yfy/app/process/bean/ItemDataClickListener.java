package com.yfy.app.process.bean;


import com.yfy.app.notice.bean.ChildBean;

/**
 * @Author Zheng Haibo
 * @PersonalWebsite http://www.mobctrl.net
 * @Description
 */
public interface ItemDataClickListener {

	public void onExpandChildren(ProBeanChild itemData);

	public void onHideChildren(ProBeanChild itemData);

}
