package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.itextpdf.text.List;
import com.techsen.consumable.util.DBConnection;

@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class StockMTAction {

	@Autowired
	private DBConnection dbConnection;

	@Action("stock")
	public String execute() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		String sub = formatVal(request.getParameter("sub"));
		ResultSet rs;

		String msg = "";

		HttpSession session = ServletActionContext.getRequest().getSession();
		String group = "" + (String) session.getAttribute("group");
		if (group.equals("null"))
			group = "";

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			dbConnection.getConnection();
			String sql;
			if (sub.equals("") || sub.equals("recount")) {

				String fld1 = formatVal(request.getParameter("fld1"));
				String fld2 = formatVal(request.getParameter("fld2"));
				String cdt1 = formatVal(request.getParameter("cdt1"));
				String cdt2 = formatVal(request.getParameter("cdt2"));
				String val1 = formatVal(request.getParameter("val1"));
				String val2 = formatVal(request.getParameter("val2"));
				String start = formatVal(request.getParameter("start"));
				String limit = formatVal(request.getParameter("limit"));

				String total;

				String subSql = "";

				if (!val1.equals(""))
					subSql = subSql + " AND " + createSQL(fld1, cdt1, val1);

				if (!val2.equals(""))
					subSql = subSql + " AND " + createSQL(fld2, cdt2, val2);

				if (sub.equals("")) {
					subSql = subSql + " ORDER BY tx_date desc,tx_dept";

					sql = "SELECT COUNT(*) AS total FROM tx_det WHERE tx_group = '" + group + "'" + subSql;

					total = dbConnection.getFieldValue(sql, "total");

					if (start.equals(""))
						start = "0";
					if (limit.equals(""))
						limit = "25";

					sql = "SELECT * FROM tx_det LEFT JOIN item_mstr ON tx_item = item_num AND tx_vendor = item_vendor"
							+ " AND tx_group = item_group WHERE tx_group = '" + group + "'" + subSql + " limit " + start
							+ "," + limit;

					rs = dbConnection.executeQuery(sql);

					while (rs.next()) {
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("lot", rs.getString("tx_lot"));
						jsonObject.put("dept", rs.getString("tx_dept"));
						jsonObject.put("date", rs.getString("tx_date"));
						jsonObject.put("nbr", rs.getString("tx_item"));
						jsonObject.put("desc", rs.getString("item_desc"));
						jsonObject.put("qty", rs.getString("tx_qty"));
						jsonObject.put("current", rs.getString("tx_curr"));
						jsonObject.put("cost", rs.getString("tx_cost"));
						jsonObject.put("rmk", rs.getString("tx_rmk"));
						jsonObject.put("um", rs.getString("tx_um"));
						jsonObject.put("expiry_date", rs.getString("tx_expiry_date"));
						jsonObject.put("po", rs.getString("tx_po"));
						jsonObject.put("vendor", rs.getString("tx_vendor"));
						jsonObject.put("vendor_lot", rs.getString("tx_vendor_lot"));
						jsonObject.put("label_printed", rs.getString("tx_label_printed"));
						array.add(jsonObject);
					}

					json.put("total", total);
					json.put("stocks", array);

					ServletActionContext.getResponse().setCharacterEncoding("utf-8");
					ServletActionContext.getResponse().getWriter().print(json.toString());
				} else if (sub.equals("recount")) {
					String totalQty;

					sql = "SELECT ROUND(sum(tx_qty),2) AS total FROM tx_det WHERE tx_group = '" + group + "'" + subSql;

					totalQty = dbConnection.getFieldValue(sql, "total");

					ServletActionContext.getResponse().getWriter().print(totalQty);

				}

			} else if (sub.equals("Save")) {
				String orgdept = formatVal(request.getParameter("orgdept"));
				String orgdate = formatVal(request.getParameter("orgdate"));
				String orgnbr = formatVal(request.getParameter("orgnbr"));
				String orgvend = formatVal(request.getParameter("orgvend"));
				String dept = formatVal(request.getParameter("dept"));
				String date = formatVal(request.getParameter("date"));
				String nbr = formatVal(request.getParameter("nbr"));
				String qty = formatVal(request.getParameter("qty"));
				String po = formatVal(request.getParameter("po"));
				String vendor = formatVal(request.getParameter("vendor"));
				String current = formatVal(request.getParameter("current"));
				String cost = formatVal(request.getParameter("cost"));
				String um = formatVal(request.getParameter("um"));
				String vendorlot = formatVal(request.getParameter("vendorlot"));
				String expdate = formatVal(request.getParameter("expirydate"));
				String rmk = formatVal(request.getParameter("rmk"));
				String lot = formatVal(request.getParameter("lot"));
				String printed = formatVal(request.getParameter("labelprinted"));

				if (orgnbr.equals(""))
					sql = "SELECT COUNT(*) AS total FROM tx_det " + " WHERE tx_dept = " + formatValue(dept)
							+ " and tx_date = " + formatValue(date) + " and tx_item = " + formatValue(nbr)
							+ " and tx_vendor = " + formatValue(vendor) + " and tx_group = " + formatValue(group);
				else
					sql = "SELECT COUNT(*) AS total FROM tx_det " + " WHERE tx_dept = " + formatValue(dept)
							+ " and tx_item = " + formatValue(nbr) + " and tx_vendor = " + formatValue(vendor)
							+ " and tx_vendor <> " + formatValue(orgvend);

				if (Integer.valueOf(dbConnection.getFieldValue(sql, "total")) != 0)
					msg = "exists";

				if (msg.equals("")) {
					if (orgnbr.equals(""))
						sql = "INSERT INTO tx_det (tx_lot,tx_dept,tx_date,tx_item,tx_qty, "
								+ "tx_po,tx_vendor, tx_um,tx_vendor_lot,tx_expiry_date,tx_label_printed"
								+ ",tx_curr,tx_cost,tx_rmk,tx_group) " + " VALUES (" + formatValue(lot) + ","
								+ formatValue(dept) + "," + formatValue(date) + "," + formatValue(nbr) + ","
								+ formatValue(qty) + "," + formatValue(po) + "," + formatValue(vendor) + ","
								+ formatValue(um) + "," + formatValue(vendorlot) + "," + formatValue(expdate) + ","
								+ formatValue(printed) + "," + formatValue(current) + "," + formatValue(cost) + ","
								+ formatValue(rmk) + "," + formatValue(group) + ")";
					else
						sql = "UPDATE tx_det SET tx_dept = " + formatValue(dept) + ",tx_date = " + formatValue(date)
								+ ",tx_lot = " + formatValue(lot) + ",tx_um = " + formatValue(um) + ",tx_po = "
								+ formatValue(po) + ",tx_curr = " + formatValue(current) + ",tx_vendor_lot = "
								+ formatValue(vendorlot) + ",tx_expiry_date = " + formatValue(expdate) + ",tx_rmk = "
								+ formatValue(rmk) + ",tx_label_printed = " + formatValue(printed) + ",tx_qty = "
								+ formatValue(qty) + ",tx_vendor = " + formatValue(vendor) + " WHERE tx_dept = "
								+ formatValue(orgdept) + " AND tx_date = " + formatValue(orgdate) + " AND tx_item = "
								+ formatValue(orgnbr) + " AND tx_vendor = " + formatValue(orgvend) + " AND tx_group = "
								+ formatValue(group);

					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
				}

				ServletActionContext.getResponse().getWriter().print(msg);

			} else if (sub.equals("Delete")) {
				String dept = formatVal(request.getParameter("dept"));
				String date = formatVal(request.getParameter("date"));
				String nbr = formatVal(request.getParameter("nbr"));
				String vendor = formatVal(request.getParameter("vendor"));

				if (!nbr.equals("")) {
					sql = "DELETE FROM tx_det WHERE " + " tx_dept = " + formatValue(dept) + " and tx_vendor = "
							+ formatValue(vendor) + " and tx_date = " + formatValue(date) + " and tx_item = "
							+ formatValue(nbr) + " and tx_group = " + formatValue(group);
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";

					ServletActionContext.getResponse().getWriter().print(msg);
				}
			} else if (sub.equals("bindDept")) {
				sql = "SELECT dept_code FROM dept_mstr WHERE dept_group = " + formatValue(group)
						+ " ORDER BY dept_code";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("dept_code"));
					jsonObject.put("desc", rs.getString("dept_code"));

					array.add(jsonObject);
				}

				json.put("depts", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindItem")) {
				sql = "SELECT item_num,item_cost,item_vendor,item_curr,item_um FROM item_mstr WHERE item_group = "
						+ formatValue(group) + " ORDER BY item_num";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("item_num"));
					jsonObject.put("desc", rs.getString("item_num"));
					jsonObject.put("cost", rs.getString("item_cost"));
					jsonObject.put("vendor", rs.getString("item_vendor"));
					jsonObject.put("curr", rs.getString("item_curr"));
					jsonObject.put("um", rs.getString("item_um"));
					array.add(jsonObject);
				}

				json.put("items", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindSortByItem")) {
				sql = "SELECT item_num,item_cost FROM item_mstr WHERE item_group = " + formatValue(group)
						+ " GROUP BY item_num  ORDER BY item_num";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("item_num"));
					jsonObject.put("desc", rs.getString("item_num"));
					array.add(jsonObject);
				}

				json.put("itemsorts", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindDesc")) {
				sql = "SELECT item_num,item_desc,item_cost,item_curr,item_vendor,item_um FROM item_mstr WHERE item_group = "
						+ formatValue(group) + " ORDER BY item_num";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("item_num"));
					jsonObject.put("desc", rs.getString("item_desc"));
					jsonObject.put("cost", rs.getString("item_cost"));
					jsonObject.put("vendor", rs.getString("item_vendor"));
					jsonObject.put("curr", rs.getString("item_curr"));
					jsonObject.put("um", rs.getString("item_um"));
					array.add(jsonObject);
				}

				json.put("descs", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindCurr")) {
				sql = "SELECT curr_code, curr_desc FROM curr_mstr ORDER BY curr_desc";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("curr_code"));
					jsonObject.put("desc", rs.getString("curr_desc"));

					array.add(jsonObject);
				}

				json.put("currs", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("getBalance")) {
				String nbr = formatVal(request.getParameter("nbr"));
				String totalQty;
				sql = "SELECT ROUND(sum(tx_qty),2) AS total FROM tx_det WHERE tx_group = '" + group
						+ "' AND tx_item = '" + nbr + "' GROUP BY tx_item";

				totalQty = dbConnection.getSumFieldValue(sql, "total");
				ServletActionContext.getResponse().getWriter().print(totalQty);

			} else if (sub.equals("bindGroup")) {
				ServletActionContext.getResponse().getWriter().print(group);
			} else if (sub.equals("bindVendor")) {
				sql = null;
				String nbr = formatVal(request.getParameter("nbr"));
				if (nbr.isEmpty() || nbr.isBlank() && nbr == "") {
					sql = "SELECT DISTINCT item_vendor FROM item_mstr ORDER BY item_vendor";
				} else {
					sql = "SELECT item_vendor FROM item_mstr WHERE item_num = '" + nbr + "' ORDER BY item_vendor";
				}
				rs = dbConnection.executeQuery(sql);
				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("item_vendor"));
					array.add(jsonObject);
				}
				json.put("vendors", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbConnection.closeConn();
			dbConnection = null;
		}

		return null;

	}

	private String formatValue(String value) {

		value = "" + value;
		if (value.equals("null"))
			value = "";
		if (value.equals(""))
			value = "null";
		else
			value = "'" + value + "'";

		return value;
	}

	public String createSQL(String field, String condition, String value) {
		if (condition.toUpperCase().trim().equals("LIKE"))
			return field + " " + " " + condition.toLowerCase() + " '%" + value + "%'";
		else
			return field + " " + " " + condition.toLowerCase() + " '" + value + "'";
	}

	public String formatVal(String val) {
		val = "" + val;
		if (val.equals("null"))
			val = "";
		return val;
	}

}
