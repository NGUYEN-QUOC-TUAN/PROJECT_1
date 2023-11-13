package com.techsen.consumable.sql.utils;

import java.util.ArrayList;
import java.util.List;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月1日 下午5:09:33
 **/
public class SqlRowValueList {

	List<SqlRowValue> sqlRowValues = new ArrayList<SqlRowValue>();
	
	public String toJSONString (){
		StringBuffer stringBuffer = new StringBuffer();
		for (SqlRowValue rowValue : sqlRowValues) {
			stringBuffer.append(rowValue.toJSONString() + ",");
		}
		String jsonString = stringBuffer.toString();
		if(sqlRowValues.size() > 0){
			jsonString = jsonString.substring(0 , jsonString.length() - 1 );
			jsonString = "[" + jsonString + "]";
		}
		return jsonString;
	}

	public List<SqlRowValue> getSqlRowValues() {
		return sqlRowValues;
	}

	public void setSqlRowValues(List<SqlRowValue> sqlRowValues) {
		this.sqlRowValues = sqlRowValues;
	}
}
