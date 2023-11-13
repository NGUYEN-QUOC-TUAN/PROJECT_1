package com.techsen.consumable.util;

import java.util.HashMap;
import java.util.Vector;

import com.techsen.consumable.bean.KeyValue;

public class HashMapUtils {
	
	public HashMap<String,String> setHashMap(Vector<String> values,String split) {
		HashMap<String,String> map = new HashMap<String,String>();
		
		String str,key,value;
		int index;
		for(int i=0;i<values.size();i++){
			str = (String)values.elementAt(i);
			index = str.indexOf(split);
			if (index > -1) {
				key = str.substring(0, index).trim();
				value = str.substring(index + 1).trim();
			} else {
				key = "";
				value = "";
			}	
			
			if (!key.equals("")) {
				map.put(key, value);
			}
			
		}
		
		return map;
	}
	
	public HashMap<String,String> setHashMap(Vector<KeyValue> values) {
		HashMap<String,String> map = new HashMap<String,String>();
		for(int i=0;i<values.size();i++){
			KeyValue keyval = new KeyValue();
			keyval = values.elementAt(i);
			map.put(keyval.getKey(), keyval.getValue());
		}
		
		return map;
	}
	
	public String getHashValue(HashMap<String,String> map,String key) {
		String result;
		result = "" + (String)map.get(key);
		if (result.equals("null"))
			result = "";
		return result;
	}

}
