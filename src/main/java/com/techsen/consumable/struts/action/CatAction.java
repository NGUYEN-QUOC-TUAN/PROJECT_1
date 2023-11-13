package com.techsen.consumable.struts.action;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.techsen.consumable.util.DBConnection;
@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class CatAction {
	
	@Autowired
	private DBConnection dbConnection;
	
	@Action("cat")
	public String execute() throws Exception{
		ResultSet rs;
		String sql;
		JSONObject json = new JSONObject();;
		JSONArray array = new JSONArray();
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String option = "" + request.getParameter("option");
		if (option.equals("null"))
			option = "";
		
		HttpSession session = ServletActionContext. getRequest().getSession();
		String group = "" + (String) session.getAttribute("group");
		if (group.equals("null"))
			group = "";
		
		try {
			dbConnection.getConnection();
			if (group.equals("")) {
				return null;
			}
			
			if (option.equals("") || option.equals("load")) {
				
				sql = "SELECT * FROM cat_mstr WHERE cat_group = '" + group + "' ORDER BY cat_code";
				rs = dbConnection.executeQuery(sql);
				
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("code", rs.getString("cat_code"));
					jsonObject.put("desc", rs.getString("cat_desc"));
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
				String desc = "" + request.getParameter("desc");
				
				if (orgcode.equals("null"))
					orgcode = "";
				if (code.equals("null"))
					code = "";
				
				if (orgcode.equals(""))
					sql = "SELECT COUNT(*) AS total FROM cat_mstr WHERE cat_code = " 
						+ formatValue(code) + " and cat_group = " + formatValue(group);
				else
					sql = "SELECT COUNT(*) AS total FROM cat_mstr WHERE cat_code = " + formatValue(code) 
					+ " AND cat_code <> " + formatValue(orgcode) + " and cat_group = " + formatValue(group);
				
				if (Integer.valueOf(dbConnection.getFieldValue(sql, "total")) != 0) 
					msg = "exists";
				//记录存在检查完成
				
				if (msg.equals("")) {
					
					if (orgcode.equals(""))
						sql = "REPLACE INTO cat_mstr (cat_code,cat_desc,cat_group) VALUES (" 
							+ formatValue(code) + "," + formatValue(desc) + "," + formatValue(group) + ")";
					else
						sql = "UPDATE cat_mstr SET "
							+"cat_code = " +  formatValue(code) 
							+ ",cat_desc = " + formatValue(desc) 
							+ ",cat_group = " + formatValue(group) 
							+ " WHERE cat_code = " + formatValue(orgcode) 
							+ " AND cat_group = " + formatValue(group);
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
					
				sql = "DELETE FROM cat_mstr WHERE cat_code = " + formatValue(code) 
				+ " AND cat_group = " + formatValue(group);
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
