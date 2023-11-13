package com.techsen.consumable.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class SessionCheckFilter extends HttpServlet implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpRequest =  (HttpServletRequest) request;
		HttpServletResponse httpResponse =  (HttpServletResponse) response;			
		HttpSession session= httpRequest.getSession();
		
		if (session == null || session.getAttribute("id") == null) {  
			httpResponse.setHeader("sessionstatus", "timeout"); 
		}  
		
		filterChain.doFilter(httpRequest, httpResponse);
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
