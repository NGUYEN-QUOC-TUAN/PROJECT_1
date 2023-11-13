package com.techsen.consumable.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.dao.ApplyStationeryDao;
import com.techsen.consumable.sql.utils.SqlRowValueList;
import com.techsen.consumable.util.WriteFile;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月21日 下午3:59:33
 **/
@Repository("applyStationeryService")
public class ApplyStationeryServiceImpl implements ApplyStationeryServiceI {
	
	@Autowired
	private ApplyStationeryDao deptDetailsDao;

	public SqlRowValueList getList(String reportStartDate, String reportEndDate) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(deptDetailsDao.getList( reportStartDate,
				reportEndDate));
		return rowValueList;
	}

	@Override
	public File getFile(String table) {
		String fileName = "StationeryReport.xls";
		String html = "<html><body> <table cellpadding=\"5\" cellspacing=\"0\" style=\"margin: auto;\"><tr> <td colSpan=\"7\" rowSpan=\"3\"><div align=\"center\" style=\"font-size: 36px;\">Stationery Report</div></td></tr></table>" + table + "</body></html>";
		if(WriteFile.write(fileName , html)){
			return new File(fileName);
		}
		return null;
	}

}
