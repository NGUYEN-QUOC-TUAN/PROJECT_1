package com.techsen.consumable.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.techsen.consumable.sql.utils.SqlRowValue;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年4月2日 下午5:10:10
 **/
@Repository("detailsDao")
public class DetailsDao extends BaseDao {

	public DetailsDao() {
		super.table = "tx_det";
	}

	public List<SqlRowValue> getList(String reportDate, String materiel, String limit, String start) {
		try {
			return getResult(" SELECT SUM(`tx_qty`) sum ,`tx_det`.`tx_item`, `tx_det`.`tx_qty`, `tx_det`.`tx_date`, `item_mstr`.`item_desc`, `dept_mstr`.`dept_desc`, `item_mstr`.`item_std`, `item_mstr`.`item_um`, `item_mstr`.`item_cost`, `tx_det`.`tx_group`"
						+ " FROM   (`vConsumable`.`tx_det` `tx_det` INNER JOIN `vConsumable`.`item_mstr` `item_mstr` ON (`tx_det`.`tx_item`=`item_mstr`.`item_num`) AND (`tx_det`.`tx_group`=`item_mstr`.`item_group`)) INNER JOIN `vConsumable`.`dept_mstr` `dept_mstr` ON (`tx_det`.`tx_dept`=`dept_mstr`.`dept_code`) AND (`tx_det`.`tx_group`=`dept_mstr`.`dept_group`)"
						+ " WHERE  `tx_det`.`tx_group`='ADMIN' AND `tx_det`.`tx_date`<={d '" + reportDate + "'} AND (`tx_det`.`tx_item` LIKE '00%' OR `tx_det`.`tx_item` LIKE '" + materiel + "%') GROUP BY tx_item"
						+ " ORDER BY `tx_det`.`tx_item`");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getTotal(String reportDate, String materiel) {
		try {
			return getResultString("SELECT COUNT(*) total from ( SELECT `tx_item` FROM `tx_det` INNER JOIN `dept_mstr` ON `tx_dept` =`dept_code` INNER JOIN `item_mstr` ON `tx_item` = `item_num`  WHERE `tx_item` LIKE '" + materiel + "%' AND `tx_date` <=  '" + reportDate + "'  GROUP BY tx_item ) aa " , "total");
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";
		}
	}

	public List<SqlRowValue> getAllList(String reportDate, String materiel) {
		return getList(reportDate ,materiel , "0","0");
	}
}
