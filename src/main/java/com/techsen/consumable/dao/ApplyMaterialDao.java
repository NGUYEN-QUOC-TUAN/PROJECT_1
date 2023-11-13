package com.techsen.consumable.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.techsen.consumable.sql.utils.SqlRowValue;


/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月10日 上午11:26:40
 **/
@Repository("applyMaterialDao")
public class ApplyMaterialDao extends BaseDao {

	public ApplyMaterialDao() {
		super.table = "tx_det";
	}
	
	public List<SqlRowValue> getList(String reportStartDate,
			String reportEndDate, String materiel) {
		try {
			return getResult(" SELECT SUM(tx_cost * tx_qty) sum ,`cat_mstr`.`cat_code`, `tx_det`.`tx_qty`, `tx_det`.`tx_cost`, `tx_det`.`tx_date`, `tx_det`.`tx_dept`, `cat_mstr`.`cat_desc`, `tx_det`.`tx_curr`, `dept_mstr`.`dept_desc`, `tx_det`.`tx_group`"
						+ " FROM   ((`vConsumable`.`tx_det` `tx_det` INNER JOIN `vConsumable`.`dept_mstr` `dept_mstr` ON (`tx_det`.`tx_dept`=`dept_mstr`.`dept_code`) AND (`tx_det`.`tx_group`=`dept_mstr`.`dept_group`)) INNER JOIN `vConsumable`.`item_mstr` `item_mstr` ON (`tx_det`.`tx_item`=`item_mstr`.`item_num`) AND (`tx_det`.`tx_group`=`item_mstr`.`item_group`)) INNER JOIN `vConsumable`.`cat_mstr` `cat_mstr` ON (`item_mstr`.`item_cat`=`cat_mstr`.`cat_code`) AND (`item_mstr`.`item_group`=`cat_mstr`.`cat_group`)"
						+ " WHERE  (`cat_mstr`.`cat_code`='" + materiel + "') AND `tx_det`.`tx_group`='ADMIN' AND `tx_det`.`tx_dept`<>'STOCK' AND (`tx_det`.`tx_date`>={d '"+ reportStartDate +"'} AND `tx_det`.`tx_date`<={d '" + reportEndDate + "'})"
						+ " GROUP BY dept_desc  ORDER BY `tx_curr` ASC, `dept_desc` ASC");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
