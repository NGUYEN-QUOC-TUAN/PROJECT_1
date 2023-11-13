package com.techsen.consumable.service;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.dao.PrintMaterialDao;
import com.techsen.consumable.sql.utils.SqlRowValueList;
import com.techsen.consumable.util.WriteFile;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月8日 下午4:27:34
 **/
@Repository("printMaterialService")
public class PrintMaterialServiceImpl implements PrintMaterialServiceI {

	private static final Log logger = LogFactory.getLog(PrintMaterialServiceImpl.class);
	
	@Autowired
	private PrintMaterialDao printMaterialDao;

	public SqlRowValueList getList(String reportStartDate, String reportEndDate) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(printMaterialDao.getList( reportStartDate,
				reportEndDate));
		return rowValueList;
	}

	@Override
	public File getFile(String table) {
		String fileName = "PrintConsumablesReport.xls";
		try{
			InputStream ism = Thread.currentThread().
			getContextClassLoader().getResourceAsStream("/consumable.properties");
			Properties prop = new Properties();
			prop.load(ism);
			String exportPath = prop.getProperty("export.excel.file.path");
			fileName = exportPath + fileName;
			String html = "<!DOCTYPE html><html><body> <table cellpadding=\"5\" cellspacing=\"0\" style=\"margin: auto;\"><tr> <td colSpan=\"7\" rowSpan=\"3\"><div align=\"center\" style=\"font-size: 36px;\">Print Consumables Report</div></td></tr></table>" + table + "</body></html>";
			return WriteFile.getWriteFile(fileName, html);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
		//		if(WriteFile.write(fileName , html)){
//			//File file = null;
//			try{
//				return new File(fileName);
//			}catch (Exception e) {
//				logger.error("File Generate Error => ", e);
//			}
//		}
//		return null;
	}
	
	@Override
	public String getFilePath(String table) {
		String fileName = "PrintConsumablesReport.xls";
		try{
			InputStream ism = Thread.currentThread().
			getContextClassLoader().getResourceAsStream("/consumable.properties");
			Properties prop = new Properties();
			prop.load(ism);
			String exportPath = prop.getProperty("export.excel.file.path");
			fileName = exportPath + fileName;
			String html = "<!DOCTYPE html><html><body> <table cellpadding=\"5\" cellspacing=\"0\" style=\"margin: auto;\"><tr> <td colSpan=\"7\" rowSpan=\"3\"><div align=\"center\" style=\"font-size: 36px;\">Print Consumables Report</div></td></tr></table>" + table + "</body></html>";
			WriteFile.getWriteFile(fileName, html);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		fileName = fileName.replace("/mnt/vnad_Apps/", "/vnad/Apps/");
		return fileName;
	}
}
