package com.techsen.consumable.sql.utils;
/**  
 * @author fantasy 
 * E-mail: vi2014@qq.com
 * @version 2014年10月31日 上午8:33:41
 **/
public class QueryItem {

	//查询条件
	private String[] relation;
	//查询字段
	private String[] item;
	//查询字段条件
	private String[] condition;
	//查询字段值
	private String[] value;
	//分组字段
	private String[] groupValue;
	//排序字段
	private String[] orderValue;
	//排序字段的排序方式
	private String[] orderRule;
	//记录开始的标记
	private int start;
	//查询多少记录
	private int limit;
	//隐藏参数
	private String[] hiddenValue;
	
	
	public String[] getRelation() {
		return relation;
	}
	public void setRelation(String[] relation) {
		this.relation = relation;
	}
	public String[] getItem() {
		return item;
	}
	public void setItem(String[] item) {
		this.item = item;
	}
	public String[] getCondition() {
		return condition;
	}
	public void setCondition(String[] condition) {
		this.condition = condition;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	public String[] getGroupValue() {
		return groupValue;
	}
	public void setGroupValue(String[] groupValue) {
		this.groupValue = groupValue;
	}
	public String[] getOrderValue() {
		return orderValue;
	}
	public void setOrderValue(String[] orderValue) {
		this.orderValue = orderValue;
	}
	public String[] getOrderRule() {
		return orderRule;
	}
	public void setOrderRule(String[] orderRule) {
		this.orderRule = orderRule;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String[] getHiddenValue() {
		return hiddenValue;
	}
	public void setHiddenValue(String[] hiddenValue) {
		this.hiddenValue = hiddenValue;
	}
}
