package com.techsen.consumable.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author fantasy E-mail: vi2014@qq.com
 * @version 2015年3月11日 下午3:57:27
 **/
public class StringUtils {

	public static boolean isVoid(String string) {
		boolean is = false;
		if (string == null || string.isEmpty()) {
			is = true;
		}
		return is;
	}
	
	public static String toHexString(String s) {
		StringBuffer str = new StringBuffer("");
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str.append(s4);
		}
		return str.toString();
	}
	
	public static int getFieldIndex(String[] fields , String field) {
		int index = 0;
		for(String f : fields){
			if(f.equals(field)){
				return index;
			}
			index ++;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		String aaa = "啊";
		String bbb = "高喊口号额问问";
		String sss = "";
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(date));
		System.out.println("=========================>>>>>>>>>>>>>");
		for(int i = 0 ; i <1000 ; i ++){
			sss = sss +  aaa + bbb ;
		}
		System.out.println(toHexString(sss));
	}
}
