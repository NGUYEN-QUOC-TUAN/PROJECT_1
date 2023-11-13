package com.techsen.consumable.service;

import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月2日 下午5:01:19
 **/
public interface DetailsServiceI {

	public SqlRowValueList getList(String reportDate, String materiel, String limit, String start);

	public String getTotal(String reportDate, String materiel);

	public SqlRowValueList getAllList(String reportDate, String materiel);
}
