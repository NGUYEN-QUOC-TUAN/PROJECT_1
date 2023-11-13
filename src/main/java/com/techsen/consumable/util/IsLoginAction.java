package com.techsen.consumable.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class IsLoginAction extends AbstractInterceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String intercept(ActionInvocation invocation) throws Exception{
		
		// 取得请求的Action名
		 String  name = invocation.getInvocationContext().getName();
		 if (name.toUpperCase().equals("LOGIN")) {
             // 如果用户想登录，则使之通过
             return invocation.invoke();
         } else {
             // 取得Session。
        	HttpServletRequest request = ServletActionContext.getRequest();
     		HttpSession session = ServletActionContext. getRequest().getSession();
     		HttpServletResponse response = ServletActionContext.getResponse(); 
 
             if (session == null) {
                 // 如果Session为空，则让用户登陆。
            	 if (request.getHeader("x-requested-with") != null
                 && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {  
            		 response.addHeader("sessionstatus", "timeout");
                 }   
            	 
          //  	 filter.doFilter(request, response);   
            	 
            	 return invocation.invoke(); 
             } else {
                 String username = (String)session.getAttribute("id");
                 if (username == null) {
                	 if (request.getHeader("x-requested-with") != null
                	 && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) { 
                		 response.addHeader("sessionstatus", "timeout"); 
                	 }    
                	 
                	 return invocation.invoke(); 
                 } else {
                     // 用户已经登陆，放行~
                     return invocation.invoke();
                 }
             }
         }
		
	}
	
}
