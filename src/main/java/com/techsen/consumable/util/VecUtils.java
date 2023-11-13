package com.techsen.consumable.util;

import java.util.Vector;

import com.techsen.consumable.bean.KeyValue;

public class VecUtils {
	
	public Vector<String> getVector(String value,String split) {
		Vector<String> values = new Vector<String>();
		String val;
		int index = 0;
		while (value.length() > 0) {
			index = value.indexOf(split);
			if (index > -1) {	
				val = value.substring(0, index).trim();
				value = value.substring(index+1);
			} else {
				val = value;
				value = "";
			}
			values.add(val);
		}
		return values;
	}
	
	public Vector<Integer> getVector(String value,String split,Integer flag) {
		Vector<Integer> values = new Vector<Integer>();
		value = value.trim();
		String val;
		
		int index = 0;
		while (value.length() > 0) {
			index = value.indexOf(split);
			if (index > -1) {	
				val = value.substring(0, index).trim();
				value = value.substring(index+1);
			} else {
				val = value;
				value = "";
			}
			
			if (!val.equals(""))
				values.add(Integer.valueOf(val));
		}
		
		return values;
	}
	
	public Vector<KeyValue> setKeyValue(Vector<String> keys,Vector<String> values) {
		Vector<KeyValue> KeyValues = new Vector<KeyValue>();
		
		try {
			if (keys.size() ==  values.size()) {
				for (int i = 0;i < keys.size();i++) {
					KeyValue keyvalue = new KeyValue();
					keyvalue.setKey(keys.elementAt(i));
					keyvalue.setValue(values.elementAt(i));
					KeyValues.add(keyvalue);
				}
			}
		} finally {
			keys = null;
			values = null;
		}
		
			
		return KeyValues;
	}
	
	//获取名值得对应
	public Vector<KeyValue> setKeyValue(Vector<String> values,String split) {
		Vector<KeyValue> KeyValues = new Vector<KeyValue>();
		String str,key,value;
		int index;
		
		try {
			for(int i=0;i<values.size();i++){
				str = (String)values.elementAt(i);
				KeyValue keyvalue = new KeyValue();
				index = str.indexOf(split);
				if (index > -1) {
					key = str.substring(0, index).trim();
					value = str.substring(index + 1).trim();
				} else {
					key = "";
					value = "";
				}	
				
				if (!key.equals("")&&!value.equals("")) {
					keyvalue.setKey(key);	
					keyvalue.setValue(value);	
					KeyValues.add(keyvalue);
				}
				
			}
		} finally {
			values = null;
		}
		
		
		return KeyValues;
	}

}
