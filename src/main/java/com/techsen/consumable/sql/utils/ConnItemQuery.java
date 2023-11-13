package com.techsen.consumable.sql.utils;


/**
 * @author fantasy E-mail: vi2014@qq.com
 * @version 2014年10月31日 上午8:54:41
 **/
public class ConnItemQuery {

	public ConnItemQuery() {
	}
	
	public static String connItemQuery(QueryItem queryItem) {
		StringBuffer sqlQueryItem = new StringBuffer();
		if (queryItem.getHiddenValue() != null
				&& queryItem.getHiddenValue().length > 0) {
			String hiddenValue = getHiddenValue(queryItem.getHiddenValue());
			if (hiddenValue != null && !hiddenValue.equals("")) {
				sqlQueryItem.append(hiddenValue);
			}
		}
		if(queryItem.getValue()!= null&&queryItem.getValue().length>0){
			for (int i = 0; i < queryItem.getCondition().length; i++) {
				//System.out.println(i+"===>"+queryItem.getRelation()[i]+"===>"+queryItem.getCondition()[i]+"===>"+queryItem.getItem()[i]);
				sqlQueryItem.append(getChildItem(queryItem.getRelation()[i],
						queryItem.getItem()[i], queryItem.getCondition()[i],
						queryItem.getValue()[i]));
			}
		}
		if (queryItem.getGroupValue() != null
				&& queryItem.getGroupValue().length > 0) {
			String groupItem = getGroupItem(queryItem.getGroupValue());
			if (groupItem != null && !groupItem.equals("")) {
				sqlQueryItem.append(" GROUP BY " + groupItem.substring(0, groupItem.length()-1));
			}
		}
		if (queryItem.getOrderValue() != null
				&& queryItem.getOrderValue().length > 0) {
			String orderItem = getOrderItem(queryItem.getOrderValue(),
					queryItem.getOrderRule());
			if (orderItem != null && !orderItem.equals("")) {
				sqlQueryItem.append(" ORDER BY " + orderItem.substring(0, orderItem.length()-1));
			}
		}
		return sqlQueryItem.toString();
	}

	private static String getChildItem(String relation, String item,
			String condition, String value) {
		String childItem = "";
		if (value != null && !value.equals("")) {
			if (condition.equals("like")||condition.contains("like")) {
				value = "%" + value + "%";
			}
			if(value.contains("null")){
				childItem = " " + relation + " " + item + " " + condition + " " +value ;
			}else{
				childItem = " " + relation + " " + item + " " + condition + "'"
						+ value + "'";
			}
		}
		return childItem;
	}

	private static String getOrderItem(String[] orderValue, String[] orderRule) {
		StringBuffer orderItem = new StringBuffer();
		for (int i = 0; i < orderValue.length; i++) {
			if (orderValue[i] != null && !orderValue[i].equals("")) {
				orderItem.append(" " + orderValue[i] + " " + orderRule[i]+",");
			}
		}
		return orderItem.toString();
	}

	private static String getGroupItem(String[] groupValue) {
		StringBuffer groupItem = new StringBuffer();
		for (int i = 0; i < groupValue.length; i++) {
			if (groupValue[i] != null && !groupValue[i].equals("")) {
				groupItem.append(" " + groupValue[i]+",");
			}
		}
		return groupItem.toString();
	}
	/**
	 * 将“%#”转义为逗号
	 * */
	private static String getHiddenValue(String[] hiddenValues) {
		StringBuffer hiddenValue = new StringBuffer();
		for (int i = 0; i < hiddenValues.length; i++) {
			if (hiddenValues[i] != null && !hiddenValues[i].equals("")) {
				hiddenValue.append(" " + hiddenValues[i].replace("%#", ",")+" ");
			}
		}
		return hiddenValue.toString();
	}
}
