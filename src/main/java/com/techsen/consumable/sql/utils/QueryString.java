package com.techsen.consumable.sql.utils;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午8:48:59
 **/
public class QueryString {

	private String table;
	private String executeType;
	private String queryItem;
	private String queryString;

	public final static String EXECUTE_TYPE_QUERY = "select";
	public final static String EXECUTE_TYPE_UPDATE = "update";
	public final static String EXECUTE_TYPE_INSERT = "insert";
	public final static String EXECUTE_TYPE_DELETE = "delete";
	
	public QueryString(String executeType, String table){
		this.executeType = executeType;
		this.table = table;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getExecuteType() {
		return executeType;
	}

	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}

	public String getQueryItem() {
		return queryItem;
	}

	public void setQueryItem(String queryItem) {
		this.queryItem = queryItem;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getExecuteString() {
		String executetring = "";
		if (executeType.equals(EXECUTE_TYPE_UPDATE)) {
			executetring = executeType + " " + table + " set " + queryString;
		}
		if (executeType.equals(EXECUTE_TYPE_INSERT)) {
			executetring = executeType + " into " + table + " " + queryString;
		}
		if (executeType.equals(EXECUTE_TYPE_QUERY)) {
			executetring = executeType + " " + queryItem + " " + table + " from " + queryString;
		}
		if (executeType.equals(EXECUTE_TYPE_DELETE)) {
			executetring = executeType + " from " + table + queryString;
		}
		return executetring;
	}

}
