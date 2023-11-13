package com.techsen.consumable.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**  
 * @author fantasy <br>
 * E-mail: vi2014@qq.com
 * @version 2015年5月13日 下午12:07:36
 **/
@Action("")
@Results({
	@Result(name="page", location="/index.html")
})
public class SysAction extends BaseAction {

	public String execute(){
		return "page";
	}
}
