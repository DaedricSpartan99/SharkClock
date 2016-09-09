package it.sharkcraft.sharkclock;

import java.util.Calendar;

public class Time {
	
	public int hour, min, sec;
	
	public static Time current = new Time();
	
	public static int year() {
		
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static int month() {
		
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}
	
	public static int day() {
		
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static int hours() {
		
		return current.hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	public static int minutes() {
		
		return current.min = Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static int seconds() {
		
		return current.sec = Calendar.getInstance().get(Calendar.SECOND);
	}

}
