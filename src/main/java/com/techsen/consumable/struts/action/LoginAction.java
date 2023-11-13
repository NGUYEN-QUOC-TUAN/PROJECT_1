package com.techsen.consumable.struts.action;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.opensymphony.xwork2.ActionSupport;
import com.techsen.consumable.bean.User;
import com.techsen.consumable.util.DBConnection;


@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
@Results({
	@Result(name="logout", location="/login.html")
})
public class LoginAction extends ActionSupport {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private DBConnection dbConnection;

	@Action("/login")
	public String execute() throws IOException {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = ServletActionContext. getRequest().getSession();
		
		String name = formatVal(request.getParameter("name"));
		String pwd = formatVal(request.getParameter("pwd"));
		String sql,msg = "";
		ResultSet rs;
		
		try {
			dbConnection.getConnection();
			
			sql = "SELECT COUNT(*) AS total FROM usr_mstr WHERE usr_id = '" 
				+ name + "' AND usr_pwd = '" + pwd + "'";
			
			String total = dbConnection.getFieldValue(sql, "total");
			if(total.equals("")){
				total = "" + 0;
				System.out.println(name + "=======" + pwd);
			}
			if (Integer.valueOf(total) == 0) {
				msg = "Invalid username or password";
				return null;
			}
			
			
			sql = "SELECT * from usr_mstr WHERE usr_id = '" 
				+ name + "'";
			
			rs = dbConnection.executeQuery(sql);
			
			User usr = new User();
			
			while (rs.next()) {
				usr.setId(formatVal(rs.getString("usr_id")));
				usr.setGroup(formatVal(rs.getString("usr_group")));				
				usr.setScrgrp(formatVal(rs.getString("usr_scrgrp")));
				usr.setRestrict(formatVal(rs.getString("usr_restrict")));
			}
			
			if (usr.getRestrict().equals("Y")) {
				msg = "User is locked, please contact administrator.";
				return null;
			}
			
			if (usr.getScrgrp().equals("")) {
				msg = "The user does not have any permission, please contact the administrator.";
				return null;
			}
			
			//登入成功后记录Session 记录用户名账号，组，权限等
			session.setAttribute("id", usr.getId());
			session.setAttribute("group", usr.getGroup());
			session.setAttribute("scrgrp", usr.getScrgrp());
			
			msg = "ok";
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			String rtVal = "{success:true,msg:'"+msg +"'}";
			ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		    ServletActionContext.getResponse().getWriter().write(rtVal);
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
			dbConnection.closeConn();
			dbConnection = null;
		}
		
		return null;
	}
	
	@Action("/logout")
	public String logout() {
		HttpSession session = ServletActionContext. getRequest().getSession();
		session.removeAttribute("id");
		session.removeAttribute("group");
		session.removeAttribute("scrgrp");
		return "logout";
	}
	
	public String formatVal(String val) {
		val = "" + val;
		if (val.equals("null"))
			val = "";
		return val;
	}

}
