package com.techsen.consumable.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月8日 上午10:35:04
 **/
public class WriteFile {

	private static final Log logger = LogFactory.getLog(WriteFile.class);
	
	public static boolean write(String fileName , String content){
		try {

			File file = new File(fileName);
			if (file.exists()){
				file.delete();
			}else {
				File parent = file.getParentFile();
				parent.mkdirs();
			}
			file.createNewFile();
			FileWriter fileWritter = new FileWriter(file.getName());
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(content);
			bufferWritter.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static File getWriteFile(String fileName , String content){
		try {

			File file = new File(fileName);
			if (file.exists()){
				file.delete();
			}else {
				File parent = file.getParentFile();
				parent.mkdirs();
			}
			file.createNewFile();		
			FileWriter fileWritter = new FileWriter(file.getName());
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(content);
			bufferWritter.close();
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
	}
}
