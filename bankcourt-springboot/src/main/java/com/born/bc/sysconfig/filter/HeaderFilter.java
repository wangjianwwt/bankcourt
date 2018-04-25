	package com.born.bc.sysconfig.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.born.bc.body.commons.utils.LogUtils;

/**
 * request请求头filter
 * 
 * @author wangjian
 *
 */
public class HeaderFilter implements Filter {

	
	public void destroy() {
		LogUtils.debug("HeaderFilter-- destroy--");
	}

	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		
		Map<String, String> map = new HashMap<String, String>();
	    Enumeration<?> headerNames = request.getHeaderNames();
	    while (headerNames.hasMoreElements()) {
	        String key = (String) headerNames.nextElement();
	        String value = request.getHeader(key);
	        map.put(key, value);
	    }

		// 设置跨域请求
//		response.addHeader("Access-Control-Allow-Origin", requset.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
		response.setHeader("Access-Control-Allow-Methods", "*");  
		chain.doFilter(req, response);
	}

	
	public void init(FilterConfig arg0) throws ServletException {
		LogUtils.debug("HeaderFilter-- init--");
	}

}
