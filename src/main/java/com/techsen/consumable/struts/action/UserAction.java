package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.ActionSupport;
import com.techsen.consumable.util.DBConnection;
@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class UserAction extends ActionSupport {
	
	@Autowired
	private DBConnection dbConnection;

	private static final long serialVersionUID = 1L;

	@Action("/user")
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
			if (sub.equals("Find")||sub.equals("First")||sub.equals("Prev") 
					||sub.equals("Next")||sub.equals("Last")||sub.equals("Init")
					||sub.equals("Cancel")||sub.equals("Reset")) {
				
				
				String fld1 = formatVal(request.getParameter("fld1"));
				String fld2 = formatVal(request.getParameter("fld2"));
				String cdt1 = formatVal(request.getParameter("cdt1"));
				String cdt2 = formatVal(request.getParameter("cdt2"));
				String val1 = formatVal(request.getParameter("val1"));
				String val2 = formatVal(request.getParameter("val2"));
				String record = formatVal(request.getParameter("record")); 
				
				String total;
				
				String subSql = "";
				
				if (!val1.equals(""))
					subSql = subSql + " AND " + createSQL(fld1,cdt1,val1);
				
				if (!val2.equals(""))
					subSql = subSql + " AND " + createSQL(fld2,cdt2,val2);
				
				subSql = subSql + " ORDER BY usr_id";
				
				sql = "SELECT COUNT(*) AS total FROM usr_mstr WHERE 1 = 1 " + subSql;
				
				total = dbConnection.getFieldValue(sql, "total");
				
				if (record.equals(""))
					record = "0";
				
				if (sub.equals("First"))
					record = "1";
				if (sub.equals("Prev"))
					record = String.valueOf(Integer.valueOf(record) - 1);
				if (sub.equals("Next"))
					record = String.valueOf(Integer.valueOf(record) + 1);
				if (sub.equals("Last"))
					record = total;
				if (sub.equals("Reset"))
					record = "0";
				
				if (Integer.valueOf(record) <= 0)
					if (Integer.valueOf(total) > 0) 
						record = "1";
				
				if (Integer.valueOf(record) > Integer.valueOf(total))
					record = total;
				
				String current = String.valueOf(Integer.valueOf(record) - 1);
				
				if (Integer.valueOf(current) < 0)
					current = "0";
				
				sql = "SELECT * FROM usr_mstr WHERE 1 = 1 " + subSql + " limit " + current + ",1";
				
				rs = dbConnection.executeQuery(sql);
				
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", rs.getString("usr_id"));
					jsonObject.put("name", rs.getString("usr_name"));
					jsonObject.put("pwd", rs.getString("usr_pwd")); 
					jsonObject.put("group", rs.getString("usr_group"));
					jsonObject.put("record", record);
					jsonObject.put("total", total);
					array.add(jsonObject);
				}
				
				//如果没有查到
				if (array.size() == 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", "");
					jsonObject.put("name", "");
					jsonObject.put("pwd", ""); 
					jsonObject.put("group", "");
					array.add(jsonObject);
				}
				
				json.put("users", array);
				
				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
				
			} else if (sub.equals("Save")) {
				String orgid = formatVal(request.getParameter("orgid"));
				String id = formatVal(request.getParameter("id"));
				String name = formatVal(request.getParameter("name"));
				String pwd = formatVal(request.getParameter("pwd"));
				String group = formatVal(request.getParameter("group"));
				
				if (orgid.equals(""))
					sql = "SELECT COUNT(*) AS total FROM usr_mstr " 
					+ " WHERE usr_id = " + formatValue(id);
				else
					sql = "SELECT COUNT(*) AS total FROM usr_mstr " 
					+ " WHERE usr_id = " + formatValue(id) 
					+ " and usr_id <> " + formatValue(orgid);
				
				if (Integer.valueOf(dbConnection.getFieldValue(sql, "total")) != 0) 
					msg = "exists";
				
				
				if (msg.equals("")) {
					if (orgid.equals(""))
						sql = "INSERT INTO usr_mstr (usr_id,usr_name,usr_pwd,usr_group) "
							+" VALUES (" + formatValue(id) + "," + formatValue(name) + "," 
							+ formatValue(pwd) + "," + formatValue(group) + ")";
					else
						sql = "UPDATE usr_mstr SET usr_id = " + formatValue(id) + ",usr_name = " + formatValue(name) 
						+ ",usr_pwd = " + formatValue(pwd) + ",usr_group = " + formatValue(group) 
						+" WHERE usr_id = " + formatValue(orgid);
					
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
				}
				
				ServletActionContext.getResponse().getWriter().print(msg);
				
			} else if (sub.equals("Delete")) {
				String id = formatVal(request.getParameter("id"));
				
				if (!id.equals("")) {
					sql = "DELETE FROM usr_mstr WHERE usr_id = " + formatValue(id);
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
					
					ServletActionContext.getResponse().getWriter().print(msg);
				}
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
	
	public String createSQL(String field,String condition,String value) {
		if (condition.toUpperCase().trim().equals("LIKE"))
			return field + " " + " " + condition.toLowerCase() + " '%" + value + "%'";
		else 
			return field + " " + " " + condition.toLowerCase() + " '" + value + "'";
	}
	
	public String formatVal(String val) {
		val = "" + val;
		if (val.equals("null"))
			val = "";
		return val;
	}

}
