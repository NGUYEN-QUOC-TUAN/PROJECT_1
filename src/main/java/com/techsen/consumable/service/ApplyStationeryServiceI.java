package com.techsen.consumable.service;

import java.io.File;

import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月8日 下午2:57:10
 **/
public interface ApplyStationeryServiceI {

	public SqlRowValueList getList(String reportStartDate, String reportEndDate);

	public File getFile(String table);

}
