/*
 * <p>Title: ManagerFactory.java </p> <p>Description: </p> <p>Copyright:
 * digu.com (c) 2012 </p> <p>Company: digu</p>
 */
package com.web.abt.m.service;

import com.web.abt.m.service.impl.CaseServiceImpl;
import com.web.abt.m.service.impl.DataViewServiceImpl;
import com.web.abt.m.service.impl.GoalServiceImpl;
import com.web.abt.m.service.impl.GoalTypeServiceImpl;
import com.web.abt.m.service.impl.ProjectServiceImpl;
import com.web.abt.m.service.impl.UserServiceImpl;



/**
 * 
 * @ClassName: ServiceFactory
 * @Description: Service的工厂类，对Service都以单例方式来访问，无需重复创建实例
 * 
 */
public class ServiceFactory {

	private static UserService userService;
	private static ProjectService projectService;
	private static CaseService caseService;
	private static GoalService goalService;
	private static GoalTypeService goalTypeService;
	private static DataViewService dataViewService;

	public static UserService getUserService() {
        if (userService == null) {
            synchronized (ServiceFactory.class) {
                if (userService == null) {
                	userService = new UserServiceImpl();
                }
            }
        }
        return userService;
    }
	
	public static ProjectService getProjectService() {
        if (projectService == null) {
            synchronized (ServiceFactory.class) {
                if (projectService == null) {
                	projectService = new ProjectServiceImpl();
                }
            }
        }
        return projectService;
    }
	
	public static CaseService getCaseService() {
        if (caseService == null) {
            synchronized (ServiceFactory.class) {
                if (caseService == null) {
                	caseService = new CaseServiceImpl();
                }
            }
        }
        return caseService;
    }
	
	public static GoalService getGoalService() {
        if (goalService == null) {
            synchronized (ServiceFactory.class) {
                if (goalService == null) {
                	goalService = new GoalServiceImpl();
                }
            }
        }
        return goalService;
    }
	
	public static GoalTypeService getGoalTypeService() {
        if (goalTypeService == null) {
            synchronized (ServiceFactory.class) {
                if (goalTypeService == null) {
                	goalTypeService = new GoalTypeServiceImpl();
                }
            }
        }
        return goalTypeService;
    }

	public static DataViewService getDataViewService() {
		if (dataViewService == null) {
            synchronized (ServiceFactory.class) {
                if (dataViewService == null) {
                	dataViewService = new DataViewServiceImpl();
                }
            }
        }
		return dataViewService;
	}
	
}
