package com.techsen.consumable.sql.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月1日 下午3:08:48
 **/
public class SqlRowValue {

	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	public static String KEY_TYPE = "key";
	public static String VALUE_TYPE = "value";
	
	
	public String getValue(String key){
		return getValue2String(hashMap.get(key));
	}
	
	public void add(String key , Object value){
		hashMap.put(key, value);
	}
	
	public void setValue(String key , Object value){
		hashMap.remove(key);
		hashMap.put(key, value);
	}
	
	public String toJSONString(){
		return "{" + toString() + "}" ;
	}
	
	public String toString(){
		StringBuffer string = new StringBuffer("");
		Iterator<Entry<String, Object>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
			String key = entry.getKey();
			key = key.replace("\"", "\\\"");
			String val = getValue2String(entry.getValue());
			string.append( "\"" + key + "\"" + ":\"" + val + "\","); 
		}
		if(!string.toString().equals("")){
			string.deleteCharAt(string.length() - 1);
		}
		return string.toString();
	}
	
	public String toQueryString(){
		return toUpdateString();
	}
	
	public String toDeleteString(){
		return toUpdateString();
	}
	
	public String toUpdateString(){
		StringBuffer updateStringBuf = new StringBuffer("");
		Iterator<Entry<String, Object>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
			String key = entry.getKey();
			String val = getValue2String(entry.getValue());
			updateStringBuf.append( "`" + key + "`" + " = \"" + val + "\","); 
		}
		if(!updateStringBuf.toString().equals("")){
			updateStringBuf.deleteCharAt(updateStringBuf.length() - 1);
		}
		return updateStringBuf.toString();
	}
	
	public String toInsertString(){
		StringBuffer insertStringBuf = new StringBuffer("");
		insertStringBuf.append(getKeyValueString(KEY_TYPE));
		insertStringBuf.append(" values ");
		insertStringBuf.append(getKeyValueString(VALUE_TYPE));
		return insertStringBuf.toString();
	}
	
	public String getKeyValueString(String type){
		StringBuffer keyValueStringBuf = new StringBuffer("");
		Iterator<Entry<String, Object>> iter = hashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iter.next();
			String key = entry.getKey();
			String val = getValue2String(entry.getValue());
			if(type.equals(KEY_TYPE)){
				keyValueStringBuf.append( "`" + key + "`,"); 
			}
			if(type.equals(VALUE_TYPE)){
				keyValueStringBuf.append( " \"" + val + "\","); 
			}
			
		}
		if(!keyValueStringBuf.toString().equals("")){
			keyValueStringBuf.deleteCharAt(keyValueStringBuf.length() - 1);
		}
		return "(" + keyValueStringBuf.toString() + ")";
	}

	public HashMap<String, Object> getHashMap() {
		return hashMap;
	}
	
	public void setHashMap(HashMap<String, Object> hashMap) {
		this.hashMap = hashMap;
	}
	
	public String getValue2String(Object o){
		String valueString = "";
		if(o == null){
			return "";
		}
		if(o.getClass().getName().equals("[Ljava.lang.String;")){
			for(String string : (String[])o){
				valueString = valueString + string + "#|" ;
			}
			valueString = valueString.substring(0, valueString.length() -2);
		}
		if(o.getClass().getName().equals("java.lang.String")){
			valueString = (String)o;
		}
		valueString = valueString.replace("\"", "\\\"");
		return valueString;
	}
	
}
