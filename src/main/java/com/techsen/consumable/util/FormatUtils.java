package com.techsen.consumable.util;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年3月26日 上午11:38:01
 **/
public class FormatUtils {
	
	public static String DATE_TYPE = "date";
	public static String DATETIME_TYPE = "dateTime";
	public static String TIME_TYPE = "time";

	public static String date2String(Date datidate , String type) {
		String dateString = null;
		SimpleDateFormat formatter = null;
		if(type.equals(DATE_TYPE)){
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}
		if(type.equals(DATETIME_TYPE)){
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		if(type.equals(TIME_TYPE)){
			formatter = new SimpleDateFormat("HH:mm:ss");
		}
		dateString = formatter.format(datidate);
		return dateString;
	}
	
	public Date string2Date(String dateString , String dateType) {
		SimpleDateFormat formatter = null;
		if(dateType.equals(DATETIME_TYPE)){
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		if(dateType.equals(DATE_TYPE)){
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}
		if(dateType.equals(TIME_TYPE)){
			formatter = new SimpleDateFormat("HH:mm:ss");
		}
		Date date;
		try {
			date = formatter.parse( "dateString" );
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
}
