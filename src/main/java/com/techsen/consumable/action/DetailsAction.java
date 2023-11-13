package com.techsen.consumable.action;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.excel.utils.OutputExcel;
import com.techsen.consumable.service.DetailsServiceI;
import com.techsen.consumable.sql.utils.SqlRowValue;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午11:14:38
 **/
@Action("details")
@Results({
	@Result(name="page", location="/report/details.jsp")
})
public class DetailsAction extends BaseAction {
	
	@Autowired
	private DetailsServiceI detailsService;
	private SqlRowValue rowValue = new SqlRowValue();
	private static final Logger logger = Logger.getLogger(DetailsAction.class);

	@SuppressWarnings("unchecked")
	public void test() {
		HttpServletRequest request = ServletActionContext.getRequest();
		rowValue.setHashMap((HashMap<String, Object>) request.getParameterMap());
		super.writeJson(rowValue.toJSONString());
	}
	
	public String getPage(){
		return "page";
	}
	
	@SuppressWarnings("unchecked")
	public void getList(){
		HttpServletRequest request = ServletActionContext.getRequest();
		rowValue.setHashMap((HashMap<String, Object>) request.getParameterMap());
		String reportDate = rowValue.getValue("reportDate").replace("T00:00:00", "");
		String materiel = request.getParameter("materiel");
		if(materiel.length() < 2){
			materiel = "0" + materiel;
		}
		
		SqlRowValueList sqlRowValueList = detailsService.getList(reportDate , materiel , rowValue.getValue("limit") , rowValue.getValue("start"));
		super.writePage(sqlRowValueList , detailsService.getTotal(reportDate , materiel));
	}
	
	@SuppressWarnings("unchecked")
	public void output(){
		HttpServletRequest request = ServletActionContext.getRequest();
		rowValue.setHashMap((HashMap<String, Object>) request.getParameterMap());
		String reportDate = request.getParameter("reportDate");
		logger.debug(reportDate + "==============================>");
		//String reportDate = rowValue.getValue("reportDate").replace("T00:00:00", "");
		String materiel = request.getParameter("materiel");
		if(materiel.length() < 2){
			materiel = "0" + materiel;
		}
		SqlRowValueList sqlRowValueList = detailsService.getAllList(reportDate , materiel);
		String[] fields = {"tx_item" , "item_desc" , "item_std" , "item_cost" , "item_um" , "sum"};
		String[] headTitles = {"Item No" , "Description" , "Standard" , "Unit cost" , "um" , "Bal Qty"};
		File f = OutputExcel.outputFile(sqlRowValueList, fields, headTitles ,null ,"物料库存");
		try {
			super.downloadFileDel(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}