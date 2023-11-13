package com.techsen.consumable.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.dao.DetailsDao;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月2日 下午5:09:07
 **/
@Repository("detailsService")
public class DetailsServiceImpl implements DetailsServiceI {
	
	@Autowired
	private DetailsDao detailsDao;

	@Override
	public SqlRowValueList getList(String reportDate, String materiel, String limit, String start) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(detailsDao.getList( reportDate,  materiel, limit, start));
		return rowValueList;
	}

	@Override
	public String getTotal(String reportDate, String materiel) {
		return detailsDao.getTotal( reportDate,  materiel);
	}

	@Override
	public SqlRowValueList getAllList(String reportDate, String materiel) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		rowValueList.setSqlRowValues(detailsDao.getAllList( reportDate,  materiel));
		return rowValueList;
	}

}
