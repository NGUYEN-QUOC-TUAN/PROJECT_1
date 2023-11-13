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
@Repository("deptDetailsDao")
public class DeptDetailsDao extends BaseDao{

	public List<SqlRowValue> getList(String reportStartDate,
			String reportEndDate) {
		try {
			return getResult("SELECT `cat_mstr`.`cat_desc`, `item_mstr`.`item_num`, `item_mstr`.`item_desc`, `item_mstr`.`item_std`, `item_mstr`.`item_cost`, `dept_mstr`.`dept_desc`, `tx_det`.`tx_date`, `tx_det`.`tx_qty`, `tx_det`.`tx_curr`,"
					+ " `tx_det`.`tx_cost`, `tx_det`.`tx_rmk`, `tx_det`.`tx_dept`, `cat_mstr`.`cat_group` FROM ( ( `vConsumable`.`cat_mstr` `cat_mstr` INNER JOIN `vConsumable`.`item_mstr` `item_mstr` ON ( `cat_mstr`.`cat_code` = `item_mstr`.`item_cat`"
					+ " ) AND ( `cat_mstr`.`cat_group` = `item_mstr`.`item_group` ) ) INNER JOIN `vConsumable`.`tx_det` `tx_det` ON ( `item_mstr`.`item_num` = `tx_det`.`tx_item` ) AND ( `item_mstr`.`item_group` = `tx_det`.`tx_group`) ) INNER JOIN `vConsumable`.`dept_mstr` `dept_mstr` ON ( `tx_det`.`tx_dept` = `dept_mstr`.`dept_code` )"
					+ " AND ( `tx_det`.`tx_group` = `dept_mstr`.`dept_group` ) WHERE ( `item_mstr`.`item_num` LIKE '00%' OR `item_mstr`.`item_num` LIKE '02%' OR `item_mstr`.`item_num` LIKE '04%' OR `item_mstr`.`item_num` LIKE '05%' "
					+ "OR `item_mstr`.`item_num` LIKE '06%' OR `item_mstr`.`item_num` LIKE '08%' OR `item_mstr`.`item_num` LIKE '09%' OR `item_mstr`.`item_num` LIKE '10%' OR `item_mstr`.`item_num` LIKE '11%' OR `item_mstr`.`item_num` LIKE '88%' OR `item_mstr`.`item_num` LIKE '99%' "
					+ ") AND `cat_mstr`.`cat_group` = 'ADMIN' AND `tx_det`.`tx_dept` <> 'STOCK' AND ( `tx_det`.`tx_date` >={ d '" + reportStartDate + "' } AND `tx_det`.`tx_date` <={ d '" + reportEndDate + "' })"
					+ " ORDER BY `tx_curr` ASC , `dept_desc` ASC , `cat_desc` ASC, `item_desc` ASC");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
