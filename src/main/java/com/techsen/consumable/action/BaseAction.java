package com.techsen.consumable.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.techsen.consumable.sql.utils.SqlRowValueList;

@ParentPackage("basePackage")
@Scope("prototype")
@Namespace("/")
public class BaseAction {

	public void writePage(SqlRowValueList rowValueList , String total) {
		String rowValueListString;
		if(rowValueList == null){
			rowValueListString = "[{}]";
		}else{
			rowValueListString = rowValueList.toJSONString();
		}
		if(StringUtils.isEmpty(rowValueListString)){
			rowValueListString = "\"\"";
		}
		if(StringUtils.isEmpty(total)){
			total = "\"\"";
		}
		String string = "{\"success\":\"true\",\"total\" :\"" + total + "\" ,\"rows\":"; 
		string = string + rowValueListString + "}";
		writeJson(string);
	}
	
	public void writeList(SqlRowValueList rowValueList) {
		String rowValueListString;
		if(rowValueList == null){
			rowValueListString = "[{}]";
		}else{
			rowValueListString = rowValueList.toJSONString();
		}
		if(StringUtils.isEmpty(rowValueListString)){
			rowValueListString = "\"\"";
		}
		String string = "{\"success\":\"true\",\"rows\":"; 
		string = string + rowValueListString + "}";
		writeJson(string);
	}
	
	public void writeJson(String json) {
		try {
			ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
			ServletActionContext.getResponse().getWriter().write(json);
			ServletActionContext.getResponse().getWriter().flush();
			ServletActionContext.getResponse().getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFileBase(File file ) throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		String fileName = file.getName();
		FileInputStream inputStream = new FileInputStream(file);
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.reset();  
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
		response.addHeader("Content-Length", "" + inputStream.available());
		response.setContentType("application/x-download;charset=UTF-8"); 
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		int len = 0;
		byte[] data = new byte[1024];
		try {
			while (-1 != (len=inputStream.read(data, 0, data.length))) {  
				outputStream.write(data, 0, len);  
			}
		} catch (Exception e) {
			inputStream.close();
			outputStream.flush();
			outputStream.close();
		}
		inputStream.close();
		outputStream.flush();
		outputStream.close();
	}
	
	public void reponseBase(String filePath ) throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("filepath",filePath);
		response.reset(); 
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(JSONArray.toJSONString(map));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void downloadFileDel(File file ) throws IOException{
		downloadFileBase(file);
	    File fp = new File(file.getParent());
	    String[] children = fp.list();
	    //递归删除目录中的子目录下
        for (int i=0; i<children.length; i++) {
            new File(fp, children[i]).delete();
        }
	    fp.delete();
	    file.delete();
	}

}
