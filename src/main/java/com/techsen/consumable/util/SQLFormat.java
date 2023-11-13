package com.techsen.consumable.util;

public class SQLFormat {
	//将NULL转换为空
	public String  valueFormat(String orgValue) {
		orgValue = "" + orgValue +  "";
		if (orgValue.toUpperCase().equals("NULL"))
			orgValue = "";
		return orgValue;
		
	}
	
	//格式化日期时间精确到分
	public String formatDateTime (String datetime) {
		datetime =  "" + datetime;
		if (datetime.toUpperCase().equals("NULL"))
			datetime = "";
		if (!datetime.equals(""))
			datetime = datetime.substring(0, 16);
		return datetime;
	}
	
	//构造SQL查询语句
	public String createSQL(String field,String condition,String value) {
		String subsql = "";
		if (!value.equals("")) 
			subsql = " and " + field + " " +  condition + " '" + value + "' ";
		return subsql;
	}
	
	//格式化查询值
	public String QuotedStr(String value) {
		value = "" + value;
		if (value.toUpperCase().equals("NULL")) 
			value = "";
		if (value.equals(""))
			value = "null";
		else
			value = "'" + value + "'";
		return value;
	}

}
