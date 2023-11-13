package com.techsen.consumable.service;

import java.io.File;

import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月21日 下午3:59:33
 **/
public interface DeptDetailsServiceI {

	public SqlRowValueList getList(String reportStartDate, String reportEndDate);

	public File getFile(String table);

}
