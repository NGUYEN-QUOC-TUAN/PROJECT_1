package com.techsen.consumable.util;

import org.apache.struts2.ServletActionContext;

public class DataVerifyAction {
	private String sqlval;

	public String getSqlval() {
		return sqlval;
	}

	public void setSqlval(String sqlval) {
		this.sqlval = sqlval;
	}
	
	public String execute() throws Exception{
		DBConnection con = new DBConnection();
		String result = "Y"; 
		
		try {
			con.getConnection();
			sqlval = "" + sqlval;
			if (sqlval.toUpperCase().equals("NULL"))
				sqlval = "";
			
			if (!sqlval.equals("")) {
				 if (Integer.valueOf(con.getFieldValue(sqlval, "total")) == 0)
					 result = "N";
			}
		} finally {
			con.closeConn();
			con = null;
		}
		
		ServletActionContext.getResponse().setCharacterEncoding("utf-8");
		ServletActionContext.getResponse().getWriter().print(result);
		return null;	//attend the return value.
	}
	
}
