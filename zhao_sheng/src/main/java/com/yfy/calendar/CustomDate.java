package com.yfy.calendar;

import com.yfy.final_tag.Logger;

import java.io.Serializable;
import java.util.Calendar;

public class CustomDate implements Serializable {

	private static final long serialVersionUID = 1L;
	public int year;
	public int month;
	public int day;
	public int week;
	public int monthDays;

	public CustomDate(int year, int month, int day) {
		if (month > 12) {
			month = 1;
			year++;
		} else if (month < 1) {
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
		this.monthDays = DateUtil.getMonthDays(year, month);
	}

	public CustomDate(CustomDate date) {
		this.year = date.getYear();
		this.month = date.getMonth();
		this.day = date.getDay();
		this.monthDays = DateUtil.getMonthDays(year, month);
	}

	public CustomDate() {
		this.year = DateUtil.getYear();
		this.month = DateUtil.getMonth();
		this.day = DateUtil.getCurrentMonthDay();
		this.monthDays = DateUtil.getMonthDays(year, month);
		this.week=DateUtil.getWeekDay();

	}

	public static CustomDate modifiDayForObject(CustomDate date, int day) {
		CustomDate modifiDate = new CustomDate(date.year, date.month, day);
		return modifiDate;
	}

	@Override
	public String toString() {
		return getString(year) + "/" + getString(month) + "/" + getString(day);
	}


	public String toString(String s) {
		if (s.equals("")){
			return  getString(month) + "月" + getString(day)+"日"+getWeekName(getWeek());
		}else{
			return  getString(year) + s + getString(month) + s + getString(day);
		}

	}
	private String getString(int i) {
		if (i < 10) {
			return "0" + i;
		} else {
			return Integer.toString(i);
		}
	}

	public boolean isCurrentDay() {
		int curYear = DateUtil.getYear();
		int curMonth = DateUtil.getMonth();
		int curDay = DateUtil.getCurrentMonthDay();
		return year == curYear && month == curMonth && day == curDay;
	}


	public void increaseDay(String s) {
		day++;
		week(true);
		if (day > monthDays) {
			day = 1;
			month++;
			if (month > 12) {
				month = 1;
				year++;
			}
		}
		monthDays = DateUtil.getMonthDays(year, month);
	}


	public boolean decreaseDay(String s) {
		if (isCurrentDay()) {
			return false;
		}
		day--;
		week(false);
		if (day <= 0) {
			month--;
			if (month < 0) {
				month = 12;
				year--;
			}
		}
		monthDays = DateUtil.getMonthDays(year, month);
		if (day <= 0) {
			day = monthDays;
		}
		return true;
	}

	public void reduceMonth(int des) {
		int off = Math.abs(des);
		int remainder = 0;
		if (des < 0) {
			year = year - off / 12;
			remainder = off % 12;
			month = month - remainder;
			if (month < 0) {
				month = month + 12;
				year--;
			}
		} else {
			year = year + off / 12;
			remainder = off % 12;
			month = month + remainder;
			if (month > 12) {
				month = month - 12;
				year++;
			}
		}
		monthDays = DateUtil.getMonthDays(year, month);
	}

	public void week(boolean is){
		if (is){
			if (week==6){
				week=0;
			}else {
				++week;
			}
		}else{
			if (week==0){
				week=6;
			}else {
				--week;
			}
		}
	}


	public String getWeekName(final int i){
		String name=null;
		switch (i){
			case 1:
				name="周一";
				break;
			case  2:
				name="周二";
				break;
			case  3:
				name="周三";
				break;
			case  4:
				name="周四";
				break;
			case  5:
				name="周五";
				break;
			case  6:
				name="周六";
				break;
            case  0:
				name="周日";
				break;
		}
		return name;
	}


	public String getWeekNamefrish(final int i){
		String name=null;
		Logger.i("zxx","  "+week);
		switch (i){
			case Calendar.MONDAY:
				name="周一";
				break;
			case  Calendar.TUESDAY:
				name="周二";
				break;
			case  Calendar.WEDNESDAY:
				name="周三";
				break;
			case  Calendar.THURSDAY:
				name="周四";
				break;
			case  Calendar.FRIDAY:
				name="周五";
				break;
			case  Calendar.SATURDAY:
				name="周六";
				break;
			case  Calendar.SUNDAY:
				name="周日";
				break;
		}
		return name;
	}


	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public int getWeek() {
		return week;
	}

}
