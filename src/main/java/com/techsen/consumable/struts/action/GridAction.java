package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.techsen.consumable.bean.KeyValue;
import com.techsen.consumable.util.DBConnection;
import com.techsen.consumable.util.VecUtils;
import com.techsen.consumable.util.XMLRead;
@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class GridAction {
	
	@Autowired
	private DBConnection dbConnection;
	
	@Action("grid")
	public String execute() throws Exception{
		ResultSet rs;
		String sql,group = "PUR";
		JSONObject json = new JSONObject();;
		JSONArray array = new JSONArray();
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String option = "" + request.getParameter("option");
		String func = "" + request.getParameter("function");
		if (option.equals("null"))
			option = "";
		
		if (func.equals("null"))
			func = "";
		
		XMLRead xmlRead = new XMLRead();
		VecUtils vec = new VecUtils();
		
		System.out.println(xmlRead.readXML("grids.xml",func, "fields"));
		
		System.out.println(option);
		System.out.println(func);
		
		try {
			dbConnection.getConnection();
			
			if (func.equals(""))
				return null;
			
			String field = xmlRead.readXML("grids.xml",func, "fields");
			String table = xmlRead.readXML("grids.xml",func, "table");
			String where = xmlRead.readXML("grids.xml",func, "where");
			String order = xmlRead.readXML("grids.xml",func, "order");
			String object = xmlRead.readXML("grids.xml",func, "objects");
			String upfield = xmlRead.readXML("grids.xml",func, "upfields");
			String keyfield = xmlRead.readXML("grids.xml",func, "keyFields");
			
			if (option.equals("") || option.equals("load")) {
				Vector<String> fields = vec.getVector(field, ",");
				Vector<String> objects = vec.getVector(object, ",");
				Vector<KeyValue> joson = vec.setKeyValue(objects,fields);
				
				sql = "SELECT " + field + " FROM " + table + " WHERE " + where
					 + " ORDER BY " + order;
				
				sql = sql.replace("${0}", group);
				
				rs = dbConnection.executeQuery(sql);
				
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					
					for (int i = 0; i < joson.size();i++) {
						KeyValue keyvalue = joson.elementAt(i);
						jsonObject.put(keyvalue.getKey(), rs.getString(keyvalue.getValue()));
					}
					
					array.add(jsonObject);
				}
				
				json.put("roots", array);
				
				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
				
			} else if (option.equals("update")) {
				String msg = "";
				
				//检查记录是否存在
				String orgcode = "" + request.getParameter("orgcode");
				String code = "" + request.getParameter("code");
				
				if (orgcode.equals("null"))
					orgcode = "";
				if (code.equals("null"))
					code = "";
				
				if (orgcode.equals(""))
					sql = "SELECT COUNT(*) AS total FROM " + table 
					+ " WHERE " + keyfield + " = " + formatValue(code);
				else
					sql = "SELECT COUNT(*) AS total FROM " + table 
					+ " WHERE " + keyfield + " = " + formatValue(code) 
					+ " AND " + keyfield + " <> " + formatValue(orgcode);
				
				if (Integer.valueOf(dbConnection.getFieldValue(sql, "total")) != 0) 
					msg = "exists";
				//记录存在检查完成
				
				if (msg.equals("")) {
					Vector<String> objects =  vec.getVector(object, ",");
					Vector<String> values = new Vector<String>();
					
					for (int i = 0;i<objects.size();i++)
						values.add(request.getParameter(objects.elementAt(i)));
					
					values.add(group);
					
					String valStr = "";
					
					for (int i = 0;i<values.size();i++) {
						if (i==0) 
							valStr = formatValue(values.elementAt(i));
						else
							valStr = valStr + "," + formatValue(values.elementAt(i));
					}
					
					sql = "REPLACE INTO " + table + " (" + upfield + ") VALUES (" + valStr + ")";
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
					
					sql = "";
					
				}
				
				ServletActionContext.getResponse().getWriter().print(msg);
				
			}  else if (option.equals("delete")) {
				String msg = "";
				
				//检查记录是否存在
				String code = "" + request.getParameter("code");
				
				if (code.equals("null"))
					code = "";
				
				//记录存在检查完成
					
				sql = "DELETE FROM " + table + " WHERE " + keyfield + " = " + formatValue(code);
				if (dbConnection.executeUpdate(sql) == true)
					msg = "ok";
				else
					msg = "failed";
				
				ServletActionContext.getResponse().getWriter().print(msg); 
			}
				
		
			
			
		} finally {
			dbConnection.closeConn();
			rs = null;
			dbConnection = null;
			xmlRead = null;
			vec = null;
		}
		
		return null;
	}
	
	private String formatValue(String value) {
		
		value = "" + value;
		if (value.equals("null"))
			value = "";
		if (value.equals(""))
			value = "null";
		else
			value = "'" + value + "'";
		
		return value;
	}

}
