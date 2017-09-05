package com.zhenai.channel_dispatcher.listenner;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

import com.zhenai.clogger.client.LoggerStarter;

public class ContextLoaderListenerPlus extends ContextLoaderListener {
	
	public void contextInitialized(ServletContextEvent event) {
		LoggerStarter.init();
	}

}
