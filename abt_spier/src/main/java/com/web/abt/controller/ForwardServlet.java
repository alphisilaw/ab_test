package com.web.abt.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.web.abt.service.ProxyService;

/**
 * Servlet implementation class ForwardServlet
 */
public class ForwardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForwardServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uri = StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
		if (StringUtils.equals("/setVersionHtml", uri)) {
			ProxyService.newInstance().saveVersionHtml(request, response);
		} else if (StringUtils.equals("/previewVersionHtml", uri)) {
			ProxyService.newInstance().previewVersionHtml(request, response);
		} else if (StringUtils.equals("/proxySource", uri)) {
			ProxyService.newInstance().writeProxy(request, response);
		} else {
			ProxyService.newInstance().write(request, response);
		}
	}
	
}
