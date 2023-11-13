package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.util.Vector;

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
public class TreeAction {
	
	@Autowired
	private DBConnection dbConnection;
	
	@Action("tree")
	public String execute() throws Exception{
		
		HttpSession session = ServletActionContext. getRequest().getSession();
		
		String right = "" + (String) session.getAttribute("scrgrp");
		
		if (right.equals("null"))
			right = "";
		
		ResultSet rs;
		String maxLevel;
		
		JSONObject root = new JSONObject();
		
		JSONArray rootArray = new JSONArray();
		
		try {
			dbConnection.getConnection();
			String sql;
			
			sql = "{CALL SP_Menu('" + right + "')}";
			if (dbConnection.callProcIn(sql) == false) {
				return null;
			}
			
			sql = "SELECT max(menu_lvl) AS menu_lvl FROM menu_tmp";
			maxLevel = dbConnection.getFieldValue(sql, "menu_lvl");
			
			JSONArray sLvlArray = new JSONArray();
			
			sql = "SELECT menu_pid FROM menu_tmp WHERE menu_lvl = '" 
				+ maxLevel + "' GROUP BY menu_pid ORDER BY menu_pid";
			
			
			Vector<String> parent =  dbConnection.getValues(sql, "menu_pid");
			
			for (int j = 0;j < parent.size();j++) {
				
				String sLvlParText = "";
				JSONArray sLvlParArray = new JSONArray();
				
				sql = "SELECT * FROM menu_tmp WHERE menu_lvl = '" 
					+ maxLevel + "' AND menu_pid = '" 
					+ parent.elementAt(j)  + "' ORDER BY menu_cid";
				
				rs = dbConnection.executeQuery(sql);
				
				while (rs.next()) {	
					sLvlParArray.add(children(rs.getString("menu_cdesc"),rs.getString("menu_curl"),null));
					sLvlParText =  rs.getString("menu_pdesc");
				}
				
				JSONObject sLvlParJson = new JSONObject();
				
				sLvlParJson.put("text", sLvlParText);
				sLvlParJson.put("leaf", false);
				sLvlParJson.put("expanded", true);
				sLvlParJson.put("children", sLvlParArray);
				
				sLvlArray.add(sLvlParJson);
			}
			
			root = parent("Auxiliary Material Management",true,sLvlArray);
			
			rootArray.add(root);
			
	/*		compStr = "[{" 
				+ "'text':'辅料系统','expanded':true,'children':[{" +
						"'text':'基本信息','leaf':false," +
						"'children':[{" +
						"'text':'类型维护'," +
						"'leaf':true," +
						"'href':'itemmt.html'},{" +
						"'text':'供应商维护','leaf':true},{" +
						"'text':'部门维护','leaf':true},{" +
						"'text':'物料维护','leaf':true}]" +
						"},{" +
						"'text':'库存维护','leaf':false,'children':[{'text':'物料出入库'," +
						"'leaf':true," +
						"'href':'itemmt.html'}]" +
						"},{'text':'系统管理','leaf':false,'children':[{'text':'用户维护','leaf':true" +
						"},{'text':'权限维护','leaf':true" +
						"}]" +
						"}]" +
						"}]";    */
			
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
			ServletActionContext.getResponse().getWriter().print(rootArray.toString());
				
		//	ServletActionContext.getResponse().getWriter().print(compStr);
		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbConnection.closeConn();
			dbConnection = null;
		}
		
		return null;
		
	}
	
	private JSONObject children(String text,String href,JSONObject json) {
		JSONObject jsonObject = new JSONObject();
		boolean leaf = true;
		if (href.equals("+"))
			leaf = false;
		
		jsonObject.put("text", text);
		jsonObject.put("leaf", leaf);
		if (leaf == true) {
			jsonObject.put("id", href);
		} else {
			jsonObject.put("children", json);
		}
		
		return jsonObject;
	}
	
	private JSONObject parent(String text,boolean expanded,JSONArray childArray) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("text", text);
		jsonObject.put("expanded", expanded);
		jsonObject.put("children", childArray);
		return jsonObject;
	}
	
}
