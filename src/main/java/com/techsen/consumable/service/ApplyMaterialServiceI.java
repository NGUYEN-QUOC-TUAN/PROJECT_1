package com.techsen.consumable.service;

import java.io.File;

import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月10日 上午11:04:32
 **/
public interface ApplyMaterialServiceI {

	public SqlRowValueList getList(String reportStartDate, String reportEndDate,
			String materiel);

	public File getFile(String table);
}
