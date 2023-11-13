package com.techsen.consumable.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.dao.DeptCostBillDao;
import com.techsen.consumable.sql.utils.SqlRowValueList;
import com.techsen.consumable.util.WriteFile;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月7日 上午11:40:32
 **/
@Repository("deptCostBillService")
public class DeptCostBillServiceImpl implements DeptCostBillServiceI {

	@Autowired
	DeptCostBillDao deptCostBillDao;
	
	@Override
	public SqlRowValueList getList(String reportStartDate, String reportEndDate) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(deptCostBillDao.getList( reportStartDate,
				reportEndDate));
		return rowValueList;
	}
	
	@Override
	public File getFile(String table) {
		String fileName = "DepartmentAuxiliaryMaterialCostSummary.xls";
		String html = "<html><body> <table cellpadding=\"5\" cellspacing=\"0\" style=\"margin: auto;\"><tr> <td colSpan=\"7\" rowSpan=\"3\"><div align=\"center\" style=\"font-size: 36px;\">Department Auxiliary Material Cost Summary</div></td></tr></table>" + table + "</body></html>";
		if(WriteFile.write(fileName , html)){
			return new File(fileName);
		}
		return null;
	}

}
