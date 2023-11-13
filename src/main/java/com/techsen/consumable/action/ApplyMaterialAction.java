package com.techsen.consumable.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.service.ApplyMaterialServiceI;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午11:14:38
 **/
@Action("applyMaterial")
@Results({
	@Result(name="page", location="/report/applyMaterial.jsp")
})
public class ApplyMaterialAction extends BaseAction {
	
	@Autowired
	private ApplyMaterialServiceI applyMaterialService;
	public String getPage(){
		return "page";
	}
	
	public void getList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String reportStartDate = request.getParameter("startDate");
		String reportEndDate = request.getParameter("endDate");
		String materiel = request.getParameter("materiel");
		if(materiel.length() < 1 ){
			materiel = "0" + materiel;
		}
		Cookie myCookie = new Cookie("group","tx_curr,cat_desc");
		ServletActionContext.getResponse().addCookie(myCookie);
		
		SqlRowValueList sqlRowValueList = applyMaterialService.getList(reportStartDate , reportEndDate, materiel);
		super.writeList(sqlRowValueList );
	}
	
	public void output(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String table = request.getParameter("table");
		File f = applyMaterialService.getFile(table);
		try {
			super.downloadFileBase(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
