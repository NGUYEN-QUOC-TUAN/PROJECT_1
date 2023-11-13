package com.techsen.consumable.action;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.service.PrintMaterialServiceI;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午11:14:38
 **/
@Action("printMaterial")
@Results({
	@Result(name="page", location="/report/printMaterial.jsp")
})
public class PrintMaterialAction extends BaseAction {
	
	private static final Log logger = LogFactory.getLog(PrintMaterialAction.class);
	
	@Autowired
	private PrintMaterialServiceI printMaterialService;
	
	
	public String getPage(){
		return "page";
	}
	
	public void getList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String reportStartDate = request.getParameter("startDate");
		String reportEndDate = request.getParameter("endDate");
		
		SqlRowValueList sqlRowValueList = printMaterialService.getList(reportStartDate , reportEndDate);
		super.writeList(sqlRowValueList );
	}
	
	public void output(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String table = request.getParameter("table");
		String filepath = printMaterialService.getFilePath(table);
		try {
			super.reponseBase(filepath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
