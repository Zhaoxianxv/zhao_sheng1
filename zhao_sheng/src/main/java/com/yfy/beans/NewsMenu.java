/**
 * 
 */
package com.yfy.beans;

import java.util.List;

/**
 * @version 1.0
 * @Desprition
 */
public class NewsMenu {

	private String programa_id;
	private String programa_name;
	private String islast;
	private List<NewsMenu> newsMenuList;

	public NewsMenu(String programa_id, String programa_name) {
		super();
		this.programa_id = programa_id;
		this.programa_name = programa_name;
	}

	public NewsMenu(String programa_id, String programa_name, String islast,
			List<NewsMenu> newsMenuList) {
		super();
		this.programa_id = programa_id;
		this.programa_name = programa_name;
		this.islast = islast;
		this.newsMenuList = newsMenuList;
	}

	public String getPrograma_id() {
		return programa_id;
	}

	public void setPrograma_id(String programa_id) {
		this.programa_id = programa_id;
	}

	public String getPrograma_name() {
		return programa_name;
	}

	public void setPrograma_name(String programa_name) {
		this.programa_name = programa_name;
	}

	public String getIslast() {
		return islast;
	}

	public void setIslast(String islast) {
		this.islast = islast;
	}

	public List<NewsMenu> getNewsMenuList() {
		return newsMenuList;
	}

	public void setNewsMenuList(List<NewsMenu> newsMenuList) {
		this.newsMenuList = newsMenuList;
	}

	@Override
	public String toString() {
		return "NewsMenu [programa_id=" + programa_id + ", programa_name="
				+ programa_name + ", islast=" + islast + ", newsMenuList="
				+ newsMenuList + "]";
	}

}
