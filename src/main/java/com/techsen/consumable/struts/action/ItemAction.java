package com.techsen.consumable.struts.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.techsen.consumable.util.DBConnection;

@Namespace("/")
@Scope("prototype")
@ParentPackage("basePackage")
public class ItemAction {

	@Autowired
	private DBConnection dbConnection;

	@Action("item")
	public String execute() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		String sub = formatVal(request.getParameter("sub"));
		ResultSet rs;

		HttpSession session = ServletActionContext.getRequest().getSession();

		String group = "" + (String) session.getAttribute("group");

		String msg = "";

		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();

		try {
			dbConnection.getConnection();
			String sql;
			if (sub.equals("Find") || sub.equals("First") || sub.equals("Prev") || sub.equals("Next")
					|| sub.equals("Last") || sub.equals("Init") || sub.equals("Cancel") || sub.equals("Reset")) {

				String fld1 = formatVal(request.getParameter("fld1"));
				String fld2 = formatVal(request.getParameter("fld2"));
				String cdt1 = formatVal(request.getParameter("cdt1"));
				String cdt2 = formatVal(request.getParameter("cdt2"));
				String val1 = formatVal(request.getParameter("val1"));
				String val2 = formatVal(request.getParameter("val2"));
				String record = formatVal(request.getParameter("record"));

				String total;

				String subSql = "";

				if (!val1.equals(""))
					subSql = subSql + " AND " + createSQL(fld1, cdt1, val1);

				if (!val2.equals(""))
					subSql = subSql + " AND " + createSQL(fld2, cdt2, val2);

				subSql = subSql + " ORDER BY item_num";

				sql = "SELECT COUNT(*) AS total FROM item_mstr WHERE item_group = '" + group + "'" + subSql;

				total = dbConnection.getFieldValue(sql, "total");

				if (record.equals(""))
					record = "0";

				if (sub.equals("First"))
					record = "1";
				if (sub.equals("Prev"))
					record = String.valueOf(Integer.valueOf(record) - 1);
				if (sub.equals("Next"))
					record = String.valueOf(Integer.valueOf(record) + 1);
				if (sub.equals("Last"))
					record = total;
				if (sub.equals("Reset"))
					record = "0";

				if (Integer.valueOf(record) <= 0)
					if (Integer.valueOf(total) > 0)
						record = "1";

				if (Integer.valueOf(record) > Integer.valueOf(total))
					record = total;

				String current = String.valueOf(Integer.valueOf(record) - 1);

				if (Integer.valueOf(current) < 0)
					current = "0";

				sql = "SELECT * FROM item_mstr WHERE item_group = '" + group + "'" + subSql + " limit " + current
						+ ",1";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("nbr", rs.getString("item_num"));
					jsonObject.put("desc", rs.getString("item_desc"));
					jsonObject.put("std", rs.getString("item_std"));
					jsonObject.put("cat", rs.getString("item_cat"));
					jsonObject.put("cost", rs.getString("item_cost"));
					jsonObject.put("um", rs.getString("item_um"));
					jsonObject.put("vend", rs.getString("item_vendor"));
					jsonObject.put("lvl", rs.getString("item_ordlvl"));
					jsonObject.put("curr", rs.getString("item_curr"));
					jsonObject.put("moq", rs.getString("item_moq"));
					jsonObject.put("record", record);
					jsonObject.put("total", total);
					array.add(jsonObject);
				}

				// 如果没有查到
				if (array.size() == 0) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("nbr", "");
					jsonObject.put("desc", "");
					jsonObject.put("std", "");
					jsonObject.put("cat", "");
					jsonObject.put("cost", "");
					jsonObject.put("um", "");
					jsonObject.put("vend", "");
					jsonObject.put("curr", "");
					jsonObject.put("lvl", "");
					jsonObject.put("moq", "");
					array.add(jsonObject);
				}

				json.put("items", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());

			} else if (sub.equals("Save")) {
				String orgnbr = formatVal(request.getParameter("orgnbr"));
				String orgvend = formatVal(request.getParameter("orgvend"));
				String nbr = formatVal(request.getParameter("nbr"));
				String desc = formatVal(request.getParameter("desc"));
				String std = formatVal(request.getParameter("std"));
				String cat = formatVal(request.getParameter("cat"));
				String cost = formatVal(request.getParameter("cost"));
				String um = formatVal(request.getParameter("um"));
				String vend = formatVal(request.getParameter("vend"));
				String lvl = formatVal(request.getParameter("lvl"));
				String curr = formatVal(request.getParameter("curr"));
				String moq = formatVal(request.getParameter("moq"));

				if (orgnbr.equals(""))
					sql = "SELECT COUNT(*) AS total FROM item_mstr " + " WHERE item_num = " + formatValue(nbr)
							+ " and item_group = " + formatValue(group) + " and item_vendor = " + formatValue(vend);
				else
					sql = "SELECT COUNT(*) AS total FROM item_mstr " + " WHERE item_num = " + formatValue(nbr)
							+ " and item_group = " + formatValue(group)
							+ " and item_vendor = " + formatValue(vend) + " and item_vendor <> " + formatValue(orgvend);
				System.out.println(Integer.valueOf(dbConnection.getFieldValue(sql, "total")));
				if (Integer.valueOf(dbConnection.getFieldValue(sql, "total")) != 0)
					msg = "exists";

				if (msg.equals("")) {
					if (orgnbr.equals(""))
						sql = "INSERT INTO item_mstr (item_num, item_desc, item_std, item_cat, item_cost, item_um, item_vendor, item_ordlvl, item_group, item_curr, item_moq) "
								+ "VALUES (" + formatValue(nbr) + ", " + formatValue(desc) + ", " + formatValue(std)
								+ ", " + formatValue(cat) + ", " + formatValue(cost) + ", " + formatValue(um) + ", "
								+ formatValue(vend) + ", " + formatValue(lvl) + ", " + formatValue(group) + ", "
								+ formatValue(curr) + ", " + formatValue(moq) + ")";

					else
						sql = "UPDATE item_mstr SET item_num = " + formatValue(nbr) + ",item_desc = "
								+ formatValue(desc) + ",item_std = " + formatValue(std) + ",item_cat = "
								+ formatValue(cat) + ",item_curr = " + formatValue(curr) + ",item_cost = "
								+ formatValue(cost) + ",item_um = " + formatValue(um) + ",item_vendor = "
								+ formatValue(vend) + ",item_ordlvl = " + formatValue(lvl) + ",item_moq = "
								+ formatValue(moq) + " WHERE item_num = " + formatValue(orgnbr) + " AND item_group = "
								+ formatValue(group) + " AND item_vendor = " + formatValue(orgvend);

					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";
				}

				ServletActionContext.getResponse().getWriter().print(msg);

			} else if (sub.equals("Delete")) {
				String nbr = formatVal(request.getParameter("nbr"));
				String vendor = formatVal(request.getParameter("vend"));
				if (!nbr.equals("")) {
					sql = "DELETE FROM item_mstr WHERE item_num = " + formatValue(nbr) + " and item_group = "
							+ formatValue(group) + " and item_vendor = " + formatValue(vendor);
					if (dbConnection.executeUpdate(sql) == true)
						msg = "ok";
					else
						msg = "failed";

					ServletActionContext.getResponse().getWriter().print(msg);
				}
			} else if (sub.equals("bindCat")) {
				sql = "SELECT cat_code,concat(cat_code,'  [',cat_desc,']') AS cat_desc FROM cat_mstr WHERE cat_group = "
						+ formatValue(group) + " ORDER BY cat_code";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("cat_code"));
					jsonObject.put("desc", rs.getString("cat_desc"));

					array.add(jsonObject);
				}

				json.put("categorys", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindVend")) {
				sql = "SELECT vd_code,concat(vd_code,'  [',vd_name,']') AS vd_name FROM vd_mstr WHERE vd_group = "
						+ formatValue(group) + " ORDER BY vd_code";

				rs = dbConnection.executeQuery(sql);

				while (rs.next()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("val", rs.getString("vd_code"));
					jsonObject.put("desc", rs.getString("vd_name"));

					array.add(jsonObject);
				}

				json.put("vendors", array);

				ServletActionContext.getResponse().setCharacterEncoding("utf-8");
				ServletActionContext.getResponse().getWriter().print(json.toString());
			} else if (sub.equals("bindGroup")) {
				ServletActionContext.getResponse().getWriter().print(group);
			} else if (sub.equals("bindCurr")) {

				sql = "SELECT curr_code, curr_desc FROM curr_mstr";

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
