package com.web.abt.m.common;

public class ResultConstant {
	public static String USER_NOT_EXIST = "用户不存在";
	public static String PASSWORD_INCORRECT = "密码错误";
	public static String USERNAME_EMPTY = "请输入用户名";
	public static String EMAIL_EMPTY = "请输入邮箱地址";
	public static String PHONE_EMPTY = "请输入手机号码";
	public static String ACCOUNT_EMPTY = "请输入账号";
	public static String PASSWORD_EMPTY = "请输入密码";
	public static String USER_NOT_LOGIN = "用户未登陆";
	public static String DEMO_USER_NOT_AUTH = "这是一个体验账号，无权进行该操作";
	public static String USER_REG_ALREADY = "用户已经注册";
	public static String EXCEPTION_IN_USER_REG = "用户注册时发生错误";
	public static String EXCEPTION_IN_USER_CREATE = "注册用户时发生错误";
	public static String REG_CODE_EMPTY = "注册码不可为空";
	public static String CANNOT_FIND_REG_USER = "无法找到该用户";
	public static String USER_REG_ACTIVE_SUCC = "激活成功";
	public static String USER_REG_ACTIVE_FAIL = "激活失败";
	
	public static String EXCEPTION_IN_PROJECT_SEARCH = "查询项目出错";
	public static String EXCEPTION_IN_PROJECT_ADD = "新增项目出错";
	public static String EXCEPTION_IN_PROJECT_EDIT = "修改项目出错";
	public static String EXCEPTION_IN_PROJECT_DEL = "删除项目出错";
	public static String EXCEPTION_DEL_NOEMPTY_PROJECT = "无法删除含有实验的项目";
	public static String EMPTY_IN_PROJECT_SEARCH = "没有有效项目";
	public static String EMPTY_IN_PROJECT_STATISTIC_SEARCH = "项目统计记录为空";
	public static String PROJECT_NAME_EMPTY = "请输入项目名";
	public static String PROJECT_ID_EMPTY = "项目ID为空";
	public static String EXCEPTION_IN_PROJECT_STATISTIC_UPDATE = "项目统计时出错";
	
	public static String CASE_NAME_EMPTY = "请输入实验名";
	public static String CASE_URL_EMPTY = "请驶入实验URL";
	public static String EXCEPTION_IN_CASE_ADD = "新增实验时出错";
	public static String EXCEPTION_IN_CASE_STATUS_UPDATE = "修改实验状态出错";
	public static String EXCEPTION_IN_CASE_NAME_UPDATE = "修改实验名出错";
	public static String EXCEPTION_IN_CASE_COPY = "复制实验时出错";
	public static String EXCEPTION_IN_CASE_STATISTIC_UPDATE = "实验统计时出错";
	public static String EMPTY_IN_CASE_SEARCH = "实验不存在";
	public static String INVALID_PROJECT_ID = "无效的项目编号";
	public static String INVALID_CASE_BUIZ_TYPE = "所属业务类型错误";
	public static String INVALID_CASE_ID = "无效的实验编号";
	public static String INVALID_DATA_TYPE = "无效的数据类型";
	public static String CASE_NOT_RUN = "实验尚未启动";
	public static String CASE_NAME_EXISTED_ALREADY = "实验名已经存在";
	public static String CASE_URL_EXISTED_ALREADY = "启动失败，含有相同url的实验已经开始";
	public static String SYSTEM_CASE_NAME_CANNOT_USE="该类型的实验名只能由系统分配，用户无法自定义";
	
	public static String EXCEPTION_IN_VERSION_ADD = "新增版本出错";
	public static String EXCEPTION_IN_VERSION_UPDATE = "版本更新时出错";
	public static String EXCEPTION_IN_VERSION_STATUS_UPDATE = "更改版本状态出错";
	public static String EXCEPTION_IN_VERSION_DELETING = "删除版本出错";
	public static String INVALID_VERSION_ID = "无效的版本ID";
	public static String EMPTY_IN_VERSION_SEARCH = "查询版本为空";
	public static String VERSION_NOT_CHANGE = "version_not_change";
	public static String EXCEPTION_IN_VERSION_STATISTIC_UPDATE = "版本统计时出错";
	public static String VERSION_NAME_EXISTED_ALREADY = "当前实验已经存在同样的版本名";
	
	public static String EXCEPTION_IN_GOAL_ADD = "新增指标出错";
	public static String EXCEPTION_IN_GOAL_UPDATE = "更改指标出错";
	public static String EXCEPTION_IN_GOAL_DELETE = "删除指标出错";
	public static String INVALID_GOAL_ID = "无效的指标ID";
	public static String EMPTY_IN_GOAL_SEARCH = "查询指标为空";
	public static String CURR_GOAL_IS_FULL = "可建立的指标已满";
	public static String EXCEPTION_IN_GOAL_STATISTIC_UPDATE = "更新指标统计时出错";
	public static String EXCEPTION_IN_GOAL_STATISTIC_SEARCH = "查询指标统计时出错";
	public static String EMPTY_IN_GOAL_STATISTIC_SEARCH = "查询指标统计时出错";
	public static String MASTER_GOAL_EXIST_ALREADY = "请先取消其他的主追踪目标";
	public static String KPI_GOAL_EXIST_ALREADY = "不可添加相同KPI目标";
	public static String COM_GOAL_NAME_EXIST_ALREADY = "该名称目标已经存在";
	public static String EXCEPTION_IN_SET_MASTER_GOAL = "更改主追踪目标状态时出现错误";
	
	public static String EXCEPTION_IN_CASE_LIMIT_ADD = "增加实验限定时出现错误";
	public static String EXCEPTION_IN_CASE_LIMIT_DELETE = "删除实验限定时出现错误";
	public static String INVALID_CASE_LIMIT_ID = "无法获取到该限定ID";
	public static String INVALID_IS_MOBILE = "无法获得网页类型（手机端、PC端）";
	
	public static String SET_REDIS_ERROR = "set_redis_error";
	public static String REDIS_CASE_NOT_RUNNING = "redis_case_not_running";
	
	public static String EMPTY_PAGE_ATTR = "无法找到页面参数";
	
	public static String CASE_ID_EMPTY="没有指定实验ID";
	public static String CLEAR_CACHE_EXCEPTION="清除缓存失败";
	public static String URL_ENCODE_EXCEPTION="url加密失败";
}
