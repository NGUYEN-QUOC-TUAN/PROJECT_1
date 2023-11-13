package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.techsen.consumable.util.DBConnection;
@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class SecAction {
	
	@Autowired
	private DBConnection dbConnection;
	
	@Action("sec")
	public String execute() throws Exception{
		
		HttpServletRequest request = ServletActionContext.getRequest();
		String sub = formatVal(request.getParameter("sub"));
		ResultSet rs;
		
		String msg = "";
		
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		try {
			dbConnection.getConnection();
			String sql;
			if (sub.equals("Init")) {
				//获取用户
				String usrid = formatVal(request.getParameter("usrid"));
				
				if (usrid.equals("")) {
					sql = "SELECT usr_id FROM usr_mstr ORDER BY usr_id LIMIT 0,1";
					usrid = formatVal(dbConnection.getFieldValue(sql, "usr_id"));
				}
				
				ServletActionContext.getResponse().getWriter().print(usrid);
				
			} else if (sub.equals("Change")) {
				String usrid = formatVal(request.getParameter("usrid"));
				
				if (!usrid.equals("")) {
					sql = "SELECT usr_scrgrp FROM usr_mstr "
					+" WHERE usr_id = " + formatValue(usrid);
					String right = formatVal(dbConnection.getFieldValue(sql, "usr_scrgrp"));
					List<String> rights = new Vector<String>();
					
					String[] arrayStr =new String[]{};
					if (!right.equals("")) {
						arrayStr = right.split(",");
						rights = java.util.Arrays.asList(arrayStr);
					}
					
					
					sql = "SELECT sec_id FROM sec_mstr WHERE sec_url <> '+' ORDER BY sec_id";
					List<String> secs = dbConnection.getValues(sql, "sec_id");
					
					List<String> rightLists = new Vector<String>();
					rightLists.add("catmt");
					rightLists.add("vendmt");
					rightLists.add("deptmt");
					rightLists.add("itemmt");
					rightLists.add("stkmt");
					rightLists.add("details");
					rightLists.add("applyMaterial");
					rightLists.add("deptDetails");
					rightLists.add("deptCostBill");
					rightLists.add("printMaterial");
					rightLists.add("applyStationery");
					rightLists.add("usrmt");
					rightLists.add("secmt");
					
					
					JSONObject jsonObject = new JSONObject();
					
					if (rightLists.size() == secs.size()) {
						for (int i = 0;i < rights.size(); i++) {	
							jsonObject.put(rightLists.get(secs.indexOf(rights.get(i))),true);
						}
					}
					
					array.add(jsonObject);
					
					json.put("securitys", array);
					
				}
				
				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
				
				
				
			} else if (sub.equals("Save")) {
				List<Boolean> rights = new Vector<Boolean>();
				List<String> secs;
				String usrid = formatVal(request.getParameter("usrid"));
				
				rights.add(StrToBool(formatVal(request.getParameter("catmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("vendmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("deptmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("itemmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("stkmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("usrmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("secmt"))));
				rights.add(StrToBool(formatVal(request.getParameter("details"))));
				rights.add(StrToBool(formatVal(request.getParameter("applyMaterial"))));
				rights.add(StrToBool(formatVal(request.getParameter("deptDetails"))));
				rights.add(StrToBool(formatVal(request.getParameter("deptCostBill"))));
				rights.add(StrToBool(formatVal(request.getParameter("printMaterial"))));
				rights.add(StrToBool(formatVal(request.getParameter("applyStationery"))));
				
				sql = "SELECT sec_id FROM sec_mstr WHERE sec_url <> '+' ORDER BY sec_id";
				secs = dbConnection.getValues(sql, "sec_id");
				
				String right = "";
				
				if (rights.size() == secs.size()) {
					for (int i = 0;i < rights.size();i++) {
						if (rights.get(i) == true) {
							if (right.equals(""))
								right = secs.get(i);
							else
								right = right + "," +  secs.get(i);
						}
					}
				}	
				
				if (msg.equals("")) {
					sql = "UPDATE usr_mstr SET usr_scrgrp = " + formatValue(right)
						+" WHERE usr_id = " + formatValue(usrid);
					
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
				}
				
				ServletActionContext.getResponse().getWriter().print(msg);	
			}  else if (sub.equals("bindUser")) {
				sql = "SELECT usr_id FROM usr_mstr ORDER BY usr_id";
				
				rs = dbConnection.executeQuery(sql);
				
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("usr_id"));
					jsonObject.put("desc", rs.getString("usr_id"));
				
					array.add(jsonObject);
				}
				
				json.put("users", array);
				
				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbConnection.closeConn();
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
    
    public Boolean StrToBool(String val) {
    	if (val.toUpperCase().equals("TRUE"))
    		return true;
    	else
    		return false;
    }
	
	public String formatVal(String val) {
		val = "" + val;
		if (val.equals("null"))
			val = "";
		return val;
	}

}
