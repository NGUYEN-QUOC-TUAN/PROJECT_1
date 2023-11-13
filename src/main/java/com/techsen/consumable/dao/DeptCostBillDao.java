package com.techsen.consumable.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.techsen.consumable.sql.utils.SqlRowValue;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月21日 下午4:09:25
 **/
@Repository("deptCostBillDao")
public class DeptCostBillDao extends BaseDao{

	public List<SqlRowValue> getList(String reportStartDate,
			String reportEndDate) {
		try {
			return getResult("SELECT `tx_det`.`tx_dept`, `tx_det`.`tx_item`, `tx_det`.`tx_qty`, `tx_det`.`tx_cost`, `cat_mstr`.`cat_code`, CONCAT(dept_desc,'(',dept_code,')') dept_desc, `tx_det`.`tx_date`, `cat_mstr`.`cat_desc`, `dept_mstr`.`dept_code`, `tx_det`.`tx_curr`, `tx_det`.`tx_group` , CONCAT(cat_code, cat_desc) cat_col"
						+ " FROM   ((`vConsumable`.`tx_det` `tx_det` INNER JOIN `vConsumable`.`item_mstr` `item_mstr` ON (`tx_det`.`tx_item`=`item_mstr`.`item_num`) AND (`tx_det`.`tx_group`=`item_mstr`.`item_group`)) INNER JOIN `vConsumable`.`dept_mstr` `dept_mstr` ON (`tx_det`.`tx_dept`=`dept_mstr`.`dept_code`) AND (`tx_det`.`tx_group`=`dept_mstr`.`dept_group`)) INNER JOIN `vConsumable`.`cat_mstr` `cat_mstr` ON (`item_mstr`.`item_cat`=`cat_mstr`.`cat_code`) AND (`item_mstr`.`item_group`=`cat_mstr`.`cat_group`)"
						+ " WHERE  `tx_det`.`tx_dept`<>'STOCK' AND `tx_det`.`tx_group`='ADMIN' AND (`tx_det`.`tx_date`>={d '" + reportStartDate + "'} AND `tx_det`.`tx_date`<={d '" + reportEndDate + "'})"
						+ " ORDER BY `tx_curr` ASC, dept_desc ASC");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
