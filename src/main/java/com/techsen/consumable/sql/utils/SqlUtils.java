package com.techsen.consumable.sql.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.util.FormatUtils;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月1日 下午3:01:36
 **/
@Repository("sqlUtils")
@Scope("prototype")
public class SqlUtils {

	@Autowired
	private DataSource dataSource;
	
	private Logger logger = Logger.getLogger(SqlUtils.class);

	public SqlUtils() {
		//conn = dataSource.getConnection();
	}
	
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
	public int query(SqlRowValue rowValue , String table , String queryItem) throws SQLException {
		QueryString queryString = new QueryString(QueryString.EXECUTE_TYPE_QUERY , table);
		queryString.setQueryString("where " + rowValue.toQueryString());
		queryString.setQueryItem(queryItem);
		return executeSql(queryString.getExecuteString());
	}
	
	public int insert(SqlRowValue rowValue , String table) throws SQLException {
		QueryString queryString = new QueryString(QueryString.EXECUTE_TYPE_INSERT , table);
		queryString.setQueryString(rowValue.toInsertString());
		return executeSql(queryString.getExecuteString());
	}
	
	public int updateById(SqlRowValue rowValue , String table ,String id , String idValue) throws SQLException {
		QueryString queryString = new QueryString(QueryString.EXECUTE_TYPE_UPDATE , table);
		queryString.setQueryString(rowValue.toUpdateString() + "where `" + id + "` = '" + idValue + "'");
		return executeSql(queryString.getExecuteString());
	}
	
	public int delete(SqlRowValue rowValue , String table) throws SQLException {
		QueryString queryString = new QueryString(QueryString.EXECUTE_TYPE_QUERY , table);
		queryString.setQueryString("where " + rowValue.toQueryString());
		return executeSql(queryString.getExecuteString());
	}
	
	public int executeSql(String sqlString) throws SQLException{
		Connection conn = dataSource.getConnection();
		Statement stmt ;
		stmt = conn.createStatement();
		logger.debug("执行数据库语句：" + sqlString);
		return stmt.executeUpdate(sqlString);
	}
	
	public List<SqlRowValue> getResult(String sqlString) throws SQLException{
		Connection conn = dataSource.getConnection();
		Statement statement = conn.createStatement();
		logger.debug("查询数据库:" + sqlString);
		ResultSet rs = statement.executeQuery(sqlString);
		List<SqlRowValue> rowValues = result2SqlRowValue(rs);
		colseSql(conn, statement, rs);
		return rowValues;
	}
	
	public String getResultString(String sqlString , String fieldName) throws SQLException {
		Connection conn = dataSource.getConnection();
		Statement statement = conn.createStatement();
		logger.debug("查询数据库:" + sqlString);
		ResultSet rs = statement.executeQuery(sqlString);
		List<SqlRowValue> rowValues = result2SqlRowValue(rs);
		colseSql(conn, statement, rs);
		if(rowValues.size() > 0){
			return rowValues.get(0).getValue(fieldName);
		}else{
			return "";
		}
	}
	
	private List<SqlRowValue> result2SqlRowValue(ResultSet rs) throws SQLException{
		List<SqlRowValue> rowValues = new ArrayList<SqlRowValue>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int colcount = rsmd.getColumnCount();//取得全部列数
		while (rs.next()) {
			SqlRowValue rowValue = new SqlRowValue();
			for(int i=1;i<=colcount;i++){
				String elemString = object2String(rs.getObject(i));
				String colname = rsmd.getColumnName(i);
				rowValue.add(colname , elemString );
			}
			rowValues.add(rowValue);
		}
		return rowValues;
	}
	
	private String object2String(Object o){
		String string = "";
		if(o!=null){
			if(o.getClass().getName().equals("java.lang.Long")){
				string = String.valueOf((java.lang.Long)o);
			}
			if(o.getClass().getName().equals("java.lang.String")){
				string = (String) o;
			}
			if(o.getClass().getName().equals("java.sql.Date")){
				string = FormatUtils.date2String((Date)o, FormatUtils.DATE_TYPE);
			}
			if(o.getClass().getName().equals("java.sql.Timestamp")){
				string = FormatUtils.date2String(new Date(((Timestamp)o).getTime()), FormatUtils.DATETIME_TYPE);
			}
			if(o.getClass().getName().equals("java.math.BigDecimal")){
				string = String.valueOf((java.math.BigDecimal)o);
			}
			if(o.getClass().getName().equals("java.lang.Double")){
				string = String.valueOf((java.lang.Double)o);
				if(string.endsWith(".0")){
					string = string.replace(".0", "");
				}
			}
			if(o.getClass().getName().equals("java.lang.Float")){
				string = String.valueOf((java.lang.Float)o);
			}
			if(o.getClass().getName().equals("java.lang.Integer")){
				string = String.valueOf((java.lang.Integer)o);
			}
			if(o.getClass().getName().equals("java.sql.Time")){
				string = FormatUtils.date2String((Date)o, FormatUtils.TIME_TYPE);
			}
		}else{
			return "";
		}
		return string;
	}
	
	public void colseSql(Connection connection , Statement statement , ResultSet resultSet ) throws SQLException{
		logger.debug("关闭数据库");
		if(resultSet != null){
			resultSet.close();
		}
		if(statement != null){
			statement.close();
		}
		if(connection != null){
			connection.close();
		}
	}
}
