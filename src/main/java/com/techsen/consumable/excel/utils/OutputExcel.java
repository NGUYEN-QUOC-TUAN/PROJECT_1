package com.techsen.consumable.excel.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.techsen.consumable.sql.utils.SqlRowValue;
import com.techsen.consumable.sql.utils.SqlRowValueList;

/**
 * @author fantasy <br>
 *         E-mail: vi2014@qq.com
 * @version 2015年4月8日 上午11:25:08
 **/
public class OutputExcel {

	private static Logger logger = Logger.getLogger(OutputExcel.class);

	private static File getFile(String zipName) {
		if (zipName == null) {
			zipName = "outload";
		}
		File file = new File(zipName);
		if (!file.exists()) {
			file.mkdirs();
		} else {
			zipName = zipName + "1";
			return getFile(zipName);
		}
		logger.debug("导出路径：" + file.getAbsolutePath());
		return file;
	}

	public static File outputFile(SqlRowValueList sqlRowValueList,
			String[] fields, String[] headTitles, String[] groupFields,
			String title) {
		File file = new File(getFile(null).getAbsolutePath() + "/" + title
				+ ".xls");
		FileOutputStream fileOut = null;
		@SuppressWarnings("resource")
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet(title);

		int startRow = 2;
		int startCol = 0;
		if (groupFields != null) {
			ArrayList<int[]> groupList = getGroupList(sqlRowValueList, fields,
					groupFields);
			for (int[] group : groupList) {
				// 在sheet里增加合并单元格
				CellRangeAddress cra1 = new CellRangeAddress(group[0]
						+ startRow, group[1] + startRow, group[2] + startCol,
						group[3] + startCol);
				sheet.addMergedRegion(cra1);
				HSSFRow row = sheet.createRow(group[0]);
				HSSFCell col = row.createCell(group[2]);
				HSSFCellStyle ss = col.getCellStyle();
				ss.setVerticalAlignment(CellStyle.VERTICAL_TOP);
			}
		}
		// 产生表格标题行
		HSSFRow titleRow = sheet.createRow(0);
		Cell titleCell = titleRow.createCell(2);

		titleCell.setCellValue(title);
		CellStyle ss = workBook.createCellStyle();
		HSSFFont font = workBook.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short) 18);// 设置字体大小
		ss.setFont(font);
		//ss.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		// 产生表格表头行
		HSSFRow headerRow = sheet.createRow(1);
		for (short i = 0; i < headTitles.length; i++) {
			@SuppressWarnings("deprecation")
			Cell cell = headerRow.createCell(i);
			HSSFRichTextString text = new HSSFRichTextString(headTitles[i]);
			cell.setCellValue(text);
		}
		int rowIndex = 1;
		for (SqlRowValue rowValue : sqlRowValueList.getSqlRowValues()) {
			rowIndex++;
			HSSFRow row2 = sheet.createRow(rowIndex);
			for (int i = 0; i < fields.length; i++) {
				Cell cell = row2.createCell(i);
				HSSFRichTextString text = new HSSFRichTextString(
						rowValue.getValue(fields[i]));
				cell.setCellValue(text);
			}
		}

		try {
			fileOut = new FileOutputStream(file);
			workBook.write(fileOut);
			if (fileOut != null) {
				fileOut.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static File outputFile(SqlRowValueList sqlRowValueList,
			String[] fields, String[] groupFields, String title) {
		return outputFile(sqlRowValueList, fields, fields, groupFields, title);
	}

	public File outputFile(SqlRowValueList sqlRowValueList,
			String[] groupFields, String title) {
		String[] fields = sqlRowValueList.getSqlRowValues().get(0)
				.getKeyValueString("key").replace("`", "").replace("(", "")
				.replace(")", "").split(",");
		return outputFile(sqlRowValueList, fields, groupFields, title);
	}

	// []
	private static ArrayList<int[]> getGroupList(
			SqlRowValueList sqlRowValueList, String[] fields,
			String[] groupFields) {
		ArrayList<int[]> groupList = new ArrayList<int[]>();
		for (String group : groupFields) {
			int index = 0;
			String value = null;
			int rowIndex = -1;
			for (SqlRowValue rowValue : sqlRowValueList.getSqlRowValues()) {
				if (value == null) {
					value = rowValue.getValue(group);

				}
				// System.out.println("value:"+ value + "===> rowValue:" +
				// rowValue.getValue(group));
				if (rowValue.getValue(group).equals(value)) {
					if (rowIndex == -1) {
						rowIndex = index;
					}

					//System.out.println("相等");
				} else {
					//System.out.println("不相等");
					if (rowIndex != -1) {
						int[] groupValue = new int[4];
						groupValue[0] = rowIndex;
						groupValue[1] = index;
						groupValue[2] = getFieldIndex(fields, group);
						groupValue[3] = getFieldIndex(fields, group);
						groupList.add(groupValue);
					}
					value = rowValue.getValue(group);
					rowIndex = -1;
				}
				if (index == sqlRowValueList.getSqlRowValues().size() - 1) {
					if (rowValue.getValue(group).equals(value)) {
						int[] groupValue = new int[4];
						groupValue[0] = rowIndex;
						groupValue[1] = index;
						groupValue[2] = getFieldIndex(fields, group);
						groupValue[3] = getFieldIndex(fields, group);
						groupList.add(groupValue);
					}
				}
				index++;
			}
		}
		// logger.debug(JSON.toJSONString(groupList));
		return groupList;
	}

	private static int getFieldIndex(String[] fields, String field) {
		int index = 0;
		for (String f : fields) {
			if (f.equals(field)) {
				return index;
			}
			index++;
		}
		return 0;
	}

	public static void main(String[] args) {
		SqlRowValueList rowValueList = new SqlRowValueList();
		for (int i = 1; i < 1000; i++) {
			int num = i;
			if (num % 2 == 0)
				num = num - 1;
			SqlRowValue rowValue = new SqlRowValue();
			rowValue.add("name", num + "name");
			rowValue.add("key", num + "key");
			rowValue.add("age", num + "");
			rowValue.add("cname", num + "fan");
			rowValueList.getSqlRowValues().add(rowValue);
		}
		String[] groupFields = { "name", "cname" };

		new OutputExcel().outputFile(rowValueList, groupFields, "Auxiliary Material Inventory List");
		System.out.println("完成");
	}
}
