package com.techsen.consumable.service;

import java.io.File;

import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月8日 下午4:16:04
 **/
public interface PrintMaterialServiceI {

	public SqlRowValueList getList(String reportStartDate, String reportEndDate);

	public File getFile(String table);
	
	public String getFilePath(String table);
}
