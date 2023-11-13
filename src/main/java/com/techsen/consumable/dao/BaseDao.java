package com.techsen.consumable.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.techsen.consumable.sql.utils.SqlRowValue;
import com.techsen.consumable.sql.utils.SqlUtils;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月2日 上午10:44:22
 **/
public class BaseDao {

	@Autowired
	private SqlUtils sqlUtils;
	
	protected String table;
	
	public int updateById(SqlRowValue rowValue ,String id , String idValue) throws SQLException{
		return sqlUtils.updateById(rowValue, table, id, idValue);
	}
	
	public int query(SqlRowValue rowValue  , String queryItem) throws SQLException{
		return sqlUtils.query(rowValue, table, queryItem);
	}
	
	public int insert(SqlRowValue rowValue ) throws SQLException {
		return sqlUtils.insert(rowValue, table);
	}
	
	public int delete(SqlRowValue rowValue ) throws SQLException {
		return sqlUtils.delete(rowValue, table);
	}
	
	public List<SqlRowValue> getResult(String sqlString ) throws SQLException {
		return sqlUtils.getResult(sqlString);
	}
	
	public String getResultString(String sqlString , String fieldName) throws SQLException {
		return sqlUtils.getResultString(sqlString ,fieldName);
	}
	
}
