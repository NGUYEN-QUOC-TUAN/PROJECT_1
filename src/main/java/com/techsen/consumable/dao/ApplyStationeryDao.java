package com.techsen.consumable.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.techsen.consumable.sql.utils.SqlRowValue;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月8日 下午3:27:50
 **/
@Repository("applyStationeryDao")
public class ApplyStationeryDao extends BaseDao {

	public ApplyStationeryDao() {
		super.table = "tx_det";
	}
	
	public List<SqlRowValue> getList(String reportStartDate,
			String reportEndDate) {
		try {
			return getResult(" SELECT `cat_mstr`.`cat_code`, `tx_det`.`tx_qty`, `tx_det`.`tx_cost`, `tx_det`.`tx_date`, `tx_det`.`tx_dept`, `cat_mstr`.`cat_desc`, `item_mstr`.`item_desc`, `dept_mstr`.`dept_desc`, `tx_det`.`tx_curr`, `tx_det`.`tx_group`"
						+ " FROM   ((`vConsumable`.`tx_det` `tx_det` INNER JOIN `vConsumable`.`dept_mstr` `dept_mstr` ON (`tx_det`.`tx_dept`=`dept_mstr`.`dept_code`) AND (`tx_det`.`tx_group`=`dept_mstr`.`dept_group`)) INNER JOIN `vConsumable`.`item_mstr` `item_mstr` ON (`tx_det`.`tx_item`=`item_mstr`.`item_num`) AND (`tx_det`.`tx_group`=`item_mstr`.`item_group`)) INNER JOIN `vConsumable`.`cat_mstr` `cat_mstr` ON (`item_mstr`.`item_cat`=`cat_mstr`.`cat_code`) AND (`item_mstr`.`item_group`=`cat_mstr`.`cat_group`)"
						+ " WHERE  `cat_mstr`.`cat_code`='09' AND `tx_det`.`tx_group`='ADMIN' AND `tx_det`.`tx_dept`<>'STOCK' AND (`tx_det`.`tx_date`>={d '" + reportStartDate + "'} AND `tx_det`.`tx_date`<={d '" + reportEndDate + "'}) ORDER BY `tx_curr` ASC, `item_desc` ASC");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
