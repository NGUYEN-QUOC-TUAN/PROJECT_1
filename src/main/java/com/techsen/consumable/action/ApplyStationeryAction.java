package com.techsen.consumable.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.service.ApplyStationeryServiceI;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午11:14:38
 **/
@Action("applyStationery")
@Results({
	@Result(name="page", location="/report/applyStationery.jsp")
})
public class ApplyStationeryAction extends BaseAction {
	
	@Autowired
	private ApplyStationeryServiceI applyStationeryService;
	public String getPage(){
		return "page";
	}
	
	public void getList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String reportStartDate = request.getParameter("startDate");
		String reportEndDate = request.getParameter("endDate");
		
		SqlRowValueList sqlRowValueList = applyStationeryService.getList(reportStartDate , reportEndDate);
		super.writeList(sqlRowValueList );
	}
	
	public void output(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String table = request.getParameter("table");
		File f = applyStationeryService.getFile(table);
		try {
			super.downloadFileBase(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
