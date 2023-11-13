package com.techsen.consumable.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.service.DeptCostBillServiceI;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年5月7日 上午11:34:38
 **/
@Action("deptCostBill")
@Results({
	@Result(name="page", location="/report/deptCostBill.jsp")
})
public class DeptCostBillAction extends BaseAction {
	
	@Autowired
	private DeptCostBillServiceI deptCostBillService;
	//private static final Logger logger = Logger.getLogger(DeptCostBillAction.class);

	public String getPage(){
		return "page";
	}
	
	public void getList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String reportStartDate = request.getParameter("startDate");
		String reportEndDate = request.getParameter("endDate");
		SqlRowValueList sqlRowValueList = deptCostBillService.getList(reportStartDate , reportEndDate);
		super.writeList(sqlRowValueList);
	}
	
	public void output(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String table = request.getParameter("table");
		File f = deptCostBillService.getFile(table);
		try {
			super.downloadFileBase(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
