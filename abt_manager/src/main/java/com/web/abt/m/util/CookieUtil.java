package com.web.abt.m.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

	public static void addCookie(HttpServletResponse response,String name,String value){
	    Cookie cookie = new Cookie(name,value);
	    cookie.setPath("/");
	    cookie.setMaxAge(3600*24);
	    response.addCookie(cookie);
	}
	
	public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name){
		 Cookie cookies[] = request.getCookies() ;
	       Cookie c = null ;
	       for(int i=0;i<cookies.length;i++){
	           c = cookies[i] ;
	           if(c.getName().equals(name)){
	              c.setMaxAge(0);
	              response.addCookie(c) ;
	           }
	       }
	}
	
	public static String getCookieValueByName(HttpServletRequest request,String name){
		Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	        	if(name.equals(cookie.getName()) && cookie.getValue() != null){
		        	return cookie.getValue();
		        }
	        }
	        return null;
	    }else{
	    	return null;
	    } 
	}
	
}