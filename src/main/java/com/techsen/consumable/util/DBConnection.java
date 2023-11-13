package com.techsen.consumable.util;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.techsen.consumable.bean.KeyValue;

@Repository("dbConnection")
@Scope("prototype")
public class DBConnection {
	Connection con = null;
	Statement stmt = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	@Autowired
	private DataSource dataSource;
	
	private Logger logger = Logger.getLogger(DBConnection.class);
	
	//字符串加、解密
	public String Encrypt(String str) throws UnsupportedEncodingException {
		str = str.trim();
		if (str.length() == 0)
			return "";
		
		byte byStr[] = str.getBytes("iso8859-1");
		//ASCII 代表 M
		byte keyStr = 77; 

		String result = "";
		for (int i = 0; i <  byStr.length;i++)  {
			result = result + (char)(byStr[i] ^ (i+1) ^ keyStr)  ;
		}
		return result;
	}
	
	public String getConnection() {
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}
	
	// 查询数据库
	public ResultSet executeQuery(String sql) {
		logger.debug("查询数据库：" + sql);
		try {
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					                   ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	//查询数据库返回数据集 错误返回上一级
	public ResultSet executeResult(String sql) throws SQLException {
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					                   ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery(sql);
		return rs;
	}
	
	//更新插入数据
	public boolean executeUpdate(String sql) {
		logger.debug("更新插入数据库：" + sql);
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	//更新插入数据
	public String executeUpdate1(String sql) {
		logger.debug("更新插入数据库：" + sql);
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		return "";
	}
	
	//调用输入存储过程
	public boolean callProcIn(String procName) {
		logger.debug("调用输入存储过程：" + procName);
		CallableStatement cs = null;
		try {
			cs = con.prepareCall(procName);
			cs.executeQuery();
		} catch (Exception e) {
		    e.printStackTrace();
		    return false;
		} finally {
			 try {
				 if (cs != null)
					 cs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
	    }
		
		return true;
		
	}
	
	
	//统计记录数
	public int executeCount(String sql) {
		logger.debug("统计记录数：" + sql);
		int num = 0;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next())
				num = rs.getInt(1);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	//執行SQL查詢返回字段值的 （查詢）
	public String getSQLRValue(String sql,String fieldname) {
		try {
			String result = null;
			rs = executeQuery(sql);
			rs.first();
			result = rs.getString(fieldname);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
	}
	
	public String getFieldValue(String sql,String fieldname) {
		try {
			String result = "";
			rs = executeQuery(sql);
			
			if (rs != null) {
				rs.first();
				result = "" + rs.getString(fieldname);
			}
			
			if (result.equals("null"))
				result = "";
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";			
		}
	}
	
	public String getSumFieldValue(String sql,String fieldname) {
		try {
			String result = "";
			rs = executeQuery(sql);
			
			while (rs.next()) {
				result = "" + rs.getString(fieldname);
			}
			
			if (result.equals("null"))
				result = "";
			
			if (result.equals("null"))
				result = "0";
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";			
		}
	}
	
	//执行SQL查询，返回字段值
	public Vector<String> getValues(String sql,String fieldname) {
		try {
			Vector<String> result = new Vector<String>();
			rs = executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					result.add(rs.getString(fieldname));
				}				
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
	}
	
	//执行SQL查询，返回字段值
	public Vector<String> getValues(String sql,String fieldname,String firstValue) {
		try {
			Vector<String> result = new Vector<String>();
			result.add(firstValue);
			rs = executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					result.add(rs.getString(fieldname));
				}				
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
	}
	
	public Vector<KeyValue> getKeyValues(String sql,String keyField,String valueField) {
		try {
			Vector<KeyValue> result = new Vector<KeyValue>();
			rs = executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					KeyValue keyvalue = new KeyValue(rs.getString(keyField),rs.getString(valueField)); 
					result.add(keyvalue);
				}				
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	public Vector<KeyValue> getKeyValues(String sql,String keyField,String valueField,String firstValue) {
		try {
			Vector<KeyValue> result = new Vector<KeyValue>();
			KeyValue keyvalue1 = new KeyValue(firstValue,firstValue);
			result.add(keyvalue1);
			rs = executeQuery(sql);
			if (rs != null) {
				while (rs.next()) {
					KeyValue keyvalue = new KeyValue(rs.getString(keyField),rs.getString(valueField)); 
					result.add(keyvalue);
				}				
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
		
	}
	
	//关闭数据库连接并释放资源
	public void closeConn() {
		logger.debug("关闭数据库");
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con = null;
		}
	}
	
}
