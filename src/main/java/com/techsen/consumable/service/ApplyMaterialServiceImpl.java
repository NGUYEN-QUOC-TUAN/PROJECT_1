package com.techsen.consumable.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.dao.ApplyMaterialDao;
import com.techsen.consumable.sql.utils.SqlRowValueList;
import com.techsen.consumable.util.WriteFile;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月10日 上午11:25:02
 **/
@Repository("applyMaterialService")
public class ApplyMaterialServiceImpl implements ApplyMaterialServiceI {
	
	@Autowired
	private ApplyMaterialDao applyMaterialDao;

	@Override
	public SqlRowValueList getList(String reportStartDate,
			String reportEndDate, String materiel) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(applyMaterialDao.getList( reportStartDate,
				reportEndDate, materiel));
		return rowValueList;
	}

	@Override
	public File getFile(String table) {
		String fileName = "NewWorkClothesReport.xls";
		String html = "<html><body> <table cellpadding=\"5\" cellspacing=\"0\" style=\"margin: auto;\"><tr> <td colSpan=\"4\" rowSpan=\"2\"><div align=\"center\" style=\"font-size: 20px;\">New work clothes Report</div></td></tr></table>" + table + "</body></html>";
		if(WriteFile.write(fileName , html)){
			return new File(fileName);
		}
		return null;
	}
}
